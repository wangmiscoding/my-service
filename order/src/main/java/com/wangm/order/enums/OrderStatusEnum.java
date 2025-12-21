package com.wangm.order.enums;

import lombok.Getter;

/**
 * 订单状态枚举类
 */
@Getter
public enum OrderStatusEnum {
    
    PENDING_APPROVAL(0, "待审批"),
    APPROVED(1, "已审批"),
    REJECTED(2, "已拒绝");
    
    private final Integer code;
    private final String description;
    
    OrderStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据状态代码获取枚举值
     */
    public static OrderStatusEnum getByCode(Integer code) {
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}