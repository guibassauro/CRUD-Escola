package com.example.escola.Integracao;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.escola.dto.AtualizaProfessorRequest;
import com.example.escola.dto.CriaProfessorRequest;
import com.example.escola.model.Curso;
import com.example.escola.model.Professor;
import com.example.escola.repository.CursoRepository;
import com.example.escola.repository.ProfessorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class ProfessorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;
    
    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    @DisplayName("Testa se acha os professores de um curso")
    public void testeDadoCursoValido_QuandoBuscar_DeveRetornar200() throws Exception{
        Curso cursoExistente = Curso.builder()
        .id((long)1).nome("Ciências da Computação")
        .descricao("Curso de Ciências da Computação")
        .carga_horaria(2000).build();
        cursoRepository.save(cursoExistente);

        mockMvc.perform(get("/professores/1"))
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Testa a criação de um professor")
    public void testeDadoProfessorValido_QuandoCriar_DeveRetornar201() throws Exception{
        CriaProfessorRequest criaProfessor = CriaProfessorRequest.builder()
        .nome("Jaime").idade(50).cursos_id(List.of()).build();
        String enviaComoJson = mapper.writeValueAsString(criaProfessor);

        mockMvc.perform(post("/professores")
        .contentType(MediaType.APPLICATION_JSON)
        .content(enviaComoJson))
        .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Testa se cai na Exception de curso não encontrado")
    public void testeDadoCursoInvalidoParaProfessor_QuandoCriar_DeveRetornar404() throws Exception{
        CriaProfessorRequest criaProfessor = CriaProfessorRequest.builder()
        .nome("Licurgo").idade(22).cursos_id(List.of((long)1)).build();
        String enviaComoJson = mapper.writeValueAsString(criaProfessor);

        mockMvc.perform(post("/professores")
        .contentType(MediaType.APPLICATION_JSON)
        .content(enviaComoJson))
        .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Testa se atualiza o professor")
    public void testeDadoProfessorValido_QuandoAtualizar_DeveRetornar200() throws Exception{

        Professor professorExistente = Professor.builder()
        .id((long)1).nome("Junior").idade(22).cursos(List.of()).build();
        professorRepository.save(professorExistente);

        AtualizaProfessorRequest atualizaProfessor = AtualizaProfessorRequest.builder()
        .nome("Lucario").idade(null).cursos_id(null).build();
        String enviaComoJson = mapper.writeValueAsString(atualizaProfessor);
        
        mockMvc.perform(patch("/professores/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(enviaComoJson))
        .andExpect(status().isOk());
    }

}
