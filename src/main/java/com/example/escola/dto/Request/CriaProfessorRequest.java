package com.example.escola.dto.Request;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CriaProfessorRequest {
    
    private String nome;
    private int idade;
    private List<Long> cursos_id;
}
