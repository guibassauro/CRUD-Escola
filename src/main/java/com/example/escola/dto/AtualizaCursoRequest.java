package com.example.escola.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AtualizaCursoRequest {
    
    private String descricao;
    private Integer carga_horaria;
}
