package com.system.mapper;

import com.system.po.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Jacey on 2017/6/29.
 */
public interface ResourceMapperCustom {

    //分页查询资源信息
    List<ResourceCustom> findByPaging(PagingVO pagingVO) throws Exception;
    //查询所有数据

    //Student根据user_id查询名字
    String findNameByStudentId(Integer user_id);
    //Teacher根据user_id查询名字
    String findNameByTeacherId(Integer user_id);
    //根据课程id查找课程名
    String findNameByCourse(Integer course_id);
    //根据id查询是否存在数据
    int CountResourceById(Integer id);
    //根据id和Userid查询是否存在数据
    int CountByIdAndUserId(ResourceParameter resourceParameter);
    //根据id查资源名称
    String findPhotoById(Integer id);
    //根据多个id查询多个资源名称
    List<String> findPhotosByIds(List<Integer> ids);
    //根据Course_id和LocalData查询当前日期下的资源
    List<Resource> findResourceByCourseIdAndDate(@Param("courseId")Integer CourseId, @Param("gmtCreate")String findByDate);
    //根据日期和页码查询Resource
    List<Resource> findResourceByPaging_Admin(DateByPaging dateByPaging);
    //根据日期查询总资源数量
    int getCountResourceByDate(String findByDate);
}
