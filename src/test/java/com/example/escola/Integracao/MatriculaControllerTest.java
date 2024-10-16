package com.example.escola.Integracao;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.escola.dto.Request.CriaMatriculaRequest;
import com.example.escola.model.Aluno;
import com.example.escola.model.Curso;
import com.example.escola.model.Matricula;
import com.example.escola.repository.AlunoRepository;
import com.example.escola.repository.CursoRepository;
import com.example.escola.repository.MatriculaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class MatriculaControllerTest {
    
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @BeforeEach
    public void criaDados(){
        Aluno alunoExistente = Aluno.builder()
        .id((long)1).nome("Lucas").idade(22)
        .email("lucas@gmail.com").genero("Masculino")
        .build();
        alunoRepository.save(alunoExistente);

        Curso cursoExistente = Curso.builder()
        .id((long)1).nome("Ciências da Computação")
        .descricao("Curso de Ciências da Computação")
        .carga_horaria(2000).build();
        cursoRepository.save(cursoExistente);
    }

    @Test
    @DisplayName("Testa se cria matricula")
    public void testeDadoMatriculaValida_QuandoSalvar_DeveRetornar201() throws Exception{
        CriaMatriculaRequest criaMatricula = CriaMatriculaRequest.builder()
        .idDoAluno((long)1).idDoCurso((long)1).build();
        String enviaComoJson = mapper.writeValueAsString(criaMatricula);

        mockMvc.perform(post("/matriculas")
        .contentType(MediaType.APPLICATION_JSON)
        .content(enviaComoJson))
        .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Testa se cai na Exception de não deixar um aluno entrar no mesmo curso 2 vezes")
    public void testeDadoMatriculaRepetida_QuandoCriar_DeveRetornar400() throws Exception{
        Matricula matriculaExistente = Matricula.builder()
        .id((long)1).aluno(alunoRepository.findById((long)1).get())
        .curso(cursoRepository.findById((long)1).get()).build();
        matriculaRepository.save(matriculaExistente);

        CriaMatriculaRequest criaMatricula = CriaMatriculaRequest.builder()
        .idDoAluno((long)1).idDoCurso((long)1).build();
        String enviaComoJson = mapper.writeValueAsString(criaMatricula);

        mockMvc.perform(post("/matriculas")
        .contentType(MediaType.APPLICATION_JSON)
        .content(enviaComoJson))
        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Testa desmatricular um aluno de um curso")
    public void testeDadoMatriculaValida_QuandoDeletar_DeveRetornar200() throws Exception{
        Matricula matriculaExistente = Matricula.builder()
        .id((long)1)
        .aluno(alunoRepository.findById((long)1).get())
        .curso(cursoRepository.findById((long)1).get())
        .build();
        matriculaRepository.save(matriculaExistente);

        mockMvc.perform(delete("/matriculas/1"))
        .andExpect(status().isOk());
    }
}
