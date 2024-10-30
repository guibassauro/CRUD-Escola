package com.example.escola.Integracao;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.escola.dto.Request.AtualizaCursoRequest;
import com.example.escola.dto.Request.CriaCursoRequest;
import com.example.escola.dto.Request.CriaMatriculaRequest;
import com.example.escola.model.Aluno;
import com.example.escola.model.Curso;
import com.example.escola.repository.AlunoRepository;
import com.example.escola.repository.CursoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    ObjectMapper mapper;
    
    @Autowired
    private AlunoRepository alunoRepository;

    @Test
    @DisplayName("Deve retornar lista de Cursos")
    public void testaGetCursos() throws Exception{
        Curso curso = Curso.builder()
        .nome("Biologia").build();

        cursoRepository.save(curso);

        mockMvc.perform(MockMvcRequestBuilders.get("/cursos"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].nome").value("Biologia"));
    }

    @Test
    @DisplayName("Deve criar um curso e retornar uma resposta")
    public void testeCriarCurso() throws Exception{
        CriaCursoRequest criaCurso = CriaCursoRequest.builder()
        .nome("Biologia").build();

        String json = mapper.writeValueAsString(criaCurso);

        mockMvc.perform(MockMvcRequestBuilders.post("/cursos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.nome").value("Biologia"));
    }

    @Test
    @DisplayName("Deve retornar resposta de atualização do curso")
    public void testeAtualizarAluno() throws Exception{
        Curso curso = Curso.builder()
        .id((long)1).descricao("Biologia").build();
        
        cursoRepository.save(curso);

        AtualizaCursoRequest atualizaCurso = AtualizaCursoRequest.builder()
        .descricao("Ciências da Computação").build();
        
        String json = mapper.writeValueAsString(atualizaCurso);

        mockMvc.perform(MockMvcRequestBuilders.patch("/cursos/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.descricao").value("Ciências da Computação"));
    }

    @Test
    @DisplayName("Testa Deletar Curso")
    public void testeDeletarCurso() throws Exception{
        Curso curso = Curso.builder()
        .id((long)1).build();

        cursoRepository.save(curso);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cursos/1"))
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar BadRequest para tentar deletar um curso com matriculas")
    public void testeBadRequestDeDeletarCurso() throws Exception{
        Aluno aluno = Aluno.builder()
        .id((long)1).build();

        Curso curso = Curso.builder()
        .id((long)1).build();

        cursoRepository.save(curso);
        alunoRepository.save(aluno);

        CriaMatriculaRequest criaMatricula = CriaMatriculaRequest.builder()
        .aluno_id(aluno.getId()).curso_id(curso.getId()).build();

        String json = mapper.writeValueAsString(criaMatricula);

        mockMvc.perform(MockMvcRequestBuilders.patch("/matriculas/matricular")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json));

        mockMvc.perform(MockMvcRequestBuilders.delete("/cursos/1"))
        .andExpect(status().isBadRequest());
    }
}
