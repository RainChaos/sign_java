package cn.gljp.sign.admin.mapper.sign;

import java.util.List;
import java.util.Map;

public interface OfferMapper {
    List<Map<String, Object>> getPrintTemplateList(Map<String, Object> params);

    Long getPrintTemplateCount(Map<String, Object> params);

    Map<String, Object> getPrintTemplateById(Map<String, Object> params);

    List<Map<String, Object>> getPrintAttr(Map<String, Object> params);

    void setIsPrint(Map<String, Object> params);

    void updatePrintTemplate(Map<String, Object> params);

    void createPrintTemplate(Map<String, Object> params);

    void deletePrintTemplate(Map<String, Object> params);

    void updatePrintTemplateAttr(Map<String, Object> params);

    void deletePrintTemplateAttr(Map<String, Object> params);

    void createPrintTemplateAttr(Map<String, Object> params);

    Map<String, Object> getPrintBg();
}
