package com.pubtile.foundation.invfortesting;

import com.pubtile.foundation.domain.model.ModelFactory;
import com.pubtile.foundation.domain.instruction.update.BaseUpdateInstruction;
import com.pubtile.foundation.domain.instruction.ChangedProperty;
import com.pubtile.foundation.domain.instruction.ChangedProperties;
import com.pubtile.foundation.domain.instruction.update.UpdateInstructionResultDto;
import com.pubtile.foundation.invfortesting.app.InvForTestingApplication;
import com.pubtile.foundation.invfortesting.domain.command.increase.InvForTestingRequestDto;
import com.pubtile.foundation.invfortesting.domain.model.InvForTesting;
import com.pubtile.foundation.invfortesting.domain.model.InvForTestingModelToDtoMapper;
import com.pubtile.foundation.invfortesting.infrastructure.mybatis.mapper.InvForTestingMapper;
import com.pubtile.foundation.invfortesting.infrastructure.po.InvForTestingPo;
import com.pubtile.foundation.test.TestAssert;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 维度更新
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = InvForTestingApplication.class)
@Slf4j
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("update测试-同时更新dimension property 和 counter value property")
public class UpdateInstructionWithDimensionAndCounterPropertyTest {
    @Autowired
    BaseUpdateInstruction<InvForTesting> updateInstruction;

    @Autowired
    InvForTestingMapper dao;

    @Test
    @DisplayName("一个可定位的维度+一个更新维度 + 没有数量->成功update")
    public void noQty_successWhole(){
        long id = 2L;
        String uniqueCode = "uniqueCodeXXX";

        InvForTestingPo invPrevious = dao.selectById(id);

        InvForTestingRequestDto invForTestingRequestDto =  new InvForTestingRequestDto();
        invForTestingRequestDto.setUniqueCode(uniqueCode).setId(id);
        InvForTesting model = ModelFactory.getModelBean(InvForTesting.class, InvForTestingModelToDtoMapper.INSTANCE,invForTestingRequestDto);
        UpdateInstructionResultDto resultDto = updateInstruction.update(model);
        assertTrue(resultDto.isSuccess());
        assertEquals(resultDto.getData().getUpdateRows().size(),1);
        Pair<Long, ChangedProperties> updateOneRowData = resultDto.getData().getUpdateRows().get(0);
        assertEquals(id, updateOneRowData.getKey());
        ChangedProperties updateData = updateOneRowData.getValue();
        assertEquals(1,updateData.getAllChangedProperties().size());
        ChangedProperty updateDataUniqueCode = updateData.getAllChangedProperties().get("uniqueCode");
        assertEquals(uniqueCode,updateDataUniqueCode.getRight());


        InvForTestingPo invPost = dao.selectById(id);
        assertEquals(uniqueCode, invPost.getUniqueCode());
        assertEquals(invPrevious.getOnHandQty(),invPost.getOnHandQty());
        assertEquals(invPrevious.getSuspenseQty(),invPost.getSuspenseQty());
    }

