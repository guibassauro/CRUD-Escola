package com.example.escola.dto.Response;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class MatriculaResponse {
    
    String nomeAluno;

    String nomeCurso;
}
