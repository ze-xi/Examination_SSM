package com.system.service;

import com.system.po.Resource;
import com.system.po.ResourceCustom;
import com.system.vo.ServerMessage;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
/*
* 资料的Service层
* */
public interface ResourceService {
    //保存文件的目录
    public final static String fileROOT="D:/Examination_System-master/resources/";
    //最大的文件尺寸 -> 20 MB
    public final static Long maxRequestSize = 20 * 1024 * 1024l;

    //增加Resource
    void insert(Resource resource);

    //通用 ->根据id删除资源信息
    ServerMessage removeById(Integer id,HttpServletRequest request);

    //学生 ->根据id删除资源信息
    ServerMessage removeStudentById(Integer id,HttpServletRequest request);

    //获取分页查询资源信息
    List<ResourceCustom> findByPaging(Integer toPageNo) throws Exception;

    //添加资源信息
    Boolean save(Resource resource) throws Exception;

    //获取资源总数
    int getCountResource();

    //根据id查询
    Resource findById(Integer id) throws Exception;


    //获取全部资源
    List<ResourceCustom> findAll() throws Exception;

    //根据课程id获取课程下的资料 -> List
    List<ResourceCustom> findByCourseId(Integer CourseId);

    //上传文件并绑定
    ServerMessage uploadFile(MultipartFile multipartFile, HttpServletRequest request, Integer courseId);

    String downloadFiles(List<Integer> ids,HttpServletRequest request);

    //根据courseId和日期（可空）查询Resource
    List<ResourceCustom> selectResource(Integer courseId, String findByDate,HttpServletRequest request);

    //根据日期和页码查询Resource
    List<ResourceCustom> selectResourceByPaging_Admin(String findByDate, Integer toPageNo, HttpServletRequest request);
    //根据日期查询总资源数量
    int getCountResourceByDate(String findByDate);
}
