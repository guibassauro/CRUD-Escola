package com.example.escola.dto.Request;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CriaCursoRequest {
    
    private String nome;
    private String descricao;
    private int carga_horaria;
}
