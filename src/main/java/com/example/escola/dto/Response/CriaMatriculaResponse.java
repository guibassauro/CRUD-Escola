package com.example.escola.dto.Response;

import com.example.escola.model.Aluno;
import com.example.escola.model.Curso;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CriaMatriculaResponse {
    
    private Long id;
    private Aluno aluno;
    private Curso curso;
}
