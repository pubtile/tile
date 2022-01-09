package com.pubtile.foundation.invfortesting.domain.command.increase;

import com.pubtile.foundation.domain.model.annotation.InAndDecrementableValueProperty;
import com.pubtile.foundation.domain.model.annotation.ValueProperty;
import com.pubtile.foundation.invfortesting.domain.BaseIncreaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InvForTesting2RequestDto extends BaseIncreaseDto {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "关联库存编号", required = true)
    private String inventoryNo;

    @ApiModelProperty(value = "sku编码", required = true)
    private String skuId;

    @ApiModelProperty(value = "库位编码", required = true)
    private String locationCode = "";

    @ApiModelProperty(value = "容器编码 实物容器或虚拟容器", required = true)
    private String containerCode = "";

    @ApiModelProperty(value = "唯一码", required = true)
    private String uniqueCode;

    @ApiModelProperty(value = "货主", required = true)
    private String ownerCode;


    /**
     * 现有数量。可用数量=现有数量+移入数量-分配数量-冻结数量
     */
    @InAndDecrementableValueProperty
    private Integer onHandQty;

    /**
     * 分配数量
     */
    @InAndDecrementableValueProperty
    private Integer allocatedQty;

    /**
     * 在途数量
     */
    @InAndDecrementableValueProperty
    private Integer inTransitQty;

    /**
     * 冻结数量
     */
    @InAndDecrementableValueProperty
    private Integer suspenseQty;

    /**
     * 入库单据类型，因为不同单据类型编号不相同，类型就不需要了
     */
    @ValueProperty
    private String entryOrderType;


    private String vendorCode;

}
