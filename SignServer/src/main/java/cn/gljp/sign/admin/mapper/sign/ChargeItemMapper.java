package cn.gljp.sign.admin.mapper.sign;

import cn.gljp.sign.admin.entity.ChargeItem;

import java.util.List;
import java.util.Map;

public interface ChargeItemMapper {
    void insert(ChargeItem chargeItem);
    void insertSelective(ChargeItem chargeItem);
    void update(ChargeItem chargeItem);

    void delete(ChargeItem chargeItem);

    List<ChargeItem> getChargeItemList();

    List<Map<String, Object>> getShowChargeItems();

}
