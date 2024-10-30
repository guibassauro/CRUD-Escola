package com.example.escola.Unitario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.escola.dto.Request.AtualizaAlunoRequest;
import com.example.escola.dto.Request.CriaAlunoRequest;
import com.example.escola.dto.Response.AtualizarAlunoResponse;
import com.example.escola.dto.Response.CriaAlunoResponse;
import com.example.escola.model.Aluno;
import com.example.escola.model.Curso;
import com.example.escola.repository.AlunoRepository;
import com.example.escola.repository.CursoRepository;
import com.example.escola.service.impl.AlunoServiceImpl;
import com.example.escola.service.impl.CursoServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testar Aluno Service")
public class AlunoServiceTest {
    
    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private CursoServiceImpl cursoService;

    @InjectMocks
    private AlunoServiceImpl alunoService;

    @Test
    @DisplayName("Teste para verificar funcionamento do GetMapping geral")
    void deveVerificarOFindAll(){
        Aluno aluno1 = Aluno.builder()
        .nome("Pedro").build();

        Aluno aluno2 = Aluno.builder()
        .nome("Lucas").build();

        when(alunoRepository.findAll())
                            .thenReturn(List.of(aluno1, aluno2));

        List<Aluno> resultado = alunoService.achaTodosOsAlunos();

        assertNotNull(resultado);
        assertEquals("Pedro", resultado.get(0).getNome());
        assertEquals("Lucas", resultado.get(1).getNome());
        verify(alunoRepository).findAll();
    }

    @Test
    @DisplayName("Teste do GetMapping de alunos em determinado curso")
    public void deveVerificarOFindAllByCurso(){
        Aluno alunoExistente = Aluno.builder()
        .id((long)1).nome("Lucas").build();

        Curso cursoExistente = Curso.builder()
        .id((long)1).nome("Biologia").build();

        alunoExistente.setCursos(List.of(cursoExistente));
        cursoExistente.setAlunos(List.of(alunoExistente));

        when(cursoService.achaCursoPorId(cursoExistente.getId())).thenReturn(Optional.of(cursoExistente));
        when(alunoService.achaTodosOsAlunosDeUmCurso(cursoExistente.getId())).thenReturn(List.of(alunoExistente));

        List<Aluno> alunos = alunoService.achaTodosOsAlunosDeUmCurso((long)1);

        assertEquals("Lucas", alunos.get(0).getNome());
    }

    @Test
    @DisplayName("Teste para ver se cai na exception de não encontrar o curso")
    void deveRetornarNotFoundCurso(){

        when(cursoService.achaCursoPorId((long)2)).thenReturn(Optional.empty());

        try {
            alunoService.achaTodosOsAlunosDeUmCurso((long)2);
            fail("Não deu exception");
        } catch(Exception e){
            assertEquals("Curso não encontrado!", e.getMessage());
            verify(cursoService).achaCursoPorId((long)2);
        }
    }

    @Test
    @DisplayName("Teste para criar novo Aluno")
    void deveCriarAlunoValido(){
        CriaAlunoRequest criaAluno = CriaAlunoRequest.builder()
        .nome("Pedro").build();

        Aluno alunoNovo = Aluno.builder()
        .nome(criaAluno.getNome()).idade(criaAluno.getIdade()).build();

        CriaAlunoResponse novoAluno = alunoService.criaNovoAluno(criaAluno);

        assertEquals(novoAluno.getNome(), "Pedro");
        verify(alunoRepository).findByEmail(criaAluno.getEmail());
        verify(alunoRepository).save(alunoNovo);
    }

