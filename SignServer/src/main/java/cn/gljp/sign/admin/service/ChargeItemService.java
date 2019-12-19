package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.entity.ChargeItem;
import cn.gljp.sign.admin.mapper.charge.ChargeStudentMapper;
import cn.gljp.sign.admin.mapper.sign.ChargeItemMapper;
import cn.gljp.sign.utils.ChineseToSpell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ChargeItemService {
    @Autowired
    private ChargeItemMapper chargeItemMapper;
    @Autowired
    private ChargeStudentMapper chargeStudentMapper;
    public void insert(ChargeItem chargeItem) {
        chargeItemMapper.insert(chargeItem);
    }
    public void insertSelective(ChargeItem chargeItem) {
        chargeItemMapper.insertSelective(chargeItem);
    }
    public void update(ChargeItem chargeItem) {
        chargeItemMapper.update(chargeItem);
    }

    public void delete(ChargeItem chargeItem) {
        chargeItemMapper.delete(chargeItem);
    }

    public List<ChargeItem> getChargeItemList() {
       return chargeItemMapper.getChargeItemList();
    }

    public List<Map<String, Object>> getShowChargeItems() {
        return chargeItemMapper.getShowChargeItems();
    }
    @Transactional
    public void syncChargeItem() {
        //获取收费系统收费项目列表
        List<Map<String, Object>> chargeItemList=chargeStudentMapper.getChargeItems();
        //封装到报名系统
        if (chargeItemList.size()>0){
            int i=1;
            for (Map<String, Object> map :chargeItemList){
                ChargeItem chargeItem=new ChargeItem();
                chargeItem.setId(Integer.parseInt(map.get("AutoID").toString()));
                chargeItem.setSort(i);
                chargeItem.setFieldName(ChineseToSpell.getPingYin(map.get("ItemName").toString()));
                chargeItem.setItemName(map.get("ItemName").toString());
                if ("学费".equals(map.get("ItemName").toString())){
                    chargeItem.setIsDisabled(true);
                }else {
                    chargeItem.setIsDisabled(false);
                }
                chargeItem.setIsSelected(true);
                chargeItem.setStatus(true);
                i++;
                //插入到报名系统数据库
                chargeItemMapper.insertSelective(chargeItem);
            }
        }
    }
}
