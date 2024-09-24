package com.example.escola.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.escola.model.Matricula;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long>{
    Optional<Matricula> findByAlunoId(Long aluno_id);
    Optional<Matricula> findByCursoId(Long curso_id);

    List<Matricula> findAllByAluno_Id(Long aluno_id);
    List<Matricula> findAllByCurso_Id(Long curso_id);
}
