package com.example.escola.service;

import java.util.List;

import com.example.escola.dto.Request.CriaMatriculaRequest;
import com.example.escola.dto.Response.CriaMatriculaResponse;
import com.example.escola.model.Matricula;

public interface MatriculaService {
    
    List<Matricula> achaTodasAsMatriculas();

    CriaMatriculaResponse matriculaAluno(CriaMatriculaRequest criaMatricula);

    void desmatriculaAluno(Long matricula_id);
}
