package cn.gljp.sign.utils;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileUtil {

    public static ArrayList<Object> upload( MultipartFile[] multipartFiles, String baseUrl){
        ArrayList<Object> list = new ArrayList<>();

        if(multipartFiles != null && multipartFiles.length > 0){
            for (MultipartFile item : multipartFiles) {
                Map<String, Object> map = new HashMap<>();
                String filename = UUID.randomUUID().toString().replace("-", "").toUpperCase();
                int hashcode = filename.hashCode();
                int dir1 = hashcode & 0xf; //得到名为1到16的下及文件夹
                int dir2 = (hashcode & 0xf0) >> 4; //得到名为1到16的下下及文件夹

                String name = item.getOriginalFilename();
                String suffix = name.substring(name.lastIndexOf("."));

                String url = File.separator + "Upload" + File.separator + dir1 + File.separator + dir2 + File.separator;
                String storeUrl = "/Upload/" + dir1 + "/" + dir2 + "/";
                String dir = baseUrl + url; //得到文件路径

                File file = new File(dir, filename + suffix);
                try {
                    if(!file.exists()){
                        file.mkdirs();
                        file.createNewFile();
                    }
                    item.transferTo(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                map.put("url", storeUrl + filename + suffix);
                list.add(map);
            }
        }
        return list;
    }



    public static void responseTo(File file, HttpServletResponse response){
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());

        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
