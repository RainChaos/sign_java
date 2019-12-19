package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.mapper.charge.SchoolStructureMapper;
import cn.gljp.sign.admin.mapper.sign.OperatorMapper;
import cn.gljp.sign.admin.mapper.sign.SchoolMapper;
import cn.gljp.sign.utils.JwtUtil;
import cn.gljp.sign.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.*;

@Service
public class SchoolStructureService {
    @Autowired
    private SchoolStructureMapper schoolStructureMapper;

    @Autowired
    private SchoolMapper schoolMapper;
    @Autowired
    private OperatorMapper operatorMapper;

    @Autowired
    private OperatorService operatorService;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 学校架构，到专业为止
     * @param map 前端参数
     * @param checkedList 已勾选的专业
     * @param checked 是否只回传已勾选的专业
     * @return
     */
//    @Cacheable(cacheNames  = "getSchoolStructure")
    public Object getSchoolStructure(Map<String, Object> map, List<String> checkedList, boolean checked, boolean disable) {
        //没有专业的系部，将会删除
        boolean addForm = false;
        if(map.get("addForm") != null){
            addForm = true;
        }


        //获取整个组织架构
        List<Map<String, Object>> schoolStructure = schoolMapper.getSchoolStructure(map);//校区
        for (int j = schoolStructure.size() - 1; j >= 0; j--) {
            map.put("farther_code", schoolStructure.get(j).get("unit_code").toString());
            schoolStructure.get(j).put("disabled", disable);
            List<Map<String, Object>> school = schoolMapper.getSchoolStructure(map);//系
            if(school != null && school.size() > 0){
                schoolStructure.get(j).put("children", school);

                for (int i = school.size() - 1; i >= 0; i--) {
                    map.put("farther_code", school.get(i).get("unit_code").toString());
                    school.get(i).put("disabled", disable);
                    List<Map<String, Object>> dept = schoolMapper.getSchoolStructure(map);//专业
                    if(dept != null && dept.size() > 0){
                        school.get(i).put("children", dept);
                        getMajorExtendField(dept, map, checkedList, checked);
                        school.get(i).put("children", dept);
                    }else{
                        if(addForm)
                            school.remove(i);
                    }
                   /* if(dept.size() == 0 && addForm == true)
                        school.remove(i);*/
                }
            }
            if(school.size() == 0 && addForm == true)
                schoolStructure.remove(j);
        }

        //从系部开始往下
        if(map.get("layer") != null && "dept".equals(map.get("layer"))){
            List<Object> structure = new ArrayList<>();
            for (Map<String, Object> item : schoolStructure) {
                if (item.get("children")!=null){
                    List<Object> children = (List<Object>)item.get("children");
                    structure.addAll(children);
                }
            }
            return structure;
        }
        return schoolStructure;
    }

    /**
     * 获取当前专业在所选批次下，扩展字段信息
     * @param major
     * @param map
     */
    public void getMajorExtendField(List<Map<String, Object>> major, Map<String, Object> map, List<String> checkedList, boolean checked){
        for(int i = major.size() - 1; i >= 0; i--){
            map.put("unit_code", major.get(i).get("unit_code").toString());
            Map<String, Object> field = schoolMapper.getMajorExtendField(map);
            if(field != null){
                if(field.get("unit_code").toString().equals("001001002015")){
                    System.out.println("");
                }
                checkedList.add(field.get("unit_code").toString());
                major.get(i).put("checked", true);
                major.get(i).putAll(field);
            }else{
                major.get(i).put("checked", false);
                if(checked){
                    major.remove(i);
                }
            }

        }
        majorSort(major);
    }

    /**
     * 对专业重排序
     * @param major
     */
    public void majorSort(List<Map<String, Object>> major){
        Collections.sort(major, (o1, o2) -> {
            String sort1 = o1.get("sort") == null ? o1.get("unit_sort").toString() : o1.get("sort").toString();
            String sort2 = o2.get("sort") == null ? o2.get("unit_sort").toString() : o2.get("sort").toString();

            Integer value1 = Integer.parseInt(sort1);
            Integer value2 = Integer.parseInt(sort2);

            return value1.compareTo(value2);
        });
    }

    /**
     * 获取已选专业下的班级
     * @param params
     * @return
     */
    public List<Map<String, Object>> getSelectUnitClass(Map<String, Object> params) {

        List<Map<String, Object>> selectUnitClass = schoolMapper.getSelectUnitClass(params);
        return selectUnitClass;
    }

