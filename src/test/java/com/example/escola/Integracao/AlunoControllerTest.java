package com.example.escola.Integracao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders; 
import org.springframework.http.MediaType;

import com.example.escola.dto.Request.AtualizaAlunoRequest;
import com.example.escola.dto.Request.CriaAlunoRequest;
import com.example.escola.dto.Request.CriaMatriculaRequest;
import com.example.escola.model.Aluno;
import com.example.escola.model.Curso;
import com.example.escola.repository.AlunoRepository;
import com.example.escola.repository.CursoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    @DisplayName("Deve exibir pessoas cadastradas")
    public void getAlunosTest() throws Exception{
        Aluno aluno = Aluno.builder()
        .nome("João").build();

        alunoRepository.save(aluno);

        mockMvc.perform(MockMvcRequestBuilders.get("/alunos"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].nome").value("João"));
    }

    @Test
    @DisplayName("Deve listar alunos de um curso")
    public void getAlunosPorCurso() throws Exception{
        Aluno aluno = Aluno.builder()
        .id((long)1).nome("João").build();

        Curso curso = Curso.builder()
        .id((long)1).nome("Ciências da Computação").build();

        alunoRepository.save(aluno);
        cursoRepository.save(curso);

        CriaMatriculaRequest criaMatricula = CriaMatriculaRequest.builder()
        .aluno_id((long)1).curso_id((long)1).build();

        String json = mapper.writeValueAsString(criaMatricula);

        mockMvc.perform(MockMvcRequestBuilders.patch("/matriculas/matricular")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json));

        mockMvc.perform(MockMvcRequestBuilders.get("/alunos/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].nome").value("João"));

    }

    @Test
    @DisplayName("Deve Criar um aluno válido")
    public void criaAlunoValidoTest() throws Exception{

        CriaAlunoRequest criaAluno = CriaAlunoRequest.builder()
        .nome("João").idade(22).email("joao@gmail.com").genero("Masculino")
        .build();

        String json = mapper.writeValueAsString(criaAluno);

        mockMvc.perform(MockMvcRequestBuilders.post("/alunos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.nome").value(criaAluno.getNome()));
    }

    @Test
    @DisplayName("Deve retornar BadRequest por tentar criar aluno com email ja usado")
    public void testaBadRequestDoCriaAluno() throws Exception{
        Aluno aluno = Aluno.builder()
        .email("joao@gmail.com").build();

        alunoRepository.save(aluno);

        CriaAlunoRequest criaAluno = CriaAlunoRequest.builder()
        .email("joao@gmail.com").build();

        String json = mapper.writeValueAsString(criaAluno);

        mockMvc.perform(MockMvcRequestBuilders.post("/alunos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve Atualizar o Aluno")
    public void testaPatchAluno() throws Exception{
        Aluno aluno = Aluno.builder()
        .id((long)1).nome("João").build();

        alunoRepository.save(aluno);

        AtualizaAlunoRequest atualizaAluno = AtualizaAlunoRequest.builder()
        .nome("Pedro").build();

        String json = mapper.writeValueAsString(atualizaAluno);

        mockMvc.perform(MockMvcRequestBuilders.patch("/alunos/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome").value("Pedro"));
    }

    @Test
    @DisplayName("Testa deletar o aluno do curso")
    public void testaDeletarAluno() throws Exception{
        Aluno aluno = Aluno.builder()
        .id((long)1).nome("João").build();
        
        alunoRepository.save(aluno);

        mockMvc.perform(MockMvcRequestBuilders.delete("/alunos/1"))
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Testa BadRequest de deletar aluno com cursos")
    public void testaBadRequestDeDeletarAluno() throws Exception{
        Curso curso = Curso.builder()
        .id((long)1).build();

        Aluno aluno = Aluno.builder()
        .id((long)1).build();

        alunoRepository.save(aluno);
        cursoRepository.save(curso);

        CriaMatriculaRequest criaMatricula = CriaMatriculaRequest.builder()
        .aluno_id(aluno.getId()).curso_id(curso.getId()).build();

        String json = mapper.writeValueAsString(criaMatricula);

        mockMvc.perform(MockMvcRequestBuilders.patch("/matriculas/matricular")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json));

        mockMvc.perform(MockMvcRequestBuilders.delete("/alunos/1"))
        .andExpect(status().isBadRequest());
    }
    
}
