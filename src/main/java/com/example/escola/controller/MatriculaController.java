package com.example.escola.controller;

import org.springframework.http.ResponseEntity;
import com.example.escola.dto.CriaMatriculaRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface MatriculaController {
    
    @Operation(
        summary = "Acha Matriculas",
        description = "Retorna uma lista com todas as matriculas",
        responses = {
            @ApiResponse(responseCode = "200", description = "Sucedeu em retornar a lista")
        }
    )
    ResponseEntity<Object> achaTodasAsMatriculas();

    @Operation(
        summary = "Matricula Aluno",
        description = "Cria uma matricula para ligar aluno a curso",
        parameters = {
            @Parameter(name = "criaMatricula", description = "Corpo com id do aluno e do curso para gerar a matricula")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Sucedeu em criar a matricula"),
            @ApiResponse(responseCode = "400", description = "Não permitiu matricular um aluno duas vezes no mesmo curso"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado no banco de dados"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado no banco de dados")
        }
    )
    ResponseEntity<Object> matriculaAluno(CriaMatriculaRequest criaMatricula);

    @Operation(
        summary = "Desmatricula aluno",
        description = "Deleta matricula do banco, deletando relação entre aluno e curso",
        parameters = {
            @Parameter(name = "matricula_id", description = "Id da matricula a ser deletada")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Sucedeu em deletar a matricula do banco"),
            @ApiResponse(responseCode = "404", description = "Matricula não encontrada")
        }
    )
    ResponseEntity<Object> desmatriculaAluno(Long matricula_id);
}
