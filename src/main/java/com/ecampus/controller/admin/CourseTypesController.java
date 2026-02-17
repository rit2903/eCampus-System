package com.ecampus.controller.admin;

import com.ecampus.model.CourseTypes;
import com.ecampus.model.Programs;
import com.ecampus.model.Scheme;
import com.ecampus.repository.CourseTypesRepository;
import com.ecampus.model.CourseTypesId;
import com.ecampus.repository.ProgramsRepository;
import com.ecampus.repository.SchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/program/{programId}/scheme/{schemeId}/{splid}/coursetypes")
public class CourseTypesController {

    @Autowired
    private CourseTypesRepository courseTypesRepository;

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    private ProgramsRepository programsRepository;

//    @GetMapping
//    public String viewCourseTypes(@PathVariable Long programId,
//                                  @PathVariable Long schemeId,
//                                  @PathVariable Long splid,
//                                  Model model) {
//        Scheme scheme = schemeRepository.findById(schemeId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid scheme ID"));
//
//        // include general degree (splid = 0) and the requested specialization
//        List<Long> splids = (splid == 0L) ? List.of(0L) : Arrays.asList(0L, splid);
//        List<CourseTypes> courseTypes =
//                courseTypesRepository.findBySchemeIdAndSplidIn(schemeId, splids);
//
//        Optional<Programs> program = programsRepository.findById(programId);
//        if (program.isEmpty()) {
//            return "redirect:/dashboard";
//        }
//        String programName = program.get().getPrgname();
//
//        model.addAttribute("scheme", scheme);
//        model.addAttribute("programId", programId);
//        model.addAttribute("splid", splid);
//        model.addAttribute("courseTypes", courseTypes);
//        model.addAttribute("programName", programName);
//
//        return "course_types";
//    }
//
//    @GetMapping("/add")
//    public String showAddCourseTypeForm(@PathVariable Long programId,
//                                        @PathVariable Long schemeId,
//                                        @PathVariable Long splid,
//                                        Model model) {
//        CourseTypes ct = new CourseTypes();
//        ct.setSchemeId(schemeId);
//        ct.setSplid(splid);
//
//        model.addAttribute("courseType", ct);
//        model.addAttribute("editing", false);
//        model.addAttribute("programId", programId);
//        model.addAttribute("scheme", schemeRepository.findById(schemeId).orElse(null));
//        model.addAttribute("splid", splid);
//        return "course_type_form";
//    }
//
//    @GetMapping("/{ctpcode}/edit")
//    public String showEditCourseTypeForm(@PathVariable Long programId,
//                                         @PathVariable Long schemeId,
//                                         @PathVariable Long splid,
//                                         @PathVariable String ctpcode,
//                                         Model model,
//                                         RedirectAttributes redirectAttributes) {
//        CourseTypesId id = new CourseTypesId(schemeId, splid, ctpcode);
//        Optional<CourseTypes> ctOpt = courseTypesRepository.findById(id);
//        if (ctOpt.isEmpty()) {
//            redirectAttributes.addFlashAttribute("error", "Course type not found.");
//            return "redirect:/program/" + programId + "/scheme/" + schemeId + "/" + splid + "/coursetypes";
//        }
//        CourseTypes ct = ctOpt.get();
//
//        // Prevent editing a general-degree (splid=0) entry from a specialization page
//        if (!ct.getSplid().equals(splid)) {
//            redirectAttributes.addFlashAttribute("error", "This course type belongs to the general degree; update it from the general degree page.");
//            return "redirect:/program/" + programId + "/scheme/" + schemeId + "/" + splid + "/coursetypes";
//        }
//
//        model.addAttribute("courseType", ct);
//        model.addAttribute("editing", true);
//        model.addAttribute("programId", programId);
//        model.addAttribute("scheme", schemeRepository.findById(schemeId).orElse(null));
//        model.addAttribute("splid", splid);
//        return "course_type_form";
//    }
//
//    @PostMapping("/save")
//    public String saveCourseType(@PathVariable Long programId,
//                                 @PathVariable Long schemeId,
//                                 @PathVariable Long splid,
//                                 @RequestParam("editing") boolean editing,
//                                 @ModelAttribute("courseType") CourseTypes courseType,
//                                 RedirectAttributes redirectAttributes) {
//        // ensure composite key fields
//        courseType.setSchemeId(schemeId);
//        courseType.setSplid(splid);
//
//        CourseTypesId id = new CourseTypesId(courseType.getSchemeId(), courseType.getSplid(), courseType.getCtpcode());
//
//        if (!editing && courseTypesRepository.existsById(id)) {
//            redirectAttributes.addFlashAttribute("error", "Course Type with this code already exists.");
//            return "redirect:/program/" + programId + "/scheme/" + schemeId + "/" + splid + "/coursetypes/add";
//        }
//
//        // If attempting to save an entry that belongs to general degree from specialization page, block it
//        if (courseType.getSplid() == 0L && !splid.equals(0L)) {
//            redirectAttributes.addFlashAttribute("error", "Cannot modify a general-degree course type from a specialization page.");
//            return "redirect:/program/" + programId + "/scheme/" + schemeId + "/" + splid + "/coursetypes";
//        }
//
//        courseTypesRepository.save(courseType);
//        return "redirect:/program/" + programId + "/scheme/" + schemeId + "/" + splid + "/coursetypes";
//    }
//
//    @GetMapping("/{ctpcode}/delete")
//    public String deleteCourseType(@PathVariable Long programId,
//                                   @PathVariable Long schemeId,
//                                   @PathVariable Long splid,
//                                   @PathVariable String ctpcode,
//                                   RedirectAttributes redirectAttributes) {
//        CourseTypesId idGeneral = new CourseTypesId(schemeId, 0L, ctpcode);
//        CourseTypesId idSpec = new CourseTypesId(schemeId, splid, ctpcode);
//
//        // Prefer deleting specialization entry only if it exists for this splid
//        Optional<CourseTypes> ctOpt = courseTypesRepository.findById(idSpec);
//        if (ctOpt.isPresent()) {
//            try {
//                courseTypesRepository.deleteById(idSpec);
//            } catch (DataIntegrityViolationException ex) {
//                redirectAttributes.addFlashAttribute("error", "Cannot delete this course type because it is already in use.");
//            }
//            return "redirect:/program/" + programId + "/scheme/" + schemeId + "/" + splid + "/coursetypes";
//        }
//
//        // If only general exists and current page is specialization, block deletion
//        Optional<CourseTypes> generalOpt = courseTypesRepository.findById(idGeneral);
//        if (generalOpt.isPresent()) {
//            redirectAttributes.addFlashAttribute("error", "This course type belongs to the general degree; delete it from the general degree page.");
//            return "redirect:/program/" + programId + "/scheme/" + schemeId + "/" + splid + "/coursetypes";
//        }
//
//        redirectAttributes.addFlashAttribute("error", "Course type not found.");
//        return "redirect:/program/" + programId + "/scheme/" + schemeId + "/" + splid + "/coursetypes";
//    }
}