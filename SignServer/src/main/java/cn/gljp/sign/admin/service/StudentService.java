package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.entity.ChargeItem;
import cn.gljp.sign.admin.entity.Config;
import cn.gljp.sign.admin.mapper.charge.ChargeStudentMapper;
import cn.gljp.sign.admin.mapper.sign.OfferMapper;
import cn.gljp.sign.admin.mapper.sign.OperatorMapper;
import cn.gljp.sign.admin.mapper.sign.SchoolMapper;
import cn.gljp.sign.admin.mapper.sign.StudentMapper;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import cn.gljp.sign.utils.Utils;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private ChargeStudentMapper chargeStudentMapper;
    @Autowired
    private SchoolMapper schoolMapper;
    @Autowired
    private DictService dictService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private OperatorService operatorService;
    @Autowired
    private SchoolStructureService schoolStructureService;
    @Autowired
    private ChargeItemService chargeItemService;
    @Autowired
    private OperatorMapper operatorMapper;
    @Autowired
    private OfferMapper offerMapper;

    static ExecutorService executor = Executors.newFixedThreadPool(12);
    /**
     * 按班级权限，获取学生列表
     * @param params
     * @param formConfigLists
     * @param operator
     * @return
     */
    public LinkedHashMap<String, Object> getStudentList(Map<String, Object> params, List<Map<String, Object>> formConfigLists, Map<String, Object> operator) throws ExecutionException, InterruptedException {
        List<ChargeItem> chargeItemList = chargeItemService.getChargeItemList();
        Map<String, Object> chargeItems = chargeItemFormat(chargeItemList);
        StringBuilder ids = new StringBuilder();


        String clazzStr = clazzFormat(operator);
        params.put("clazzStr", clazzStr);

        new Utils().pageParams(params);
        String conditionSql = searchParamsFormat(params);
        if(!"".equals(conditionSql))
            params.put("condition", conditionSql + " ");

        getStudentField(formConfigLists, params);
        List<LinkedHashMap<String, Object>> studentList = studentMapper.getStudentList(params);
        Long studentListCount = studentMapper.getStudentListCount(params);
        Map<String, Object> formConfig = formConfigFormat(formConfigLists);
        Map<String, Object> formDictConfig = formDictConfigFormat(formConfigLists);

        studentListDataFormatJob(studentList, ids, formDictConfig, formConfig);

        idsFormat(ids, studentList);

        if(studentList.size() > 0 && !ids.toString().equals("")){
            getChargeInfo(studentList, chargeItems, ids.toString());
        }

        LinkedHashMap<String, Object> returnData = new LinkedHashMap<>();
        returnData.put("total", studentListCount);
        returnData.put("data", studentList);

        return returnData;
    }

    public void idsFormat(StringBuilder ids, List<LinkedHashMap<String, Object>> studentList){
        for(LinkedHashMap<String, Object> student : studentList){
            String id = student.get("id").toString();
            ids.append(id).append(",");
        }
    }

    public void studentListDataFormatJob(List<LinkedHashMap<String, Object>> studentList, StringBuilder ids, Map<String, Object> formDictConfig, Map<String, Object> formConfig) throws ExecutionException, InterruptedException {
        List<List<LinkedHashMap<String, Object>>> lists = averageAssign(studentList, 12);
        Future<List<LinkedHashMap<String, Object>>> future = executor.submit(new Callable<List<LinkedHashMap<String, Object>>>() {
            @Override
            public List<LinkedHashMap<String, Object>> call() throws Exception {
                return studentListDataFormat(lists.get(0), ids, formDictConfig, formConfig);
            }
        });
        Future<List<LinkedHashMap<String, Object>>> future1 = executor.submit(new Callable<List<LinkedHashMap<String, Object>>>() {
            @Override
            public List<LinkedHashMap<String, Object>> call() throws Exception {
                return studentListDataFormat(lists.get(1), ids, formDictConfig, formConfig);
            }
        });
        Future<List<LinkedHashMap<String, Object>>> future2 = executor.submit(new Callable<List<LinkedHashMap<String, Object>>>() {
            @Override
            public List<LinkedHashMap<String, Object>> call() throws Exception {
                return studentListDataFormat(lists.get(2), ids, formDictConfig, formConfig);
            }
        });
        Future<List<LinkedHashMap<String, Object>>> future3 = executor.submit(new Callable<List<LinkedHashMap<String, Object>>>() {
            @Override
            public List<LinkedHashMap<String, Object>> call() throws Exception {
                return studentListDataFormat(lists.get(3), ids, formDictConfig, formConfig);
            }
        });
        Future<List<LinkedHashMap<String, Object>>> future4 = executor.submit(new Callable<List<LinkedHashMap<String, Object>>>() {
            @Override
            public List<LinkedHashMap<String, Object>> call() throws Exception {
                return studentListDataFormat(lists.get(4), ids, formDictConfig, formConfig);
            }
        });
        Future<List<LinkedHashMap<String, Object>>> future5 = executor.submit(new Callable<List<LinkedHashMap<String, Object>>>() {
            @Override
            public List<LinkedHashMap<String, Object>> call() throws Exception {
                return studentListDataFormat(lists.get(5), ids, formDictConfig, formConfig);
            }
        });
        Future<List<LinkedHashMap<String, Object>>> future6 = executor.submit(new Callable<List<LinkedHashMap<String, Object>>>() {
            @Override
            public List<LinkedHashMap<String, Object>> call() throws Exception {
                return studentListDataFormat(lists.get(6), ids, formDictConfig, formConfig);
            }
        });
        Future<List<LinkedHashMap<String, Object>>> future7 = executor.submit(new Callable<List<LinkedHashMap<String, Object>>>() {
            @Override
            public List<LinkedHashMap<String, Object>> call() throws Exception {
                return studentListDataFormat(lists.get(7), ids, formDictConfig, formConfig);
            }
        });
        Future<List<LinkedHashMap<String, Object>>> future8 = executor.submit(new Callable<List<LinkedHashMap<String, Object>>>() {
            @Override
            public List<LinkedHashMap<String, Object>> call() throws Exception {
                return studentListDataFormat(lists.get(8), ids, formDictConfig, formConfig);
            }
        });
        Future<List<LinkedHashMap<String, Object>>> future9 = executor.submit(new Callable<List<LinkedHashMap<String, Object>>>() {
            @Override
            public List<LinkedHashMap<String, Object>> call() throws Exception {
                return studentListDataFormat(lists.get(9), ids, formDictConfig, formConfig);
            }
        });
        Future<List<LinkedHashMap<String, Object>>> future10 = executor.submit(new Callable<List<LinkedHashMap<String, Object>>>() {
            @Override
            public List<LinkedHashMap<String, Object>> call() throws Exception {
                return studentListDataFormat(lists.get(10), ids, formDictConfig, formConfig);
            }
        });
        Future<List<LinkedHashMap<String, Object>>> future11 = executor.submit(new Callable<List<LinkedHashMap<String, Object>>>() {
            @Override
            public List<LinkedHashMap<String, Object>> call() throws Exception {
                return studentListDataFormat(lists.get(11), ids, formDictConfig, formConfig);
            }
        });
        future.get();
        future1.get();
        future2.get();
        future3.get();
        future4.get();
        future5.get();
        future6.get();
        future7.get();
        future8.get();
        future9.get();
        future10.get();
        future11.get();
    }


    /**
     * 将一个list均分成n个list
     * @param source
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source,int n){
        List<List<T>> result=new ArrayList<List<T>>();
        int remaider = source.size()%n;  //(先计算出余数)
        int number = source.size()/n;  //然后是商
        int offset = 0;//偏移量
        for(int i = 0; i < n; i++){
            List<T> value = null;
            if(remaider > 0){
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            }else{
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

    public List<LinkedHashMap<String, Object>> studentListDataFormat(List<LinkedHashMap<String, Object>> studentList, StringBuilder ids, Map<String, Object> formDictConfig, Map<String, Object> formConfig){
        for(Map<String, Object> student : studentList){
            for(Map.Entry<String, Object> entry : student.entrySet()){
                HashMap<String, Object> map = new HashMap<>();
                map.put("key", entry.getKey());
                map.put("value", entry.getValue());
                Object data_type = formDictConfig.get(entry.getKey());
                map.put("data_type", formDictConfig.get(entry.getKey()));

                if("省市区".equals(formConfig.get(entry.getKey())) || "单位结构".equals(formConfig.get(entry.getKey()))  ){

                    String value = studentMapper.stuInfoConvert(map);
                    entry.setValue(value);
                }
            }
        }
        return studentList;
    }

    public Long getStudentListCount(Map<String, Object> params, List<Map<String, Object>> formConfigLists, Map<String, Object> operator) {

        String clazzStr = clazzFormat(operator);
        params.put("clazzStr", clazzStr);

        new Utils().pageParams(params);
        String conditionSql = searchParamsFormat(params);
        if(!"".equals(conditionSql))
            params.put("condition", conditionSql + " ");

        getStudentField(formConfigLists, params);
        Long studentListCount = studentMapper.getStudentListCount(params);

        return studentListCount;
    }

    public Map<String, Object> chargeItemFormat(List<ChargeItem> chargeItemList){
        HashMap<String, Object> chargeItems = new HashMap<>();
        for (ChargeItem item: chargeItemList) {
            chargeItems.put(item.getId().toString(), item.getFieldName());
        }
        return chargeItems;
    }

    /**
     * 获取学生的缴费信息
     */
    public void getChargeInfo(List<LinkedHashMap<String, Object>> studentList, Map<String, Object> chargeItems, String ids){
        ids = ids.substring(0, ids.length() - 1);
        List<Map<String, Object>> studentChargeItem = chargeStudentMapper.getStudentChargeById(ids);
        for (LinkedHashMap<String, Object> student : studentList) {
            String id = student.get("id").toString();
//            List<Map<String, Oz`bject>> studentChargeItems = new List<Map<String, Object>>;
            for (Map<String, Object> item : studentChargeItem) {
                String studentID = item.get("StudentID").toString();
                if(id.equals(studentID)){
                    try {
                        String itemID = item.get("ItemID").toString();//收费项目id
                        String chargeItemName = chargeItems.get(itemID).toString();//收费项目字段名
                        if(student.containsKey(chargeItemName))
                            student.put(chargeItemName, item.get("ReceivableMoney"));//应收金额
                        if(student.containsKey("pay_" + chargeItemName))
                            student.put("pay_" + chargeItemName, item.get("PayMoney"));//已交金额
                    }catch (Exception e){
                        continue;
                    }
                }
            }
        }

//        System.out.println(studentChargeItem);
    }

    /**
     * 学生字段获取
     * @param params
     */
    public void getStudentField(List<Map<String, Object>> formConfigLists, Map<String, Object> params){
        String fieldList = "";
        StringBuilder fieldName = new StringBuilder();
//        List<Map<String, Object>> studentField = studentMapper.getStudentField(params);
        for(Map<String, Object> item : formConfigLists){
            String field_name = item.get("field_name").toString();
            fieldName.append(field_name).append(",");
        }
        fieldList = fieldName.substring(0, fieldName.length() - 1);
        params.put("fieldList", fieldList);
    }

    /**
     * 班级class_code封装
     * @param operator
     * @return
     */
    public String clazzFormat(Map<String, Object> operator){
        StringBuilder str = new StringBuilder();
        operator.put("operator_id", operator.get("id"));
        List<String> operatorClass = operatorService.getOperatorClass(operator);
        for (String clazz : operatorClass) {
            str.append("'").append(clazz).append("'").append(",");
        }
        str.deleteCharAt(str.length() - 1);
        return str.toString();
    }

    /**
     * 搜索条件
     * @param params
     * @return
     */
    public String searchParamsFormat(Map<String, Object> params){
        String conditionsSql = "";//搜索条件where语句
        params.put("table_name", params.get("form_name"));
        List<Map<String, Object>> field = getSearchField(params);//搜索条件字段

        if(params.get("conditions") == null){//没有搜索条件
            return "";
        }

        JSONObject conditions = JSONObject.parseObject(params.get("conditions").toString());//前端获取到的搜索条件信息

        for (Map<String, Object> item : field) {
            //搜索字段的值
            String value = "";
            String where_sql = "";
            try {
                value = conditions.get(item.get("field_name").toString()).toString();
                if(value == null || "null".equals(value)|| "".equals(value) || "-1".equals(value)){//搜索的值为空，跳过
                    continue;
                }
            }catch (Exception e){
                continue;
            }

            if("日期".equals(item.get("data_type"))){
                //搜索字段字典为日期类型
                //日期类型传值格式为字符串："yyyy-mm-dd","yyyy-mm-dd" 前者为开始时间，后者为结束时间
                String[] date = value.split(",");
                if(date.length == 2){
                    where_sql = MessageFormat.format(item.get("where_sql").toString(), date[0], date[1] + " 23:59:59");
                    conditions.put(item.get("field_name").toString(), where_sql);
                }
            }else if("省市区".equals(item.get("data_type"))){
                if(!"".equals(value)){
                    String[] region = value.split(",");
                    if(region.length == 1){
                        where_sql = MessageFormat.format(item.get("where_sql").toString(), region[region.length - 1].substring(0, 2));
                    }else if(region.length == 2){
                        where_sql = MessageFormat.format(item.get("where_sql").toString(), region[region.length - 1].substring(0, 4));
                    }else if(region.length == 3){
                        where_sql = MessageFormat.format(item.get("where_sql").toString(), region[region.length - 1]);
                    }
                    conditions.put(item.get("field_name").toString(), where_sql);
                }
            }else{
                //其他字典类型不做处理
                //值原样替换
                if(value.equals("未填写"))
                    value = "";
                where_sql = MessageFormat.format(item.get("where_sql").toString(), value );
                conditions.put(item.get("field_name").toString(), where_sql);
            }

            if(conditionsSql != ""){
                conditionsSql = conditionsSql + " and " + where_sql;
            }else{
                conditionsSql += where_sql;
            }
        }

        //关键字条件：姓名，学号，身份证
        if(conditions.get("keyword") != null){
            String keyword = conditions.get("keyword").toString();
            if(!"".equals(keyword)){
                if(!"".equals(conditionsSql))
                    conditionsSql = conditionsSql + " and ";
                conditionsSql = conditionsSql + " (stu.name like '%"+ keyword +"%'" + " or stu.sno like '%"+ keyword +"%'" + " or stu.idcode like '%"+ keyword +"%')";
            }
        }

        return conditionsSql;
    }

    /**
     * 表单配置数据格式转换
     * @param formConfigLists
     * @return
     */
    public Map<String, Object> formConfigFormat(List<Map<String, Object>> formConfigLists){
        HashMap<String, Object> formConfig = new HashMap<>();
        for(Map<String, Object> config : formConfigLists){
            formConfig.put(config.get("field_name").toString(), config.get("data_type"));
        }
        return formConfig;
    }

    /**
     * 表单数据字典格式转换
     * @param formConfigLists
     * @return
     */
    public Map<String, Object> formDictConfigFormat(List<Map<String, Object>> formConfigLists){
        HashMap<String, Object> formConfig = new HashMap<>();
        for(Map<String, Object> config : formConfigLists){
            formConfig.put(config.get("field_name").toString(), config.get("dictionary_type"));
        }
        return formConfig;
    }

    /**
     * 获取搜索字段
     * @param params
     * @return
     */
    public List<Map<String, Object>> getSearchField(Map<String, Object> params) {
        List<Map<String, Object>> field = dictService.getSearchField(params);

        for(Map<String, Object> item : field){
            if("标准数据".equals(item.get("data_type"))){
                List<Map<String, Object>> dictionary = dictService.getDictionaryByFieldName(item);
                item.put("dataLists", dictionary);
            }
        }
        return field;
    }
    /**
     * 前端报名字段格式矫正
     * @param params
     * @return
     */
    public Map<String, Object> dataFormat(Map<String, Object> params) {
        Iterator it = params.keySet().iterator();
        for (; it.hasNext();) {
            String key =  it.next().toString();
            if (params.get(key) instanceof String){
                String value =params.get(key).toString();
                params.put(key, value.trim());
            }
        }
        return  params;
    }

    /**
     * 添加学生信息
     * @param map
     * @return
     */
    @Transactional
    public Map<String, Object> addStudentInfo(Map<String, Object> map) throws Exception {
        /*添加学生信息到收费系统*/
        Map<String, Object> chargeStuMap=new HashMap<>();
        Map<String, Object> classMap=schoolMapper.queryMajor(map);
        if (map.get("idcode")!=""&&map.get("idcode")!=null){
            String idcode=map.get("idcode").toString();
            chargeStuMap.put("SPassword",idcode.substring(12));
        }else {
            chargeStuMap.put("SPassword","666666");
        }
        //填充表单数据到chargeStuMap中
        chargeStuMap.put("UnitCode",map.get("unit_code"));
        chargeStuMap.put("SID",map.get("sno"));
        chargeStuMap.put("StudentName",map.get("name"));
        chargeStuMap.put("Sex","男".equals(map.get("sex"))?"男":"女");
        chargeStuMap.put("IDcode",map.get("idcode"));
        chargeStuMap.put("Telephone",map.get("phone"));
        chargeStuMap.put("Address",map.get("address"));
        chargeStuMap.put("State","在校");
        chargeStuMap.put("Type",classMap.get("unit_type"));
        chargeStuMap.put("StudentID","");
        chargeStudentMapper.addChargeStudentInfo(chargeStuMap);
        //如果添加成功,则根据班级的收费项目，把收费项目添加给这个学生
        //根据班级代码获取班级收费项目列表
        List<Map<String, Object>> unitChargeItemList=schoolStructureService.getChargeItemListByUnitID(chargeStuMap);
        if (unitChargeItemList.size()==0||unitChargeItemList==null){
            throw new Exception("错误：添加的学生的专业下的班级还没设置收费项目，请联系财务处进行设置");
        }
        //添加学生收费项目到数据库表中

        for (Map<String, Object> chargeItem:unitChargeItemList){

            Map<String, Object> stuChargeItem=new HashMap<>();
            stuChargeItem.put("StudentID",chargeStuMap.get("StudentID"));
            stuChargeItem.put("Term",classMap.get("unit_term"));
            stuChargeItem.put("Num",chargeItem.get("Number"));
            stuChargeItem.put("ItemID",chargeItem.get("ItemID"));
            stuChargeItem.put("ChargeStanderd",chargeItem.get("Standard"));
            stuChargeItem.put("ReceivableMoney",chargeItem.get("Receivable"));
            stuChargeItem.put("RemitMoney",0);
            stuChargeItem.put("PayMoney",0);
            stuChargeItem.put("LimitCost",0);
            stuChargeItem.put("UnLimitCost",0);
            stuChargeItem.put("FactArrears",0);
            stuChargeItem.put("LoanMoney",0);
            stuChargeItem.put("Remark","");
            //如果学生不住校，住宿费改为0
            //根据收费项目ID查询收费项目名称
            Map<String, Object> chargeItemMap=chargeStudentMapper.getChargeItemByID(stuChargeItem.get("ItemID").toString());
            if (map.get("is_zhuxiao")!=null&&map.get("is_zhuxiao").equals("否")&&chargeItemMap.get("ItemName").equals("住宿费")){
                stuChargeItem.put("ReceivableMoney",0);
            }

            chargeStudentMapper.addStudentChargeItem(stuChargeItem);
        }

        /*添加学生信息到报名系统中*/
        map.put("id",chargeStuMap.get("StudentID"));
//        map.put("is_register", "否");
//        map.put("is_pay", "否");
//        map.put("ldybz_check", "否");
        map.put("create_time",new Date());
        map.put("update_time",new Date());
        studentMapper.addSignStudentInfo(map);
        return map;
    }
    /*
    * 获取判断学生是否唯一字段列表
    * */
    public List<String> getUnitqueFieldList(Map<String, Object> map) {
       return studentMapper.getUnitqueFieldList(map);
    }
    /*
     * 判断学生是否唯一
     * */
    public Boolean isHaveStudent(Map<String, Object> map, List<String> unitqueFieldList) {
        Map<String, Object> searchMap=new HashMap<>();
        for (String s:unitqueFieldList){
            searchMap.put(s,map.get(s));
        }
        if(map.get("sno")!=null){
            searchMap.put("sno",map.get("sno"));
        }
        Integer count=studentMapper.isHaveStudent(searchMap);
        if (count==0||count==null){
            return false;
        }else {
            return true;
        }
    }
    /*
    * 获取学生班级单位，并且判断人数是否达到限制
    * 1.学生选择专业下不存在对应年级
    * 2.学生选择专业不存在可报名班级
    * 3.学生选择专业下班级学生已满
    * stuClassCode 正常的可报名班级代码
    * */
    public String getStudentClassCode(Config config, Map<String, Object> dataFormat, Map<String, Object> majorUnit) throws Exception {
        //定义学生报名的班级
        String stuClassCode="";
        String majorName=majorUnit.get("unit_name").toString();
        //获取年级代码长度
        Integer gradeLength=config.getSchoolStructure()-3;
        //获取当前年级
        String currentGrade = config.getCurrentGrade();
        String unitCode=dataFormat.get("unit_code").toString();
        //查询学生选择专业下是否存在对应年级
        Map<String, Object> isHaveGrade= schoolStructureService.isHaveGrade(gradeLength,currentGrade,unitCode);
        if (isHaveGrade==null ||isHaveGrade.isEmpty()){
            throw new Exception("错误："+majorName+"专业下不存在对应的年级单位及班级单位数据");
        }
        //查询学生选择专业是否存在可报名班级
        List<Map<String,Object>> classList=schoolStructureService.getClassListByMajor(majorUnit);
        if (classList.size()==0){
            throw new Exception("错误："+majorName+"专业下不存在可报名的班级单位数据，请先在可报名班级设置");
        }

        for (Map<String,Object> classMap:classList) {
            String classCode=classMap.get("class_code").toString();
            //按照班级总人数限制
            String limitClassType=config.getLimitClassType();
            if (limitClassType.equals("1")){
                //限制总人数
                Integer limitCount=Integer.parseInt(classMap.get("number").toString());
                //根据班级代码查询总人数
                Integer totalCount=schoolStructureService.getStudentCountByClassCode(classCode,0);
                if (totalCount>=limitCount){
                    continue;
                }
            }
            //按照男女人数限制
            if (limitClassType.equals("2")){
                //获取报读学生性别
                Integer sex=Integer.parseInt(dataFormat.get("sex").toString());
                //根据性别和班级代码查询总人数
                Integer sexCount=schoolStructureService.getStudentCountByClassCode(classCode,sex);
                if (sex==1&&sexCount>=Integer.parseInt(classMap.get("nan_number").toString())){
                    continue;
                }
                if (sex==2&&sexCount>=Integer.parseInt(classMap.get("nv_number").toString())){
                    continue;
                }
            }
            stuClassCode=classCode;
            break;
        }
        //判断是否自动添加班级
        String isAutoAddClass=majorUnit.get("is_auto_add_class").toString();
        if (stuClassCode==""&&isAutoAddClass=="true"){
            //添加新的班级

        }else if(stuClassCode==""&&isAutoAddClass=="false"){
            throw new Exception("错误："+majorName+"专业下班级学生已满，请先添加新的班级");
        }
        return stuClassCode;
    }

    public String getNewSID(String classCode, Config config) {
        //定义新学号
        String newSID="";
        //获取学号后几位配置个数
        Integer lastNum=config.getLastSnoBumber();
        Map<String,Object> map=new HashMap<>();
        map.put("unit_code",classCode);
        //查找班级信息
        Map<String, Object> classMap = schoolMapper.queryMajor(map);
        //获取班级标识代码
        String selfCode=classMap.get("unit_self_code").toString();
        //替换掉班级标识代码的中文
        selfCode=selfCode.replaceAll("[^\\d]", "");
        //查询班级下学号以班级代码开头的学生
        List<Map<String,Object>> stuList=schoolStructureService.getStuCountByUnitCode(selfCode);

        if (stuList.size()==0||stuList==null){
            //该班级下还没有学号以班级代码开头学生
            String last = String.format("%0"+lastNum+"d",1);
            newSID=selfCode+last;
        }else {
            //班级存在学号以班级代码开头学生，查找最大学号
           /* Long maxSID=0L;
            for (Map<String,Object> stu:stuList){
                Long SID=Long.parseLong(stu.get("SID").toString());
                if (SID>maxSID){
                    maxSID=SID;
                }
            }*/
            Long maxSID=Long.parseLong(stuList.get(0).get("SID").toString());
            //班级已有最大学号加1作为新学号
            maxSID++;
            newSID=maxSID.toString();
        }
        return newSID;
    }

    public Map<String, Object> getStudentInfoById(Map<String, Object> params) {
        Map<String, Object> student = studentMapper.getStudentInfoById(params);
        return student;
    }

    public List<Map<String, Object>> getEditFormConfigList(Map<String, Object> params) {
        if (params.get("pici_id")==null){
            params.put("pici_id",configService.getCurrentPici());
        }
        //获取学生信息
        Map<String, Object> student = studentMapper.getStudentInfoById(params);
        //可以用于获取编辑时的字段配置
        List<Map<String, Object>> data = dictService.getAddFormConfigLists(params);
        for(int i = data.size() - 1; i >= 0; i--){
            String field_name = data.get(i).get("field_name").toString();
            String value = "";
            try{
                value = student.get(field_name).toString();
                data.get(i).put("value", value);
            }catch (Exception e){
                data.get(i).put("value", value);
                continue;
            }

            if("".equals(value)) continue;

            if(data.get(i).get("data_type").toString().equals("省市区")){
                String province = value.substring(0, 2) + "0000";
                String city = value.substring(0, 4) + "00";
                LinkedList<Integer> region = new LinkedList<>();
                region.add(Integer.parseInt(province));
                region.add(Integer.parseInt(city));
                region.add(Integer.parseInt(value));
                data.get(i).put("value", region);
            }else if(data.get(i).get("data_type").toString().equals("单位结构")){
                HashMap<String, Object> map = new HashMap<>();
                map.put("key", field_name);
                map.put("value", value);
                value = studentMapper.stuInfoConvert(map);
                data.get(i).put("value", value);
            }else if(data.get(i).get("data_type").toString().equals("图片")){
                Map<String,Object> mapFile=new HashMap<>();
                mapFile.put("name",value.substring(value.lastIndexOf("\\")+1));
                mapFile.put("url",value);
                mapFile.put("field_name",field_name);
                List<Map<String,Object>> file=new ArrayList<>();
                file.add(mapFile);
                data.get(i).put("file",file);
            }
        }

        return data;
    }

    public List<Map<String, Object>> getStudentChargeItems(Map<String, Object> map) {
        return chargeStudentMapper.getStudentChargeItems(map);
    }
    @Transactional
    public Map<String, Object> editStudentInfo(Map<String, Object> map) throws Exception {
        //格式校验，去掉前后空格
        Map<String, Object> dataFormat=studentService.dataFormat(map);
        //获取判断学生是否存在的唯一的字段
        List<String> unitqueFieldList=studentService.getUnitqueFieldList(dataFormat);
        if (unitqueFieldList.size()>0){
            //根据唯一字段判断学生是否存在
            Boolean isHaveStudent=studentService.isHaveStudent(dataFormat,unitqueFieldList);
            if (isHaveStudent){
                throw new Exception("错误：要编辑的信息在系统中已有重复字段，请重新编辑");
            }
        }
        //获取学生编辑前信息
        Map<String, Object> stuMap=studentService.getStudentInfoById(dataFormat);
        dataFormat.put("pici_id",stuMap.get("pici_id"));
        if (dataFormat.get("unit_code")!=null){
            dataFormat.remove("unit_code");
            /*//如果所选专业和编辑前信息一致
            if (stuMap.get("unit_code").toString().startsWith(dataFormat.get("unit_code").toString())){
                //删除unit_code参数，班级代码不做修改
                dataFormat.remove("unit_code");
            }else {
                //获取配置数据
                Config config = configService.getConfig();
                //根据专业代码和批次获取单位数据
                Map<String, Object> majorUnit=schoolStructureService.queryUnitMajor(dataFormat);
                //获取学生班级单位，并且判断人数是否达到限制
                String classCode=studentService.getStudentClassCode(config,dataFormat,majorUnit);
                dataFormat.put("unit_code",classCode);
            }*/
        }

        /*添加学生信息到收费系统*/
        Map<String, Object> chargeStuMap=new HashMap<>();
        Map<String, Object> classMap=new HashMap<>();
        if (dataFormat.get("unit_code")!=null){
           classMap=schoolMapper.queryMajor(dataFormat);
        }
        if (dataFormat.get("idcode")!=null&&!"".equals(dataFormat.get("idcode"))){
            chargeStuMap.put("SPassword",dataFormat.get("idcode").toString().substring(12));
        }else {
            chargeStuMap.put("SPassword","666666");
        }
        //填充表单数据到chargeStuMap中
        if (dataFormat.get("unit_code")!=null){
            chargeStuMap.put("UnitCode",dataFormat.get("unit_code"));
        }
        chargeStuMap.put("StudentName",dataFormat.get("name"));
        chargeStuMap.put("Sex","1".equals(dataFormat.get("sex"))?"男":"女");
        chargeStuMap.put("IDcode",dataFormat.get("idcode"));
        chargeStuMap.put("Telephone",dataFormat.get("phone"));
        chargeStuMap.put("Address",dataFormat.get("address"));
        chargeStuMap.put("State","在校");
        if (classMap.size()>0){
            chargeStuMap.put("Type",classMap.get("unit_type"));
        }
        //根据学号修改学生收费系统信息
        String studentid=dataFormat.get("id").toString();
        chargeStudentMapper.editChargeStudentInfo(chargeStuMap,studentid);

        dataFormat.put("update_time",new Date());
        studentMapper.editSignStudentInfo(dataFormat);
        //如果学生修改了专业，调用收费系统接口修改学生的收费项目
        if (dataFormat.get("unit_code")!=null){
            changeChargeItemsByStudentIdAndUnitCode(dataFormat.get("unit_code").toString(),dataFormat.get("id").toString());
        }
        return dataFormat;
    }
    @Value("${charge.interfaceUrl}")
    private   String chargeInterfaceUrl;
    public void setChargeInterfaceUrl(String chargeInterfaceUrl) {
        this.chargeInterfaceUrl = chargeInterfaceUrl;
    }
    public void changeChargeItemsByStudentIdAndUnitCode(String unitCode, String studentid) {
        String postUrl=chargeInterfaceUrl+"?method=changestudentclassandunitid&studentID="+studentid+"&unitID="+unitCode;
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = "";
        HttpGet httpGet = new HttpGet(postUrl);
        RequestConfig build = RequestConfig.custom().setConnectTimeout(2000).setSocketTimeout(2000).build();
        httpGet.setConfig(build);
        try {
            response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode != HttpStatus.SC_OK){
                httpGet.abort();
                throw new Exception("错误:" + "修改学生收费项目失败！");
            }
            result = EntityUtils.toString(response.getEntity());
            JSONObject operator = JSONObject.parseObject(result);

        } catch (Exception e) {
        }
    }


    public Map<String, Object> getStudentInfoBySno(Map<String, Object> map) {
        return studentMapper.getStudentInfoBySno(map);
    }

    public List<Map<String, Object>> searchStudentInfo(Map<String, Object> dataFormat) {
        return studentMapper.searchStudentInfo(dataFormat);
    }

    public Integer getLdybzCount(Map<String, Object> majorUnit) {
        return studentMapper.getLdybzCount(majorUnit);
    }

    public void editStudentInfoForLDYBZ(Map<String, Object> map) {
        studentMapper.editSignStudentInfo(map);
    }

    public List<Map<String, Object>> getShowChargeItems() {
        return chargeItemService.getShowChargeItems();
    }

    public Long getStudentListCount(Map<String, Object> params) {
        return studentMapper.getStudentListCount(params);
    }

    @Value("${noticeFileUrl}")
    private String noticeFileUrl;
    public String getElectronicOffer(Map<String, Object> user) throws Exception {
        Map<String, Object> bg = offerMapper.getPrintBg();

        String targetUrl = "http://localhost:"+ request.getLocalPort() +"/SignServer/notice/"+ user.get("idcode") +".html";
        String fileDesUrl = noticeFileUrl + user.get("idcode") +".jpg";
        HtmlToImage(targetUrl, fileDesUrl, bg);

        return user.get("idcode") +".jpg";
    }

    /**
     * 获取学生字段值，生成静态页面
     * @param printAttr
     * @param user
     * @throws IOException
     */
    public void offerHtmlGenerate(List<Map<String, Object>> printAttr, Map<String, Object> user) throws IOException {
        String content = "";
        String barcodeHtml = "";
        Map<String, Object> bg = offerMapper.getPrintBg();

        content += "<img style=\"position: absolute\" src=\"http://localhost:"+ request.getLocalPort() +"/SignServer/notice/template.jpg\" width=\"" + bg.get("width") +"\" height=\""+ bg.get("height") +"\"/>";

        for(Map<String, Object> attr : printAttr){
            String opt_sql = attr.get("opt_sql").toString();
            if(opt_sql.contains(":student_id")){
                opt_sql = opt_sql.replace(":student_id", user.get("id").toString());
            }
            List<Map<String, Object>> printData = operatorMapper.operatorSql(opt_sql);
            if(printData.size() > 0){
                String value = printData.get(0).get("value").toString();
                if(attr.get("barcode") != null && !attr.get("barcode").toString().trim().equals("")){
                    long id = System.currentTimeMillis();
                    content += "<img id="+ id +" style=\""+ attr.get("style") +"\"/>";
                    String barcode = barCodeHtml(id, attr.get("barcode").toString(), value);
                    barcodeHtml += barcode;
                }else{
                    content += "<p style=\"position: absolute; text-align:center; word-break:break-all;"+ attr.get("style") +"\">"+ value +"</p>";
                }

            }
        }
        FileWriter fileWriter = new FileWriter(noticeFileUrl + user.get("idcode") + ".html");
        fileWriter.write("<!DOCTYPE html>\n<style>*{margin:0;padding:0}</style>" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" + "<script src=\"./JsBarcode.all.min.js\"></script>" +
                "<body style=\"margin=0px; padding=0px\">\n" +
                content +
                "</body>\n" +
                "</html>\n" + "<script>" + barcodeHtml+ "</script>");
        fileWriter.close();
    }

    public String barCodeHtml(long id, String barcode, String value){
        String barcodeHtml = "var barcode" + id + " = document.getElementById('" + id + "');var str" + id + " = '" + value
                + "';var options" + id + " = " + barcode + ";JsBarcode(barcode" + id + ", str" + id + ", options"+id + ");";
        return barcodeHtml;
    }

    @Value("${wkhtmlurl}")
    private String wkhtmlurl;
    public void HtmlToImage(String targetUrl, String fileDesUrl, Map<String, Object> bg) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(wkhtmlurl, "--crop-w", bg.get("width").toString(), targetUrl, fileDesUrl);
        Process process;
        process = pb.start();
        BufferedReader errStreamReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line = null;
        line = errStreamReader.readLine();
        while(line != null) {
            System.out.println(line);
            line = errStreamReader.readLine();
        }
        process.destroy();

    }

    public Map<String, Object> searchStudentInfoV2(Map<String, Object> dataFormat) throws Exception {
        String name = "";
        String idcode = "";
        if (dataFormat.get("name") != null && !"".equals(dataFormat.get("name").toString())){
            name = dataFormat.get("name").toString();
        }else{
            throw new Exception("错误：姓名不能为空");
        }
        if (dataFormat.get("idcode") != null && !"".equals(dataFormat.get("idcode").toString())){
            idcode = dataFormat.get("idcode").toString();
        }else{
            throw new Exception("错误：身份证号不能为空");
        }
        //修改学生到收费系统和报名系统
        List<Map<String, Object>> studentInfo = studentService.searchStudentInfo(dataFormat);
        if (studentInfo.size()==0){
            throw new Exception("错误：不存在该学生,请先报名后在查询学生信息");
        }
        if (studentInfo.size()>1){
            throw new Exception("错误：学生信息重复，请联系学校老师进行处理");
        }

        return studentInfo.get(0);
    }


    public String getUnitCodeByID(Map<String, Object> map) {
        return schoolMapper.getUnitCodeByID(map);
    }

    public List<Map<String, Object>> getClassListByNID(String nid) {
        return  schoolMapper.getClassListByNID(nid);
    }
    @Transactional
    public void modifyStudentClass(Map<String, Object> map) {
        //修改mysql数据库中：base_student表的unit_code字段
        studentMapper.modifyStudentClass(map);
        //修改sqlserver数据库中：t_scs_student表中的unitCode字段
        chargeStudentMapper.modifyStudentClass(map);
    }
    public void modifyStudentClass2(Map<String, Object> map) {
        //修改mysql数据库中：base_student表的unit_code字段和转班备注
        studentMapper.modifyStudentClass(map);
    }

    public void setRegister(Map<String, Object> map) {
        studentMapper.setRegister(map);
    }

    public void changeStudentStatus(Map<String, Object> map) {
        studentMapper.changeStudentStatus(map);
    }
    public void autoFenBan(Map<String, Object> params) throws Exception {
        //根据年级代码、批次和是否缴费获取年级下的学生列表
        List<Map<String,Object>> stuList=studentMapper.getStudentListBySearch(params);
        //男生列表
        List<Map<String,Object>> nanList= stuList.stream().filter(map->map.get("sex").toString().equals("男")).collect(Collectors.toList());
        //女生列表
        List<Map<String,Object>> nvList= stuList.stream().filter(map->map.get("sex").toString().equals("女")).collect(Collectors.toList());
        //随机打乱排序
        Collections.shuffle(nanList);
        Collections.shuffle(nvList);
        Integer banjiIndex=1;
        //男生循环插入分班班级
        banjiIndex=autoChangeUnitCode(nanList,params,banjiIndex);
        //女生循环插入分班班级
        autoChangeUnitCode(nvList,params,banjiIndex);
    }
    @Transactional
    public int autoChangeUnitCode(List<Map<String, Object>> list, Map<String, Object> params, Integer banjiIndex) throws Exception {
        //班级数量
        Integer banjiNumber=3;
        if (params.get("banji_number")!=null){
            banjiNumber=Integer.parseInt(params.get("banji_number").toString());
        }
        //班级名称
        String banjiName="";
        if (params.get("banji_template")!=null){
            banjiName=params.get("banji_template").toString();
        }
        //年级代码
        String nianjiCode="";
        if (params.get("nianji_code")!=null){
            nianjiCode=params.get("nianji_code").toString();
        }
        for (Map<String, Object> stuMap:list){
            if (banjiIndex==banjiNumber+1){
                banjiIndex=1;
            }
            String tempName=banjiName.replace("#N#",banjiIndex.toString());
            //根据年级代码和班级名称获取或者创建班级信息
            Map<String,Object> classMap=new HashMap<>();
            try {
                classMap=schoolStructureService.getNewClass(nianjiCode,tempName,params);
            } catch (Exception e) {
                throw new Exception("错误：获取班级信息失败");
            }
            //更新报名系统的学生班级代码
            stuMap.put("unit_code",classMap.get("unit_code"));
            studentMapper.changeUnitCodeByID(stuMap);
            //更新收费系统的学生班级代码
            stuMap.put("targetClass",classMap.get("unit_code"));
            stuMap.put("student_id",stuMap.get("id"));
            chargeStudentMapper.modifyStudentClass(stuMap);
            banjiIndex++;
        }
        return banjiIndex;
    }
    public void autoFenBanByLDYBZ(Map<String, Object> params) throws Exception {
        //根据年级代码、批次和是否缴费获取年级下的学生列表
        List<Map<String,Object>> stuList=studentMapper.getStudentListBySearch(params);
        //男生列表
        List<Map<String,Object>> nanList= stuList.stream().filter(map->map.get("sex").toString().equals("男")).collect(Collectors.toList());
        //女生列表
        List<Map<String,Object>> nvList= stuList.stream().filter(map->map.get("sex").toString().equals("女")).collect(Collectors.toList());
        //随机打乱排序
        Collections.shuffle(nanList);
        Collections.shuffle(nvList);
        //男生区分劳免
        List<Map<String,Object>> nanLaoList= nanList.stream().filter(map->map.get("is_ldybz").toString().equals("是")).collect(Collectors.toList());
        List<Map<String,Object>> nanMianList= nanList.stream().filter(map->map.get("is_ldybz").toString().equals("否")).collect(Collectors.toList());
        //女生区分劳免
        List<Map<String,Object>> nvLaoList= nvList.stream().filter(map->map.get("is_ldybz").toString().equals("是")).collect(Collectors.toList());
        List<Map<String,Object>> nvMianList= nvList.stream().filter(map->map.get("is_ldybz").toString().equals("否")).collect(Collectors.toList());
        Integer laoIndex=1;
        Integer mianIndex=Integer.parseInt(params.get("lao_banji_number").toString())+1;
        //劳学生循环插入劳班班级
        params.put("ldybz","true");
        laoIndex=autoChangeUnitCodeByLDYBZ(nanLaoList,params,laoIndex);
        autoChangeUnitCodeByLDYBZ(nvLaoList,params,laoIndex);
        //免学生循环插入免班班级
        params.put("ldybz","false");
        mianIndex=autoChangeUnitCodeByLDYBZ(nanMianList,params,mianIndex);
        autoChangeUnitCodeByLDYBZ(nvMianList,params,mianIndex);

    }
    @Transactional
    public int autoChangeUnitCodeByLDYBZ(List<Map<String, Object>> list, Map<String, Object> params, Integer banjiIndex) throws Exception {
        //总班级数量
        Integer banjiNumber=0;
        //劳班级数量
        Integer laoBanjiNumber=0;
        Integer mianBanjiNumber0;
        //免班级数量
        if (params.get("lao_banji_number")!=null){
            laoBanjiNumber=Integer.parseInt(params.get("lao_banji_number").toString());
            banjiNumber+=Integer.parseInt(params.get("lao_banji_number").toString());
        }
        if (params.get("mian_banji_number")!=null){
            mianBanjiNumber0=Integer.parseInt(params.get("mian_banji_number").toString());
            banjiNumber+=Integer.parseInt(params.get("mian_banji_number").toString());
        }
        //班级名称
        String banjiName="";
        if (params.get("banji_template")!=null){
            banjiName=params.get("banji_template").toString();
        }
        //年级代码
        String nianjiCode="";
        if (params.get("nianji_code")!=null){
            nianjiCode=params.get("nianji_code").toString();
        }
        for (Map<String, Object> stuMap:list){
            String tempName="";
            if ("true".equals(params.get("ldybz").toString())){
                if (banjiIndex==laoBanjiNumber+1){
                    banjiIndex=1;
                }
                tempName= banjiName.replace("#N#",banjiIndex.toString())+"(劳)";
            }else {
                if (banjiIndex==banjiNumber+1){
                    banjiIndex=laoBanjiNumber+1;
                }
                tempName= banjiName.replace("#N#",banjiIndex.toString())+"(免)";
            }


            //根据年级代码和班级名称获取或者创建班级信息
            Map<String,Object> classMap=new HashMap<>();
            try {
                classMap=schoolStructureService.getNewClass(nianjiCode,tempName,params);
            } catch (Exception e) {
                throw new Exception("错误：获取班级信息失败");
            }
            //更新报名系统的学生班级代码
            stuMap.put("unit_code",classMap.get("unit_code"));
            studentMapper.changeUnitCodeByID(stuMap);
            //更新收费系统的学生班级代码
            stuMap.put("targetClass",classMap.get("unit_code"));
            stuMap.put("student_id",stuMap.get("id"));
            chargeStudentMapper.modifyStudentClass(stuMap);
            banjiIndex++;
        }
        return banjiIndex;
    }

    public List<String> getSnoById(String[] ids){
        return studentMapper.getSnoById(ids);
    }

    public void setStudentDorm(Map<String, Object> params){
        studentMapper.setStudentDorm(params);
    }

    public void editStudentChargeStatus(Map<String, Object> chargeStatus) {
        studentMapper.editSignStudentInfo(chargeStatus);
    }
    @Transactional
    public void deleteStudentInfo(Map<String, Object> params) throws Exception {
        //查询该学生是否已缴费
        List<Map<String, Object>> studentChargeItemList=studentService.getStudentChargeItems(params);
        Double totalMoney= 0.00;
        for (Map<String, Object> chargeItem:studentChargeItemList){
            totalMoney+=Double.parseDouble(chargeItem.get("PayMoney").toString());
        }
        //如果已缴费，不能删除
        if (totalMoney>0){
            throw new Exception("错误：该学生已经缴费，不能删除");
        }
        //删除收费系统学生数据
        chargeStudentMapper.deleteStudentInfo(params);
        //删除报名系统学生数据
        studentMapper.deleteStudentInfo(params);
    }
}
