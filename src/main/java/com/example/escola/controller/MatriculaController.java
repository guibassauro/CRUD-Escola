package com.example.escola.controller;

import org.springframework.http.ResponseEntity;

import com.example.escola.dto.Request.CriaMatriculaRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface MatriculaController {
    
    @Operation(
        summary = "Lista Matriculas",
        description = "Lista todas as matriculas do banco",
        responses = {
            @ApiResponse(responseCode = "200", description = "Sucedeu em listar todos as matriculas")
        }
    )
    ResponseEntity<Object> listaMatriculas();

    @Operation(
        summary = "Matricula Aluno",
        description = "Matricula aluno em curso",
        parameters = {
            @Parameter(name = "criaMatricula", description = "Corpo JSON com ID do aluno e curso")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Sucedeu em matricular o aluno no curso"),
            @ApiResponse(responseCode = "400", description = "Tentou matricular um aluno que já está matriculado neste curso"),
            @ApiResponse(responseCode = "404", description = "Não encontrou aluno ou curso por Id")
        }
    )
    ResponseEntity<Object> matriculaAluno(CriaMatriculaRequest criaMatricula);

    @Operation(
        summary = "Desmatricula Aluno",
        description = "Desmatricula Aluno por ids",
        parameters = {
            @Parameter(name = "criaMatricula", description = "Corpo JSON com ID de aluno e curso")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Sucedeu em desmatricular o aluno"),
            @ApiResponse(responseCode = "400", description = "Aluno não estava matriculado no curso"),
            @ApiResponse(responseCode = "404", description = "Não encontrou aluno ou curso")
        }
    )
    ResponseEntity<Object> desmatriculaAluno(CriaMatriculaRequest criaMatricula);
}
