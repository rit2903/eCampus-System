package com.ecampus.controller.admin;

import com.ecampus.dto.BatchViewDTO;
import com.ecampus.dto.StudentGraduationRequirementsAdminDTO;
import com.ecampus.model.Batches;
import com.ecampus.repository.BatchesRepository;
import com.ecampus.service.StudentGraduationRequirementsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/graduation-requirements")
public class AdminGraduationRequirementsController {

    @Autowired
    private BatchesRepository batchesRepo;

    @Autowired
    private StudentGraduationRequirementsService graduationReqService;

    /**
     * Show all batches for selecting graduation requirements
     */
    @GetMapping("/batches")
    public String listBatches(Model model) {
        List<Object[]> rows = batchesRepo.getAllBatchesDetailsRaw();

        List<BatchViewDTO> batches = rows.stream()
                .map(r -> new BatchViewDTO(
                        ((Number) r[0]).longValue(),
                        (String) r[1],
                        (String) r[2],
                        (String) r[3],
                        ((Number) r[4]).intValue(),
                        r[5] != null ? ((Number) r[5]).intValue() : null
                )).toList();

        model.addAttribute("batches", batches);
        return "admin/graduation-requirements/batches";
    }

    /**
     * Show graduation requirements for all students in a specific batch
     */
    @GetMapping("/batches/{batchId}")
    public String showBatchGraduationRequirements(
            @PathVariable Long batchId,
            Model model) {

        // Get batch details
        Batches batch = batchesRepo.findById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("Batch not found: " + batchId));

        // Get all students' graduation requirements for this batch
        List<StudentGraduationRequirementsAdminDTO> studentRequirements = 
                graduationReqService.buildBatchGraduationRequirements(
                        batchId, batch.getSchemeId(), batch.getSplid());

        model.addAttribute("batch", batch);
        model.addAttribute("studentRequirements", studentRequirements);
        
        // Calculate overall batch statistics
        if (!studentRequirements.isEmpty()) {
            long completedCount = studentRequirements.stream()
                    .filter(s -> "Completed".equals(s.getGraduationStatus()))
                    .count();
            long totalStudents = studentRequirements.size();
            double completionPercentage = (completedCount * 100.0) / totalStudents;
            
            model.addAttribute("completedCount", completedCount);
            model.addAttribute("totalStudents", totalStudents);
            model.addAttribute("completionPercentage", completionPercentage);
            // model.addAttribute("completionPercentage", String.format("%.1f", completionPercentage));
        }

        return "admin/graduation-requirements/batch-details";
    }
}
