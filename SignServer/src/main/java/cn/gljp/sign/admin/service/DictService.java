package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.entity.Dictionary;
import cn.gljp.sign.admin.entity.DictionaryType;
import cn.gljp.sign.admin.mapper.sign.DictMapper;
import cn.gljp.sign.admin.mapper.sign.FieldMapper;
import cn.gljp.sign.admin.mapper.sign.OperatorMapper;
import cn.gljp.sign.utils.PageResult;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DictService {
    @Autowired
    private DictMapper dictMapper;
    @Autowired
    private FieldMapper fieldMapper;
    @Autowired
    private OperatorMapper operatorMapper;

    public List<DictionaryType> getDictionaryTypeList() {
        return  dictMapper.getDictionaryTypeList();
    }

    public List<DictionaryType> listForPid(int pid) {
        return  dictMapper.listForPid(pid);

    }

    public void insert(DictionaryType dictionaryType) throws Exception {
        dictMapper.insert(dictionaryType);
    }

    public void update(DictionaryType dictionaryType) {
        dictMapper.update(dictionaryType);

    }

    public void delete(DictionaryType dictionaryType) {
        dictMapper.delete(dictionaryType);
    }

    public Result listDictionaryByType(int pageIndex, int pageSize, String type) {
        Long total=dictMapper.listDictionaryByTypeCount(type);
        List<Dictionary> list = dictMapper.listDictionaryByType(pageIndex, pageSize, type);
        PageResult<Dictionary> pageList=new PageResult<Dictionary>(total,list);
        return  new Result(StatusCode.OK,true, "", pageList);
    }

    public void dictionaryInsert(Dictionary dictionary) {
        dictMapper.dictionaryInsert(dictionary);
    }
    @Transactional
    public void dictionaryUpdate(Dictionary dictionary) {
        //获取修改前的数据
        Dictionary oldDict = dictMapper.getDictById(dictionary.getId().toString());
        //执行更新
        dictMapper.dictionaryUpdate(dictionary);
        //获取学生表数据需要修改的字段
        List<String> fieldNameList = fieldMapper.getFieldNameByDictionaryType(oldDict.getType());

        for(String fieldName : fieldNameList){
            String value = dictionary.getCode();
            String sql = "update base_student set `" + fieldName + "` = '" + value + "' where `" + fieldName+ "` = '" + oldDict.getCode() + "'";
            operatorMapper.excuteSql(sql);
        }


    }

    public void dictionaryDelete(List<String> ids) {
        dictMapper.dictionaryDelete(ids);
    }

    public void updateStatus(Dictionary dictionary) {
        dictMapper.updateStatus(dictionary);
    }

    public void setOrderSortAll(String type) {
        List<Dictionary> dictionaryList= dictMapper.listDictionaryByType2(type);
        short i=1;
        for (Dictionary d: dictionaryList) {
            d.setSort(i);
            dictMapper.dictionaryUpdate(d);
            i++;
        }
    }

    /**
     * 获取报名填报字段
     * @param params
     * @return
     */
    public List<Map<String, Object>> getAddFormConfigLists(Map<String, Object> params){
        List<Map<String, Object>> formConfigLists = dictMapper.getFormConfigLists(params);
        for(Map<String, Object> item : formConfigLists){
            if(null != item.get("dictionary_type") && !"".equals(item.get("dictionary_type"))){
                //dictionary_type不为空时，获取字典数据
                //添加status参数，获取启用的数据
                item.put("status", '1');
                List<Map<String, Object>> result = dictMapper.getDictionaryByType(item);
                item.put("dataLists", result);
            }
            if("单位结构".equals(item.get("data_type"))){
                item.put("value", new ArrayList<String>());
            }
            if("图片".equals(item.get("data_type"))){
                item.put("limit", 1);
                item.put("file", new ArrayList<String>());
                item.put("multiple", false);
            }
        }
        return formConfigLists;
    }

    /**
     * 获取表单字段配置
     * @param params
     * @return
     */
    public List<Map<String, Object>> getFormConfigLists(Map<String, Object> params){
        List<Map<String, Object>> formConfigLists = dictMapper.getFormConfigListsV2(params);
        return formConfigLists;
    }

    /**
     * 获取搜索配置字段
     * @param params
     * @return
     */
    public List<Map<String, Object>> getSearchField(Map<String, Object> params) {
        List<Map<String, Object>> field = dictMapper.getSearchField(params);
        return field;
    }

    public List<Map<String, Object>> getDictionaryByFieldName(Map<String, Object> params){
        List<Map<String, Object>> dict = dictMapper.getDictionaryByFieldName(params);
        return dict;
    }

    public List<Map<String, Object>> getStudentEducation() {
        List<Map<String, Object>> dict = dictMapper.getStudentEducation();
        return dict;
    }

    /**
     * 数据字典设置默认值
     * 学生表base_student对应字段，为空且为null的进行赋值
     * 设置数据库字段默认值
     * @param params
     */
    @Transactional
    public void setDefault(Map<Object, String> params) {
        dictMapper.setDefault(params);
        Dictionary dictionary = dictMapper.getDictById(params.get("id"));
        List<String> fieldNameList = fieldMapper.getFieldNameByDictionaryType(dictionary.getType());
        for(String fieldName : fieldNameList){
            params.put("dictionary_type", dictionary.getType());

            List<Map<String, Object>> fieldList = fieldMapper.getFieldByType(params);

            for(Map<String, Object> studentField : fieldList){
                Object columnType = studentField.get("column_type");

                String sql2 = "alter table base_student modify column " + fieldName + " " + columnType + "  default '" + dictionary.getName() + "';";
                operatorMapper.operatorSql(sql2);

                String sql = "update base_student set " + fieldName + " = '" + dictionary.getName() + "' where " + fieldName + " is null or " + fieldName + " = ''";
                operatorMapper.operatorSql(sql);
            }
        }
    }
}
