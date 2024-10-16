package com.example.escola.dto.Request;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CriaAlunoRequest {

    private String nome;
    private int idade;
    private String email;
    private String genero;
}