    @Test
    @DisplayName("一个可定位的维度+一个更新维度+拆分一个数量->成功insert和update")
    public void identifyByOne_updateDimension_oneQty_successInsertUpdate(){
        long id = 6L;
        String locationCode = "SH01-A-1-X";
        int onHandQty = 1;
        long tenantId = 1L;
        long operatorId = 44L;
        InvForTestingPo invPrevious = dao.selectById(id);

        //准备数据并执行
        InvForTestingRequestDto invForTestingRequestDto =  new InvForTestingRequestDto();
        invForTestingRequestDto.setLocationCode(locationCode).setId(id).setOnHandQty(onHandQty).setOperatorId(operatorId);
        InvForTesting model = ModelFactory.getModelBean(InvForTesting.class, InvForTestingModelToDtoMapper.INSTANCE,invForTestingRequestDto);

        UpdateInstructionResultDto resultDto = updateInstruction.update(model);

        //断言返回值
        assertTrue(resultDto.isSuccess());
        assertEquals(1,resultDto.getData().getUpdateRows().size());
//        assertEquals(1,resultDto.getData().getInsertRows().size());

        Pair<Long, ChangedProperties> updateOneRowData = resultDto.getData().getUpdateRows().get(0);
        assertEquals(id, updateOneRowData.getKey());
        ChangedProperties changedProperties = updateOneRowData.getValue();
        assertEquals(0, changedProperties.getChangedDimensionsProperties().size());
        assertEquals(1, changedProperties.getChangedNormalValueProperties().size(),"应该只有最后更新人被更新");
        assertEquals(1, changedProperties.getChangedCounterValueProperties().size());
        ChangedProperty objectObjectChangedProperty = changedProperties.getChangedCounterValueProperties().get(0);
        assertEquals("onHandQty", objectObjectChangedProperty.getPropertyMeta().getName());

        assertEquals("updatedBy", changedProperties.getChangedNormalValueProperties().get(0).getPropertyMeta().getName(),"最后更新人");
        assertEquals(operatorId, changedProperties.getChangedNormalValueProperties().get(0).getRight());

        assertEquals(invPrevious.getOnHandQty(), objectObjectChangedProperty.getMiddle());
        assertEquals(invPrevious.getOnHandQty() - onHandQty, objectObjectChangedProperty.getRight());

        Long insertId = resultDto.getData().getInsertRows().get(0).getId();
        assertNotEquals(id, insertId);

        //断言存储层
        InvForTestingPo invPost = dao.selectById(id);
        assertEquals(invPrevious.getLocationCode(), invPost.getLocationCode());
        assertEquals(invPrevious.getOnHandQty() - onHandQty,invPost.getOnHandQty()  ,"原来数量减少");
        assertEquals(invPrevious.getSuspenseQty(),invPost.getSuspenseQty());


        InvForTestingPo invNew = dao.selectById(resultDto.getData().getInsertRows().get(0).getId());
        assertEquals(locationCode, invNew.getLocationCode(),"新更改的维度属性，location");
        assertEquals(onHandQty,invNew.getOnHandQty()  ,"新数量增加");

        assertEquals(invPrevious.getSkuId(),invNew.getSkuId(),"维度复制过来");
        assertEquals(invPrevious.getVendorCode(),invNew.getVendorCode(),"维度复制过来");
        assertEquals(invPrevious.getContainerCode(),invNew.getContainerCode(),"维度复制过来");
        assertEquals(invPrevious.getOwnerCode(),invNew.getOwnerCode(),"维度复制过来");
        assertEquals(invPrevious.getUniqueCode(),invNew.getUniqueCode(),"维度复制过来");
        assertEquals(invPrevious.getTenantId(),invNew.getTenantId(),"维度复制过来");

        assertEquals(operatorId,invNew.getCreatedBy(),"创建人");
        assertEquals(operatorId,invNew.getUpdatedBy(),"更新人");
        TestAssert.assertRecently(invNew.getCreatedTime(),"创建时间应该在最近");
        assertEquals(invPrevious.getSkuId(),invNew.getSkuId(),"更新时间应该在最近");

        assertNotEquals(invPrevious.getId(),invNew.getId(),"新维度产生");
        assertNotEquals(invPrevious.getInventoryNo(),invNew.getInventoryNo(),"维度复制过来");

        assertEquals(onHandQty,invNew.getOnHandQty()  ,"数量增加");

        assertEquals(0,invNew.getSuspenseQty()  ,"其他数量为0");
        assertEquals(0,invNew.getInTransitQty()  ,"其他数量为0");


    }


