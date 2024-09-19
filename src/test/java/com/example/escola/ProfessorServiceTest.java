package com.example.escola;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.escola.dto.AtualizaProfessorRequest;
import com.example.escola.dto.CriaProfessorRequest;
import com.example.escola.model.Curso;
import com.example.escola.model.Professor;
import com.example.escola.repository.CursoRepository;
import com.example.escola.repository.ProfessorRepository;
import com.example.escola.service.impl.ProfessorServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testa o ProfessorService")
public class ProfessorServiceTest {

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private ProfessorServiceImpl professorService;

    @Test
    @DisplayName("Deve retornar professores de um curso")
    void deveRetornarProfessoresPorCurso(){
        Curso curso = new Curso();
        Professor profe1 = new Professor();
        Professor profe2 = new Professor();

        profe1.setNome("Lucas");
        profe2.setNome("Pedro");
        curso.setProfessores(List.of(profe1, profe2));

        when(cursoRepository.findById(curso.getId())).thenReturn(Optional.of(curso));

        List<Professor> resultado = professorService.achaTodosOsProfessoresDeUmCurso(curso.getId());

        assertEquals(resultado.get(0).getNome(), "Lucas");
        assertEquals(resultado.get(1).getNome(), "Pedro");
        
    }

    @Test
    @DisplayName("Deve retornar exception formatada")
    void deveRetornarExceptionFormatada(){
        CriaProfessorRequest criaProfessor = new CriaProfessorRequest();
        criaProfessor.setCursos_id(List.of((long)1));
        
        when(cursoRepository.findById((long)1)).thenReturn(Optional.empty());

        try{
            professorService.criaNovoProfessor(criaProfessor);
            fail("Não deu exception");
        } catch(Exception e){
            assertEquals(e.getMessage(), "Curso 1 não encontrado");
        }
    }

    @Test
    @DisplayName("Deve criar um novo Professor sem cursos")
    void criaNovoProfessor(){
        CriaProfessorRequest criaProfessor = new CriaProfessorRequest();
        criaProfessor.setNome("Lucas");
        criaProfessor.setCursos_id(List.of());

        Professor novoProfessor = professorService.criaNovoProfessor(criaProfessor);

        assertEquals(novoProfessor.getNome(), "Lucas");
    }

    @Test
    @DisplayName("Deve criar um novo professor com cursos")
    void criaNovoProfessorComCursos(){
        Curso curso1 = new Curso();
        curso1.setId((long)1);
        curso1.setNome("Ciências da Computação");

        CriaProfessorRequest criaProfessor = new CriaProfessorRequest();
        criaProfessor.setNome("Lucas");
        criaProfessor.setCursos_id(List.of(curso1.getId()));

        when(cursoRepository.findAllById(criaProfessor.getCursos_id())).thenReturn(List.of(curso1));
        when(cursoRepository.findById((long)1)).thenReturn(Optional.of(curso1));

        Professor novoProfessor = professorService.criaNovoProfessor(criaProfessor);

        assertEquals(novoProfessor.getNome(), "Lucas");
        assertEquals(novoProfessor.getCursos().get(0).getNome(), "Ciências da Computação");
    }

    @Test
    @DisplayName("Testa atualizar professor mudando os seus cursos")
    void deveAtualizarProfessorValidoMudandoOsCursos(){
        Curso curso1 = new Curso();
        curso1.setId((long)1);
        curso1.setNome("Ciências da Computação");

        Curso curso2 = new Curso();
        curso2.setId((long)2);
        curso2.setNome("Matemática");

        Professor professorExistente = new Professor();
        professorExistente.setNome("Lucas");
        professorExistente.setCursos(List.of(curso1));

        AtualizaProfessorRequest atualizaProfessor = new AtualizaProfessorRequest();
        atualizaProfessor.setNome("Pedro");
        atualizaProfessor.setCursos_id(List.of((long)2));

        when(professorRepository.findById(professorExistente.getId()))
                                .thenReturn(Optional.of(professorExistente));

        when(cursoRepository.findAllById(atualizaProfessor.getCursos_id()))
                                .thenReturn(List.of(curso2));

        when(cursoRepository.findById((long)2))
                                .thenReturn(Optional.of(curso2));
        
        professorService.atualizaProfessor(professorExistente.getId(), atualizaProfessor);

        assertEquals(professorExistente.getNome(), "Pedro");
        assertEquals(professorExistente.getCursos().get(0).getNome(), "Matemática");
    }

    
}
