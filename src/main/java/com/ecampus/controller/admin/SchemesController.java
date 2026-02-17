package com.ecampus.controller.admin;

import com.ecampus.model.*;
import com.ecampus.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.List;
@Controller
@RequestMapping("/admin/programs/{programId}/schemes")
public class SchemesController {

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    private ProgramsRepository programsRepository;
    @Autowired
    private CourseTypesRepository courseTypesRepository;
    @Autowired
    private SchemeCoursesRepository schemeCourseRepository;
    @Autowired
    private SchemeDetailsRepository schemeDetailsRepository;
    // Show Add Scheme form
    @GetMapping("/add")
    public String showAddSchemeForm(@PathVariable("programId") Short programId, Model model) {
        Optional<Programs> program = programsRepository.findById(Long.valueOf(programId));
        if (program.isEmpty()) {
            return "redirect:/admin/programs";
        }

        Scheme scheme = new Scheme();
        scheme.setProgram(program.get());
        model.addAttribute("scheme", scheme);
        model.addAttribute("editing", false);
        return "admin/scheme-form";
    }

    // Handle Scheme submission
    @PostMapping("/save")
    public String saveScheme(@ModelAttribute("scheme") Scheme scheme,
                             @RequestParam("editing") boolean editing,
                             RedirectAttributes redirectAttributes) {

        if (!editing && schemeRepository.existsById(scheme.getId())) {
            redirectAttributes.addFlashAttribute("error", "Scheme with this ID already exists.");
            return "redirect:/admin/programs/" + scheme.getProgram().getPrgid() + "/schemes/add";
        }

        schemeRepository.save(scheme);
        return "redirect:/admin/programs/" + scheme.getProgram().getPrgid();
    }

