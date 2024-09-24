package com.example.escola.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CriaMatriculaRequest {
    
    private Long idDoAluno;
    private Long idDoCurso;
}
