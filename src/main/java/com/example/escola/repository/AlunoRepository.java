package com.example.escola.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.escola.model.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno,Long>{
    Optional<Aluno> findByEmail(String email);
}
