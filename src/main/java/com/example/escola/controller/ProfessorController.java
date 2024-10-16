package com.example.escola.controller;

import org.springframework.http.ResponseEntity;

import com.example.escola.dto.Request.AtualizaProfessorRequest;
import com.example.escola.dto.Request.CriaProfessorRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface ProfessorController {
    
    @Operation(
        summary = "Acha todos os professores",
        description = "Retorna uma lista com todos os professores do banco de dados",
        responses = {
            @ApiResponse(responseCode = "200", description = "Sucedeu em retornar lista")
        }
    )
    ResponseEntity<Object> achaTodosOsProfessores();

    @Operation(
        summary = "Acha todos os professores de um curso",
        description = "Retorna uma lista com todos os professores de um curso",
        parameters = {
            @Parameter(name = "curso_id", description = "Id do curso a serem retornados os professores")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Sucedeu em retornar a lista"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado no banco")
        }
    )
    ResponseEntity<Object> achaTodosOsProfessoresDeUmCurso(Long curso_id);

    @Operation(
        summary = "Cria Professor",
        description = "Cria novo professor no banco de dados",
        parameters = {
            @Parameter(name = "criaProfessor", description = "Corpo de requisição para criação do objeto")
        },
        responses = {
            @ApiResponse(responseCode = "201", description = "Sucedeu em criar o Professor"),
            @ApiResponse(responseCode = "404", description = "Um dos cursos não foi encontrado")
        }
    )
    ResponseEntity<Object> criaNovoProfessor(CriaProfessorRequest criaProfessor);

    @Operation(
        summary = "Atualiza Professor",
        description = "Atualiza dados de um professor que já existe",
        parameters = {
            @Parameter(name = "professor_id", description = "Id do professor a ser atualizado"),
            @Parameter(name = "atualizaProfessor", description = "Corpo de atualização do professor")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Sucedeu em atualizar o professor"),
            @ApiResponse(responseCode = "404", description = "Professor não encontrado no banco"),
            @ApiResponse(responseCode = "404", description = "Um dos cursos do professor não foi encontrado")
        }
    )
    ResponseEntity<Object> atualizaProfessor(Long professor_id, AtualizaProfessorRequest atualizaProfessor);
}
