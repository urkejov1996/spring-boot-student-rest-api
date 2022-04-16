package com.springbootstudentdemo.services;

import com.springbootstudentdemo.Entities.Student;
import com.springbootstudentdemo.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        System.out.println(student);
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("student with id = " + studentId + " does not exists");
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("student with id = " + studentId + " does not exists"));

        // provera imena
        if (name != null &&
                name.length() > 0 &&
                !Objects.equals(student.getName(), name)) {
            student.setName(name);
        }

        // provera email-a
        if (email != null &&
                email.length() > 0 &&
                !Objects.equals(student.getEmail(), email)) {
            // proverava da li je mail zauzet
            if (studentRepository.findStudentByEmail(email).isPresent()) {
                throw new IllegalStateException("email taken");
            }

            student.setEmail(email);
        }


    }
}
