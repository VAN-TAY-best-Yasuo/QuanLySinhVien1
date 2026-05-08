package com.example.schoolmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.schoolmanager.model.Student;
import com.example.schoolmanager.service.StudentService;

@RestController
@RequestMapping("/api/students")
@CrossOrigin
public class StudentController {

    @Autowired
    private StudentService service;

    // 1. Thêm sinh viên
    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return service.save(student);
    }

    // 2. Xóa sinh viên
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Integer id) {
        service.delete(id);
    }

    // 3. Tìm kiếm sinh viên theo ID hoặc theo TÊN
    // Dùng cho ô tìm kiếm trên giao diện
    // Frontend gọi: /api/students/search?name=...
    @GetMapping("/search")
    public List<Student> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String keyword) {

        // Lấy giá trị người dùng nhập (frontend đang dùng param name)
        String value = (keyword != null && !keyword.isBlank()) ? keyword : name;

        if (value == null || value.isBlank()) {
            return List.of();
        }

        // Nếu là số → tìm theo ID
        if (value.matches("\\d+")) {
            Student student = service.getStudentById(Integer.parseInt(value));
            if (student != null) {
                return List.of(student);
            }
            return List.of();
        }

        // Nếu là chữ → tìm theo tên
        return service.findByName(value);
    }

    // 4. Lấy sinh viên theo ID (khi cần dùng riêng)
    @GetMapping("/id/{id}")
    public Student getStudentById(@PathVariable Integer id) {
        return service.getStudentById(id);
    }

    // 5. Lấy danh sách tất cả sinh viên
    @GetMapping
    public List<Student> getAllStudents() {
        return service.getAllStudents();
    }

    // 6. Cập nhật sinh viên
    @PutMapping("/{id}")
    public Student updateStudent(
            @PathVariable Integer id,
            @RequestBody Student student) {

        Student existing = service.getStudentById(id);
        if (existing == null) {
            throw new RuntimeException("Không tìm thấy sinh viên với id: " + id);
        }

        existing.setName(student.getName());
        existing.setAge(student.getAge());
        existing.setEmail(student.getEmail());

        return service.save(existing);
    }
}
