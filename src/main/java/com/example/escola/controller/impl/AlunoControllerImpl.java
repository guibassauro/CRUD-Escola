package com.example.escola.controller.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.escola.controller.AlunoController;
import com.example.escola.dto.AtualizaAlunoRequest;
import com.example.escola.dto.CriaAlunoRequest;
import com.example.escola.service.AlunoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoControllerImpl implements AlunoController{
    private final AlunoService alunoService;

    @Override
    @GetMapping
    public ResponseEntity<Object> achaTodosOsAlunos(){
        return ResponseEntity.ok().body(alunoService.achaTodosOsAlunos());
    }

    @Override
    @GetMapping("/{curso_id}")
    public ResponseEntity<Object> achaTodosOsAlunosDeUmCurso(@PathVariable Long curso_id){
        return ResponseEntity.ok().body(alunoService.achaTodosOsAlunosDeUmCurso(curso_id));
    }

    @Override
    @PostMapping
    public ResponseEntity<Object> criaNovoAluno(@RequestBody CriaAlunoRequest criaAluno){
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoService.criaNovoAluno(criaAluno));
    }

    @Override
    @PatchMapping("/{aluno_id}")
    public ResponseEntity<Object> atualizaAluno(@PathVariable Long aluno_id, @RequestBody AtualizaAlunoRequest atualizaAluno){
        return ResponseEntity.ok().body(alunoService.atualizaAluno(aluno_id, atualizaAluno));
    }

    @Override
    @DeleteMapping("/{aluno_id}")
    public ResponseEntity<Object> deletaAluno(@PathVariable Long aluno_id){
        alunoService.deletaAluno(aluno_id);
        return ResponseEntity.ok().body("Aluno deletado");
    }
}
