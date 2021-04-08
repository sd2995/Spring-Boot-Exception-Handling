package com.sd.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sd.exception.BusinessException;
import com.sd.exception.ResourceNotFoundException;
import com.sd.model.Student;
import com.sd.repository.StudentRepository;

@RestController
@RequestMapping("/v1")
public class StudentController {

  @Autowired
  StudentRepository studentRepository;

  @GetMapping("/students")
  public ResponseEntity<List<Student>> getAllStudents(@RequestParam(required = false) String collegeName) {
    List<Student> students = new ArrayList<Student>();

    if (collegeName == null)
      studentRepository.findAll().forEach(students::add);
    else
      studentRepository.findByCollegeName(collegeName).forEach(students::add);

    if (students.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    return new ResponseEntity<>(students, HttpStatus.OK);
  }

  @GetMapping("/students/{id}")
  public ResponseEntity<Student> getStudentById(@PathVariable("id") long id) {
	  Student student = studentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Student with id = " + id));

    return new ResponseEntity<>(student, HttpStatus.OK);
  }

  @PostMapping("/students")
  public ResponseEntity<Student> createStudent(@RequestBody Student student) {
	  Student _tutorial = studentRepository.save(new Student(student.getName(),student.getCollegeName(),student.getScore()));
    return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
  }

  @PutMapping("/students/{id}")
  public ResponseEntity<Student> updateStudent(@PathVariable("id") long id, @RequestBody Student student) {
	  Student studentR = studentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found student with id = " + id));

	  studentR.setName(student.getName());
	  studentR.setCollegeName(student.getCollegeName());
	  studentR.setScore(student.getScore());
    
    return new ResponseEntity<>(studentRepository.save(studentR), HttpStatus.OK);
  }

  @DeleteMapping("/students/{id}")
  public ResponseEntity<HttpStatus> deleteStudent(@PathVariable("id") long id) {
	Student student = studentRepository.findById(id).orElseThrow(()-> new BusinessException("Id not valid " + id)); 
	if(student!= null)
    studentRepository.deleteById(id);
    
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/students")
  public ResponseEntity<HttpStatus> deleteAllStudents() {
    studentRepository.deleteAll();
    
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/students/score/{score}")
  public ResponseEntity<List<Student>> findByScore(@PathVariable("score") int score) {
    List<Student> students = studentRepository.findByScore(score);

    if (students.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    return new ResponseEntity<>(students, HttpStatus.OK);
  }

}