    /**
     * 添加新专业
     * @param params
     */
    public void addSelectUnitFromStructure(Map<String, Object> params) {
        String unit_code = params.get("unit_code").toString();
        if(unit_code.length() == 12){
            //合法专业长度
            Map<String, Object> major = schoolMapper.queryMajor(params);
            if(major != null && !major.isEmpty()){
                Map<String, Object> unitMajor = schoolMapper.queryUnitMajor(params);
                if(unitMajor == null || unitMajor.isEmpty()){
                    //添加该批次下、该专业
                    major.putAll(params);
                    major.put("unit_self_code",major.get("unit_self_code"));
                    schoolMapper.addUnitMajor(major);
                }
            }

        }else{

        }
    }

    /**
     * 删除专业
     * @param params
     */
    @Transactional
    public void deleteSelectUnitFromStructure(Map<String, Object> params) throws Exception {
        String unit_code = params.get("unit_code").toString();
        if(unit_code.length() == 12){
            //合法专业长度
            Integer existTotalStudent = schoolMapper.unitHasStudent(params);
            if(existTotalStudent > 0){
                throw new Exception("错误：该专业已有学生报名，禁止删除");
            }
            schoolMapper.deleteUnitMajor(params);
            schoolMapper.deleteSelectUnitClassWhenDeleteSelectUnit(params);
        }
    }

    /**
     * 更新已选专业信息
     * @param params
     */
    @Transactional
    public void updateSelectUnit(Map<String, Object> params) {
        String unit_code = params.get("unit_code").toString();
        if(unit_code.length() == 12){
            //合法专业长度
            //更新报名系统专业信息
            schoolMapper.updateUnitMajor(params);
            //更新收费系统专业名称
            schoolStructureMapper.updateUnitNameByUnitCode(params);
        }
    }

    /**
     * 更新已选班级信息
     * @param params
     */
    @Transactional
    public void updateSelectUnitClass(Map<String, Object> params) {
        String class_code = params.get("class_code").toString();
        if(class_code.length() == 18){
            //合法班级长度
            //更新报名系统班级信息
            schoolMapper.updateUnitClass(params);
            //更新收费系统班级名称
            schoolStructureMapper.updateClassNameByClassCode(params);
        }
    }

    /**
     * 删除已选班级
     * @param params
     */
    public void deleteSelectUnitClass(Map<String, Object> params) throws Exception {
        Integer hasStudent = schoolMapper.classHasStudent(params);
        if(hasStudent > 0){
            throw new Exception("错误：该班级已有学生报名，禁止删除！");
        }
        schoolMapper.deleteSelectUnitClass(params);
    }

    public List<Map<String, Object>> getClassLists(Map<String, Object> params) {
        return schoolMapper.getClassLists(params);
    }

    /**
     * 创建所选班级
     * @param params
     */
    @Transactional
    public void createSelectUnitClass(Map<String, Object> params) {
        //需要创建班级的class_code
        String[] str = params.get("class_codes").toString().split(",");
        List<String> class_codes = new ArrayList<String>();
        for(String item : str){
            class_codes.add(item);
        }
        for(int i = class_codes.size() - 1; i >= 0; i--){
            String unit_code = class_codes.get(i);
            params.put("unit_code", unit_code);
            //判断当前班级是否已创建
            Integer exist = schoolMapper.classExist(params);
            if(exist > 0){
                class_codes.remove(unit_code);
                continue;
            }
            //根据unitcode获取班级信息
            Map<String,Object> map=schoolMapper.queryMajor(params);
            params.put("unit_self_code", map.get("unit_self_code"));
            schoolMapper.createSelectUnitClass(params);
        }
    }
    /**
     * 获取可选班级数量
     * @param params
     */
    public int getSelectUnitCount(Map<String, Object> params) {
        return  schoolMapper.getSelectUnitCount(params);
    }
    /**
     * 获取可选院系
     *
     */
    public List<Map<String, Object>> getSelectDept(Map<String, Object> map) {
        return  schoolMapper.getSelectDept(map);
    }
    /**
     * 根据批次和学历获取可选院系和专业
     * @param map
     */
    public List<Map<String, Object>> getSelectUnitList(Map<String, Object> map) {
        //获取可报名院系
        List<Map<String, Object>> deptList=schoolMapper.getSelectDept(map);
        //获取可报名专业
        List<Map<String, Object>> majorList= schoolMapper.getSelectUnitList(map);
        for(int j=0;j<deptList.size();j++){
            List<Map<String, Object>> subList= new ArrayList<>();
            for(int i=0;i<majorList.size();i++){
                String subCode=majorList.get(i).get("unit_code").toString();
                subCode=subCode.substring(0,9);
                String unitCode=deptList.get(j).get("unit_code").toString();
                //如果可报名专业属于该院系，则添加
                if (unitCode.equals(subCode) ){
                    subList.add(majorList.get(i));
                }
            }
            deptList.get(j).put("children",subList);
        }
        return  deptList;
    }
    /*
     * 根据单位代码获取单位数据
     * */
    public Map<String, Object> getUnitByUnitCode(String unitCode) {
        return schoolMapper.getUnitByUnitCode(unitCode);
    }
    @Transactional
    public void deleteSelectUnit(Map<String, Object> params) throws Exception {
        Integer hasStudent = schoolMapper.unitHasStudentV2(params);
        if(hasStudent > 0)
            throw new Exception("错误：该专业已有学生报名，禁止删除");
        schoolMapper.deleteSelectUnitClassV2(params);
        schoolMapper.deleteSelectUnit(params);

    }
    public Map<String, Object> isHaveGrade(Integer gradeLength, String currentGrade, String unitCode) {
        return schoolMapper.isHaveGrade(gradeLength,currentGrade,unitCode);
    }

