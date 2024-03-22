package com.system.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.system.po.*;
import com.system.service.*;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacey on 2017/7/6.
 */

@Controller
@RequestMapping(value = "/teacher")
public class TeacherController {

    @Resource(name = "teacherServiceImpl")
    private TeacherService teacherService;

    @Resource(name = "courseServiceImpl")
    private CourseService courseService;

    @Resource(name = "selectedCourseServiceImpl")
    private SelectedCourseService selectedCourseService;

    @Resource(name = "resourceServiceImpl")
    private ResourceService resourceService;

    // 显示我的课程
    @RequestMapping(value = "/showCourse")
    public String stuCourseShow(Model model) throws Exception {

        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();

        List<CourseCustom> list = courseService.findByTeacherID(Integer.parseInt(username));
        model.addAttribute("courseList", list);

        return "teacher/showCourse";
    }

    // 显示成绩
    @RequestMapping(value = "/gradeCourse")
    public String gradeCourse(Integer id, Model model) throws Exception {
        if (id == null) {
            return "";
        }
        List<SelectedCourseCustom> list = selectedCourseService.findByCourseID(id);
        model.addAttribute("selectedCourseList", list);
        return "teacher/showGrade";
    }

    // 打分
    @RequestMapping(value = "/mark", method = {RequestMethod.GET})
    public String markUI(SelectedCourseCustom scc, Model model) throws Exception {

        SelectedCourseCustom selectedCourseCustom = selectedCourseService.findOne(scc);

        model.addAttribute("selectedCourse", selectedCourseCustom);

        return "teacher/mark";
    }

    // 打分
    @RequestMapping(value = "/mark", method = {RequestMethod.POST})
    public String mark(SelectedCourseCustom scc) throws Exception {

        selectedCourseService.updataOne(scc);

        return "redirect:/teacher/gradeCourse?id=" + scc.getCourseid();
    }

    //修改密码
    @RequestMapping(value = "/passwordRest")
    public String passwordRest() throws Exception {
        return "teacher/passwordRest";
    }

    //CourseResource课程下的资料
    @RequestMapping(value = "/selectResource", method = {RequestMethod.GET})
    public String CourseResource(Model model, Integer CourseId) {
        //返回该课程id下的资料
        List<ResourceCustom> list = resourceService.findByCourseId(CourseId);
        model.addAttribute("courseResourceList", list);

        return "teacher/selectResource";
    }

    //文件上传
    @PostMapping("/upload")
    public String uploadFile(Model model, @RequestPart("fileUpload") MultipartFile multipartFile, HttpServletRequest request) {

        int courseId = Integer.parseInt(request.getParameter("CourseId"));

        ServerMessage message = resourceService.uploadFile(multipartFile, request, courseId);
        if (message.getStatus_code().equals("error")) {
            model.addAttribute("message", message.getMessage());
            return "error";
        }
        if (!message.getStatus_code().equals("success")) {
            return "error";
        }
        return message.getMessage();
    }

    @GetMapping("/removeResource")
    public String removeResource(Model model, Integer id, Integer CourseId, HttpServletRequest request) {
        ServerMessage message = resourceService.removeById(id, request);
        if (message.getStatus_code().equals("error")) {
            model.addAttribute("message", message.getMessage());
            return message.getStatus_code();
        }

        return "redirect:/teacher/selectResource?CourseId=" + CourseId;
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
        System.out.println(id);

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

    //根据日期和班级查询资料
    @RequestMapping(value = "/selectResource/{CourseId}/{findByDate}", method = {RequestMethod.GET})
    public String selectResource(Model model,@PathVariable("CourseId") Integer CourseId,@PathVariable("findByDate") String findByDate,HttpServletRequest request) {
//        System.out.println(findByDate+" -> "+CourseId);
        // "2024-01-18" 转为 LocalDate/Date
        LocalDate parse=null;
        if (!(findByDate.equals("") || findByDate == null)) {
            parse = LocalDate.parse(findByDate);
        }
        //查询 LocalDate/Date 对应日期的 数据
        //返回该课程id下的资料
        if(parse==null){
            List<ResourceCustom> list = resourceService.findByCourseId(CourseId);
            model.addAttribute("courseResourceList", list);
        }
        if(parse!=null){
            List<ResourceCustom> list = resourceService.selectResource(CourseId,findByDate,request);
            model.addAttribute("courseResourceList", list);
        }

        return "teacher/selectResource";
    }
}
