package com.example.escola.controller;

import org.springframework.http.ResponseEntity;

import com.example.escola.dto.AtualizaCursoRequest;
import com.example.escola.dto.CriaCursoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface CursoController {
    
    @Operation(
        summary = "Acha todos os cursos",
        description = "Retorna uma lista com todos os cursos no banco de dados",
        responses = {
            @ApiResponse(responseCode = "200", description = "Sucedeu em retornar a lista")
        }
    )
    ResponseEntity<Object> achaTodosOsCursos();

    @Operation(
        summary = "Cria Curso",
        description = "Cria um novo curso no banco de dados",
        parameters = {
            @Parameter(name = "criaCurso", description = "Corpo da requisição para criação do curso")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Sucedeu em criar o Curso")
        }
    )
    ResponseEntity<Object> criaNovoCurso(CriaCursoRequest criaCurso);

    @Operation(
        summary = "Atualiza Curso",
        description = "Atualiza um curso que já existe",
        parameters = {
            @Parameter(name = "curso_id", description = "Id do curso a ser atualizado"),
            @Parameter(name = "atualizCurso", description = "Corpo para atualização o curso")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Sucedeu em atualizar o curso"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado no banco")
        }
    )
    ResponseEntity<Object> atualizaCurso(Long curso_id, AtualizaCursoRequest atualizaCurso);

    @Operation(
        summary = "Deleta Curso",
        description = "Deleta um determinado curso do banco de dados",
        parameters = {
            @Parameter(name = "curso_id", description = "Id do curso a ser deletado")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Sucedeu em deletar o curso"),
            @ApiResponse(responseCode = "400", description = "Não permitiu deletar um curso com matriculas"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado no banco")
        }
    )
    ResponseEntity<Object> deletaCurso(Long curso_id);
}
