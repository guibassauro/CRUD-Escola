package com.example.escola.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtualizaCursoRequest {
    
    private String descricao;
    private Integer carga_horaria;
}
