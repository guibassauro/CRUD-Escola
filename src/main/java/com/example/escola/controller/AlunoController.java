package com.example.escola.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.escola.dto.AtualizaAlunoRequest;
import com.example.escola.dto.CriaAlunoRequest;
import com.example.escola.service.AlunoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {
    
    private final AlunoService alunoService;

    @GetMapping
    public ResponseEntity<Object> achaTodosOsAlunos(){
        return ResponseEntity.ok().body(alunoService.achaTodosOsAlunos());
    }

    @GetMapping("/{curso_id}")
    public ResponseEntity<Object> achaTodosOsAlunosDeUmCurso(@PathVariable Long curso_id){
        return ResponseEntity.ok().body(alunoService.achaTodosOsAlunosDeUmCurso(curso_id));
    }

    @PostMapping
    public ResponseEntity<Object> criaNovoAluno(@RequestBody CriaAlunoRequest criaAluno){
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoService.criaNovoAluno(criaAluno));
    }

    @PutMapping("/{aluno_id}")
    public ResponseEntity<Object> atualizaAluno(@PathVariable Long aluno_id, @RequestBody AtualizaAlunoRequest atualizaAluno){
        return ResponseEntity.ok().body(alunoService.atualizaAluno(aluno_id, atualizaAluno));
    }

    @DeleteMapping("/{aluno_id}")
    public ResponseEntity<Object> deletaAluno(@PathVariable Long aluno_id){
        alunoService.deletaAluno(aluno_id);
        return ResponseEntity.ok().body("Aluno deletado");
    }
}