    @Test
    @DisplayName("一个可定位的维度+一个更新维度和属性+拆分一个数量->成功insert和update")
    public void identifyByOne_updateDimensionAndNormalValueProperty_oneQty_successInsertUpdate(){
        long id = 6L;
        String locationCode = "SH01-A-2-X";
        String entryOrderType = "TypeXXX";
        int onHandQty = 1;
        long tenantId = 1L;
        long operatorId = 31L;
        InvForTestingPo invPrevious = dao.selectById(id);

        //准备数据并执行
        InvForTestingRequestDto invForTestingRequestDto =  new InvForTestingRequestDto();
        invForTestingRequestDto.setLocationCode(locationCode).setId(id).setOnHandQty(onHandQty).setEntryOrderType(entryOrderType).setOperatorId(operatorId);
        InvForTesting model = ModelFactory.getModelBean(InvForTesting.class, InvForTestingModelToDtoMapper.INSTANCE,invForTestingRequestDto);

        UpdateInstructionResultDto resultDto = updateInstruction.update(model);

        //断言返回值
        assertTrue(resultDto.isSuccess());
        assertEquals(1,resultDto.getData().getUpdateRows().size());
//        assertEquals(1,resultDto.getData().getInsertRows().size());

        Pair<Long, ChangedProperties> updateOneRowData = resultDto.getData().getUpdateRows().get(0);
        assertEquals(id, updateOneRowData.getKey());
        ChangedProperties changedProperties = updateOneRowData.getValue();
        assertEquals(0, changedProperties.getChangedDimensionsProperties().size());
        assertEquals(2, changedProperties.getChangedNormalValueProperties().size(),"应该只有最后更新人被更新和entryOrderType");
        assertEquals(1, changedProperties.getChangedCounterValueProperties().size());
        ChangedProperty objectObjectChangedProperty = changedProperties.getChangedCounterValueProperties().get(0);
        assertEquals("onHandQty", objectObjectChangedProperty.getPropertyMeta().getName());


        assertEquals(invPrevious.getOnHandQty(), objectObjectChangedProperty.getMiddle());
        assertEquals(invPrevious.getOnHandQty() - onHandQty, objectObjectChangedProperty.getRight());

        Long insertId = resultDto.getData().getInsertRows().get(0).getId();
        assertNotEquals(id, insertId);

        //断言存储层
        InvForTestingPo invPost = dao.selectById(id);
        assertEquals(invPrevious.getLocationCode(), invPost.getLocationCode());
        assertEquals(invPrevious.getOnHandQty() - onHandQty,invPost.getOnHandQty()  ,"原来数量减少");
        assertEquals(entryOrderType,invPost.getEntryOrderType()  ,"原行entryOrderType 被更新");
        assertEquals(operatorId,invPost.getUpdatedBy()  ,"operatorId");

        assertEquals(invPrevious.getSuspenseQty(),invPost.getSuspenseQty());


        InvForTestingPo invNew = dao.selectById(resultDto.getData().getInsertRows().get(0).getId());
        assertEquals(locationCode, invNew.getLocationCode(),"新更改的维度属性，location");
        assertEquals(onHandQty,invNew.getOnHandQty()  ,"新数量增加");

        assertEquals(invPrevious.getSkuId(),invNew.getSkuId(),"维度复制过来");
        assertEquals(invPrevious.getVendorCode(),invNew.getVendorCode(),"维度复制过来");
        assertEquals(invPrevious.getContainerCode(),invNew.getContainerCode(),"维度复制过来");
        assertEquals(invPrevious.getOwnerCode(),invNew.getOwnerCode(),"维度复制过来");
        assertEquals(invPrevious.getUniqueCode(),invNew.getUniqueCode(),"维度复制过来");
        assertEquals(invPrevious.getTenantId(),invNew.getTenantId(),"维度复制过来");

        assertEquals(operatorId,invNew.getCreatedBy(),"创建人");
        assertEquals(operatorId,invNew.getUpdatedBy(),"更新人");
        TestAssert.assertRecently(invNew.getCreatedTime(),"创建时间应该在最近");
        assertEquals(invPrevious.getSkuId(),invNew.getSkuId(),"更新时间应该在最近");

        assertNotEquals(invPrevious.getId(),invNew.getId(),"新维度产生");
        assertNotEquals(invPrevious.getInventoryNo(),invNew.getInventoryNo(),"维度复制过来");

        assertEquals(onHandQty,invNew.getOnHandQty()  ,"数量增加");

        assertEquals(0,invNew.getSuspenseQty()  ,"其他数量为0");
        assertEquals(0,invNew.getInTransitQty()  ,"其他数量为0");
        assertEquals(entryOrderType,invNew.getEntryOrderType()  ,"新行entryOrderType 被更新");
    }