    @Test
    @DisplayName("Teste para garantir que a exception é lançada ao tentar utilizar um email já cadastrado")
    void deveLancarExcepitonParaEmailRepetido(){
        CriaAlunoRequest criaAluno = CriaAlunoRequest.builder()
        .email("email@email.com").build();

        Aluno alunoExistente = Aluno.builder()
        .email("email@email.com").build();

        when(alunoRepository.findByEmail(criaAluno.getEmail()))
                            .thenReturn(Optional.of(alunoExistente));

        try{
            alunoService.criaNovoAluno(criaAluno);
            fail("Não deu exception");
        } catch(Exception e){
            assertEquals("Email já cadastrado!", e.getMessage());
            verify(alunoRepository).findByEmail(criaAluno.getEmail());
        }
    }

    @Test
    @DisplayName("Teste para atualizar aluno existente")
    void deveAtualizarAluno(){
        AtualizaAlunoRequest atualizaAluno = AtualizaAlunoRequest.builder()
        .nome("Lucas")
        .idade(22)
        .genero(null)
        .build();

        Aluno alunoExistente = Aluno.builder()
        .id((long)1)
        .nome("Pedro")
        .idade(23)
        .genero("Masculino")
        .build();

        when(alunoRepository.findById(alunoExistente.getId()))
                            .thenReturn(Optional.of(alunoExistente));

        AtualizarAlunoResponse alunoAtualizado = alunoService.atualizaAluno((long)1, atualizaAluno);

        assertEquals("Lucas", alunoAtualizado.getNome());
        assertEquals(22, alunoAtualizado.getIdade());
        assertEquals("Masculino", alunoExistente.getGenero());
        verify(alunoRepository).findById((long)1);
    }

    @Test
    @DisplayName("Garantir Exception caso o id não exista no banco")
    void deveLancarExcepitonParaAlunoNãoExistente(){

        AtualizaAlunoRequest atualizaAluno = AtualizaAlunoRequest.builder()
        .nome("Lucas").build();
        
        Aluno alunoExistente = Aluno.builder()
        .nome("Pedro").build();

        when(alunoRepository.findById((long)2))
                            .thenReturn(Optional.empty());

        try{
            alunoService.atualizaAluno((long)2, atualizaAluno);
            fail("Não deu exception!");
        } catch(Exception e){
            assertEquals("Aluno não encontrado!", e.getMessage());
            assertEquals(alunoExistente.getNome(), "Pedro");
            verify(alunoRepository).findById((long)2);
        }
    }

    @Test
    @DisplayName("Teste para deletar o Aluno")
    void deveDeletarOAluno(){
        Aluno alunoExistente = Aluno.builder()
        .id((long)1).build();

        when(alunoRepository.findById((long)1))
                            .thenReturn(Optional.of(alunoExistente));

        alunoService.deletaAluno((long)1);

        verify(alunoRepository).findById((long)1);
        verify(alunoRepository).deleteById((long)1);
    }

    @Test
    @DisplayName("Deve testar a exception caso o Aluno não esteja no banco")
    void deveLançarException(){

        when(alunoRepository.findById((long)2))
                            .thenReturn(Optional.empty());

        try{
            alunoService.deletaAluno((long)2);
            fail("Não deu exception");
        } catch(Exception e){
            assertEquals("Aluno não encontrado!", e.getMessage());
            verify(alunoRepository).findById((long)2);
        }
    }

    @Test
    @DisplayName("Testar deletar um aluno com matriculas pra ver se cai na exception")
    void deveRetornarExceptionDeAlunoTemMatriculas(){
        Aluno alunoExistente = Aluno.builder()
        .id((long)1).build();

        Curso cursoExistente = Curso.builder()
        .id((long)1).alunos(List.of(alunoExistente)).build();

        alunoExistente.setCursos(List.of(cursoExistente));

        when(alunoRepository.findById((long)1)).thenReturn(Optional.of(alunoExistente));

        try{
            alunoService.deletaAluno(alunoExistente.getId());
            fail("Não deu Exception");
        } catch(Exception e){
            assertEquals("Você não pode deletar um aluno com matriculas em andamento", e.getMessage());
            verify(alunoRepository).findById((long)1);
        }
    }
}
