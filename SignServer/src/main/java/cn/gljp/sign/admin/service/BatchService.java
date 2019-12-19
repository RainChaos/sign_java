package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.mapper.sign.BatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BatchService {
    @Autowired
    private BatchMapper batchMapper;

    public List<Map<String, Object>> getBatch(Map<String, Object> params){
        if(null == params.get("limit")){
            params.put("limit", 20);
        }else{
            params.put("limit", Integer.parseInt(params.get("limit").toString()));
        }
        if(null == params.get("page")){
            params.put("page", 1);
        }else {
            params.put("page", (Integer.parseInt(params.get("page").toString()) - 1) * Integer.parseInt(params.get("page").toString()));
        }

        return batchMapper.getBatch(params);
    }

    public Integer getBatchCountV2(){
        return batchMapper.getBatchCountV2();
    }
    public List<Map<String, Object>> getBatchV2(Map<String, Object> params){
        if(null == params.get("limit")){
            params.put("limit", 20);
        }else{
            params.put("limit", Integer.parseInt(params.get("limit").toString()));
        }
        if(null == params.get("page")){
            params.put("page", 1);
        }else {
            params.put("page", (Integer.parseInt(params.get("page").toString()) - 1) * Integer.parseInt(params.get("page").toString()));
        }

        return batchMapper.getBatchV2(params);
    }

    public Integer getBatchCount(){
        return batchMapper.getBatchCount();
    }
    public void updateBatch(Map<String, Object> params) {
        String[] rangetimes = params.get("rangetime").toString().split(",");
        params.put("start_time", new Date());
        params.put("end_time", new Date());
        if(rangetimes.length == 2){
            params.put("start_time", rangetimes[0]);
            params.put("end_time", rangetimes[1]);
        }
        batchMapper.updateBatch(params);
    }

    public void createBatch(Map<String, Object> params) {
        String[] rangetimes = params.get("rangetime").toString().split(",");
        params.put("start_time", new Date());
        params.put("end_time", new Date());
        if(rangetimes.length == 2){
            params.put("start_time", rangetimes[0]);
            params.put("end_time", rangetimes[1]);
        }
        if("".equals(params.get("end_charge_time"))){
            params.put("end_charge_time", new Date());
        }

        batchMapper.createBatch(params);
    }

    public void currentBatchStudentCount(Map<String, Object> params) throws Exception {
        Integer count = batchMapper.currentBatchStudentCount(params);
        if(count > 0){
            throw new Exception("错误：当前批次已有学生报名，不可删除");
        }
    }

    public void deleteBatch(Map<String, Object> params) {
        batchMapper.deleteBatch(params);
    }

    public Map<String, Object> getPiciInfoById(String pici_id) {
        return batchMapper.getPiciInfoById(pici_id);
    }
}
