package com.example.escola.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.escola.dto.Request.CriaMatriculaRequest;
import com.example.escola.dto.Response.CriaMatriculaResponse;
import com.example.escola.exception.BadRequestException;
import com.example.escola.exception.NotFoundException;
import com.example.escola.model.Aluno;
import com.example.escola.model.Curso;
import com.example.escola.model.Matricula;
import com.example.escola.repository.AlunoRepository;
import com.example.escola.repository.CursoRepository;
import com.example.escola.repository.MatriculaRepository;
import com.example.escola.service.MatriculaService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MatriculaServiceImpl implements MatriculaService{
    
    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;

    @Override
    public List<Matricula> achaTodasAsMatriculas(){
        return matriculaRepository.findAll();
    }

    @Override
    public CriaMatriculaResponse matriculaAluno(CriaMatriculaRequest criaMatricula){
        Optional<Aluno> existeAluno = alunoRepository.findById(criaMatricula.getIdDoAluno());
        Optional<Curso> existeCurso = cursoRepository.findById(criaMatricula.getIdDoCurso());
        List<Matricula> matriculasDoAluno = matriculaRepository.findAllByAluno_Id(criaMatricula.getIdDoAluno());

        if(existeAluno.isEmpty()){
            throw new NotFoundException("Aluno " + criaMatricula.getIdDoAluno() + " não encontrado");
        }

        if(existeCurso.isEmpty()){
            throw new NotFoundException("Curso " + criaMatricula.getIdDoCurso() + " não encontrado");
        }

        matriculasDoAluno.forEach(matricula -> {
            if(matricula.getCurso().getId() == criaMatricula.getIdDoCurso()){
                throw new BadRequestException("Aluno já está matriculado neste curso!");
            }
        });

        Matricula novaMatricula = Matricula.builder()
        .aluno(existeAluno.get())
        .curso(existeCurso.get())
        .build();

        matriculaRepository.save(novaMatricula);

        CriaMatriculaResponse resposta = CriaMatriculaResponse.builder()
        .id(novaMatricula.getId())
        .aluno(novaMatricula.getAluno())
        .curso(novaMatricula.getCurso())
        .build();

        return resposta;
    }

    @Override
    public void desmatriculaAluno(Long matricula_id){
        Optional<Matricula> existeMatricula = matriculaRepository.findById(matricula_id);

        if(existeMatricula.isEmpty()){
            throw new NotFoundException("Matricula não encontrada");
        }


        matriculaRepository.deleteById(matricula_id);
    }
}
