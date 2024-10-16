package com.example.escola.dto.Request;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AtualizaCursoRequest {
    
    private String descricao;
    private Integer carga_horaria;
}