    public List<Map<String, Object>> getClassListByMajor(Map<String, Object> majorUnit) {
        return  schoolMapper.getClassListByMajor(majorUnit);
    }

    public Integer getStudentCountByClassCode(String classCode, Integer type) {
        return  schoolMapper.getStudentCountByClassCode(classCode,type);
    }

    public Map<String, Object> queryUnitMajor(Map<String, Object> dataFormat) {
        return  schoolMapper.queryUnitMajor(dataFormat);
    }

    public List<Map<String, Object>> getStuCountByUnitCode(String classCode) {
        return schoolStructureMapper.getStuCountByUnitCode(classCode);
    }

    public List<Map<String, Object>> getChargeItemListByUnitID(Map<String, Object> chargeStuMap) {
        return schoolStructureMapper.getChargeItemListByUnitID(chargeStuMap);
    }

    public Map<String, Object> queryMajor(Map<String, Object> param) {
        return  schoolMapper.queryMajor(param);
    }

    public Map<String, Object> queryUnitClass(Map<String, Object> paramMap) {
        return schoolMapper.queryUnitClass(paramMap);
    }
    public List<Map<String, Object>> queryUnitClassV2(Map<String, Object> paramMap) {
        return schoolMapper.queryUnitClassV2(paramMap);
    }

    /**
     * 组织架构同步
     */
    @Transactional
    public void synStructure() {
        //获取收费系统单位架构
        List<Map<String, Object>> schoolStructureList=schoolStructureMapper.getSchoolStructureList();
        for (Map<String, Object> chargeSchoolStructure:schoolStructureList){
            //封装收费系统单位架构参数到报名系统单位架构Map中
            Map<String, Object> signSchoolStructure=new HashMap<>();
            signSchoolStructure.put("unit_code",chargeSchoolStructure.get("UnitID"));
            signSchoolStructure.put("unit_self_code",chargeSchoolStructure.get("UnitCode"));
            signSchoolStructure.put("unit_name",chargeSchoolStructure.get("UnitName"));
            signSchoolStructure.put("farther_code",chargeSchoolStructure.get("FartherID"));
            signSchoolStructure.put("unit_type",chargeSchoolStructure.get("StudentType"));
            signSchoolStructure.put("unit_manage_system",chargeSchoolStructure.get("ManageSystem"));
            signSchoolStructure.put("unit_status",chargeSchoolStructure.get("Stata"));
            signSchoolStructure.put("unit_term",chargeSchoolStructure.get("Term"));
            signSchoolStructure.put("unit_entrance_date",chargeSchoolStructure.get("EntranceData"));
            signSchoolStructure.put("unit_sort",chargeSchoolStructure.get("Row_Index"));
            //根据unit_self_code查询该单位结构在报名系统是否存在
            Map<String, Object> unit = schoolMapper.queryMajor2(signSchoolStructure);
            //如果存在则根据条件选择是否修改，否则添加
            if (unit!=null){
                //判断两个map的值是否有改变，没有则跳过
                unit.remove("update_time");
                unit.remove("create_time");
                boolean flag= Utils.mapCompar(unit,signSchoolStructure);
                if (!flag){
                    continue;
                }
                signSchoolStructure.put("update_time",new Date());
                schoolMapper.updateSchoolStructure2(signSchoolStructure);
                //如果unit_code有更新
                if (!unit.get("unit_code").equals(signSchoolStructure.get("unit_code"))){
                    //更新可报名专业或者可报名班级的unit_code
                    if (signSchoolStructure.get("unit_code").toString().length()==12){
                        schoolMapper.updateSelectUnitBySelfCode(signSchoolStructure);
                    }else if(signSchoolStructure.get("unit_code").toString().length()==18){
                        schoolMapper.updateSelectUnitClassBySelfCode(signSchoolStructure);
                        //更新报名系统学生表的unit_code
                        signSchoolStructure.put("unit_code_update",unit.get("unit_code"));
                        schoolMapper.updateStudentByUnitCode(signSchoolStructure);
                    }
                }

            }else {
                signSchoolStructure.put("create_time",new Date());
                signSchoolStructure.put("update_time",new Date());
                schoolMapper.addSchoolStructure(signSchoolStructure);
            }
        }
    }

