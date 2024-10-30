package com.example.escola.Integracao;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.escola.dto.Request.CriaMatriculaRequest;
import com.example.escola.model.Aluno;
import com.example.escola.model.Curso;
import com.example.escola.repository.AlunoRepository;
import com.example.escola.repository.CursoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest
public class MatriculaControllerTest {
    
    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("Deve Matricular o aluno")
    public void testeMatricular() throws Exception{
        Aluno aluno = Aluno.builder()
        .id((long)1).nome("João").build();

        Curso curso = Curso.builder()
        .id((long)1).nome("Biologia").build();

        alunoRepository.save(aluno);
        cursoRepository.save(curso);

        CriaMatriculaRequest criaMatricula = CriaMatriculaRequest.builder()
        .aluno_id(aluno.getId()).curso_id(curso.getId()).build();

        String json = mapper.writeValueAsString(criaMatricula);

        mockMvc.perform(MockMvcRequestBuilders.patch("/matriculas/matricular")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk())
        .andExpect(content().string("João foi matriculado no curso Biologia."));
    }

    @Test
    @DisplayName("Deve retoranar BadRequest por tentar matricular o aluno 2x")
    public void testeBadRequestMatricular() throws Exception{
        Aluno aluno = Aluno.builder()
        .id((long)1).nome("João").build();
        alunoRepository.save(aluno);

        Curso curso = Curso.builder()
        .id((long)1).nome("Ciências da Computação").build();
        cursoRepository.save(curso);

        CriaMatriculaRequest criaMatricula = CriaMatriculaRequest.builder()
        .aluno_id(aluno.getId()).curso_id(curso.getId()).build();
        String json = mapper.writeValueAsString(criaMatricula);

        mockMvc.perform(MockMvcRequestBuilders.patch("/matriculas/matricular")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json));

        mockMvc.perform(MockMvcRequestBuilders.patch("/matriculas/matricular")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Testa Desmatricular Aluno")
    public void testeDesmatricularAluno() throws Exception{
        Aluno aluno = Aluno.builder()
        .id((long)1).nome("João").build();
        alunoRepository.save(aluno);

        Curso curso = Curso.builder()
        .id((long)1).nome("Biologia").build();
        cursoRepository.save(curso);

        CriaMatriculaRequest criaMatricula = CriaMatriculaRequest.builder()
        .aluno_id(aluno.getId()).curso_id(curso.getId()).build();
        String json = mapper.writeValueAsString(criaMatricula);

        mockMvc.perform(MockMvcRequestBuilders.patch("/matriculas/matricular")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json));

        mockMvc.perform(MockMvcRequestBuilders.patch("/matriculas/desmatricular")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk())
        .andExpect(content().string("João foi desmatriculado do curso Biologia."));
    }

    @Test
    @DisplayName("Testa BadRequest ao tentar desmatricular alguém não matriculado")
    public void testeBadRequestDesmatricular() throws Exception{
        Aluno aluno = Aluno.builder()
        .id((long)1).nome("João").build();
        alunoRepository.save(aluno);

        Curso curso = Curso.builder()
        .id((long)1).nome("Ciências da Computação").build();
        cursoRepository.save(curso);

        CriaMatriculaRequest criaMatricula = CriaMatriculaRequest.builder()
        .aluno_id(aluno.getId()).curso_id(curso.getId()).build();
        String json = mapper.writeValueAsString(criaMatricula);

        mockMvc.perform(MockMvcRequestBuilders.patch("/matriculas/desmatricular")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isBadRequest());
    }
}
