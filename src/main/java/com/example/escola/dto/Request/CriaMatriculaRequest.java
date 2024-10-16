package com.example.escola.dto.Request;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CriaMatriculaRequest {
    
    private Long idDoAluno;
    private Long idDoCurso;
}
