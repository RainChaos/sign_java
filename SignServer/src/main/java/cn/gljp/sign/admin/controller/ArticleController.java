package cn.gljp.sign.admin.controller;

import cn.gljp.sign.admin.entity.Article;
import cn.gljp.sign.admin.entity.ArticleCategory;
import cn.gljp.sign.admin.entity.BaseFile;
import cn.gljp.sign.admin.service.ArticleService;
import cn.gljp.sign.admin.service.ConfigService;
import cn.gljp.sign.utils.FileUtil;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import cn.gljp.sign.utils.TreeUtils;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class ArticleController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ConfigService configService;
    // 支持的文件类型
    private static final List<String> suffixes = Arrays.asList("image/jpg","image/jpeg","image/png","image/gif");



    private   String baseUrl;
    @Value("${web.upload-path}")
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    /*-------文章管理--------------------------------------------------------------------------------------------------------------------------------------*/
    @RequestMapping("/article/uploadImage")
    public Result uploadImage(@RequestParam("articleCategoryThumb") MultipartFile file, BaseFile baseFile) {
        try {
            //校验文件大小
            long size = file.getSize();
            baseFile.setSize(size/1024+"KB");
            if (size/1024/1024>2){
                return new Result(StatusCode.ERROR, false, "上传文件大小超过2M！","上传文件大小超过2M！");
            }
            //校验文件类型
            String type = file.getContentType();
            baseFile.setType(type);
            if(!suffixes.contains(type)){
                return new Result(StatusCode.ERROR, false, "上传文件类型不符！","上传文件类型不符！");
            }
            // 校验文件内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                return new Result(StatusCode.ERROR, false, "上传文件内容不符！","上传文件内容不符！");
            }
            baseFile.setName(file.getName());
            baseFile.setCreateTime(new Date());
            baseFile.setUpdateTime(new Date());
            List list=FileUtil.upload(new MultipartFile[]{file},baseUrl);
            Map<String,Object> map= (Map<String, Object>) list.get(0);
            baseFile.setUrl(map.get("url").toString());
            configService.insertBaseFileSelective(baseFile);
            return new Result(StatusCode.OK, true,"上传成功",baseFile);
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "上传失败！","上传失败！");
        }
    }

    @RequestMapping("/article/getArticleList")
    public Result getArticleList(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "20") int limit) {
        int index = (page - 1) * limit;
        return  articleService.getArticleList(index,limit);
    }
    @RequestMapping("/article/getArticleByTitle")
    public Result getArticleByTitle( Article article) {
        request.setAttribute("params", JSONUtil.toJsonStr(article));
        try {
            Article a =articleService.getArticleByTitle(article);
            return new Result(StatusCode.OK, true,"获取成功",a);
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "查询文章失败！","查询文章失败！");
        }
    }
    @RequestMapping("/article/getOneArticle")
    public Result getOneArticle(@RequestBody Article article) {
        request.setAttribute("params", JSONUtil.toJsonStr(article));
        try {
            Article a =articleService.getOneArticle(article);
            return new Result(StatusCode.OK, true,"获取成功",a);
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "查询文章失败！","查询文章失败！");
        }
    }
    @RequestMapping("/article/insert")
    public Result insert(@RequestBody Article article) {
        request.setAttribute("params", JSONUtil.toJsonStr(article));
        try {
            article.setPublishTime(new Date());
            article.setCreateTime(new Date());
            article.setNewTime(new Date());
            article.setTopTime(new Date());
            article.setUpdateTime(new Date());
            article.setTuijianTime(new Date());
            articleService.insertSelective(article);
            return new Result(StatusCode.OK, true,"添加成功","添加成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "添加文章失败！","添加文章失败！");
        }
    }
    @RequestMapping("/article/update")
    public Result update(@RequestBody Article article) {
        request.setAttribute("params", JSONUtil.toJsonStr(article));
        try {
            article.setUpdateTime( new Date());
            articleService.update(article);
            return new Result(StatusCode.OK, true,"修改成功","修改成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "修改失败","修改失败");
        }
    }
    @RequestMapping("/article/delete")
    public Result delete(@RequestBody Article article) {
        request.setAttribute("params", JSONUtil.toJsonStr(article));
        try {
            articleService.delete(article);
            return new Result(StatusCode.OK, true,"删除成功","删除成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "删除失败","删除失败");
        }
    }
    /*-------文章分类--------------------------------------------------------------------------------------------------------------------------------------*/
    @RequestMapping("/articleCategory/getArticleCategoryTree")
    public Result getArticleCategoryTree() {
            List<ArticleCategory> list=articleService.getArticleCategoryTree();
            list= TreeUtils.listToTreeForArticle(list);
            return new Result(StatusCode.OK, true,"",list);
    }
    @RequestMapping("/articleCategory/listArticleCategoryByFid")
    public Result listArticleCategoryByFid(ArticleCategory article) {
        request.setAttribute("params", JSONUtil.toJsonStr(article));
        List<ArticleCategory> list=articleService.listArticleCategoryByFid(article);
        return new Result(StatusCode.OK, true,"",list);
    }
    @RequestMapping("/articleCategory/insert")
    public Result categoryInsert(@RequestBody ArticleCategory article) {
        request.setAttribute("params", JSONUtil.toJsonStr(article));
        try {
            articleService.categoryInsert(article);
            return new Result(StatusCode.OK, true,"添加成功","添加成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "添加失败！","添加失败！");
        }
    }
    @RequestMapping("/articleCategory/update")
    public Result categoryUpdate(@RequestBody ArticleCategory article) {
        request.setAttribute("params", JSONUtil.toJsonStr(article));
        try {
            articleService.categoryUpdate(article);
            return new Result(StatusCode.OK, true,"修改成功","修改成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "修改失败","修改失败");
        }
    }
    @RequestMapping("/articleCategory/delete")
    public Result categoryDelete(@RequestBody ArticleCategory article) {
        request.setAttribute("params", JSONUtil.toJsonStr(article));
        try {
            articleService.categoryDelete(article);
            return new Result(StatusCode.OK, true,"删除成功","删除成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "删除失败","删除失败");
        }
    }
}