    public Integer getChildsCount(Map<String, Object> map) {
        return schoolStructureMapper.getChildsCount(map);
    }

    public String getNextUnitCode(Map<String, Object> map) {
        Map<String, Object> unitMap=schoolStructureMapper.getNextUnitCode(map);
        String code="";
        if (map.get("farther_code")!=null){
            code=map.get("farther_code").toString()+"001";
        }
        if (unitMap.size()>0){
            Integer number=Integer.parseInt(unitMap.get("UnitID").toString().substring(14));
            number++;
            if (map.get("farther_code")!=null){
                code=map.get("farther_code").toString()+number.toString().substring(1,4);
            }
        }
        return code;
    }

    public void addSchoolStructure(Map<String, Object> map) {
        schoolMapper.addSchoolStructure(map);
    }

    public void updateSchoolStructure(Map<String, Object> map) {
        schoolMapper.updateSchoolStructure(map);
    }

    public Map<String, Object> queryUnitClassV3(Map<String, Object> map) {
        return schoolStructureMapper.queryUnitClass(map);
    }

    public void addChargeSchoolStructure(Map<String, Object> map) {
        schoolStructureMapper.addSchoolStructure(map);
    }
    public void updateChargeSchoolStructure(Map<String, Object> map) {
        schoolStructureMapper.updateSchoolStructure(map);
    }

    public void addUnitChargeItem(Map<String, Object> classChargeItem) {
        schoolStructureMapper.addUnitChargeItem(classChargeItem);
    }

    public Integer getClassCountByUnitID(Map<String, Object> map) {
        return schoolStructureMapper.getClassCountByUnitID( map);
    }
    @Transactional
    public void deleteSchoolStructure(Map<String, Object> map) {
        //先删除可选班级表里的班级数据
        schoolMapper.deleteSelectUnitClassByUnitID(map);
        schoolMapper.deleteSchoolStructure(map);
    }
   @Transactional
    public void deleteChargeSchoolStructure(Map<String, Object> map) {
        //先删除班级下的收费项目
        schoolStructureMapper.deleteUnitChargeItem(map);
        schoolStructureMapper.deleteChargeSchoolStructure(map);
    }
    @Transactional
    public String getMajorCodeByName(String name){
        String code = schoolMapper.getMajorCodeByName(name);
        return code;
    }
    //根据年级代码和班级名称获取班级信息或者创建新班级
    @Transactional
    public Map<String, Object> getNewClass(String nianjiCode, String banjiName, Map<String, Object> params) throws Exception {
        Map<String,Object> map=new HashMap<>();
        map.put("unit_code",nianjiCode);
        //根据年级代码查询年级信息
        Map<String, Object> nianjiMap=schoolMapper.queryMajor(map);
        //根据年级代码和班级名称判断班级是否存在
        Map<String, Object> classList = schoolMapper.queryUnitClassV3(nianjiCode, banjiName);
        //如果存在该班级,直接返回
        if (classList!=null){
            return classList;
        }else {
            //不存在则新建
            Map<String,Object> classMap=new HashMap<>();
            classMap.put("farther_code",nianjiCode);
            classMap.put("unit_code",getNextUnitCode(classMap));
            Integer banjiCount=getChildsCount(classMap)+1;
            Long selfCode=Long.parseLong(nianjiMap.get("unit_self_code").toString()+"00")+banjiCount;
            classMap.put("unit_name",banjiName);
            classMap.put("unit_self_code",selfCode.toString());
            classMap.put("unit_entrance_date",nianjiMap.get("unit_entrance_date"));
            classMap.put("unit_manage_system",nianjiMap.get("unit_manage_system"));
            classMap.put("unit_sort",banjiCount);
            classMap.put("unit_status",nianjiMap.get("unit_status"));
            if (nianjiMap.get("unit_term")==null||"".equals(nianjiMap.get("unit_term").toString())){
                String unitTerm=schoolMapper.getClassUnitItemByNID(nianjiCode);
                classMap.put("unit_term",unitTerm);
            }else {
                classMap.put("unit_term",nianjiMap.get("unit_term"));
            }
            classMap.put("unit_type",nianjiMap.get("unit_type"));
            classMap.put("update_time",new Date());
            classMap.put("create_time",new Date());
            classMap.put("token",params.get("token"));
            //新增班级到数据库
            saveUnit(classMap);
            return classMap;
        }
    }

