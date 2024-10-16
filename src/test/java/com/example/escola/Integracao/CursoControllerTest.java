package com.example.escola.Integracao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.escola.dto.Request.AtualizaCursoRequest;
import com.example.escola.dto.Request.CriaCursoRequest;
import com.example.escola.model.Aluno;
import com.example.escola.model.Curso;
import com.example.escola.model.Matricula;
import com.example.escola.repository.AlunoRepository;
import com.example.escola.repository.CursoRepository;
import com.example.escola.repository.MatriculaRepository;
import com.example.escola.service.CursoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class CursoControllerTest {
    
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;
    
    @Autowired
    private CursoService cursoService;

    @Test
    @DisplayName("Teste para criação de Curso valido")
    public void testeDadoCursoValido_QuandoSalvar_DeveRetornar201() throws Exception{
        CriaCursoRequest criaCurso = CriaCursoRequest.builder()
        .nome("Ciências da Computação")
        .descricao("Curso de Ciências da Computação")
        .carga_horaria(2000)
        .build();
        String enviaComoJson = mapper.writeValueAsString(criaCurso);

        mockMvc.perform(post("/cursos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(enviaComoJson))
        .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Testa se atualiza o curso valido")
    public void testeDadoCursoValido_QuandoAtualizar_DeveRetornar200() throws Exception{
        Curso cursoExistente = Curso.builder()
        .id((long)1)
        .nome("Ciências da Computação")
        .descricao("Curso de Ciências Computação")
        .carga_horaria(2000)
        .build();
        cursoRepository.save(cursoExistente);

        AtualizaCursoRequest atualizaCurso = AtualizaCursoRequest.builder()
        .descricao("Curso de Ciências da Computação")
        .carga_horaria(null)
        .build();
        String enviaComoString = mapper.writeValueAsString(atualizaCurso);

        mockMvc.perform(patch("/cursos/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(enviaComoString))
        .andExpect(status().isOk());
        
        Curso cursoAtualizado = cursoService.atualizaCurso((long)1, atualizaCurso);

        assertEquals(cursoAtualizado.getDescricao(), "Curso de Ciências da Computação");
        assertEquals(cursoAtualizado.getCarga_horaria(), 2000);
    }

    @Test
    @DisplayName("Testa se cai na Exception de não encontrar o curso para atualizar")
    public void testDadoCursoNãoExistente_QuandoAtualizar_DeveRetornar404() throws Exception{
        AtualizaCursoRequest atualizaCurso = AtualizaCursoRequest.builder()
        .descricao("Descrição atualizada")
        .carga_horaria(2000)
        .build();
        String enviaComoJson = mapper.writeValueAsString(atualizaCurso);

        mockMvc.perform(patch("/alunos/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(enviaComoJson))
        .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Testa se deleta o curso")
    public void testeDadoCursoValido_QuandoDeletar_DeveRetornar200() throws Exception{
        Curso cursoExistente = Curso.builder()
        .id((long)1)
        .nome("Ciências da computação")
        .descricao("Curo de Ciências da Computação")
        .carga_horaria(2000)
        .build();
        cursoRepository.save(cursoExistente);

        mockMvc.perform(delete("/cursos/1"))
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Testa se cai na Exception de não poder deletar cursos com matriculas")
    public void testeDadoCursoComMatriculas_QuandoDeletar_DeveRetornar400() throws Exception{
        Curso cursoExistente = Curso.builder()
        .id((long)1).nome("Ciências da Computação")
        .descricao("Curso de Ciências da Computação")
        .carga_horaria(2000).build();
        cursoRepository.save(cursoExistente);

        Aluno alunoExistente = Aluno.builder()
        .id((long) 1).nome("Lucas").idade(22).email("lucas@gmail.com")
        .genero("Masculino").build();
        alunoRepository.save(alunoExistente);

        Matricula matriculaExistente = Matricula.builder()
        .id((long)1).aluno(alunoExistente).curso(cursoExistente).build();
        matriculaRepository.save(matriculaExistente);

        mockMvc.perform(delete("/cursos/1"))
        .andExpect(status().isBadRequest());
    }
}
