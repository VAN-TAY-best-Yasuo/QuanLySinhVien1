package com.example.schoolmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.schoolmanager.model.Student;
import com.example.schoolmanager.service.StudentService;

@Controller
public class StudentWebController {

    @Autowired
    private StudentService service;

    // Redirect từ trang chủ sang danh sách sinh viên
    @GetMapping("/")
    public String home() {
        return "redirect:/students";
    }

    // 1. Danh sách + tìm kiếm (ID hoặc TÊN)
    @GetMapping("/students")
    public String list(
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        List<Student> students;

        if (keyword != null && !keyword.trim().isEmpty()) {
            keyword = keyword.trim();

            // Nếu là số → tìm theo ID
            try {
                int id = Integer.parseInt(keyword);
                Student student = service.getStudentById(id);
                students = (student != null) ? List.of(student) : List.of();
            } catch (NumberFormatException e) {
                // Nếu là chữ → tìm theo tên
                students = service.findByName(keyword);
            }

        } else {
            students = service.getAllStudents();
        }

        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword);

        return "students";
    }

    // 2. Form thêm
    @GetMapping("/students/add")
    public String addForm(Model model) {
        model.addAttribute("student", new Student());
        return "student-form";
    }

    // 3. Lưu (thêm + sửa)
    @PostMapping("/students/save")
    public String save(@ModelAttribute Student student) {
        service.save(student);
        return "redirect:/students";
    }

    // 4. Form sửa
    @GetMapping("/students/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        model.addAttribute("student", service.getStudentById(id));
        return "student-form";
    }

    // 5. Xóa
    @GetMapping("/students/delete/{id}")
    public String delete(@PathVariable int id) {
        service.delete(id);
        return "redirect:/students";
    }
}
