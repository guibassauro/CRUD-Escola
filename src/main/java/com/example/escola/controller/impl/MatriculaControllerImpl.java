package com.example.escola.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.escola.controller.MatriculaController;
import com.example.escola.dto.Request.CriaMatriculaRequest;
import com.example.escola.service.AlunoService;
import com.example.escola.service.CursoService;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/matriculas")
@RequiredArgsConstructor
public class MatriculaControllerImpl implements MatriculaController{
    private final AlunoService alunoService;
    private final CursoService cursoService;

    @Override
    @GetMapping
    public ResponseEntity<Object> listaMatriculas(){
        return ResponseEntity.ok().body(cursoService.listaMatriculas());
    }

    @Override
    @PatchMapping("/matricular")
    public ResponseEntity<Object> matriculaAluno(@RequestBody CriaMatriculaRequest criaMatricula){
        return ResponseEntity.ok().body(alunoService.matriculaAluno(criaMatricula));
    }

    @Override
    @PatchMapping("/desmatricular")
    public ResponseEntity<Object> desmatriculaAluno(@RequestBody CriaMatriculaRequest criaMatricula){
        return ResponseEntity.ok().body(alunoService.desmatriculaAluno(criaMatricula));
    }
}
