package com.example.escola.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriaCursoRequest {
    
    private String nome;
    private String descricao;
    private int carga_horaria;
}
