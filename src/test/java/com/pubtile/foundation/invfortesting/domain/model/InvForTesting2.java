package com.pubtile.foundation.invfortesting.domain.model;

import com.pubtile.foundation.domain.model.*;
import com.pubtile.foundation.domain.model.annotation.DimensionProperty;
import com.pubtile.foundation.domain.model.annotation.InAndDecrementableValueProperty;
import com.pubtile.foundation.domain.model.annotation.ValueProperty;
import com.pubtile.foundation.invfortesting.infrastructure.po.InvForTesting2Po;
import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)

@Component
@Scope("prototype")
public class InvForTesting2 extends BaseModel<InvForTesting2, InvForTesting2Po> {

    /**
     * 库存编号
     */
    @DimensionProperty(name = "inventoryNo",immutable = false, autoGenerate = true, priority = 50)
    private String inventoryNo;

    /**
     * 商品编码
     */
    @DimensionProperty(name = "inventoryDimension", priority = 500)
    private String skuId;

    /**
     * 库位编码
     */
    @DimensionProperty(name = "inventoryDimension")
    private String locationCode;

    /**
     * 容器编码 实物容器或虚拟容器
     */
    @DimensionProperty(name = "inventoryDimension", bankable = true)
    private String containerCode;

    /**
     * 货主编码
     */
    @DimensionProperty(name = "inventoryDimension")
    private String ownerCode;

    @ValueProperty()
    private String vendorCode;

    /**
     * 唯一码
     */
    @DimensionProperty(name = "inventoryDimension", bankable = true)
    @DimensionProperty(name = "uniqueCode", bankable = false, priority = 80)
    private String uniqueCode;

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
    @ValueProperty(counter = true)
    private Integer inTransitQty;

    /**
     * 冻结数量
     */
    @InAndDecrementableValueProperty
    private Integer suspenseQty;

    /**
     * 入库单据类型，因为不同单据类型编号不相同，类型就不需要了
     * 错误类型
     */
    @ValueProperty(counter = true)
    private String entryOrderType;

    @Override
    public InvForTesting2Po toPo() {
        return InvForTesting2ModelToPoMapper.INSTANCE.modelToPo(this);
    }

    @Override
    public InvForTesting2 fromPo(InvForTesting2Po po) {
        return InvForTesting2ModelToPoMapper.INSTANCE.poToModel(po);
    }
//
//
//    @Mapper
//    public static interface Converter  {
//        Converter INSTANCE = Mappers.getMapper(Converter.class);
//
//        @Mapping(target = "meta",ignore = true)
//        @Mapping(target = "fromPo",ignore = true)
//        public InvForTesting2 po2Model(InvForTesting2Po dto);
//        public  InvForTesting2Po model2Po(InvForTesting2 model);
//    }
}
