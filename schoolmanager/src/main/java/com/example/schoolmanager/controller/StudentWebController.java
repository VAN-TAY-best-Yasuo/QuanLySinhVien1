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

    @GetMapping("/")
    public String home() {
        return "redirect:/students";
    }

    @GetMapping("/students")
    public String list(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Student> students;
        List<Student> allInDb = service.getAllStudents();

        if (keyword != null && !keyword.trim().isEmpty()) {
            String value = keyword.trim();
            // Nếu nhập số thì tìm theo ID, nếu nhập chữ thì tìm theo Tên
            if (value.matches("\\d+")) {
                Student s = service.getStudentById(Integer.parseInt(value));
                students = (s != null) ? List.of(s) : List.of();
            } else {
                students = service.search(value);
            }
        } else {
            students = allInDb;
        }

        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalCount", allInDb.size());
        return "students";
    }

    @GetMapping("/students/add")
    public String addForm(Model model) {
        model.addAttribute("student", new Student());
        return "student-form";
    }

    @GetMapping("/students/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("student", service.getStudentById(id));
        return "student-form";
    }

    @PostMapping("/students/save")
    public String save(@ModelAttribute Student student) {
        service.save(student);
        return "redirect:/students";
    }

    @GetMapping("/students/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "redirect:/students";
    }
}