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
import com.example.escola.dto.AtualizaAlunoRequest;
import com.example.escola.dto.CriaAlunoRequest;
import com.example.escola.model.Aluno;
import com.example.escola.model.Curso;
import com.example.escola.model.Matricula;
import com.example.escola.repository.AlunoRepository;
import com.example.escola.repository.CursoRepository;
import com.example.escola.repository.MatriculaRepository;
import com.example.escola.service.impl.AlunoServiceImpl;
import com.example.escola.service.impl.CursoServiceImpl;
import com.example.escola.service.impl.MatriculaServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testar Aluno Service")
public class AlunoServiceTest {
    
    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private MatriculaRepository matriculaRepository;

    @InjectMocks
    private AlunoServiceImpl alunoService;

    @InjectMocks
    private CursoServiceImpl cursoService;

    @InjectMocks
    private MatriculaServiceImpl matriculaService;

    @Test
    @DisplayName("Teste para verificar funcionamento do GetMapping geral")
    void deveVerificarOFindAll(){
        Aluno aluno1 = new Aluno();
        aluno1.setNome("Pedro");

        Aluno aluno2 = new Aluno();
        aluno2.setNome("Lucas");

        when(alunoRepository.findAll())
                            .thenReturn(List.of(aluno1, aluno2));

        List<Aluno> resultado = alunoService.achaTodosOsAlunos();

        assertNotNull(resultado);
        assertEquals("Pedro", resultado.get(0).getNome());
        assertEquals("Lucas", resultado.get(1).getNome());
    }

    @Test
    @DisplayName("Teste do GetMapping de alunos em determinado curso")
    public void deveVerificarOFindAllByCurso(){
        Aluno alunoExistente = Aluno.builder()
        .id((long)1).nome("Lucas").build();

        Curso cursoExistente = Curso.builder()
        .id((long)1).nome("Biologia").build();

        Matricula matriculaExistente = Matricula.builder()
        .id((long)1).aluno(alunoExistente).curso(cursoExistente).build();

        when(cursoRepository.findById((long)1)).thenReturn(Optional.of(cursoExistente));
        when(matriculaRepository.findAllByCurso_Id((long)1)).thenReturn(List.of(matriculaExistente));
        
        List<Aluno> listaDeAlunos = alunoService.achaTodosOsAlunosDeUmCurso((long)1);

        assertEquals(listaDeAlunos.get(0).getNome(), "Lucas");
    }

    @Test
    @DisplayName("Teste para ver se cai na exception de não encontrar o curso")
    void deveRetornarNotFoundCurso(){

        when(cursoRepository.findById((long)2)).thenReturn(Optional.empty());

        try {
            alunoService.achaTodosOsAlunosDeUmCurso((long)2);
            fail("Não deu exception");
        } catch(Exception e){
            assertEquals("Curso não encontrado!", e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para criar novo Aluno")
    void deveCriarAlunoValido(){
        CriaAlunoRequest criaAluno = new CriaAlunoRequest("Pedro", 22, "pedro@gmaile.com", "masculino");

        Aluno novoAluno = alunoService.criaNovoAluno(criaAluno);

        assertEquals(novoAluno.getNome(), "Pedro");
    }

    @Test
    @DisplayName("Teste para garantir que a exception é lançada ao tentar utilizar um email já cadastrado")
    void deveLancarExcepitonParaEmailRepetido(){
        CriaAlunoRequest criaAluno = new CriaAlunoRequest();
        criaAluno.setEmail("email@email.com");

        Aluno alunoJaExistente = new Aluno();
        alunoJaExistente.setEmail("email@email.com");

        when(alunoRepository.findByEmail(criaAluno.getEmail()))
                            .thenReturn(Optional.of(alunoJaExistente));

        try{
            alunoService.criaNovoAluno(criaAluno);
            fail("Não deu exception");
        } catch(Exception e){
            assertEquals("Email já cadastrado!", e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para atualizar aluno existente")
    void deveAtualizarAluno(){
        AtualizaAlunoRequest atualizaAluno = new AtualizaAlunoRequest();
        atualizaAluno.setNome("Lucas");

        Aluno alunoJaExistente = new Aluno();
        alunoJaExistente.setId((long) 1);
        alunoJaExistente.setNome("Pedro");

        when(alunoRepository.findById(alunoJaExistente.getId()))
                            .thenReturn(Optional.of(alunoJaExistente));

        alunoService.atualizaAluno((long)1, atualizaAluno);

        assertEquals(alunoJaExistente.getNome(), "Lucas");
    }

    @Test
    @DisplayName("Garantir Exception caso o id não exista no banco")
    void deveLancarExcepitonParaAlunoNãoExistente(){

        AtualizaAlunoRequest atualizaAluno = new AtualizaAlunoRequest();
        atualizaAluno.setNome("Lucas");
        
        Aluno alunoJaExistente = new Aluno();
        alunoJaExistente.setId((long)1);
        alunoJaExistente.setNome("Pedro");

        when(alunoRepository.findById((long)2))
                            .thenReturn(Optional.empty());

        try{
            alunoService.atualizaAluno((long)2, atualizaAluno);
            fail("Não deu exception!");
        } catch(Exception e){
            assertEquals("Aluno não encontrado!", e.getMessage());
            assertEquals(alunoJaExistente.getNome(), "Pedro");
        }
    }

    @Test
    @DisplayName("Teste para deletar o Aluno")
    void deveDeletarOAluno(){
        Aluno alunoJaExistente = new Aluno();
        alunoJaExistente.setId((long)1);

        when(alunoRepository.findById((long)1))
                            .thenReturn(Optional.of(alunoJaExistente));

        alunoService.deletaAluno((long)1);

        verify(alunoRepository).deleteById((long)1);
    }

    @Test
    @DisplayName("Deve testar a exception caso o Aluno não esteja no banco")
    void deveLançarException(){
        Aluno alunoJaExistente = new Aluno();
        alunoJaExistente.setId((long)1);

        when(alunoRepository.findById((long)2))
                            .thenReturn(Optional.empty());

        try{
            alunoService.deletaAluno((long)2);
            fail("Não deu exception");
        } catch(Exception e){
            assertEquals("Aluno não encontrado!", e.getMessage());
        }
    }

    @Test
    @DisplayName("Testar deletar um aluno com matriculas pra ver se cai na exception")
    void deveRetornarExceptionDeAlunoTemMatriculas(){
        Aluno alunoExistente = Aluno.builder()
        .id((long)1).build();

        Matricula matriculaExistente = Matricula.builder()
        .id((long)1).aluno(alunoExistente).build();

        when(alunoRepository.findById((long)1)).thenReturn(Optional.of(alunoExistente));
        when(matriculaRepository.findAllByAluno_Id((long)1)).thenReturn(List.of(matriculaExistente));

        try{
            alunoService.deletaAluno(alunoExistente.getId());
            fail("Não deu Exception");
        } catch(Exception e){
            assertEquals("Você não pode deletar um aluno com matriculas em andamento", e.getMessage());
        }
    }
}
