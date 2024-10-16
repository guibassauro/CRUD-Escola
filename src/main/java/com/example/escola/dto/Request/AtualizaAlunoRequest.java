package com.example.escola.dto.Request;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AtualizaAlunoRequest {
    
    private String nome;
    private Integer idade;
    private String genero;
}
