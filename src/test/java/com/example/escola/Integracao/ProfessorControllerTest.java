package com.example.escola.Integracao;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.escola.dto.Request.AtualizaProfessorRequest;
import com.example.escola.dto.Request.CriaProfessorRequest;
import com.example.escola.model.Curso;
import com.example.escola.model.Professor;
import com.example.escola.repository.CursoRepository;
import com.example.escola.repository.ProfessorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest
public class ProfessorControllerTest {
    
    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("Deve retornar lista de professores")
    public void testeGetProfessores() throws Exception{
        Professor professor = Professor.builder()
        .nome("Jaime").build();

        professorRepository.save(professor);

        mockMvc.perform(MockMvcRequestBuilders.get("/professores"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].nome").value("Jaime"));
    }

    @Test
    @DisplayName("Deve retornar lista de Professores de um Curso")
    public void testeGetProfessoresByCurso() throws Exception{
        Curso curso = Curso.builder()
        .id((long)1).build();

        Professor professor = Professor.builder()
        .nome("Jaime").cursos(List.of(curso)).build();

        cursoRepository.save(curso);
        professorRepository.save(professor);

        mockMvc.perform(MockMvcRequestBuilders.get("/professores/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].nome").value("Jaime"));
    }

    @Test
    @DisplayName("Testa criar um professor")
    public void testaCriarProfessor() throws Exception{
        CriaProfessorRequest criaProfessor = CriaProfessorRequest.builder()
        .nome("Jaime").cursos_id(List.of()).build();

        String json = mapper.writeValueAsString(criaProfessor);

        mockMvc.perform(MockMvcRequestBuilders.post("/professores")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.nome").value("Jaime"));
    }

    @Test
    @DisplayName("Testa atualizar o Professor")
    public void testeAtualizarProfessor() throws Exception{
        Professor professor = Professor.builder()
        .id((long)1).nome("Jaime").build();

        professorRepository.save(professor);

        AtualizaProfessorRequest atualizaProfessor = AtualizaProfessorRequest.builder()
        .nome("Marcelo").cursos_id(List.of()).build();

        String json = mapper.writeValueAsString(atualizaProfessor);

        mockMvc.perform(MockMvcRequestBuilders.patch("/professores/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome").value("Marcelo"));
    }
}