    @Transactional
    public void saveUnit(Map<String, Object> map) throws Exception {
        String token = request.getHeader("Admin-Sign-Token");
        if (map.get("unit_code")!=null){
            boolean flag=true;
            //根据unit_code判断该班级在报名库是否存在
            List<Map<String, Object>> classMap=queryUnitClassV2(map);
            if (classMap.size()>0){
                flag=false;
            }
            //如果不存在则新增，否则修改
            if (flag){
                map.put("update_time",new Date());
                map.put("create_time",new Date());
               addSchoolStructure(map);
               //赋予用户班级权限
                Map<String, Object> operator = JwtUtil.tokenCheck(token);
                operator.put("class_code",map.get("unit_code"));
                operatorMapper.insertOperatorClassV2(operator);
            }else {
                updateSchoolStructure(map);
            }
            //根据unit_code判断该班级在收费库是否存在
            Map<String, Object> classMap2=queryUnitClassV3(map);
            //如果不存在则新增，否则修改
            if (classMap2!=null){
                flag=false;
            }
            //如果不存在则新增，否则修改
            if (flag){
                addChargeSchoolStructure(map);
                // 从收费系统获取上级设置好的收费项目，并且添加到新单位中
                map.put("UnitCode",map.get("farther_code"));
                List<Map<String, Object>> chargeItems=getChargeItemListByUnitID(map);
                if (chargeItems==null){
                    throw new Exception("错误：该年级尚未设置收费项目");
                }
                //添加学生收费项目到数据库表中
                for (Map<String, Object> chargeItem:chargeItems) {

                    Map<String, Object> classChargeItem = new HashMap<>();
                    classChargeItem.put("UnitID", map.get("unit_code"));
                    classChargeItem.put("Number", chargeItem.get("Number"));
                    classChargeItem.put("ItemID", chargeItem.get("ItemID"));
                    classChargeItem.put("Standard", chargeItem.get("Standard"));
                    classChargeItem.put("Receivable", chargeItem.get("Receivable"));
                    classChargeItem.put("Coverage", chargeItem.get("Coverage"));
                    addUnitChargeItem(classChargeItem);
                }
            }else {
                updateChargeSchoolStructure(map);
            }
        }
    }

    public void updateCache(){
        redisTemplate.delete("SchoolStructure");
        redisTemplate.delete("checkedList");
        redisTemplate.delete("selectUnitTrue");

    }
    @Transactional
    public void addSelectUnit(Map<String, Object> params) {
        if (params.get("farther_code")==null){
            params.put("farther_code","001");
        }
        List<String> checkedList = new LinkedList<>();
        //获取架构——到专业
        List<Map<String, Object>> schoolStructure = (List<Map<String, Object>>) getSchoolStructure(params, checkedList, false, true);
        //院系列表
        List<Map<String, Object>> selectUnit = (List<Map<String, Object>>) schoolStructure.get(0).get("children");
        for (Map<String, Object> map:selectUnit){
            //专业列表
            List<Map<String, Object>> unitList = (List<Map<String, Object>>) map.get("children");
            for (Map<String, Object> unit:unitList){
                unit.put("pici_id",params.get("pici_id"));
                //添加到可选择专业中
                addSelectUnitFromStructure(unit);
                //获取专业下的班级列表
                List<Map<String, Object>> classLists = getClassLists(unit);
                if (classLists.size()==0){
                    continue;
                }
                //查询可选择专业表
                Map<String, Object> unitMap = queryUnitMajor(unit);
                //封装添加可选择班级参数
                Map<String, Object> createMap=new HashMap<>();
                createMap.put("pici_id",params.get("pici_id"));
                createMap.put("select_unit_id",unitMap.get("id"));
                List<String> list=new ArrayList<>();
                for (Map<String, Object> classMap:classLists){
                    list.add(classMap.get("unit_code").toString());
                }
                String class_codes=String.join(",",list);
                createMap.put("class_codes",class_codes);
                createSelectUnitClass(createMap);
            }
        }
        updateCache();
    }

}
