package com.example.escola.controller;

import org.springframework.http.ResponseEntity;

import com.example.escola.dto.AtualizaAlunoRequest;
import com.example.escola.dto.CriaAlunoRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface AlunoController {
    
    @Operation(
        summary = "Acha todos os alunos",
        description = "Retorna uma lista com todos os alunos do banco de dados",
        responses = {
            @ApiResponse(responseCode = "200", description = "Sucedeu em retornar a lista")
        }
    )
    ResponseEntity<Object> achaTodosOsAlunos();

    @Operation(
        summary = "Acha todos os alunos de um curso",
        description = "Retorna uma lista com todos os alunos com matricula em determinado curso",
        parameters = {
            @Parameter(name = "curso_id", description = "ID do curso do qual você quer acessar os alunos")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Sucedeu em retornar a lista"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado no banco")
        }
    )
    ResponseEntity<Object> achaTodosOsAlunosDeUmCurso(Long curso_id);

    @Operation(
        summary = "Cria aluno",
        description = "Cria um novo aluno no banco de dados",
        parameters = {
            @Parameter(name = "criaAluno", description = "Corpo da requisição do aluno")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Sucedeu em criar o aluno"),
            @ApiResponse(responseCode = "400", description = "Não permitiu a criação com um e-mail já cadastrado")
        }
    )
    ResponseEntity<Object> criaNovoAluno(CriaAlunoRequest criaAluno);

    @Operation(
        summary = "Atualiza aluno",
        description = "Atualiza aluno que já existe",
        parameters = {
            @Parameter(name = "aluno_id", description = "id do aluno a ser atualizado"),
            @Parameter(name = "atualizaAluno", description = "corpo de atualização do aluno")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Sucedeu em atualizar um aluno"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado no banco")
        }
    )
    ResponseEntity<Object> atualizaAluno(Long aluno_id, AtualizaAlunoRequest atualizaAluno);

    @Operation(
        summary = "Deleta aluno",
        description = "Deleta aluno do banco",
        parameters = {
            @Parameter(name = "aluno_id", description = "id do aluno a ser deletado")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Sucedeu em deletar o aluno"),
            @ApiResponse(responseCode = "400", description = "Não permitiu deletar aluno com matriculas"),
            @ApiResponse(responseCode = "404", description = "Não encontrou aluno no banco")
        }
    )
    ResponseEntity<Object> deletaAluno(Long aluno_id);
}
