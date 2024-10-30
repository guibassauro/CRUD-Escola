package com.example.escola.dto.Request;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CriaMatriculaRequest {
    
    private Long aluno_id;
    private Long curso_id;
}
