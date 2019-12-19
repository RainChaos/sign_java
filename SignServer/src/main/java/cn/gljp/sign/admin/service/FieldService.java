package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.entity.Field;
import cn.gljp.sign.admin.entity.FormField;
import cn.gljp.sign.admin.entity.GridSearchField;
import cn.gljp.sign.admin.entity.SearchField;
import cn.gljp.sign.admin.mapper.sign.FieldMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FieldService {
    @Autowired
    private FieldMapper fieldMapper;

    public List<Field> getStudentFieldListByKey(String keyword) {
        return  fieldMapper.getStudentFieldListByKey(keyword);
    }

    public void insert(Field field) {
        fieldMapper.insertSelective(field);
    }
    public void update(Field field) {
        //修改base_student表结构
        fieldMapper.studentFieldUpdate(field);
        fieldMapper.updateSelective(field);
    }

    public void delete(Field field) {
        fieldMapper.delete(field);
    }

    public List<Field> getStudentFieldListBySearch(Field field) {
        return fieldMapper.getStudentFieldListBySearch(field);
    }

    public List<FormField> getFormFieldBySearch(FormField formField) {
        return fieldMapper.getFormFieldBySearch(formField);
    }

    public List<FormField> getFieldNameByFormName(FormField formField) {
        return  fieldMapper.getFieldNameByFormName(formField);
    }

    public void formInsert(FormField field) {
        fieldMapper.formInsertSelective(field);
    }

    public void formUpdate(FormField field) {
        fieldMapper.formUpdate(field);
    }

    public void formDelete(FormField field) {
        fieldMapper.formDelete(field);
    }

    public List<Field> getStudentFieldByFieldName(String fieldName) {
        return  fieldMapper.getStudentFieldByFieldName(fieldName);
    }

    public void updateStudentFormFieldWidth(FormField field) {
        fieldMapper.updateStudentFormFieldWidth(field);
    }
    @Transactional
    public void setOrderSortAll(FormField field) {
        List<FormField> formFieldList= fieldMapper.getFormFieldBySearch(field);
        int i=1;
        for (FormField d: formFieldList) {
            d.setSort(i);
            fieldMapper.formUpdate(d);
            i++;
        }
    }

    public List<SearchField> getStudentSearchFieldList(SearchField searchField) {
        return  fieldMapper.getStudentSearchFieldList(searchField);
    }

    public List<GridSearchField> getGridSearchFieldListBySearch(GridSearchField gridSearchField) {
        return  fieldMapper.getGridSearchFieldListBySearch(gridSearchField);
    }


    public List<GridSearchField> getFieldNameByTableName(GridSearchField gridSearchField) {
        return fieldMapper.getFieldNameByTableName(gridSearchField);
    }

    public void gridInsert(GridSearchField gridSearchField) {
        fieldMapper.gridInsert(gridSearchField);
    }

    public void gridUpdate(GridSearchField gridSearchField) {
        fieldMapper.gridUpdate(gridSearchField);
    }

    public void gridDelete(GridSearchField gridSearchField) {
        fieldMapper.gridDelete(gridSearchField);
    }

    public void searchInsert(SearchField searchField) {
        fieldMapper.searchInsert(searchField);
    }

    public void searchUpdate(SearchField searchField) {
        fieldMapper.searchUpdate(searchField);
    }

    public void searchDelete(SearchField searchField) {
        fieldMapper.searchDelete(searchField);
    }

    public void addColumn(Field field) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("column", field.getFieldName());
        map.put("type", field.getColumnType());
        map.put("desc", field.getDescribe());
        fieldMapper.addColumn(map);
    }

    public Map<String, Object> getFieldNameByDescribe(Object title) {
        Map<String, Object> field = fieldMapper.getFieldNameByDescribe(title);
        return field;
    }

    public List<Map<String, Object>> getField() {
        return fieldMapper.getField();
    }

    public void fieldNameAndTypeFormat(List<Map<String, Object>> fieldList, Map<String, Object> field, Map<String, Object> type) {
        for (Map<String, Object> item : fieldList) {
            String field_name = item.get("field_name").toString();
            String describe = item.get("describe").toString();
            String data_type = item.get("data_type").toString();

            field.put(describe,field_name);
            type.put(field_name, data_type);
        }
    }

    public void updateStudentFormFieldWidthV2(FormField field) {
        fieldMapper.updateStudentFormFieldWidthV2(field);
    }

    public void fieldIsExistInTable(FormField field) throws Exception {
        Integer integer = fieldMapper.fieldIsExistInTable(field);
        if(integer > 0){
            throw new Exception("错误：字段已配置");
        }
    }
}
