package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.mapper.sign.OfferMapper;
import cn.gljp.sign.admin.mapper.sign.OperatorMapper;
import cn.gljp.sign.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OfferService {
    @Autowired
    private OfferMapper offerMapper;

    @Autowired
    private OperatorMapper operatorMapper;

    /**
     * 获取打印模板
     * @param params
     * @return
     */
    public Map<String, Object> getPrintTemplate(Map<String, Object> params){
        HashMap<String, Object> data = new HashMap<>();

        new Utils().pageParams(params);
        List<Map<String, Object>> list = offerMapper.getPrintTemplateList(params);
        Long total = offerMapper.getPrintTemplateCount(params);

        data.put("total", total);
        data.put("data", list);

        return data;
    }

    /**
     * 打印通知书
     * @param params
     * @return
     */
    public String getPrint(Map<String, Object> params) throws Exception {
        String content = "";
        Map<String, Object> data = offerMapper.getPrintTemplateById(params);
        params.put("status", 1);
        if(data != null && data.size() > 0){
            String template_content = data.get("template_content").toString();
            content = template_content;
            List<Map<String, Object>> printAttr = offerMapper.getPrintAttr(params);
            for(Map<String, Object> attr : printAttr){
                String opt_sql = attr.get("opt_sql").toString();
                if(opt_sql.contains(":student_id")){
                    if(params.get("student_id") == null)
                        throw new Exception("错误：表格设置学生ID可能未配置");
                    opt_sql = opt_sql.replace(":student_id", params.get("student_id").toString());
                }
                List<Map<String, Object>> printData = operatorMapper.operatorSql(opt_sql);
                if(printData.size() > 0){
                    String value = printData.get(0).get("value").toString();
                    content = content.replace("#" + attr.get("attribute_name").toString() + "#", value);
                }
            }
        }

        return content;
    }

    /**
     * 打印通知书模板列表
     * @param params
     * @return
     */
    public List<Map<String, Object>> getPrintTemplateList(Map<String, Object> params) {
        new Utils().pageParams(params);
        List<Map<String, Object>> list = offerMapper.getPrintTemplateList(params);
        return list;
    }

    /**
     * 打印通知书模板列表总条数
     * @param params
     * @return
     */
    public Long getPrintTemplateCount(Map<String, Object> params) {
        Long count = offerMapper.getPrintTemplateCount(params);
        return count;
    }

    public void setIsPrint(Map<String, Object> params) {
        offerMapper.setIsPrint(params);
    }

    public void updatePrintTemplate(Map<String, Object> params) {
        offerMapper.updatePrintTemplate(params);
    }

    public void createPrintTemplate(Map<String, Object> params) {
        offerMapper.createPrintTemplate(params);
    }

    public void deletePrintTemplate(Map<String, Object> params) {
        offerMapper.deletePrintTemplate(params);
    }

    public List<Map<String, Object>> getPrintAttribute() {
        HashMap<String, Object> params = new HashMap<>();
        return offerMapper.getPrintAttr(params);
    }

    public void updatePrintTemplateAttr(Map<String, Object> params) {
        offerMapper.updatePrintTemplateAttr(params);
    }

    public void deletePrintTemplateAttr(Map<String, Object> params) {
        offerMapper.deletePrintTemplateAttr(params);
    }

    public void createPrintTemplateAttr(Map<String, Object> params) {
        offerMapper.createPrintTemplateAttr(params);
    }
}
