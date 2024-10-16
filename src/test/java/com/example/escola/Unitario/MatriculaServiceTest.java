package com.example.escola.Unitario;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.example.escola.dto.Request.CriaMatriculaRequest;
import com.example.escola.dto.Response.CriaMatriculaResponse;
import com.example.escola.model.Aluno;
import com.example.escola.model.Curso;
import com.example.escola.model.Matricula;
import com.example.escola.repository.MatriculaRepository;
import com.example.escola.service.impl.AlunoServiceImpl;
import com.example.escola.service.impl.CursoServiceImpl;
import com.example.escola.service.impl.MatriculaServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testa o MatriculaService")
public class MatriculaServiceTest {
    
    @Mock
    private MatriculaRepository matriculaRepository;

    @Mock
    private AlunoServiceImpl alunoService;

    @Mock
    private CursoServiceImpl cursoService;

    @InjectMocks
    private MatriculaServiceImpl matriculaService;
    

    @Test
    @DisplayName("Testa se sai a exception de não achar o aluno")
    void deveRetornarNotFoundAluno(){
        CriaMatriculaRequest criaMatricula = CriaMatriculaRequest.builder()
        .idDoAluno((long)2).build();
        
        when(alunoService.achaPorAlunoId((long)2)).thenReturn(Optional.empty());

        try{
            matriculaService.matriculaAluno(criaMatricula);
            fail("Não deu exception");
        } catch(Exception e){
            assertEquals(e.getMessage(), "Aluno 2 não encontrado");
            verify(alunoService).achaPorAlunoId((long)2);
        }
    }

    @Test
    @DisplayName("Testa se sai a exception formatada de não achar o curso")
    void deveRetornarNotFoundCurso(){
        Aluno alunoExistente = Aluno.builder()
        .id((long)1).build();
        CriaMatriculaRequest criaMatricula = CriaMatriculaRequest.builder()
        .idDoAluno((long)1).idDoCurso((long)2).build();
        
        when(alunoService.achaPorAlunoId((long)1)).thenReturn(Optional.of(alunoExistente));

        when(cursoService.achaCursoPorId((long)2)).thenReturn(Optional.empty());

        try{
            matriculaService.matriculaAluno(criaMatricula);
            fail("Não deu exception");
        } catch(Exception e){
            assertEquals(e.getMessage(), "Curso 2 não encontrado");
            verify(alunoService).achaPorAlunoId((long)1);
            verify(cursoService).achaCursoPorId((long)2);
        }
    }

    @Test
    @DisplayName("Testa exception de não poder matricular o mesmo aluno 2 vezes no mesmo curso")
    void deveRetornarBadExceptionMatriculaJaFeita(){
        Aluno alunoExistente = Aluno.builder()
        .id((long)1).build();

        Curso cursoExistente = Curso.builder()
        .id((long)1).build();

        Matricula matriculaExistente = Matricula.builder()
        .id((long)1).aluno(alunoExistente).curso(cursoExistente).build();

        CriaMatriculaRequest criaMatricula = CriaMatriculaRequest.builder()
        .idDoAluno((long)1).idDoCurso((long)1).build();

        when(alunoService.achaPorAlunoId((long)1)).thenReturn(Optional.of(alunoExistente));
        when(cursoService.achaCursoPorId((long)1)).thenReturn(Optional.of(cursoExistente));
        when(matriculaRepository.findAllByAluno_Id((long)1)).thenReturn(List.of(matriculaExistente));

        try{
            matriculaService.matriculaAluno(criaMatricula);
            fail("Não deu exception");
        } catch(Exception e){
            assertEquals("Aluno já está matriculado neste curso!", e.getMessage());
            verify(alunoService).achaPorAlunoId((long)1);
            verify(cursoService).achaCursoPorId((long)1);
            verify(matriculaRepository).findAllByAluno_Id((long)1);
        }
    }

    @Test
    @DisplayName("Teste para matricular aluno")
    void deveRetornarMatriculaFeita(){
        Aluno aluno = Aluno.builder()
        .id((long)1).nome("Lucas").build();
        Curso curso = Curso.builder()
        .id((long)1).nome("Ciências da Computação").build();

        CriaMatriculaRequest criaMatricula = CriaMatriculaRequest.builder()
        .idDoAluno(aluno.getId())
        .idDoCurso(curso.getId())
        .build();

        when(alunoService.achaPorAlunoId((long)1)).thenReturn(Optional.of(aluno));
        when(cursoService.achaCursoPorId((long)1)).thenReturn(Optional.of(curso));

        CriaMatriculaResponse novaMatricula = matriculaService.matriculaAluno(criaMatricula);

        Matricula matriculaFeita = Matricula.builder()
        .id(novaMatricula.getId())
        .aluno(novaMatricula.getAluno())
        .curso(novaMatricula.getCurso())
        .build();

        assertEquals(novaMatricula.getAluno().getNome(), "Lucas");
        assertEquals(novaMatricula.getCurso().getNome(), "Ciências da Computação");
        verify(alunoService).achaPorAlunoId((long)1);
        verify(cursoService).achaCursoPorId((long)1);
        verify(matriculaRepository).findAllByAluno_Id((long)1);
        verify(matriculaRepository).save(matriculaFeita);
    }

    @Test
    @DisplayName("Verifica se a matricula foi desfeita corretamente")
    void deveDesmatricularOAluno(){
        Matricula matriculaExistente = Matricula.builder().build();

        when(matriculaRepository.findById(matriculaExistente.getId()))
                                .thenReturn(Optional.of(matriculaExistente));

        matriculaService.desmatriculaAluno(matriculaExistente.getId());

        verify(matriculaRepository).findById(matriculaExistente.getId());
        verify(matriculaRepository).deleteById(matriculaExistente.getId());
    }

}
