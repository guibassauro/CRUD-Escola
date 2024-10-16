package com.example.escola.service;

import java.util.List;

import com.example.escola.dto.Request.AtualizaAlunoRequest;
import com.example.escola.dto.Request.CriaAlunoRequest;
import com.example.escola.dto.Response.AtualizarAlunoResponse;
import com.example.escola.dto.Response.CriaAlunoResponse;
import com.example.escola.model.Aluno;

public interface AlunoService {
    
    List<Aluno> achaTodosOsAlunos();

    public List<Aluno> achaTodosOsAlunosDeUmCurso(Long curso_id);

    CriaAlunoResponse criaNovoAluno(CriaAlunoRequest criaAluno);

    AtualizarAlunoResponse atualizaAluno(Long aluno_id, AtualizaAlunoRequest atualizaAluno);

    void deletaAluno(Long aluno_id);
}
