package com.example.escola.Integracao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.escola.dto.AtualizaAlunoRequest;
import com.example.escola.dto.CriaAlunoRequest;
import com.example.escola.model.Aluno;
import com.example.escola.model.Curso;
import com.example.escola.model.Matricula;
import com.example.escola.repository.AlunoRepository;
import com.example.escola.repository.CursoRepository;
import com.example.escola.repository.MatriculaRepository;
import com.example.escola.service.impl.AlunoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testa o Controller do Aluno")
public class AlunoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CursoRepository cursoRepository;
    
    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private AlunoServiceImpl alunoService;

    @Test
    @DisplayName("Testa a criação de um aluno válido")
    public void testeDadoUmAlunoValido_QuandoSalvar_DeveRetornar201() throws Exception{
        CriaAlunoRequest criaAluno = CriaAlunoRequest.builder()
        .nome("Lucas")
        .idade(22)
        .email("lucas@gmail.com")
        .genero("Masculino")
        .build();

        String criaAlunoString = mapper.writeValueAsString(criaAluno);

        mockMvc.perform(post("/alunos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(criaAlunoString))
        .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Testa exception de não criar aluno com e-mail duplicado")
    public void testeDadoAlunoInvalido_QuandoSalvar_DeveRetornarBadRequest() throws Exception{
        Aluno alunoExistente = Aluno.builder()
        .id((long) 1)
        .nome("Pedro")
        .idade(21)
        .email("pedro@gmail.com")
        .genero("Masculino")
        .build();
        alunoRepository.save(alunoExistente);

        CriaAlunoRequest criaAluno = CriaAlunoRequest.builder()
        .nome("Lucas")
        .idade(22)
        .email("pedro@gmail.com")
        .genero("Masculino")
        .build();

        String criaAlunoString = mapper.writeValueAsString(criaAluno);

        mockMvc.perform(post("/alunos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(criaAlunoString))
        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Testa se atualiza o Aluno corretamente")
    public void testeDadoAlunoExistente_QuandoAtualizar_DeveRetornar200() throws Exception{

        Aluno alunoExsitente = Aluno.builder()
        .id((long)1)
        .nome("Pedro")
        .idade(22)
        .email("pedro@gmail.com")
        .genero("Masculino")
        .build();
        alunoRepository.save(alunoExsitente);

        AtualizaAlunoRequest atualizaAluno = AtualizaAlunoRequest.builder()
        .nome("Lucas")
        .idade(21)
        .genero(null)
        .build();
        String atualizaAlunoString = mapper.writeValueAsString(atualizaAluno);

        Aluno alunoAtualizado = alunoService.atualizaAluno((long) 1, atualizaAluno);

        mockMvc.perform(patch("/alunos/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(atualizaAlunoString))
        .andExpect(status().isOk());

        assertEquals(alunoAtualizado.getNome(), "Lucas");
        assertEquals(alunoAtualizado.getIdade(), 21);
        assertNotNull(alunoAtualizado.getGenero());
        assertEquals(alunoAtualizado.getGenero(), "Masculino");
    }

    @Test
    @DisplayName("Testa se sai a exception caso o aluno não exista no banco")
    public void testeDadoAlunoInexistente_QuandoAtualizar_DeveRetornarNotFound() throws Exception{
        alunoRepository.deleteAll();

        AtualizaAlunoRequest atualizaAluno = AtualizaAlunoRequest.builder()
        .nome("Lucas")
        .idade(22)
        .genero("Masculino")
        .build();
        String atualizaAlunoString = mapper.writeValueAsString(atualizaAluno);

        mockMvc.perform(patch("/alunos/2")
        .contentType(MediaType.APPLICATION_JSON)
        .content(atualizaAlunoString))
        .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Testa se deleta um aluno")
    public void testeDadoAlunoValido_QuandoDeleta_DeveRetornarOk() throws Exception{
        Aluno alunoExistente = Aluno.builder()
        .id((long)1)
        .nome("Pedro")
        .idade(22)
        .email("pedro@gmail.com")
        .genero("Masculino")
        .build();
        alunoRepository.save(alunoExistente);
        String aluno_id = alunoExistente.getId().toString();

        mockMvc.perform(delete("/alunos/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(aluno_id))
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Testa se cai no BadRequest de ter matriculas ao deletar o aluno")
    public void testeDadoAlunoComMatriculas_QuandoDeletar_DeveRetornarBadRequest() throws Exception{
        Aluno alunoExistente = Aluno.builder()
        .id((long)1)
        .nome("Pedro")
        .idade(22)
        .email("pedro@gmail.com")
        .genero("Masculino")
        .build();
        alunoRepository.save(alunoExistente);

        Curso cursoExistente = Curso.builder()
        .id((long)1)
        .nome("Ciências da Computação")
        .descricao("Curso de Ciências da Computação")
        .carga_horaria(2000)
        .build();
        cursoRepository.save(cursoExistente);
        
        Matricula matriculaExistente = Matricula.builder()
        .id((long)1)
        .aluno(alunoExistente)
        .curso(cursoExistente)
        .build();
        matriculaRepository.save(matriculaExistente);
        String matricula_id = matriculaExistente.getId().toString();

        mockMvc.perform(delete("/alunos/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(matricula_id))
        .andExpect(status().isBadRequest());
    }
}
