package com.example.escola.dto.Response;

import java.util.List;

import com.example.escola.model.Curso;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AtualizaProfessorResponse {
    
    private Long id;
    private String nome;
    private Integer idade;
    private List<Curso> cursos;
}
