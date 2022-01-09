package com.pubtile.foundation.invfortesting.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pubtile.foundation.infrastructure.po.BasePo;
import com.pubtile.foundation.invfortesting.domain.model.InvForTesting2;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 库存实体
 *
 * @category @author kevinli
 * @since 2020/4/15
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data

@TableName(value = "InvForTesting")
//old name:InvInventoryEntity
public class InvForTesting2Po extends BasePo<InvForTesting2Po> {
    /**
     * 库存编号
     */
    @TableField(value = "inventory_no")
    private String inventoryNo;

    /**
     * 商品编码
     */
    @TableField(value = "sku_id")
    private String skuId;

    /**
     * 容器编码 实物容器或虚拟容器
     */
    @TableField(value = "container_code")
    private String containerCode;

    /**
     * 唯一码
     */
    @TableField(value = "unique_code")
    private String uniqueCode;

    /**
     * 货主编码
     */
    @TableField(value = "owner_code")
    private String ownerCode;

    /**
     * 现有数量。可用数量=现有数量+移入数量-分配数量-冻结数量
     */
    @TableField(value = "on_hand_qty")
    private Integer onHandQty;

    /**
     * 分配数量
     */
    @TableField(value = "allocated_qty")
    private Integer allocatedQty;

    /**
     * 移入数量
     */
    @TableField(value = "in_transit_qty")
    private Integer inTransitQty;

    /**
     * 冻结数量
     */
    @TableField(value = "suspense_qty")
    private Integer suspenseQty;

    /**
     * 库位编码
     */
    @TableField(value = "location_code")
    private String locationCode;

    /**
     * 供应商编码
     */
    @TableField(value = "vendor_code")
    private String vendorCode;

    @TableField(value = "entry_order_type")
    private String entryOrderType;

}

