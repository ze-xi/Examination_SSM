package com.system.service.impl;

import com.system.mapper.ResourceMapper;
import com.system.mapper.ResourceMapperCustom;
import com.system.po.*;
import com.system.service.ResourceService;
import com.system.service.StudentService;
import com.system.service.TeacherService;
import com.system.vo.ServerMessage;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Array;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private ResourceMapperCustom resourceMapperCustom;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    public Integer getUserId() {
        //身份认证，获取userId
        Subject currentUser = SecurityUtils.getSubject();
        Integer userId = null;
        if (currentUser.isAuthenticated()) {
            String username = (String) currentUser.getPrincipal();// "1001" "10001" "admin"
            final String superRoot = "admin";
            if (username.hashCode() == superRoot.hashCode() && username.equals(superRoot)) {
                //TODO admin 超级权限
                userId = 1;
            } else {
                userId = Integer.parseInt(username);
            }
        }
        return userId;
    }

    @Override
    public void insert(Resource resource) {
        resourceMapper.insert(resource);
    }

    @Override
    public ServerMessage removeById(Integer id, HttpServletRequest request) {
        ServerMessage message = new ServerMessage();

        Integer userId = getUserId();
        if (userId == null) {
            message.setStatus_code("error");
            message.setMessage("没有该用户，非正常");
            return message;
        }
        //如果是学生，抛出异常
        if (studentService.getCountStudentById(userId) > 0) {
            message.setStatus_code("error");
            message.setMessage("非法用户");
            return message;
        }
        //存在数据，执行删除
        if (resourceMapperCustom.CountResourceById(id) > 0) {
            //TODO 删除本地文件操作
            String localPath = request.getServletContext().getRealPath("\\images") + "\\";
            //资源名
            String photo = resourceMapperCustom.findPhotoById(id);
            //当时存了两个地方，这次删两个地方
            ServerMessage deleteMessage1 = deleteLocalMethod(fileROOT, photo);
            ServerMessage deleteMessage2 = deleteLocalMethod(localPath, photo);
            if (!(deleteMessage1 == null && deleteMessage2 == null)) {
                message.setStatus_code("error");
                message.setMessage("删除失败 -> 未知异常");
            }
            //TODO 删除数据库记录操作
            int i = resourceMapper.deleteByPrimaryKey(id);

            message.setStatus_code("success");
            message.setMessage("删除成功  -> 受影响的行 -> " + i);
            return message;
        }
        message.setStatus_code("error");
        message.setMessage("id不存在，删除失败");
        return message;

    }

    private static ServerMessage deleteLocalMethod(String fileROOT, String photo) {
        ServerMessage serverMessage = null;
        File deleteRoot = new File(fileROOT);
        File deleteFile = new File(deleteRoot + "\\" + photo);
//        System.out.println(deleteFile.getAbsoluteFile());
        if (!deleteFile.exists()) {
            serverMessage = new ServerMessage();
            serverMessage.setStatus_code("error");
            serverMessage.setMessage("删除失败 -> 文件不存在");
            return serverMessage;
        }
        if (!FileUtils.deleteQuietly(deleteFile)) {
            serverMessage = new ServerMessage();
            serverMessage.setStatus_code("error");
            serverMessage.setMessage("删除失败 -> 无法删除文件，文件被占用");
        }
        return serverMessage;
    }

    @Override
    public ServerMessage removeStudentById(Integer id, HttpServletRequest request) {
        ServerMessage message = new ServerMessage();

        Integer userId = getUserId();
        if (userId == null) {
            message.setStatus_code("error");
            message.setMessage("没有该用户，非正常");
            return message;
        }

        //判断是否为自己发的资料
        if (resourceMapperCustom.CountByIdAndUserId(
                new ResourceParameter(id, userId)) < 1) {
            //不是自己的数据
            message.setStatus_code("error");
            message.setMessage("你不能删除别人的资料");
            return message;
        }
        //存在数据，执行删除
        if (resourceMapperCustom.CountResourceById(id) > 0) {
            //TODO 删除本地文件操作
            String localPath = request.getServletContext().getRealPath("\\images") + "\\";
            //资源名
            String photo = resourceMapperCustom.findPhotoById(id);
            //当时存了两个地方，这次删两个地方
            ServerMessage deleteMessage1 = deleteLocalMethod(fileROOT, photo);
            ServerMessage deleteMessage2 = deleteLocalMethod(localPath, photo);
            if (!(deleteMessage1 == null && deleteMessage2 == null)) {
                message.setStatus_code("error");
                message.setMessage("删除失败 -> 未知异常");
            }
            //TODO 删除数据库记录操作
            int i = resourceMapper.deleteByPrimaryKey(id);

            message.setStatus_code("success");
            message.setMessage("删除成功  -> 受影响的行 -> " + i);
            return message;
        }
        message.setStatus_code("error");
        message.setMessage("id不存在，删除失败");
        return message;
    }

    public List<ResourceCustom> findByPaging(Integer toPageNo) throws Exception {
        PagingVO pagingVO = new PagingVO();
        pagingVO.setToPageNo(toPageNo);
        List<ResourceCustom> list = resourceMapperCustom.findByPaging(pagingVO);

        List<ResourceCustom> newList = new ArrayList<ResourceCustom>();

        for (ResourceCustom resourceCustom : list) {
            //添加学生或老师名字，以及课程名
            resourceCustom = IsTeacherMethod(resourceCustom);
            //设置课程名
            String nameByCourse = resourceMapperCustom.findNameByCourse(resourceCustom.getCourseId());
            resourceCustom.setCourseName(nameByCourse);
            //判断文件类型是否zip，设置静态图片
            String photos = resourceCustom.getPhotos();
            String[] split = photos.split("\\.");
            String images = "../images/";

            resourceCustom.setPhotos(images + photos);
            if (split[1].equals("zip")) {
                resourceCustom.setPhotos(images + "zip.png");
            }

            //遍历添加
            newList.add(resourceCustom);
        }
        return list;
    }

    public Boolean save(Resource resource) throws Exception {
        if (resourceMapper.insert(resource) == 1) {
            System.out.println("添加成功," + resource.getGmtCreate());
            return true;
        }

        return null;
    }

    public int getCountResource() {
        ResourceExample resourceExample = new ResourceExample();

        ResourceExample.Criteria criteria = resourceExample.createCriteria();

        criteria.andIdIsNotNull();
        return resourceMapper.countByExample(resourceExample);

    }

    public Resource findById(Integer id) throws Exception {

        return resourceMapper.selectByPrimaryKey(id);
    }

    public List<ResourceCustom> findAll() throws Exception {
        return null;
    }

    public List<ResourceCustom> findByCourseId(Integer CourseId) {
        List<Resource> list = resourceMapper.selectByCourseId(CourseId);
        List<ResourceCustom> resourceCustomList = new ArrayList<ResourceCustom>();
        for (Resource resource : list) {
            ResourceCustom resourceCustom = new ResourceCustom();

            BeanUtils.copyProperties(resource, resourceCustom);
            resourceCustom.setCourseName(resourceMapperCustom.findNameByCourse(resource.getCourseId()));
            resourceCustom = IsTeacherMethod(resourceCustom);
            //受限于SpringMvc的映射判断，只能这样了
            String photos = resourceCustom.getPhotos();
            String[] split = photos.split("\\.");
            String images = "../images/";
            //判断文件类型是否zip，设置静态图片
            resourceCustom.setPhotos(images + photos);
            if (split[1].equals("zip")) {
                resourceCustom.setPhotos(images + "zip.png");
            }

            resourceCustomList.add(resourceCustom);
        }
        return resourceCustomList;
    }

    //判断ResourceCustom下的userId是否教师，返回带有用户名字的ResourceCustom
    private ResourceCustom IsTeacherMethod(ResourceCustom resourceCustom) {

        if (resourceCustom.getIsTeacher() == 0) {
            //学生
            String nameByStudentId = resourceMapperCustom.findNameByStudentId(resourceCustom.getUserId());
            resourceCustom.setUsername(nameByStudentId);
        }
        if (resourceCustom.getIsTeacher() == 1) {
            //老师
            String nameByTeacher = resourceMapperCustom.findNameByTeacherId(resourceCustom.getUserId());
            resourceCustom.setUsername(nameByTeacher);
        }
//        System.out.println("IsTeacherMethod() -> ResourceCustom = "+resourceCustom);
        if (resourceCustom == null) {
            System.out.println("该ResourceCustom类没有数据");
            return null;
        } else {
            return resourceCustom;
        }
    }

    @Override
    public ServerMessage uploadFile(MultipartFile multipartFile, HttpServletRequest request, Integer courseId) {
        ServerMessage message = new ServerMessage();
        Resource resource = new Resource();

        Integer userId = getUserId();
        if (userId == null) {
            message.setStatus_code("error");
            message.setMessage("没有该用户，非正常");
            return message;
        }

        //判断文件空
        if (multipartFile == null || multipartFile.isEmpty()) {
            message.setStatus_code("error");
            message.setMessage("上传失败，请选择你要上传的文件");
            return message;
        }
        //判断文件大小
        long size = multipartFile.getSize();
        if (size > maxRequestSize) {
            message.setStatus_code("error");
            message.setMessage("上传失败，上传文件大小限制20mb");
            return message;
        }

        //判断文件类型，判断恶意行为,查看文件后缀类型
        String originalFilename = multipartFile.getOriginalFilename();
        String[] split = originalFilename.split(".");
        if(split.length>2){
            //恶意用户的异常数据
            message.setStatus_code("error");
            message.setMessage("异常文件数据");
            return message;
        }
        String contentType = multipartFile.getContentType();
        //设置允许的文件类型
        List<String> validTypes = new ArrayList<String>();
        validTypes.add("image/png");
        validTypes.add("image/jpeg");
        validTypes.add("application/x-zip-compressed");
        if (!validTypes.contains(contentType)) {
            String str = "上传失败，仅允许上传以下类型的文件：" + validTypes;
            message.setStatus_code("error");
            message.setMessage(str);
            return message;
        }

        // 获取项目所在路径
//        String myPath=Thread.currentThread().getContextClassLoader().getResource("")+"images/";
        String localPath = request.getServletContext().getRealPath("\\images") + "\\";
        // 这里因为读取问题，我需要两个保存路径，当然，删除的话两个都删
        File uploadDir = new File(fileROOT);
        File uploadDir2 = new File(localPath);
        //生成文件夹
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String newFileName = UUID.randomUUID().toString();
        //截取后缀
//        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //修改文件名字
        String newFullFileName = newFileName + suffix;

        File newFile = new File(uploadDir, newFullFileName);
        File newFile2 = new File(uploadDir2, newFullFileName);

        //TODO Tomcat/images目录一份，本地磁盘一份
        try {
            OutPutFile(multipartFile, newFile);
            OutPutFile(multipartFile, newFile2);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("文件已经保存到：" + newFile.getAbsoluteFile());
//        System.out.println("文件已经保存到：" + newFile2.getAbsoluteFile());

        //TODO 上传完成进行班级绑定该文件,简称insert -> Resource
        //判断教师
        int teacher = teacherService.getCountTeacherById(userId);
        LocalDate localDate = LocalDate.now();
        // 将LocalDate转换为Date
        Date date = Date.valueOf(localDate);
        if (teacher > 0) {
            //是老师
            resource.setUserId(userId);
            resource.setCourseId(courseId);
            resource.setIsTeacher(1);
            resource.setPhotos(newFullFileName);
            resource.setGmtCreate(date);
            resourceMapper.insert(resource);
            message.setStatus_code("success");
//      /teacher/CourseResource?CourseId=
            message.setMessage("redirect:/teacher/selectResource?CourseId="
                    + resource.getCourseId());
            return message;
        }
        int student = studentService.getCountStudentById(userId);
        if (student < 1) {
            //老师，学生都不是，异常数据
            message.setStatus_code("error");
            message.setMessage("绑定失败，用户不存在");
            return message;
        }
        resource.setUserId(userId);
        resource.setCourseId(courseId);
        resource.setIsTeacher(0);
        resource.setPhotos(newFullFileName);
        resource.setGmtCreate(date);
        resourceMapper.insert(resource);
        message.setStatus_code("success");
//      /teacher/CourseResource?CourseId=
        message.setMessage("redirect:/student/selectResource?CourseId="
                + resource.getCourseId());
        return message;
    }

    private void OutPutFile(MultipartFile multipartFile, File file) throws IOException {

        InputStream inputStream = multipartFile.getInputStream();
        OutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[20 * 1024 * 1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        outputStream.close();
    }

    @Override
    public String downloadFiles(List<Integer> ids, HttpServletRequest request) {
        List<String> filename = resourceMapperCustom.findPhotosByIds(ids);

        //TODO 获取多个文件打包成zip
        String zipFileName = UUID.randomUUID().toString() + ".zip";
        try {
            FileOutputStream fos = new FileOutputStream(zipFileName);
            ZipOutputStream zos = new ZipOutputStream(fos);
            //打包
            for (String file : filename) {
                addToZipFile(fileROOT + file, zos);
            }
            //已经打包完毕
            zos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zipFileName;
    }

    /**
     * @author: GPT
     * @deprecated 追加打包
     */
    private static void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(file.getName());
        zos.putNextEntry(zipEntry);
        //缓冲区大小
        byte[] bytes = new byte[1 * 1024 * 1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }
        zos.closeEntry();
        fis.close();
    }

    @Override
    public List<ResourceCustom> selectResource(Integer courseId, String findByDate,HttpServletRequest request) {
        List<Resource> list = resourceMapperCustom.findResourceByCourseIdAndDate(courseId, findByDate);
        List<ResourceCustom> resourceCustomList = new ArrayList<ResourceCustom>();

        for (Resource resource : list) {
            ResourceCustom resourceCustom = new ResourceCustom();

            BeanUtils.copyProperties(resource, resourceCustom);
            resourceCustom.setCourseName(resourceMapperCustom.findNameByCourse(resource.getCourseId()));
            resourceCustom = IsTeacherMethod(resourceCustom);
            //受限于SpringMvc的映射判断，只能这样了

            String photos = resourceCustom.getPhotos();
            String[] split = photos.split("\\.");

            String host = request.getHeader("host");
            String images = "http://"+host+"/images/";

            //判断文件类型是否zip,如果是zip则显示静态图片
            resourceCustom.setPhotos(images + photos);
            if (split[1].equals("zip")) {
                resourceCustom.setPhotos(images + "zip.png");
            }

            resourceCustomList.add(resourceCustom);
        }
        return resourceCustomList;
    }

    @Override
    public List<ResourceCustom> selectResourceByPaging_Admin(String findByDate, Integer toPageNo, HttpServletRequest request) {
        //设置页码
        DateByPaging dateByPaging=new DateByPaging();
        dateByPaging.setDate(findByDate);
        dateByPaging.setToPageNo(toPageNo);
        //查询
        List<Resource> list = resourceMapperCustom.findResourceByPaging_Admin(dateByPaging);
        List<ResourceCustom> resourceCustomList = new ArrayList<ResourceCustom>();

        for (Resource resource : list) {
            ResourceCustom resourceCustom = new ResourceCustom();

            BeanUtils.copyProperties(resource, resourceCustom);
            resourceCustom.setCourseName(resourceMapperCustom.findNameByCourse(resource.getCourseId()));
            resourceCustom = IsTeacherMethod(resourceCustom);
            //受限于SpringMvc的映射判断，只能这样了

            String photos = resourceCustom.getPhotos();
            String[] split = photos.split("\\.");

            String host = request.getHeader("host");
            String images = "http://"+host+"/images/";

            //判断文件类型是否zip,如果是zip则显示静态图片
            resourceCustom.setPhotos(images + photos);
            if (split[1].equals("zip")) {
                resourceCustom.setPhotos(images + "zip.png");
            }

            resourceCustomList.add(resourceCustom);
        }
        return resourceCustomList;
    }

    @Override
    public int getCountResourceByDate(String findByDate) {
        return resourceMapperCustom.getCountResourceByDate(findByDate);

    }
}
