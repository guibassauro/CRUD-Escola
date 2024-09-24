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

import com.example.escola.controller.CursoController;
import com.example.escola.dto.AtualizaCursoRequest;
import com.example.escola.dto.CriaCursoRequest;
import com.example.escola.service.CursoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/cursos")
@RestController
public class CursoControllerImpl implements CursoController{
    
    private final CursoService cursoService;

    @Override
    @GetMapping
    public ResponseEntity<Object> achaTodosOsCursos(){
        return ResponseEntity.ok().body(cursoService.achaTodosOsCurso());
    }

    @Override
    @PostMapping
    public ResponseEntity<Object> criaNovoCurso(@RequestBody CriaCursoRequest criaCurso){
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.criaNovoCurso(criaCurso));
    }

    @Override
    @PatchMapping("/{curso_id}")
    public ResponseEntity<Object> atualizaCurso(@PathVariable Long curso_id, @RequestBody AtualizaCursoRequest atualizaCurso){
        return ResponseEntity.ok().body(cursoService.atualizaCurso(curso_id, atualizaCurso));
    }

    @Override
    @DeleteMapping("/{curso_id}")
    public ResponseEntity<Object> deletaCurso(@PathVariable Long curso_id){
        cursoService.deletaCurso(curso_id);
        return ResponseEntity.ok().body("Curso deletado!");
    }
}
