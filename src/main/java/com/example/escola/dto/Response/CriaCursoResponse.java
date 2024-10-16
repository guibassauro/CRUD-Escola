package com.example.escola.dto.Response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CriaCursoResponse {
    
    private Long id;
    private String nome;
    private String descricao;
    private Integer cargaHoraria; 
}
