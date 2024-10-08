package com.example.escola.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.escola.dto.AtualizaAlunoRequest;
import com.example.escola.dto.CriaAlunoRequest;
import com.example.escola.exception.BadRequestException;
import com.example.escola.exception.NotFoundException;
import com.example.escola.model.Aluno;
import com.example.escola.model.Curso;
import com.example.escola.model.Matricula;
import com.example.escola.repository.AlunoRepository;
import com.example.escola.repository.CursoRepository;
import com.example.escola.repository.MatriculaRepository;
import com.example.escola.service.AlunoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlunoServiceImpl implements AlunoService{
    
    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;
    private final MatriculaRepository matriculaRepository;

    @Override
    public List<Aluno> achaTodosOsAlunos(){
        return alunoRepository.findAll();
    }

    @Override
    public List<Aluno> achaTodosOsAlunosDeUmCurso(Long curso_id){
        Optional<Curso> existeCurso = cursoRepository.findById(curso_id);

        if(existeCurso.isEmpty()){
            throw new NotFoundException("Curso não encontrado!");
        }

        List<Matricula> listaDeMatriculas = matriculaRepository.findAllByCurso_Id(curso_id);
        List<Aluno> listaDeAlunos = new ArrayList<>();

        listaDeMatriculas.forEach(matricula -> {
            listaDeAlunos.add(matricula.getAluno());
        });
        
        return listaDeAlunos;
    }

    @Override
    public Aluno criaNovoAluno(CriaAlunoRequest criaAluno){
        Optional<Aluno> existeAluno = alunoRepository.findByEmail(criaAluno.getEmail());

        if(existeAluno.isPresent()){
            throw new BadRequestException("Email já cadastrado!");
        }

        Aluno novoAluno = new Aluno();

        novoAluno.setNome(criaAluno.getNome());
        novoAluno.setIdade(criaAluno.getIdade());
        novoAluno.setEmail(criaAluno.getEmail());
        novoAluno.setGenero(criaAluno.getGenero());

        alunoRepository.save(novoAluno);

        return novoAluno;
    }

    @Override
    public Aluno atualizaAluno(Long aluno_id, AtualizaAlunoRequest atualizaAluno){
        Optional<Aluno> existeAluno = alunoRepository.findById(aluno_id);

        if(existeAluno.isEmpty()){
            throw new NotFoundException("Aluno não encontrado!");
        }

        Aluno atualizarAluno = existeAluno.get();

        if(atualizaAluno.getNome() != null){
            atualizarAluno.setNome(atualizaAluno.getNome());
        }

        if(atualizaAluno.getIdade() != null){
            atualizarAluno.setIdade(atualizaAluno.getIdade());
        }

        if(atualizaAluno.getGenero() != null){
            atualizarAluno.setGenero(atualizaAluno.getGenero());
        }

        alunoRepository.save(atualizarAluno);

        return atualizarAluno;
    }

    @Override
    public void deletaAluno(Long aluno_id){
        Optional<Aluno> existeAluno = alunoRepository.findById(aluno_id);
        List<Matricula> estaMatriculado = matriculaRepository.findAllByAluno_Id(aluno_id);

        if(existeAluno.isEmpty()){
            throw new NotFoundException("Aluno não encontrado!");
        }

        if(!estaMatriculado.isEmpty()){
            throw new BadRequestException("Você não pode deletar um aluno com matriculas em andamento");
        }

        alunoRepository.deleteById(aluno_id);
    }
}
