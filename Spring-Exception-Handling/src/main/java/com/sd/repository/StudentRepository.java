package com.sd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sd.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
  List<Student> findByScore(int score);

  List<Student> findByCollegeName(String collegeName);
}