    @Test
    @DisplayName("一个可定位的维度+一个更新维度和属性+拆分多个数量和属性->成功insert和update")
    public void identifyByOther_updateDimensionAndNormalValueProperty_multipleQty_successInsertUpdate(){
        long id = 6L;

        String inventorNo = "KC2110282364978406";
        long tenantId = 1L;

        String locationCode = "SH01-A-3-X";
        String entryOrderType = "TypeYYY";
        int onHandQty = 1;
        int allocatedQty = 1;

        long operatorId = 99L;
        InvForTestingPo invPrevious = dao.selectById(id);

        //准备数据并执行
        InvForTestingRequestDto invForTestingRequestDto =  new InvForTestingRequestDto();
        invForTestingRequestDto.setInventoryNo(inventorNo).setLocationCode(locationCode).setOnHandQty(onHandQty).
                setAllocatedQty(allocatedQty).setEntryOrderType(entryOrderType).setOperatorId(operatorId).setTenantId(tenantId);

        InvForTesting model = ModelFactory.getModelBean(InvForTesting.class, InvForTestingModelToDtoMapper.INSTANCE,invForTestingRequestDto);

        UpdateInstructionResultDto resultDto = updateInstruction.update(model);

        //断言返回值
        assertTrue(resultDto.isSuccess());
        assertEquals(1,resultDto.getData().getUpdateRows().size());
//        assertEquals(1,resultDto.getData().getInsertRows().size());

        Pair<Long, ChangedProperties> updateOneRowData = resultDto.getData().getUpdateRows().get(0);
        assertEquals(id, updateOneRowData.getKey());
        ChangedProperties changedProperties = updateOneRowData.getValue();
        assertEquals(0, changedProperties.getChangedDimensionsProperties().size(),"原来维度不变");
        assertEquals(2, changedProperties.getChangedNormalValueProperties().size(),"应该只有最后更新人被更新和entryOrderType");
        assertEquals(2, changedProperties.getChangedCounterValueProperties().size(),"两个数量");
        List<ChangedProperty> countableValues = changedProperties.getChangedCounterValueProperties();


        assertEquals(1,countableValues.stream().filter(d -> {
            return d.getPropertyMeta().getName().equals("onHandQty");
        }).count(),"onHandQty");
        assertEquals(1,countableValues.stream().filter(d -> {
            return d.getPropertyMeta().getName().equals("allocatedQty");
        }).count(),"allocatedQty");

        countableValues.forEach(d -> {
            switch (d.getPropertyMeta().getName()){
                case "onHandQty":
                    assertEquals(invPrevious.getOnHandQty(),d.getMiddle());
                    assertEquals(invPrevious.getOnHandQty() - onHandQty,d.getRight());
                    break;
                case "allocatedQty":
                    assertEquals(invPrevious.getAllocatedQty(),d.getMiddle());
                    assertEquals(invPrevious.getAllocatedQty() - allocatedQty,d.getRight());
                    break;
            }
        });
        Long insertId = resultDto.getData().getInsertRows().get(0).getId();
        assertNotEquals(id, insertId);

        //断言存储层
        InvForTestingPo invPost = dao.selectById(id);
        assertEquals(invPrevious.getLocationCode(), invPost.getLocationCode());
        assertEquals(invPrevious.getOnHandQty() - onHandQty,invPost.getOnHandQty()  ,"原来数量减少");
        assertEquals(invPrevious.getAllocatedQty() - allocatedQty,invPost.getAllocatedQty()  ,"原来数量减少");

        assertEquals(entryOrderType,invPost.getEntryOrderType()  ,"原行entryOrderType 被更新");
        assertEquals(operatorId,invPost.getUpdatedBy()  ,"operatorId");

        assertEquals(invPrevious.getSuspenseQty(),invPost.getSuspenseQty());


        InvForTestingPo invNew = dao.selectById(resultDto.getData().getInsertRows().get(0).getId());
        assertEquals(locationCode, invNew.getLocationCode(),"新更改的维度属性，location");
        assertEquals(onHandQty,invNew.getOnHandQty()  ,"新数量增加");

        assertEquals(invPrevious.getSkuId(),invNew.getSkuId(),"维度复制过来");
        assertEquals(invPrevious.getVendorCode(),invNew.getVendorCode(),"维度复制过来");
        assertEquals(invPrevious.getContainerCode(),invNew.getContainerCode(),"维度复制过来");
        assertEquals(invPrevious.getOwnerCode(),invNew.getOwnerCode(),"维度复制过来");
        assertEquals(invPrevious.getUniqueCode(),invNew.getUniqueCode(),"维度复制过来");
        assertEquals(invPrevious.getTenantId(),invNew.getTenantId(),"维度复制过来");

        assertEquals(operatorId,invNew.getCreatedBy(),"创建人");
        assertEquals(operatorId,invNew.getUpdatedBy(),"更新人");
        TestAssert.assertRecently(invNew.getCreatedTime(),"创建时间应该在最近");
        assertEquals(invPrevious.getSkuId(),invNew.getSkuId(),"更新时间应该在最近");

        assertNotEquals(invPrevious.getId(),invNew.getId(),"新维度产生");
        assertNotEquals(invPrevious.getInventoryNo(),invNew.getInventoryNo(),"维度复制过来");

        assertEquals(onHandQty,invNew.getOnHandQty()  ,"数量增加");
        assertEquals(allocatedQty,invNew.getAllocatedQty()  ,"数量增加");
        assertEquals(0,invNew.getSuspenseQty()  ,"其他数量为0");
        assertEquals(0,invNew.getInTransitQty()  ,"其他数量为0");
        assertEquals(entryOrderType,invNew.getEntryOrderType()  ,"新行entryOrderType 被更新");

    }

