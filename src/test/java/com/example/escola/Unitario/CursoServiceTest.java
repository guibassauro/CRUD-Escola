package com.example.escola.Unitario;

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

import com.example.escola.dto.Request.AtualizaCursoRequest;
import com.example.escola.dto.Request.CriaCursoRequest;
import com.example.escola.dto.Response.AtualizaCursoResponse;
import com.example.escola.dto.Response.CriaCursoResponse;
import com.example.escola.model.Curso;
import com.example.escola.model.Matricula;
import com.example.escola.repository.CursoRepository;
import com.example.escola.service.impl.CursoServiceImpl;
import com.example.escola.service.impl.MatriculaServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testar Curso Service")
public class CursoServiceTest {
    
    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private MatriculaServiceImpl matriculaService;
    
    @InjectMocks
    private CursoServiceImpl cursoService;

    @Test
    @DisplayName("Testa o acharTodos do Curso")
    void deveRetornarTodosOsCursos(){
        Curso curso1 = Curso.builder()
        .nome("Ciências da Computação").build();
        Curso curso2 = Curso.builder()
        .nome("Biologia").build();

        when(cursoRepository.findAll()).thenReturn(List.of(curso1, curso2));

        List<Curso> resultado = cursoService.achaTodosOsCurso();

        assertEquals(resultado.get(0).getNome(), "Ciências da Computação");
        assertEquals(resultado.get(1).getNome(), "Biologia");
        verify(cursoRepository).findAll();
    }

    @Test
    @DisplayName("Testa criar novo Curso")
    void deveCriarNovoCurso(){
        CriaCursoRequest criaCurso = CriaCursoRequest.builder()
        .nome("Ciências da Computação").build();

        Curso curso = Curso.builder()
        .nome(criaCurso.getNome()).build();

        CriaCursoResponse novoCurso = cursoService.criaNovoCurso(criaCurso);

        assertEquals("Ciências da Computação", novoCurso.getNome());
        verify(cursoRepository).save(curso);
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
            verify(cursoRepository).findById((long)2);
        }
    }

    @Test
    @DisplayName("Testa se atualiza um Curso valido")
    void deveAtualizarCursoValido(){
        Curso cursoExistente = Curso.builder()
        .id((long)1)
        .descricao("Ciências da Computation").build();

        AtualizaCursoRequest atualizaCurso = AtualizaCursoRequest.builder()
        .descricao("Ciências da Computação").build();

        Curso cursoAtualizado = Curso.builder()
        .id(cursoExistente.getId())
        .descricao(atualizaCurso.getDescricao())
        .build();

        when(cursoRepository.findById((long)1)).thenReturn(Optional.of(cursoExistente));

        AtualizaCursoResponse resposta = cursoService.atualizaCurso(cursoExistente.getId(), atualizaCurso);

        assertEquals("Ciências da Computação", resposta.getDescricao());
        verify(cursoRepository).findById((long)1);
        verify(cursoRepository).save(cursoAtualizado);
    }

    @Test
    @DisplayName("Testa se sai Exception de não deixar deletar um curso com matriculas")
    void deveNegarDeletarCursoComMatriculas(){
        
        Curso cursoExistente = Curso.builder()
        .id((long)1).nome("Biologia").build();

        Matricula matriculaExistente = Matricula.builder()
        .id((long)1).curso(cursoExistente).build();

        when(cursoRepository.findById((long)1)).thenReturn(Optional.of(cursoExistente));
        when(matriculaService.achaTodasPorCursoId((long)1)).thenReturn(List.of(matriculaExistente));

        try{
            cursoService.deletaCurso(cursoExistente.getId());
            fail("Não deu Exception");
        } catch(Exception e){
            assertEquals(e.getMessage(), "Você não pode excluir um curso com matriculas em andamento");
            verify(cursoRepository).findById((long)1);
            verify(matriculaService).achaTodasPorCursoId((long)1);
        }
    }

    @Test
    @DisplayName("Testa se deleta Curso válido")
    void deveDeletarCursoValido(){
        Curso cursoExistente = Curso.builder()
        .id((long)1).nome("Biologia").build();

        when(cursoRepository.findById(cursoExistente.getId())).thenReturn(Optional.of(cursoExistente));

        cursoService.deletaCurso(cursoExistente.getId());

        verify(cursoRepository).findById((long)1);
        verify(matriculaService).achaTodasPorCursoId((long)1);
        verify(cursoRepository).deleteById(cursoExistente.getId());
    }
}
