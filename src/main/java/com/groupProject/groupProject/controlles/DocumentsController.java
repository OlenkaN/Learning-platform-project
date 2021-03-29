package com.groupProject.groupProject.controlles;


import com.groupProject.groupProject.model.Course;
import com.groupProject.groupProject.model.Document;
import com.groupProject.groupProject.model.Post;
import com.groupProject.groupProject.repo.CourseRepository;
import com.groupProject.groupProject.repo.CoursesAndUsersRepository;
import com.groupProject.groupProject.repo.DocumentRepository;
import com.groupProject.groupProject.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Date;

@Controller
public class DocumentsController {
    @Autowired
    private DocumentRepository docRep;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CoursesAndUsersRepository coursesAndUsersRepository;

    @GetMapping("/admin/{userId}/course/{courseId}/materials")
    public String viewMaterialsPage(@PathVariable(value = "courseId")long courseId,
                                    @PathVariable(value = "userId") long userId,
                                    Model model)
    {
        Course course = courseRepository.findById(courseId).get();
        Iterable <Document> docs= course.getDocuments();
        model.addAttribute("docs",docs);
        model.addAttribute("courseId",courseId);
        model.addAttribute("userId", userId);

        String token = coursesAndUsersRepository.findCoursesAndUsersByCourseIdAndAndUserId(courseId, userId).getToken();
        model.addAttribute("token", token);

        return "CourseTeacherMaterials";
    }
    @PostMapping("/admin/{userId}/course/{courseId}/upload/materials")
    public String uploadFile(@PathVariable(value = "courseId")long courseId,
                             @RequestParam("document")MultipartFile multipartFile,
                             @PathVariable(value = "userId") long userId,
                             RedirectAttributes ra) throws IOException
    {
        String fileName= StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Document document= new Document();
        document.setName(fileName);
        document.setContent(multipartFile.getBytes());
        document.setSize(multipartFile.getSize());
        document.setUpload_time(new Date());
        Course course = courseRepository.findById(courseId).get();
        document.setCourse(course);
        course.addDocument(document);
        docRep.save(document);
        ra.addFlashAttribute("message","The file has been upload successfuly ");
        String token = coursesAndUsersRepository.findCoursesAndUsersByCourseIdAndAndUserId(courseId, userId).getToken();
        return "redirect:/admin/" + userId +"/course/"+courseId+"/materials?token=" + token;
    }
    @PostMapping("/admin/{userId}/course/{courseId}/remove/{docId}")
    public String CourseMaterialDelete(@PathVariable(value = "courseId")long courseId,
                                       @PathVariable(value = "userId") long userId,
                                       @PathVariable(value = "docId")long docId,Model model)
    {
        Course course=courseRepository.findById(courseId).get();
        Document doc= docRep.findById(docId).get();
        course.removeDocument(doc);
        docRep.deleteById(docId);
        String token = coursesAndUsersRepository.findCoursesAndUsersByCourseIdAndAndUserId(courseId, userId).getToken();
        return "redirect:/admin/" + userId +"/course/"+courseId+"/materials?token=" + token;
    }

}
