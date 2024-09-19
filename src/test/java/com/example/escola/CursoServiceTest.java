package com.example.escola;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.example.escola.dto.AtualizaCursoRequest;
import com.example.escola.dto.CriaCursoRequest;
import com.example.escola.model.Curso;
import com.example.escola.model.Matricula;
import com.example.escola.repository.CursoRepository;
import com.example.escola.service.impl.CursoServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testar Curso Service")
public class CursoServiceTest {
    
    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoServiceImpl cursoService;

    @Test
    @DisplayName("Testa o acharTodos do Curso")
    void deveRetornarTodosOsCursos(){
        Curso curso1 = new Curso();
        Curso curso2 = new Curso();
        curso1.setNome("Ciências da Computação");
        curso2.setNome("Biologia");

        when(cursoRepository.findAll()).thenReturn(List.of(curso1, curso2));

        List<Curso> resultado = cursoService.achaTodosOsCurso();

        assertEquals(resultado.get(0).getNome(), "Ciências da Computação");
        assertEquals(resultado.get(1).getNome(), "Biologia");
    }

    @Test
    @DisplayName("Testa criar novo Curso")
    void deveCriarNovoCurso(){
        CriaCursoRequest criaCurso = new CriaCursoRequest();
        criaCurso.setNome("Lucas");

        Curso novoCurso = cursoService.criaNovoCurso(criaCurso);

        assertEquals(novoCurso.getNome(), "Lucas");
    }

    @Test
    @DisplayName("Testa se sai exception de curso não encontrado em atualizaCurso")
    void deveRetornarNotFoundCurso(){

        when(cursoRepository.findById((long)2)).thenReturn(Optional.empty());

        try {
            cursoService.atualizaCurso((long)2, null);
            fail("Não deu exception");
        } catch(Exception e){
            assertEquals(e.getMessage(), "Curso não encontrado!");
        }
    }

    @Test
    @DisplayName("Testa se atualiza um Curso valido")
    void deveAtualizarCursoValido(){
        Curso cursoExistente = new Curso();
        cursoExistente.setDescricao("Ciências da Computation");

        AtualizaCursoRequest atualizaCurso = new AtualizaCursoRequest();
        atualizaCurso.setDescricao("Ciências da Computação");

        when(cursoRepository.findById(cursoExistente.getId())).thenReturn(Optional.of(cursoExistente));

        cursoService.atualizaCurso(cursoExistente.getId(), atualizaCurso);

        assertEquals(cursoExistente.getDescricao(), "Ciências da Computação");
    }

    @Test
    @DisplayName("Testa se sai Exception de não deixar deletar um curso com matriculas")
    void deveNegarDeletarCursoComMatriculas(){
        Matricula matriculaExistente = new Matricula();
        
        Curso cursoExistente = new Curso();
        cursoExistente.setMatriculas(List.of(matriculaExistente));

        when(cursoRepository.findById(cursoExistente.getId())).thenReturn(Optional.of(cursoExistente));

        try{
            cursoService.deletaCurso(cursoExistente.getId());
            fail("Não deu Exception");
        } catch(Exception e){
            assertEquals(e.getMessage(), "Você não pode excluir cursos com matriculas em andamento");
        }
    }

    @Test
    @DisplayName("Testa se deleta Curso válido")
    void deveDeletarCursoValido(){
        Curso cursoExistente = new Curso();

        when(cursoRepository.findById(cursoExistente.getId())).thenReturn(Optional.of(cursoExistente));

        cursoService.deletaCurso(cursoExistente.getId());

        verify(cursoRepository).deleteById(cursoExistente.getId());
    }
}
