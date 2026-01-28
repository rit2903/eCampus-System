package com.ecampus.controller.admin;

import com.ecampus.model.AcademicYears;
import com.ecampus.repository.AcademicYearsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/academicyears")
public class AcademicYearsController {

    @Autowired
    private AcademicYearsRepository academicYearsRepository;

    // 1. LIST ALL ACADEMIC YEARS
    @GetMapping
    public String listAcademicYears(Model model) {
        model.addAttribute("academicyears",
                academicYearsRepository.findAll()
                        .stream()
                        .sorted((a, b) -> b.getAyrid().compareTo(a.getAyrid())) // DESC
                        .toList()
        );
        return "admin/academic-years";
    }

    // 2. SHOW ADD FORM
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("academicYear", new AcademicYears());
        return "admin/academic-year-form";
    }

    // 3. HANDLE ADD FORM SUBMISSION
    @PostMapping("/add")
    public String saveAcademicYear(@ModelAttribute AcademicYears ay) {

        // 1. Fetch current max ID
        Long maxId = academicYearsRepository.findMaxAyrid();
        Long newId = (Long) ((maxId == null ? 0 : maxId) + 1);

        // 2. Assign new ID
        ay.setAyrid(newId);

        // 3. Default metadata
        ay.setAyrcreatedat(LocalDateTime.now());
        ay.setAyrlastupdatedat(LocalDateTime.now());
        ay.setAyrcreatedby(1L); // set your user ID
        ay.setAyrlastupdatedby(1L);
        ay.setAyrrowstate(1L);

        // 4. Save
        academicYearsRepository.save(ay);

        return "redirect:/admin/academicyears";
    }
}
