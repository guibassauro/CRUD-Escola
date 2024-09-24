package com.example.escola.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CriaCursoRequest {
    
    private String nome;
    private String descricao;
    private int carga_horaria;
}
