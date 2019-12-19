package cn.gljp.sign.index.controller;


import cn.gljp.sign.admin.entity.Article;
import cn.gljp.sign.admin.entity.BaseFile;
import cn.gljp.sign.admin.entity.Region;
import cn.gljp.sign.admin.service.*;
import cn.gljp.sign.utils.PageResult;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private MiddleSchoolService middleSchoolService;
    @Autowired
    private SchoolStructureService schoolStructureService;

    @Autowired
    private BatchService batchService;
    @Autowired
    private UploadService uploadService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private DictService dictService;


    private   String baseUrl;
    @Value("${web.upload-path}")
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    @RequestMapping("/student/uploadImage")
    public Result uploadImage(@RequestParam("photo") MultipartFile file, BaseFile baseFile) {
        try {
            uploadService.imgFileCheck(file,baseFile);
            uploadService.uploadImage(file,baseFile,baseUrl);
            configService.insertBaseFileSelective(baseFile);
            return new Result(StatusCode.OK, true,"上传成功",baseFile);
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "上传失败！","上传失败！");
        }
    }
    @RequestMapping("/student/phoneUploadImage")
    public Result phoneUploadImage(@RequestParam("photo") MultipartFile file, BaseFile baseFile, HttpServletRequest request) {
        try {
            uploadService.imgFileCheck(file,baseFile);
            uploadService.uploadImage(file,baseFile,baseUrl);
            configService.insertBaseFileSelective(baseFile);
            String url="http://" + request.getServerName() //服务器地址
                    + ":"
                    + request.getServerPort()           //端口号
                    + request.getContextPath()
                    +baseFile.getUrl();
            url=url.replace("\\","/");
            Map<String,Object> map=new HashMap<>();
            map.put("url",url);
            return new Result(StatusCode.OK, true,"上传成功",map);
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "上传失败！","上传失败！");
        }
    }
    /*
     * 获取请求者IP并传回
     * */
    @RequestMapping("/charge/getIp")
    public Result getIp(HttpServletRequest request) {
        try {
            return new Result(StatusCode.OK, true,"获取成功",request.getRemoteAddr());
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "获取失败！","");
        }
    }
    /*
     * 调用收费系统支付
     * */
    @RequestMapping("/charge/SubMoneyByItems")
    public Result SubMoneyByItems(@RequestBody Map<String, Object> params,HttpServletRequest request) throws UnsupportedEncodingException {
        //获取收费系统接口地址
        String chargeAddress = configService.getChargeAddress();
        //获取支付银行代码
        String bankCode=configService.getBankCode();
        //学生ID
        Integer studentID=Integer.parseInt(params.get("studentId").toString());
        //获取回调地址和重定向url
        String returnUrl=request.getHeader("Origin");
        String redirectUrl = request.getHeader("referer");
        //学生收费项目ID转成List
        String [] items=params.get("its").toString().split(",");
        List<Object> itemList=new ArrayList<>();
        for(String item:items){
            Map<String, Object> autoid=new HashMap<>();
            autoid.put("autoid",item);
            itemList.add(autoid);
        }
        //定义请求收费系统接口的参数
        Map<String, Object> data=new HashMap<>();
        data.put("method","servicepay_auto");
        data.put("studentid",studentID);
        data.put("amount",params.get("shiji"));
        data.put("bankname",bankCode);
        data.put("notifyurl", URLEncoder.encode(returnUrl, "UTF-8"));
        data.put("redirecturl",URLEncoder.encode(redirectUrl, "UTF-8"));
        data.put("items",itemList);
        data.put("openid",params.get("openid"));
        data.put("fingerprint",params.get("fingerprint"));
        data.put("spbill_create_ip",params.get("spbill_create_ip"));
        data.put("istelphone",params.get("istelphone"));
        data.put("isnewcharge","1");
        //转成json
        String json=JSON.toJSONString(data);
        //postUrl
        String postUrl=chargeAddress+"data="+json;
        //post请求缴费系统接口
        String result = "";
        HashMap<String, Object> param = new HashMap<>();
        param.put("data", json);
        result = HttpUtil.post(chargeAddress, param);
        return new Result(StatusCode.OK, true,"支付请求成功",result);
    }
    /*
    * 删除学生图片
    * */
    @RequestMapping("/File/delete")
    public Result deleteImage(@RequestParam(name="url") String url,@RequestParam(name="field_name")String field_name) {
        try {
            uploadService.deleteStuImage(url,field_name);
            return new Result(StatusCode.OK, true,"删除成功","");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "删除失败！","");
        }
    }
    /**
     * 获取批次列表
     * @param params
     * @return
     */
    @RequestMapping(value = "/pici/lists")
    public Result getPiciList(@RequestParam  Map<String, Object> params){
        PageResult<Map<String, Object>> result = new PageResult<>((long) batchService.getBatchCountV2(), batchService.getBatchV2(params));
        return new Result(StatusCode.OK, true, "", result);
    }
    /**
     * 根据地区代码获取不分页学校列表
     * @param regionCode
     * @return
     */
    @RequestMapping("/middleSchool/listSchoolByRegionNoPage")
    public Result listSchoolByRegionNoPage(String regionCode) {
        return middleSchoolService.listSchoolByRegionNoPage(regionCode);
    }
    /**
     * 根据title获取文章
     * @param article
     * @return
     */
    @RequestMapping("/article/getArticleByTitle")
    public Result getArticleByTitle( Article article) {
        Article a =articleService.getArticleByTitle(article);
        return new Result(StatusCode.OK, true,"获取成功",a);
    }
    /**
     * 获取当前批次当前学历下的班级数量
     * @param params
     * @return
     */
    @RequestMapping(value = "/selectUnit/getSelectUnitCount")
    public Result getSelectUnitCount(@RequestParam  Map<String, Object> params){

        int selectUnitCount = schoolStructureService.getSelectUnitCount(params);

        return new Result(StatusCode.OK, true, "", selectUnitCount);
    }
    /**
     * 根据FatherCode获取地区列表
     * @param fatherCode
     * @return
     */
    @RequestMapping("/region/listByFatherCode")
    public Result listByFatherCode(@RequestParam(name="fatherCode")int fatherCode) {
        List<Region> list=regionService.listByFatherCode(fatherCode);
        return new Result(StatusCode.OK,true, "", list);
    }
    /**
     * 根据批次和学历获取院系和专业
     * @param map
     * @return
     */
    @RequestMapping("/schoolStructure/getSelectUnitList")
    public Result getSelectUnitList(@RequestParam Map<String, Object> map){
        List<Map<String, Object>> structure =   schoolStructureService.getSelectUnitList(map);
        return new Result(StatusCode.OK, true, "", structure);
    }
    /**
     * 根据数据类型代码获取数据列表
     * @return
     */
    @RequestMapping("/dictionary/listDictionaryByType")
    public Result listDictionary(String type, @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "20") int limit ) {
        int index=(page-1)*limit;
        return dictService.listDictionaryByType(index,limit,type);
    }
    /**
     * 获取配置数据
     * @param params
     * @return
     */
    @RequestMapping(value = "/main/getConfigData")
    public Result getConfigData(@RequestParam  Map<String, Object> params){
        PageResult<Map<String, Object>> result = new PageResult<>((long) batchService.getBatchCount(), batchService.getBatch(params));
        return new Result(StatusCode.OK, true, "", result);
    }
    /**
     * 获取QQ联系列表
     */
    @RequestMapping(value = "/student/getQQ")
    public Result getQQ(){
        List<Map<String, Object>> QQList= configService.getQQ();
        return new Result(StatusCode.OK, true, "", QQList);
    } /**
     * 获取系统名称
     */
    @RequestMapping(value = "/config/getWebSiteName")
    public Result getWebSiteName(){
        String  webSiteName = configService.getWebSiteName();
        return new Result(StatusCode.OK, true, "", webSiteName);
    }
    /**
     * 获取招生简介地址
     */
    @RequestMapping(value = "/config/getAdmissionsProfile")
    public Result getAdmissionsProfile(){
        String  AdmissionsProfile = configService.getAdmissionsProfile();
        return new Result(StatusCode.OK, true, "",AdmissionsProfile);
    } /**
     * 获取官网地址
     */
    @RequestMapping(value = "/config/getWebSiteAddress")
    public Result getWebSiteAddress(){
        String  webSiteAddress = configService.getWebSiteAddress();
        return new Result(StatusCode.OK, true, "", webSiteAddress);
    }
    /**
     * 获取LOGO
     */
    @RequestMapping(value = "/config/getLOGOIMG")
    public Result getLOGOIMG(){
        String  getLOGOIMG = configService.getLOGOIMG();
        return new Result(StatusCode.OK, true, "", getLOGOIMG);
    }
    /**
     * 手机端获取背景
     */
    @RequestMapping(value = "/config/getBGIMG")
    public Result getBGIMG(){
        String  getBGIMG = configService.getBGIMG();
        return new Result(StatusCode.OK, true, "", getBGIMG);
    }
    /**
     * 获取当前批次已选择的专业
     * @param params
     * @return
     */
    @RequestMapping(value = "/selectUnit/lists")
    public Result getSelectUnit(@RequestParam  Map<String, Object> params){
        //为true时，会使得前端按钮不可选，只可选择报名专业
        boolean disable = true;
        if(null == params.get("farther_code")){
            //新增学生页面，获取专业时不传递该参数
            params.put("farther_code", "001");
            //不禁用每一个选项，令其可选
            disable = false;
        }
        Map data = new HashMap<String, Object>();
        List<String> checkedList = new LinkedList<>();
        Object schoolStructure = schoolStructureService.getSchoolStructure(params, checkedList, true, disable);
        data.put("schoolStructure", schoolStructure);
        data.put("checkedList", checkedList);

        return new Result(StatusCode.OK, true, "", data);
    }


    /**
     * 获取表单字段配置
     * @param params
     * @return
     */
    @RequestMapping(value = "/student/getAddFormConfigLists")
    public Result getAddFormConfigLists(@RequestParam  Map<String, Object> params){

        List<Map<String, Object>> list = dictService.getAddFormConfigLists(params);

        return new Result(StatusCode.OK, true, "", list);
    }



}
