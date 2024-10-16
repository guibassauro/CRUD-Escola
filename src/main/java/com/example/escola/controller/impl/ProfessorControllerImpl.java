package com.example.escola.controller.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.escola.controller.ProfessorController;
import com.example.escola.dto.Request.AtualizaProfessorRequest;
import com.example.escola.dto.Request.CriaProfessorRequest;
import com.example.escola.service.ProfessorService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/professores")
@RestController
public class ProfessorControllerImpl implements ProfessorController{
    
    private final ProfessorService professorService;

    @Override
    @GetMapping
    public ResponseEntity<Object> achaTodosOsProfessores(){
        return ResponseEntity.ok().body(professorService.achaTodosOsProfessores());
    }

    @Override
    @GetMapping("/{curso_id}")
    public ResponseEntity<Object> achaTodosOsProfessoresDeUmCurso(@PathVariable Long curso_id){
        return ResponseEntity.ok().body(professorService.achaTodosOsProfessoresDeUmCurso(curso_id));
    }

    @Override
    @PostMapping
    public ResponseEntity<Object> criaNovoProfessor(@RequestBody CriaProfessorRequest criaProfessor){
        return ResponseEntity.status(HttpStatus.CREATED).body(professorService.criaNovoProfessor(criaProfessor));
    }

    @Override
    @PatchMapping("/{professor_id}")
    public ResponseEntity<Object> atualizaProfessor(@PathVariable Long professor_id, @RequestBody AtualizaProfessorRequest atualizaProfessor){
        return ResponseEntity.ok().body(professorService.atualizaProfessor(professor_id, atualizaProfessor));
    }
}
