package com.example.escola.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.escola.model.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor,Long>{

}
