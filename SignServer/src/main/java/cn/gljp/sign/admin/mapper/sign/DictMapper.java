package cn.gljp.sign.admin.mapper.sign;

import cn.gljp.sign.admin.entity.Dictionary;
import cn.gljp.sign.admin.entity.DictionaryType;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DictMapper {
    public List<DictionaryType> getDictionaryTypeList() ;

    List<DictionaryType> listForPid(int pid);
    void insert(DictionaryType dictionaryType) throws  Exception;
    void insertSelective(DictionaryType dictionaryType) throws  Exception;
    void update(DictionaryType dictionaryType);
    void delete(DictionaryType dictionaryType);
    List<Dictionary> listDictionaryByType2(@Param("type") String type);
    List<Dictionary> listDictionaryByType(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize, @Param("type") String type);
    Long listDictionaryByTypeCount(@Param("type")String type);

    void dictionaryInsert(Dictionary dictionary);
    void dictionaryUpdate(Dictionary dictionary);
    void dictionaryDelete(List<String> ids);

    void updateStatus(Dictionary dictionary);

    void setOrderSortAll(String type);

    List<Map<String, Object>> getFormConfigLists(Map<String, Object> params);

    List<Map<String, Object>> getDictionaryByType(Map<String, Object> item);

    List<Map<String, Object>> getSearchField(Map<String, Object> params);

    List<Map<String, Object>> getDictionaryByFieldName(Map<String, Object> params);

    List<Map<String, Object>> getStudentEducation();

    List<Map<String, Object>> getFormConfigListsV2(Map<String, Object> params);

    Dictionary getDictById(String id);

    void setDefault(Map<Object, String> params);
}
