package com.example.escola.service;

import java.util.List;

import com.example.escola.dto.AtualizaAlunoRequest;
import com.example.escola.dto.CriaAlunoRequest;
import com.example.escola.model.Aluno;

public interface AlunoService {
    
    List<Aluno> achaTodosOsAlunos();

    public List<Aluno> achaTodosOsAlunosDeUmCurso(Long curso_id);

    Aluno criaNovoAluno(CriaAlunoRequest criaAluno);

    Aluno atualizaAluno(Long aluno_id, AtualizaAlunoRequest atualizaAluno);

    void deletaAluno(Long aluno_id);
}
