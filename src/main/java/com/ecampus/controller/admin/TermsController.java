package com.ecampus.controller.admin;

import com.ecampus.dto.*;
import com.ecampus.util.RomanNumeralUtil;
import com.ecampus.model.*;
import com.ecampus.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/terms")
public class TermsController {

    @Autowired
    private ProgramsRepository programsRepository;

    @Autowired
    private TermsRepository termRepository;

    @Autowired
    private AcademicYearsRepository academicYearRepository;

    @Autowired
    private SemestersRepository semesterRepository;

    @Autowired
    private BatchesRepository batchRepository;

    @Autowired
    private SchemeCoursesRepository schemeCoursesRepository;

    @Autowired
    private TermCoursesRepository termCourseRepository;

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    private SemesterCoursesRepository semesterCoursesRepository;

    // Show "Add Term" form
    @GetMapping("/add")
    public String showAddTermForm(Model model) {
        model.addAttribute("term", new Terms());
        model.addAttribute("academicYears", academicYearRepository.findAllByOrderByAyridDesc());
        model.addAttribute("termNames", List.of("Autumn", "Winter", "Summer"));
        return "admin/term-form";
    }

    // Handle "Add Term" submission
    @PostMapping("/add")
    @Transactional
    public String addTerm(
            @RequestParam("academicYearId") Long academicYearId,
            @RequestParam("termName") String termName,
            @RequestParam("termStartDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate termStartDate,
            @RequestParam("termEndDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate termEndDate,
            Model model) {

        // Generate new Term ID
        Long maxId = termRepository.findMaxTrmid();
        Long newTermId = (long) ((maxId == null ? 0 : maxId) + 1);
        Long trmSeqNo;
        if ("Autumn".equalsIgnoreCase(termName)) {
            trmSeqNo = 1L;
        } else if ("Winter".equalsIgnoreCase(termName)) {
            trmSeqNo = 2L;
        } else if ("Summer".equalsIgnoreCase(termName)) {
            trmSeqNo = 3L;
        } else {
            trmSeqNo = 0L; // default or throw exception
        }

        // SET Academic Year for Term
        AcademicYears academicYear = academicYearRepository
                .findById(academicYearId)
                .orElseThrow(() -> new RuntimeException("Academic Year not found"));

        // Create and save Term
        Terms term = new Terms();
        term.setAcademicYear(academicYear);
        term.setTrmid(newTermId);
        term.setTrmname(termName);
        term.setTrmseqno(trmSeqNo);
        term.setTrmayrid(academicYearId);
        term.setTrmstarts(termStartDate);
        term.setTrmends(termEndDate);
        term.setTrmrowstate(1L);
        term.setTrmcreatedat(LocalDateTime.now());
        termRepository.save(term);

        // Extract starting year name
        String academicAyrName = academicYear.getAyrname();

        // Extracting starting year
        int academicYearStarting = Integer.parseInt(academicAyrName.split("-")[0]);

        // Fetch all active batches
        List<Batches> allBatches = batchRepository.findAll();

        List<Batches> activeBatches = allBatches.stream()
                .filter(batch -> {
                    // Skip if schemeId is null
                    if (batch.getSchemeId() == null) return false;

                    Long batchAyrId = batch.getBchcalid();
                    if (batchAyrId == null) return false;

                    Programs program = programsRepository.findById(batch.getBchprgid()).orElse(null);
                    if (program == null || program.getPrgduration() == null) return false;

                    // Finding academic year entity
                    AcademicYears batchYear = academicYearRepository.findById(batchAyrId).orElseThrow(() -> new RuntimeException("Batch Year not found"));

                    // Extract starting year name
                    String batchAyrName = batchYear.getAyrname();

                    // Extracting starting year
                    int batchYearStarting = Integer.parseInt(batchAyrName.split("-")[0]);

                    // Calculate difference
                    int diff = academicYearStarting - batchYearStarting;
                    return diff >= 0 && diff < (program.getPrgduration());
                })
                .toList();


        int totalSemesters = 0;
        int totalCourses = 0;

        // For each active batch
        int cnt=0;
        for (Batches batch : activeBatches) {
            Programs program = programsRepository.findById(batch.getBchprgid()).orElse(null);
            if (program == null) continue;

            // Calculate semester number
            Long batchAyrId = batch.getBchcalid();

            // Finding academic year entity
            AcademicYears batchYear = academicYearRepository.findById(batchAyrId).orElseThrow(() -> new RuntimeException("Batch Year not found"));

            // Extract starting year name
            String batchAyrName = batchYear.getAyrname();

            // Extracting starting year
            int batchYearStarting = Integer.parseInt(batchAyrName.split("-")[0]);

            // Calculate difference
            int diff = academicYearStarting - batchYearStarting;
            long semNo = diff * 2L + (termName.equalsIgnoreCase("Winter") ? 2 : 1);
            String semRoman = RomanNumeralUtil.toRoman(semNo);

            // Fetch only CORE courses of that semNo from the scheme
            List<SchemeCourses> schemeCourses = schemeCoursesRepository.findBySchemeIdAndSemNo(batch.getSchemeId(), semNo)
                    .stream()
                    .filter(sc -> sc.getCourseTypeCode() != null && sc.getCourseTypeCode().equalsIgnoreCase("CORE"))
                    .toList();

            //if (schemeCourses.isEmpty()) continue;

            // Create Semester record
            Long batchId = batch.getBchid();
            Long maxSemId = semesterRepository.findMaxSemesterId();
            Long newSemId = (Long) ((maxSemId == null ? 0 : maxSemId) + 1);
            Long newSemSeqNo = semesterRepository.findMaxSemesterSeqNo(batchId);

            Semesters semester = new Semesters();
            semester.setBatches(batch);
            semester.setTerms(term);
            semester.setStrid(newSemId);
            semester.setStrseqno(newSemSeqNo);
            semester.setStrbchid(batchId);
            semester.setStrtrmid(term.getTrmid());
            semester.setStrname("Semester " + semRoman);
            semesterRepository.save(semester);
            totalSemesters++;

            // Copy only the CORE courses of that sem
            for (SchemeCourses sc : schemeCourses) {

                // Insert into TERMCourses (your existing logic)
                Long maxTcrId = termCourseRepository.findMaxTermCourseid();
                Long newTcrId = (maxTcrId == null ? 1 : maxTcrId + 1);

                TermCourses tc = new TermCourses();
                tc.setTcrid(newTcrId);
                tc.setTcrtrmid(term.getTrmid());
                tc.setTcrcrsid(sc.getCrsid());
                tc.setTcrtype("REGULAR");
                tc.setTcrrowstate(1L);          // ensure rowstate is valid
                tc.setTcrstatus("AVAILABLE");
                termCourseRepository.save(tc);
                totalCourses++;

                // Insert into SEMESTERCOURSES

                Long maxScrId = semesterCoursesRepository.findMaxSemesterCourseid();
                Long newScrId = (maxScrId == null ? 1 : maxScrId + 1);

                SemesterCourses semCourse = new SemesterCourses();
                semCourse.setScrid(newScrId);
                semCourse.setScrstrid(newSemId);           // link to newly created semester
                semCourse.setScrcrsid(sc.getCrsid());      // course ID from scheme
                semCourse.setScrelective("N");             // CORE means non-elective
                semCourse.setScrrowstate(1L);       // active row
                semCourse.setScrcreatedat(LocalDateTime.now());
                semCourse.setScrtcrid(newTcrId);

                semesterCoursesRepository.save(semCourse);
            }

        }

        model.addAttribute("message",
                "Term '" + termName + "' added successfully! Created " +
                        totalSemesters + " semesters and " + totalCourses + " CORE term courses.");

        return "redirect:/admin/terms";
    }

    // LIST TERMS (DESC ORDER)
    @GetMapping
    public String listTerms(Model model) {
        List<Object[]> rows = termRepository.getAllTermsDetailsRaw();

        List<TermViewDTO> terms = rows.stream()
                .map(r -> new TermViewDTO(
                        ((Number) r[0]).longValue(),
                        (String) r[1],
                        (String) r[2],
                        (Date) r[3],
                        (Date) r[4]
                )).toList();

        Map<String, List<TermViewDTO>> termsByAcademicYear =
                terms.stream()
                        .collect(Collectors.groupingBy(
                                TermViewDTO::ayrname,
                                LinkedHashMap::new, // preserves the order
                                Collectors.toList()
                        ));

        model.addAttribute("termsByAcademicYear", termsByAcademicYear);
        return "admin/terms";
    }

    // LIST TERM COURSES (DESC ORDER BY termId, then courseCode)
    @GetMapping("/termcourses")
    public String listTermCourses(Model model) {
        List<Object[]> rows = termCourseRepository.getAllTermCoursesDetailsRaw();

        List<TermCoursesViewDTO> termcourses = rows.stream()
                .map(r -> new TermCoursesViewDTO(
                        (String) r[0],
                        (String) r[1],
                        (String) r[2],
                        (String) r[3],
                        (String) r[4]
                )).toList();

        Map<String, Map<String, List<TermCoursesViewDTO>>> coursesByAcademicYearThenTerm =
                termcourses.stream()
                        .collect(Collectors.groupingBy(
                                TermCoursesViewDTO::ayrname,
                                LinkedHashMap::new,
                                Collectors.groupingBy(
                                        TermCoursesViewDTO::term,
                                        LinkedHashMap::new,
                                        Collectors.toList()
                                )
                        ));

        model.addAttribute("coursesByAcademicYearThenTerm", coursesByAcademicYearThenTerm);
        return "admin/termcourses";
    }

}