    /**
     * 6移动到10
     */
    @Test
    @DisplayName("一个可定位的维度+一个更新维度+拆分一个数量->成功update")
    public void identifyByOne_updateOne_oneQty_successUpdate(){
        long id = 6L;
        final int idInto = 10;
        String inventorNo = "KC2110282364978406";
        long tenantId = 1L;

        String locationCode = "SH01-A-1-2";
        String entryOrderType = "TypeYYY";
        int onHandQty = 1;
        int allocatedQty = 1;

        long operatorId = 99L;
        InvForTestingPo invPrevious = dao.selectById(id);
        InvForTestingPo invPreviousUpdateInto = dao.selectById(idInto);

        //准备数据并执行
        InvForTestingRequestDto invForTestingRequestDto =  new InvForTestingRequestDto();
        invForTestingRequestDto.setInventoryNo(inventorNo).setLocationCode(locationCode).setOnHandQty(onHandQty).
                setAllocatedQty(allocatedQty).setEntryOrderType(entryOrderType).setOperatorId(operatorId).setTenantId(tenantId);

        InvForTesting model = ModelFactory.getModelBean(InvForTesting.class, InvForTestingModelToDtoMapper.INSTANCE,invForTestingRequestDto);

        UpdateInstructionResultDto resultDto = updateInstruction.update(model);

        //断言返回值
        assertTrue(resultDto.isSuccess());
        assertEquals(2,resultDto.getData().getUpdateRows().size(),"6和10行更新");
        assertEquals(0,resultDto.getData().getInsertRows().size(),"无新增");

        resultDto.getData().getUpdateRows().forEach(d -> {
            int updatedId = d.getKey().intValue();

            switch (updatedId){
                case 6:
                    //原行
                    ChangedProperties changedProperties = d.getValue();
                    assertEquals(0, changedProperties.getChangedDimensionsProperties().size(),"原来维度不变");
                    assertEquals(2, changedProperties.getChangedNormalValueProperties().size(),"应该只有最后更新人被更新和entryOrderType");
                    assertEquals(2, changedProperties.getChangedCounterValueProperties().size(),"两个数量");
                    break;
                case idInto:
                    //新行
                    ChangedProperties changedProperties10 = d.getValue();
                    assertEquals(0, changedProperties10.getChangedDimensionsProperties().size(),"原来维度不变");
                    assertEquals(2, changedProperties10.getChangedNormalValueProperties().size(),"应该只有最后更新人被更新和entryOrderType");
                    assertEquals(2, changedProperties10.getChangedCounterValueProperties().size(),"两个数量");
                    break;
                default:
                    assertTrue(false, "只有两行更新");
            }

        });

        //断言存储层
        InvForTestingPo invPost = dao.selectById(id);
        assertEquals(invPrevious.getLocationCode(), invPost.getLocationCode());
        assertEquals(invPrevious.getOnHandQty() - onHandQty,invPost.getOnHandQty()  ,"原来数量减少");
        assertEquals(invPrevious.getAllocatedQty() - allocatedQty,invPost.getAllocatedQty()  ,"原来数量减少");

        assertEquals(entryOrderType,invPost.getEntryOrderType()  ,"原行entryOrderType 被更新");
        assertEquals(operatorId,invPost.getUpdatedBy()  ,"operatorId");

        assertEquals(invPrevious.getSuspenseQty(),invPost.getSuspenseQty());

        InvForTestingPo invUpdateInto = dao.selectById(idInto);
        assertEquals(locationCode, invUpdateInto.getLocationCode(),"新更改的维度属性，location");
        assertEquals(invPreviousUpdateInto.getOnHandQty()+onHandQty,invUpdateInto.getOnHandQty()  ,"数量增加");

        assertEquals(invPreviousUpdateInto.getSkuId(),invUpdateInto.getSkuId(),"维度复制过来");
        assertEquals(invPreviousUpdateInto.getVendorCode(),invUpdateInto.getVendorCode(),"维度复制过来");
        assertEquals(invPreviousUpdateInto.getContainerCode(),invUpdateInto.getContainerCode(),"维度复制过来");
        assertEquals(invPreviousUpdateInto.getOwnerCode(),invUpdateInto.getOwnerCode(),"维度复制过来");
        assertEquals(invPreviousUpdateInto.getUniqueCode(),invUpdateInto.getUniqueCode(),"维度复制过来");
        assertEquals(invPreviousUpdateInto.getTenantId(),invUpdateInto.getTenantId(),"维度复制过来");

        assertEquals(operatorId,invUpdateInto.getUpdatedBy(),"更新人");
        TestAssert.assertRecently(invUpdateInto.getUpdatedTime(),"更新时间应该在最近");


        assertEquals(invPreviousUpdateInto.getAllocatedQty()+allocatedQty,invUpdateInto.getAllocatedQty()  ,"数量增加");
        assertEquals(invPreviousUpdateInto.getSuspenseQty(),invUpdateInto.getSuspenseQty()  ,"其他数量为0");
        assertEquals(invPreviousUpdateInto.getInTransitQty(),invUpdateInto.getInTransitQty()  ,"其他数量为0");
        assertEquals(entryOrderType,invUpdateInto.getEntryOrderType()  ,"新行entryOrderType 被更新");
    }

