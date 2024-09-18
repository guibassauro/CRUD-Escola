package com.example.escola.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.escola.dto.CriaMatriculaRequest;
import com.example.escola.service.MatriculaService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/matriculas")
@RestController
public class MatriculaController {
    
    private final MatriculaService matriculaService;

    @GetMapping
    public ResponseEntity<Object> achaTodasAsMatriculas(){
        return ResponseEntity.ok().body(matriculaService.achaTodasAsMatriculas());
    }

    @PostMapping
    public ResponseEntity<Object> matriculaAluno(@RequestBody CriaMatriculaRequest criaMatricula){
        return ResponseEntity.status(HttpStatus.CREATED).body(matriculaService.matriculaAluno(criaMatricula));
    }

    @DeleteMapping("/{aluno_id}")
    public ResponseEntity<Object> desmatriculaAluno(@PathVariable Long aluno_id){
        matriculaService.desmatriculaAluno(aluno_id);
        return ResponseEntity.ok().body("Aluno desmatriculado!");
    }

}
