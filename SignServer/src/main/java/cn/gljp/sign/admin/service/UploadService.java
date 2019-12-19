package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.entity.BaseFile;
import cn.gljp.sign.admin.mapper.sign.StudentMapper;
import cn.gljp.sign.utils.FileUtil;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UploadService {
    @Autowired
    private StudentMapper studentMapper;
    // 支持的文件类型
    private static final List<String> imgSuffixes = Arrays.asList("image/jpg","image/jpeg","image/png","image/gif");

    public void imgFileCheck(MultipartFile file, BaseFile baseFile) throws Exception {
        //校验文件大小
        long size = file.getSize();
        baseFile.setSize(size/1024+"KB");
        if (size/1024/1024>2){
            throw new Exception("错误：上传文件大小超过2M！");
        }

        //校验文件类型
        String type = file.getContentType();
        baseFile.setType(type);
        if(!imgSuffixes.contains(type)){
            throw new Exception("错误：上传文件类型不符！");
        }

        // 校验文件内容
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) {
            throw new Exception("错误：上传文件内容不符！");
        }
        baseFile.setFileName(file.getOriginalFilename());
        baseFile.setCreateTime(new Date());
        baseFile.setUpdateTime(new Date());
    }

    public void uploadImage(MultipartFile file, BaseFile baseFile, String baseUrl) {
        List list = FileUtil.upload(new MultipartFile[]{file}, baseUrl);
        Map<String,Object> map = (Map<String, Object>) list.get(0);
        baseFile.setUrl(map.get("url").toString());
    }
    public void backGroundImage(MultipartFile file, BaseFile baseFile, String baseUrl) {
        List list = FileUtil.upload(new MultipartFile[]{file}, baseUrl);
        Map<String,Object> map = (Map<String, Object>) list.get(0);
        baseFile.setUrl(map.get("url").toString());
    }
    /*
    * 删除学生表里上传的学生照片url
    * */
    public void deleteStuImage(String url, String field_name) {
        studentMapper.deleteStuImage(url,field_name);
    }
}