    /**
     * 6->1
     */
    @Test
    @DisplayName("一个可定位的维度+多个个更新维度+所有数量->成功")
    public void identifyByOne_updateMany_allQty_success(){
        final int id = 6;
        final int idInto = 1;
        String inventorNo = "KC2110282364978406";
        long tenantId = 1L;

        String locationCode = "RECEIVE_DOCK";
        String containerCode = "C01";
        String entryOrderType = "TypeYYY";
        int onHandQty = 1;
        int allocatedQty = 1;

        long operatorId = 99L;
        InvForTestingPo invPrevious = dao.selectById(id);
        InvForTestingPo invPreviousUpdateInto = dao.selectById(idInto);

        //准备数据并执行
        InvForTestingRequestDto invForTestingRequestDto =  new InvForTestingRequestDto();
        invForTestingRequestDto.setInventoryNo(inventorNo).setLocationCode(locationCode).setContainerCode(containerCode).setOnHandQty(onHandQty).
                setAllocatedQty(allocatedQty).setEntryOrderType(entryOrderType).setOperatorId(operatorId).setTenantId(tenantId);

        InvForTesting model = ModelFactory.getModelBean(InvForTesting.class, InvForTestingModelToDtoMapper.INSTANCE,invForTestingRequestDto);

        UpdateInstructionResultDto resultDto = updateInstruction.update(model);

        //断言返回值
        assertTrue(resultDto.isSuccess());
        assertEquals(2,resultDto.getData().getUpdateRows().size(),"6和10行更新");
        assertEquals(0,resultDto.getData().getInsertRows().size(),"无新增");

        resultDto.getData().getUpdateRows().forEach(d -> {
            int updatedId = d.getKey().intValue();

            switch (updatedId){
                case id:
                    //原行
                    ChangedProperties changedProperties = d.getValue();
                    assertEquals(0, changedProperties.getChangedDimensionsProperties().size(),"原来维度不变");
                    assertEquals(2, changedProperties.getChangedNormalValueProperties().size(),"应该只有最后更新人被更新和entryOrderType");
                    assertEquals(2, changedProperties.getChangedCounterValueProperties().size(),"两个数量");
                    break;
                case idInto:
                    //新行
                    ChangedProperties changedProperties10 = d.getValue();
                    assertEquals(0, changedProperties10.getChangedDimensionsProperties().size(),"原来维度不变");
                    assertEquals(2, changedProperties10.getChangedNormalValueProperties().size(),"应该只有最后更新人被更新和entryOrderType");
                    assertEquals(2, changedProperties10.getChangedCounterValueProperties().size(),"两个数量");
                    break;
                default:
                    assertTrue(false, "只有两行更新");
            }

        });

        //断言存储层
        InvForTestingPo invPost = dao.selectById(id);
        assertEquals(invPrevious.getLocationCode(), invPost.getLocationCode());
        assertEquals(invPrevious.getOnHandQty() - onHandQty,invPost.getOnHandQty()  ,"原来数量减少");
        assertEquals(invPrevious.getAllocatedQty() - allocatedQty,invPost.getAllocatedQty()  ,"原来数量减少");

        assertEquals(entryOrderType,invPost.getEntryOrderType()  ,"原行entryOrderType 被更新");
        assertEquals(operatorId,invPost.getUpdatedBy()  ,"operatorId");

        assertEquals(invPrevious.getSuspenseQty(),invPost.getSuspenseQty());

        InvForTestingPo invUpdateInto = dao.selectById(idInto);
        assertEquals(locationCode, invUpdateInto.getLocationCode(),"新更改的维度属性，location");
        assertEquals(invPreviousUpdateInto.getOnHandQty()+onHandQty,invUpdateInto.getOnHandQty()  ,"数量增加");

        assertEquals(invPreviousUpdateInto.getSkuId(),invUpdateInto.getSkuId(),"维度复制过来");
        assertEquals(invPreviousUpdateInto.getVendorCode(),invUpdateInto.getVendorCode(),"维度复制过来");
        assertEquals(invPreviousUpdateInto.getContainerCode(),invUpdateInto.getContainerCode(),"维度复制过来");
        assertEquals(invPreviousUpdateInto.getOwnerCode(),invUpdateInto.getOwnerCode(),"维度复制过来");
        assertEquals(invPreviousUpdateInto.getUniqueCode(),invUpdateInto.getUniqueCode(),"维度复制过来");
        assertEquals(invPreviousUpdateInto.getTenantId(),invUpdateInto.getTenantId(),"维度复制过来");

        assertEquals(operatorId,invUpdateInto.getUpdatedBy(),"更新人");
        TestAssert.assertRecently(invUpdateInto.getUpdatedTime(),"更新时间应该在最近");


        assertEquals(invPreviousUpdateInto.getAllocatedQty()+allocatedQty,invUpdateInto.getAllocatedQty()  ,"数量增加");
        assertEquals(invPreviousUpdateInto.getSuspenseQty(),invUpdateInto.getSuspenseQty()  ,"其他数量为0");
        assertEquals(invPreviousUpdateInto.getInTransitQty(),invUpdateInto.getInTransitQty()  ,"其他数量为0");
        assertEquals(entryOrderType,invUpdateInto.getEntryOrderType()  ,"新行entryOrderType 被更新");
    }

}
