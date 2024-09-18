package com.example.escola.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriaMatriculaRequest {
    
    private Long idDoAluno;
    private Long idDoCurso;
}
