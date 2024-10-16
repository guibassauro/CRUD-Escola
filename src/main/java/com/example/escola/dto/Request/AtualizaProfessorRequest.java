package com.example.escola.dto.Request;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AtualizaProfessorRequest {
    
    private String nome;
    private Integer idade;
    private List<Long> cursos_id;
}
