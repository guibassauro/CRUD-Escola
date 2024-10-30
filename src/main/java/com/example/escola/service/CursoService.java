package com.example.escola.service;

import java.util.List;

import com.example.escola.dto.Request.AtualizaCursoRequest;
import com.example.escola.dto.Request.CriaCursoRequest;
import com.example.escola.dto.Response.AtualizaCursoResponse;
import com.example.escola.dto.Response.CriaCursoResponse;
import com.example.escola.dto.Response.MatriculaResponse;
import com.example.escola.model.Aluno;
import com.example.escola.model.Curso;

public interface CursoService {
    
    List<Curso> achaTodosOsCurso();

    CriaCursoResponse criaNovoCurso(CriaCursoRequest criaCurso);

    AtualizaCursoResponse atualizaCurso(Long curso_id, AtualizaCursoRequest atualizaCurso);

    void deletaCurso(Long curso_id);

    List<Aluno> listaAlunosDoCurso(Long curso_id);

    List<MatriculaResponse> listaMatriculas();
}
