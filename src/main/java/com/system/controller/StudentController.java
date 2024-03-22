package com.system.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.system.exception.CustomException;
import com.system.po.*;
import com.system.service.CourseService;
import com.system.service.ResourceService;
import com.system.service.SelectedCourseService;
import com.system.service.StudentService;
import com.system.vo.ServerMessage;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacey on 2017/7/5.
 */
@Controller
@RequestMapping(value = "/student")
public class StudentController {

    @Resource(name = "courseServiceImpl")
    private CourseService courseService;

    @Resource(name = "studentServiceImpl")
    private StudentService studentService;

    @Resource(name = "selectedCourseServiceImpl")
    private SelectedCourseService selectedCourseService;

    @Resource(name = "resourceServiceImpl")
    private ResourceService resourceService;

    @RequestMapping(value = "/showCourse")
    public String stuCourseShow(Model model, Integer page) throws Exception {

        List<CourseCustom> list = null;
        //页码对象
        PagingVO pagingVO = new PagingVO();
        //设置总页数
        pagingVO.setTotalCount(courseService.getCountCouse());
        if (page == null || page == 0) {
            pagingVO.setToPageNo(1);
            list = courseService.findByPaging(1);
        } else {
            pagingVO.setToPageNo(page);
            list = courseService.findByPaging(page);
        }

        model.addAttribute("courseList", list);
        model.addAttribute("pagingVO", pagingVO);

        return "student/showCourse";
    }

    // 选课操作
    @RequestMapping(value = "/stuSelectedCourse")
    public String stuSelectedCourse(int id) throws Exception {
        //获取当前用户名
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();

        SelectedCourseCustom selectedCourseCustom = new SelectedCourseCustom();
        selectedCourseCustom.setCourseid(id);
        selectedCourseCustom.setStudentid(Integer.parseInt(username));

        SelectedCourseCustom s = selectedCourseService.findOne(selectedCourseCustom);

        if (s == null) {
            selectedCourseService.save(selectedCourseCustom);
        } else {
            throw new CustomException("该门课程你已经选了，不能再选");
        }

        return "redirect:/student/selectedCourse";
    }

    // 退课操作
    @RequestMapping(value = "/outCourse")
    public String outCourse(int id) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();

        SelectedCourseCustom selectedCourseCustom = new SelectedCourseCustom();
        selectedCourseCustom.setCourseid(id);
        selectedCourseCustom.setStudentid(Integer.parseInt(username));

        selectedCourseService.remove(selectedCourseCustom);

        return "redirect:/student/selectedCourse";
    }

    // 已选课程
    @RequestMapping(value = "/selectedCourse")
    public String selectedCourse(Model model) throws Exception {
        //获取当前用户名
        Subject subject = SecurityUtils.getSubject();
        StudentCustom studentCustom = studentService.findStudentAndSelectCourseListByName((String) subject.getPrincipal());

        List<SelectedCourseCustom> list = studentCustom.getSelectedCourseList();

        model.addAttribute("selectedCourseList", list);

        return "student/selectCourse";
    }

    // 已修课程
    @RequestMapping(value = "/overCourse")
    public String overCourse(Model model) throws Exception {

        //获取当前用户名
        Subject subject = SecurityUtils.getSubject();
        StudentCustom studentCustom = studentService.findStudentAndSelectCourseListByName((String) subject.getPrincipal());

        List<SelectedCourseCustom> list = studentCustom.getSelectedCourseList();

        model.addAttribute("selectedCourseList", list);

        return "student/overCourse";
    }

    //修改密码
    @RequestMapping(value = "/passwordRest")
    public String passwordRest() throws Exception {
        return "student/passwordRest";
    }

    //Resource查询资料
    @RequestMapping(value = "/selectResource", method = {RequestMethod.GET})
    public String CourseResource(Model model, Integer CourseId) {
        //返回该课程id下的资料
        List<ResourceCustom> list = resourceService.findByCourseId(CourseId);
        model.addAttribute("courseResourceList", list);

        return "student/selectResource";
    }

    //文件上传
    @PostMapping("/upload")
    public String uploadFile(Model model, @RequestPart("fileUpload") MultipartFile multipartFile, HttpServletRequest request){


        int courseId = Integer.parseInt(request.getParameter("CourseId"));

        ServerMessage message = resourceService.uploadFile(multipartFile,request,courseId);
        if(message.getStatus_code().equals("error")){
            model.addAttribute("message",message.getMessage());
            return "error";
        }
        if(!message.getStatus_code().equals("success")){
            return "error";
        }
        return message.getMessage();
    }
    // TODO 学生只能删除自己的资料
    @GetMapping("/removeResource")
    public String removeResource(Model model,Integer id,Integer CourseId,HttpServletRequest request){

        ServerMessage message = resourceService.removeStudentById(id,request);

        if(message.getStatus_code().equals("error")){
            model.addAttribute("message",message.getMessage());
            return message.getStatus_code();
        }

        return "redirect:/student/selectResource?CourseId="+CourseId;
    }

    /**
     * @param data post接收id数组
     * @return 返回字节流 -> 前端触发下载
     * @throws IOException
     */
    @RequestMapping(value = "/batchDownload", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<byte[]> batchDownload(Model model, @RequestBody String data, HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Json -> String[] -> Integer
        JSONObject jo = JSONUtil.parseObj(data);
        String[] ids = jo.getJSONArray("ids").toArray(new String[0]);
        List<Integer> idss = new ArrayList<>();
        for (String id : ids) {
            idss.add(Integer.parseInt(id));
        }

        //调用多个文件转一个zip处理
        String zipfilename = resourceService.downloadFiles(idss, request);
        File file = new File(zipfilename);
        // File -> Stream流
        byte[] responseData = StreamUtils.copyToByteArray(new FileInputStream(file));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=downloaded_file.zip");

        return ResponseEntity.ok().headers(headers).body(responseData);
        // return "teacher/showCourse";
    }

    @RequestMapping(value = "/onceDownload", method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> onceDownload(Model model, String id, HttpServletRequest request, HttpServletResponse response) throws IOException {

        // String -> Integer
        List<Integer> idss = new ArrayList<>();
        idss.add(Integer.parseInt(id));

        //调用多个文件转一个zip处理
        String zipfilename = resourceService.downloadFiles(idss, request);
        File file = new File(zipfilename);
        // File -> Stream流
        byte[] responseData = StreamUtils.copyToByteArray(new FileInputStream(file));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=downloaded_file.zip");

        return ResponseEntity.ok().headers(headers).body(responseData);
    }


}
