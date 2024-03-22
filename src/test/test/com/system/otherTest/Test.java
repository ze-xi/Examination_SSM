package test.com.system.otherTest;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.system.mapper.ResourceMapper;
import com.system.po.Resource;
import com.system.po.ResourceCustom;
import com.system.service.ResourceService;
import com.system.service.impl.ResourceServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

public class Test {


    @org.junit.Test
    public void testString() {
        String s = "D:/Examination_System-master/resources/";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/");
        String dateDirName = simpleDateFormat.format(new Date());
        File uploadDir = new File(s, dateDirName);
        System.out.println(uploadDir);
    }

    @org.junit.Test
    public void testPaging() throws Exception {

    }

    @org.junit.Test
    public void testCopy() {
        Resource resource = new Resource();
        resource.setId(1);
        resource.setCourseId(1);
        resource.setIsTeacher(0);
        resource.setPhotos("1.png");
        resource.setUserId(10001);
        ResourceCustom resourceCustom = new ResourceCustom();
        BeanUtils.copyProperties(resource, resourceCustom);
        System.out.println(resourceCustom);
    }

    @org.junit.Test
    public void testLib() {
        // 获取项目所在路径
        String mysqlPath = Thread.currentThread().getContextClassLoader()
                .getResource("").toString();
        System.out.println(mysqlPath);
        // 文件夹  你自己的文件夹
        String fileUrl = "images";
    }

    @org.junit.Test
    public void testDate() {
        System.out.println(LocalDate.now());
        LocalDate localDate = LocalDate.now();
        // 将LocalDate转换为Date
        java.sql.Date date= java.sql.Date.valueOf(localDate);
        System.out.println(date);
    }
    @org.junit.Test
    public void testJsonToStringArray(){
        String str="{\"ids\":[\"53\",\"54\",\"56\"]}";
        JSONObject jo = JSONUtil.parseObj(str);
        String[] ids = jo.getJSONArray("ids").toArray(new String[0]);

        System.out.println(Arrays.toString(ids));
    }

}
