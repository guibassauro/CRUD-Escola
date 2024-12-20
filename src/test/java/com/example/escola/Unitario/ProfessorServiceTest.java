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

import com.example.escola.dto.Request.AtualizaProfessorRequest;
import com.example.escola.dto.Request.CriaProfessorRequest;
import com.example.escola.dto.Response.AtualizaProfessorResponse;
import com.example.escola.dto.Response.CriaProfessorResponse;
import com.example.escola.model.Curso;
import com.example.escola.model.Professor;
import com.example.escola.repository.ProfessorRepository;
import com.example.escola.service.impl.CursoServiceImpl;
import com.example.escola.service.impl.ProfessorServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testa o ProfessorService")
public class ProfessorServiceTest {

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private CursoServiceImpl cursoService;

    @InjectMocks
    private ProfessorServiceImpl professorService;

    @Test
    @DisplayName("Deve retornar professores de um curso")
    void deveRetornarProfessoresPorCurso(){
        Professor profe1 = Professor.builder()
        .nome("Lucas").build();
        Professor profe2 = Professor.builder()
        .nome("Pedro").build();
        Curso curso = Curso.builder()
        .professores(List.of(profe1, profe2))
        .build();

        when(cursoService.achaCursoPorId(curso.getId())).thenReturn(Optional.of(curso));

        List<Professor> resultado = professorService.achaTodosOsProfessoresDeUmCurso(curso.getId());

        assertEquals(resultado.get(0).getNome(), "Lucas");
        assertEquals(resultado.get(1).getNome(), "Pedro");
        verify(cursoService).achaCursoPorId(curso.getId());
        
    }

    @Test
    @DisplayName("Deve retornar exception formatada")
    void deveRetornarExceptionFormatada(){
        CriaProfessorRequest criaProfessor = CriaProfessorRequest.builder()
        .cursos_id(List.of((long)1)).build();
        
        when(cursoService.achaCursoPorId((long)1)).thenReturn(Optional.empty());

        try{
            professorService.criaNovoProfessor(criaProfessor);
            fail("Não deu exception");
        } catch(Exception e){
            assertEquals("Curso 1 não encontrado", e.getMessage());
            verify(cursoService).achaCursoPorId((long)1);
        }
    }

    @Test
    @DisplayName("Deve criar um novo Professor sem cursos")
    void criaNovoProfessor(){
        CriaProfessorRequest criaProfessor = CriaProfessorRequest.builder()
        .nome("Lucas")
        .cursos_id(List.of())
        .build();

        CriaProfessorResponse novoProfessor = professorService.criaNovoProfessor(criaProfessor);

        Professor professorCriado = Professor.builder()
        .id(novoProfessor.getId())
        .nome(novoProfessor.getNome())
        .cursos(novoProfessor.getCursos())
        .build();

        assertEquals(novoProfessor.getNome(), "Lucas");
        verify(cursoService).achaTodosPorCursoId(List.of());
        verify(professorRepository).save(professorCriado);
    }

    @Test
    @DisplayName("Deve criar um novo professor com cursos")
    void criaNovoProfessorComCursos(){
        Curso curso1 = Curso.builder()
        .id((long)1)
        .nome("Ciências da Computação")
        .build();

        CriaProfessorRequest criaProfessor = CriaProfessorRequest.builder()
        .nome("Lucas")
        .cursos_id(List.of((long)1))
        .build();

        when(cursoService.achaTodosPorCursoId(criaProfessor.getCursos_id())).thenReturn(List.of(curso1));
        when(cursoService.achaCursoPorId((long)1)).thenReturn(Optional.of(curso1));

        CriaProfessorResponse novoProfessor = professorService.criaNovoProfessor(criaProfessor);

        Professor professorCriado = Professor.builder()
        .id(novoProfessor.getId())
        .nome(novoProfessor.getNome())
        .cursos(novoProfessor.getCursos())
        .build();

        assertEquals(novoProfessor.getNome(), "Lucas");
        assertEquals(novoProfessor.getCursos().get(0).getNome(), "Ciências da Computação");
        verify(cursoService).achaCursoPorId((long)1);
        verify(cursoService).achaTodosPorCursoId(List.of((long)1));
        verify(professorRepository).save(professorCriado);
    }

    @Test
    @DisplayName("Testa atualizar professor mudando os seus cursos")
    void deveAtualizarProfessorValidoMudandoOsCursos(){
        Curso curso = Curso.builder()
        .id((long)1)
        .nome("Ciências da Computação")
        .build();

        Curso curso2 = Curso.builder()
        .id((long)2)
        .nome("Matemática")
        .build();

        Professor professorExistente = Professor.builder()
        .id((long)1)
        .nome("Lucas")
        .cursos(List.of(curso))
        .build();

        AtualizaProfessorRequest atualizaProfessor = AtualizaProfessorRequest.builder()
        .nome("Pedro")
        .cursos_id(List.of((long)2)).build();

        when(professorRepository.findById((long)1)).thenReturn(Optional.of(professorExistente));
        when(cursoService.achaTodosPorCursoId(atualizaProfessor.getCursos_id())).thenReturn(List.of(curso2));
        when(cursoService.achaCursoPorId((long)2)).thenReturn(Optional.of(curso2));
        
        AtualizaProfessorResponse professorAtt = professorService.atualizaProfessor(professorExistente.getId(), atualizaProfessor);

        Professor professorAtualizado = Professor.builder()
        .id(professorAtt.getId())
        .nome(professorAtt.getNome())
        .cursos(professorAtt.getCursos())
        .build();

        assertEquals("Pedro", professorAtt.getNome());
        assertEquals("Matemática", professorAtt.getCursos().get(0).getNome());
        verify(professorRepository).findById(professorExistente.getId());
        verify(cursoService).achaCursoPorId((long)2);
        verify(cursoService).achaTodosPorCursoId(List.of((long)2));
        verify(professorRepository).save(professorAtualizado);
    }

    
}
