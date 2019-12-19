package cn.gljp.sign.admin.mapper.sign;

import cn.gljp.sign.admin.entity.Field;
import cn.gljp.sign.admin.entity.FormField;
import cn.gljp.sign.admin.entity.GridSearchField;
import cn.gljp.sign.admin.entity.SearchField;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FieldMapper {


    List<Field> getStudentFieldListByKey(@Param("keyword") String keyword);

    void insert(Field field);

    void update(Field field);

    void delete(Field field);

    void insertSelective(Field field);

    void updateSelective(Field field);

    List<Field> getStudentFieldListBySearch(Field field);

    List<FormField> getFormFieldBySearch(FormField formField);

    List<FormField> getFieldNameByFormName(FormField formField);

    void formInsert(FormField field);

    void formUpdate(FormField field);

    void formDelete(FormField field);

    void formInsertSelective(FormField field);

    List<Field> getStudentFieldByFieldName(@Param("fieldName") String fieldName);

    void updateStudentFormFieldWidth(FormField field);

    List<SearchField> getStudentSearchFieldList(SearchField searchField);

    List<GridSearchField> getGridSearchFieldListBySearch(GridSearchField gridSearchField);

    List<GridSearchField> getFieldNameByTableName(GridSearchField gridSearchField);

    void gridInsert(GridSearchField gridSearchField);

    void gridUpdate(GridSearchField gridSearchField);

    void gridDelete(GridSearchField gridSearchField);

    void searchDelete(SearchField searchField);

    void searchUpdate(SearchField searchField);

    void searchInsert(SearchField searchField);

    void addColumn(Map<String, Object> map);

    Map<String, Object> getStudentFieldById(Map<Object, String> map);

    List<Map<String, Object>> getAllField();

    List<String> getFieldNameByDictionaryType(String type);

    void studentFieldUpdate(Field field);

    Map<String, Object> getFieldNameByDescribe(Object title);

    List<Map<String, Object>> getField();

    List<Map<String, Object>> getFieldByType(Map<Object, String> params);

    void updateStudentFormFieldWidthV2(FormField field);

    Integer fieldIsExistInTable(FormField field);
}