    @GetMapping("/{schemeId}")
    public String viewSchemeDetails(@PathVariable Long programId,
                                    @PathVariable Long schemeId,
                                    Model model) {
        Scheme scheme = schemeRepository.findById(schemeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid scheme ID"));

        List<SchemeDetails> degrees =
                schemeDetailsRepository.findBySchemeId(schemeId);

        Optional<Programs> program = programsRepository.findById(programId);
        if (program.isEmpty()) {
            return "redirect:/admin/programs";
        }
        String programName = program.get().getPrgname();

        model.addAttribute("scheme", scheme);
        model.addAttribute("programId", programId);
        model.addAttribute("degrees", degrees);
        model.addAttribute("programName", programName);
        return "admin/schemes";
    }

    @GetMapping("/{schemeId}/{splid}/graduation-requirements")
    public String viewGraduationRequirements(@PathVariable Long programId,
                                             @PathVariable Long schemeId,
                                             @PathVariable Long splid,
                                             Model model) {
        Scheme scheme = schemeRepository.findById(schemeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid scheme ID"));

        Optional<Programs> program = programsRepository.findById(programId);
        if (program.isEmpty()) {
            return "redirect:/admin/programs";
        }
        String programName = program.get().getPrgname();

        Optional<SchemeDetails> degreeOpt = schemeDetailsRepository.findBySchemeIdAndSplid(schemeId, splid);
        if (degreeOpt.isEmpty()) {
            return "redirect:/admin/programs/" + programId + "/schemes/" + schemeId;
        }
        SchemeDetails degree = degreeOpt.get();

        model.addAttribute("scheme", scheme);
        model.addAttribute("programId", programId);
        model.addAttribute("degree", degree);
        model.addAttribute("programName", programName);

        return "admin/graduation-requirements";
    }

    @GetMapping("/{schemeId}/{splid}/graduation-requirements/edit")
    public String showEditGraduationForm(@PathVariable Long programId,
                                         @PathVariable Long schemeId,
                                         @PathVariable Long splid,
                                         Model model) {
        Optional<SchemeDetails> degreeOpt = schemeDetailsRepository.findBySchemeIdAndSplid(schemeId, splid);
        if (degreeOpt.isEmpty()) {
            return "redirect:/admin/programs/" + programId + "/schemes/" + schemeId;
        }
        SchemeDetails degree = degreeOpt.get();

        model.addAttribute("degree", degree);
        model.addAttribute("programId", programId);
        model.addAttribute("scheme", schemeRepository.findById(schemeId).orElse(null));
        return "admin/graduation-requirements-form";
    }

    @PostMapping("/{schemeId}/{splid}/graduation-requirements/save")
    public String saveGraduationRequirements(@PathVariable Long programId,
                                             @PathVariable Long schemeId,
                                             @PathVariable Long splid,
                                             @ModelAttribute("degree") SchemeDetails degree) {
        // Ensure composite key fields are properly set
        degree.setSchemeId(schemeId);
        degree.setSplid(splid);
        schemeDetailsRepository.save(degree);
        return "redirect:/admin/programs/" + programId + "/schemes/" + schemeId + "/" + splid + "/graduation-requirements";
    }

// Edit is now not required for scheme, it is only required for SchemeDetails table
//    @GetMapping("/{schemeId}/edit")
//    public String showEditSchemeForm(@PathVariable("programId") Long programId,
//                                     @PathVariable("schemeId") Long schemeId,
//                                     Model model) {
//        Scheme scheme = schemeRepository.findById(schemeId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid scheme ID"));
//
//        model.addAttribute("scheme", scheme);
//        model.addAttribute("editing", true);
//        return "scheme_form";
//    }

//    @GetMapping("/pull")
//    public String showPullForm(@PathVariable("id") Short programId, Model model) {
//        List<Scheme> allSchemes = schemeRepository.findByProgram_Prgid(Long.valueOf(programId));
//        model.addAttribute("schemes", allSchemes);
//        model.addAttribute("programId", programId);
//        return "pull_scheme_select";
//    }
//    @PostMapping("/program/{programId}/scheme/pull")
//    public String pullFromScheme(@PathVariable Long programId,
//                                 @RequestParam("sourceSchemeId") Long fromSchemeId,
//                                 @RequestParam("effectiveYear") Long effectiveYear) {
//
//        // Fetch the scheme to pull from
//        Scheme fromScheme = schemeRepository.findById((long) fromSchemeId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid source scheme ID"));
//
//        // Generate new scheme ID based on programId and effective year
//        Long newSchemeId = (long) Integer.parseInt(programId + "" + effectiveYear);
//
//        // Create and populate new Scheme
//        Scheme newScheme = new Scheme();
//        newScheme.setId(newSchemeId);
//        newScheme.setProgram(fromScheme.getProgram());
//        newScheme.setEffectiveFromYear(effectiveYear);
//        newScheme.setMinCpi(fromScheme.getMinCpi());
//        newScheme.setMinCredits(fromScheme.getMinCredits());
//        newScheme.setMaxCreditLoad(fromScheme.getMaxCreditLoad());
//        newScheme.setMaxCourses(fromScheme.getMaxCourses());
//        schemeRepository.save(newScheme);
//
//        // Clone Course Requirements
//        List<CourseRequirement> fromRequirements = courseRequirementRepository.findBySchemeId(fromSchemeId);
//        for (CourseRequirement cr : fromRequirements) {
//            CourseRequirement newCr = new CourseRequirement();
//            newCr.setSchemeId(newSchemeId);
//            newCr.setProgramId(cr.getProgramId());
//            newCr.setCourseTypeCode(cr.getCourseTypeCode());
//            newCr.setMinCourse(cr.getMinCourse());
//            newCr.setMaxCourse(cr.getMaxCourse());
//            newCr.setMinCredits(cr.getMinCredits());
//            newCr.setMaxCredits(cr.getMaxCredits());
//            courseRequirementRepository.save(newCr);
//        }
//
//        // Clone Semester Courses
//        List<SchemeCourse> fromSemesterCourses = schemeCourseRepository.findBySchemeId(fromSchemeId);
//        for (SchemeCourse sc : fromSemesterCourses) {
//            SchemeCourse newSc = new SchemeCourse();
//            newSc.setSchemeId(newSchemeId);
//            newSc.setSemNo(sc.getSemNo());
//            newSc.setCourseSrNo(sc.getCourseSrNo());
//            newSc.setCourseTypeCode(sc.getCourseTypeCode());
//            newSc.setCourseId(sc.getCourseId());
//            newSc.setCourseCode(sc.getCourseCode());
//            newSc.setCourseTitle(sc.getCourseTitle());
//            newSc.setProgramId(sc.getProgramId());
//            newSc.setLectureHours(sc.getLectureHours());
//            newSc.setTutorialHours(sc.getTutorialHours());
//            newSc.setPracticalHours(sc.getPracticalHours());
//            newSc.setTotalCredits(sc.getTutorialHours());
//            schemeCourseRepository.save(newSc);
//        }
//
//        // Redirect to the course requirements page of the new scheme
//        return "redirect:/program/" + programId + "/scheme/" + newSchemeId + "/details";
//    }

}
