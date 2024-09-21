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

import com.example.escola.dto.CriaMatriculaRequest;
import com.example.escola.model.Aluno;
import com.example.escola.model.Curso;
import com.example.escola.model.Matricula;
import com.example.escola.repository.AlunoRepository;
import com.example.escola.repository.CursoRepository;
import com.example.escola.repository.MatriculaRepository;
import com.example.escola.service.impl.MatriculaServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testa o MatriculaService")
public class MatriculaServiceTest {
    
    @Mock
    private MatriculaRepository matriculaRepository;

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private MatriculaServiceImpl matriculaService;
    

    @Test
    @DisplayName("Testa se sai a exception de não achar o aluno")
    void deveRetornarNotFoundAluno(){
        CriaMatriculaRequest criaMatricula = new CriaMatriculaRequest();
        criaMatricula.setIdDoAluno((long)2);
        
        when(alunoRepository.findById((long)2)).thenReturn(Optional.empty());

        try{
            matriculaService.matriculaAluno(criaMatricula);
            fail("Não deu exception");
        } catch(Exception e){
            assertEquals(e.getMessage(), "Aluno 2 não encontrado");
        }
    }

    @Test
    @DisplayName("Testa se sai a exception formatada de não achar o curso")
    void deveRetornarNotFoundCurso(){
        Aluno alunoExistente = new Aluno();
        alunoExistente.setId((long)1);
        CriaMatriculaRequest criaMatricula = new CriaMatriculaRequest();
        criaMatricula.setIdDoCurso((long)2);

        when(alunoRepository.findById(criaMatricula.getIdDoAluno()))
                            .thenReturn(Optional.of(alunoExistente));
        
        when(cursoRepository.findById((long)2))
                            .thenReturn(Optional.empty());

        try{
            matriculaService.matriculaAluno(criaMatricula);
            fail("Não deu exception");
        } catch(Exception e){
            assertEquals(e.getMessage(), "Curso 2 não encontrado");
        }
    }

    @Test
    @DisplayName("Testa exception de não poder matricular o mesmo aluno 2 vezes no mesmo curso")
    void deveRetornarBadExceptionMatriculaJaFeita(){
        Aluno aluno = new Aluno();
        Curso curso = new Curso();

        Matricula matriculaExistente = new Matricula();
        matriculaExistente.setAluno(aluno);
        matriculaExistente.setCurso(curso);
        aluno.setMatriculas(List.of(matriculaExistente));

        CriaMatriculaRequest criaMatricula = new CriaMatriculaRequest();
        criaMatricula.setIdDoAluno(aluno.getId());
        criaMatricula.setIdDoCurso(curso.getId());

        when(alunoRepository.findById(aluno.getId())).thenReturn(Optional.of(aluno));
        when(cursoRepository.findById(curso.getId())).thenReturn(Optional.of(curso));

        try{
            matriculaService.matriculaAluno(criaMatricula);
            fail("Não deu exception");
        } catch(Exception e){
            assertEquals(e.getMessage(), "Aluno já está matriculado neste curso!");
        }
    }

    @Test
    @DisplayName("Teste para matricular aluno")
    void deveRetornarMatriculaFeita(){
        Aluno aluno = new Aluno();
        aluno.setNome("Lucas");
        Curso curso = new Curso();
        curso.setNome("Ciências da Computação");

        CriaMatriculaRequest criaMatricula = new CriaMatriculaRequest();
        criaMatricula.setIdDoAluno(aluno.getId());
        criaMatricula.setIdDoCurso(curso.getId());

        when(alunoRepository.findById(aluno.getId())).thenReturn(Optional.of(aluno));
        when(cursoRepository.findById(curso.getId())).thenReturn(Optional.of(curso));

        Matricula novaMatricula = matriculaService.matriculaAluno(criaMatricula);

        assertEquals(novaMatricula.getAluno().getNome(), "Lucas");
        assertEquals(novaMatricula.getCurso().getNome(), "Ciências da Computação");
    }

    @Test
    @DisplayName("Verifica se a matricula foi desfeita corretamente")
    void deveDesmatricularOAluno(){
        Matricula matriculaExistente = new Matricula();

        when(matriculaRepository.findById(matriculaExistente.getId()))
                                .thenReturn(Optional.of(matriculaExistente));

        matriculaService.desmatriculaAluno(matriculaExistente.getId());

        verify(matriculaRepository).deleteById(matriculaExistente.getId());
    }

}
