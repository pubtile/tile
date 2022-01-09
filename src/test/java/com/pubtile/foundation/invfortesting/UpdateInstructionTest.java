package com.pubtile.foundation.invfortesting;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pubtile.base.exception.BaseException;
import com.pubtile.foundation.FoundationExceptionCode;
import com.pubtile.foundation.domain.model.ModelFactory;
import com.pubtile.foundation.domain.instruction.interceptor.option.OptionEnum;
import com.pubtile.foundation.domain.instruction.interceptor.option.OptionForDimension;
import com.pubtile.foundation.domain.instruction.interceptor.option.OptionForProperty;
import com.pubtile.foundation.domain.instruction.interceptor.option.Options;
import com.pubtile.foundation.domain.instruction.update.BaseUpdateInstruction;
import com.pubtile.foundation.domain.instruction.ChangedProperties;
import com.pubtile.foundation.domain.instruction.update.UpdateInstructionResultDto;
import com.pubtile.foundation.invfortesting.app.InvForTestingApplication;
import com.pubtile.foundation.invfortesting.domain.command.increase.InvForTestingRequestDto;
import com.pubtile.foundation.invfortesting.domain.model.InvForTesting;
import com.pubtile.foundation.invfortesting.domain.model.InvForTestingModelToDtoMapper;
import com.pubtile.foundation.invfortesting.infrastructure.mybatis.mapper.InvForTestingMapper;
import com.pubtile.foundation.invfortesting.infrastructure.po.InvForTestingPo;
import com.pubtile.foundation.invfortesting.testdata.TestDataSuppliers;
import com.pubtile.foundation.test.TestAssert;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = InvForTestingApplication.class)
@Slf4j
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("update测试-不同时更新dimension property 和 counter value property")
public class UpdateInstructionTest {
    @Autowired
    BaseUpdateInstruction<InvForTesting> updateInstruction;

    @Autowired
    InvForTestingMapper dao;

    InvForTestingRequestDto invForTestingRequestDto =  new InvForTestingRequestDto();

    @Test
    @DisplayName("update 通过id更新value property")
    @Order(10010)
    public void updateById_updateSuccess(){
        long id = 1L;
        int increaseOnHandQty=100;
        int increaseSuspenseQty=20;
        long operatorId=5L;
//        String containerCode = "CXXX";
//        String newVendorCode = "ttt";
        InvForTestingRequestDto dto =  new InvForTestingRequestDto();
        dto.setId(id);
        dto.setOnHandQty(increaseOnHandQty);
        dto.setSuspenseQty(increaseSuspenseQty);
//        dto.setVendorCode(newVendorCode);
        dto.setOperatorId(operatorId);
//        dto.updatesetContainerCode(containerCode);

        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class);
        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(dto,invForTesting);
        UpdateInstructionResultDto result = updateInstruction.update(invForTesting,null);
        assertTrue(result.isSuccess());
        assertEquals(1, result.getData().getUpdateRows().size());
        Pair<Long, ChangedProperties> updateDatas = result.getData().getUpdateRows().get(0);
        assertEquals(id,updateDatas.getKey());
        assertEquals(updateDatas.getValue().getAllChangedProperties().get("onHandQty").getRight(),increaseOnHandQty);
        assertEquals(updateDatas.getValue().getAllChangedProperties().get("suspenseQty").getRight(),increaseSuspenseQty);
        assertEquals(updateDatas.getValue().getAllChangedProperties().get("updatedBy").getRight(),operatorId,"最后更新人没有更新");


        List<InvForTestingPo> invForTestingPosNew =
                dao.selectList(new QueryWrapper<InvForTestingPo>().lambda().eq(InvForTestingPo::getId,
                        Long.valueOf(id)));
        assertTrue(invForTestingPosNew.size()==1);
        InvForTestingPo newOne =  invForTestingPosNew.get(0);

        assertEquals(newOne.getOnHandQty(),increaseOnHandQty);
        assertEquals(newOne.getSuspenseQty(),increaseSuspenseQty);
        assertEquals(newOne.getUpdatedBy(),operatorId);
    }

    @Test
    @DisplayName("update-通过id更新维度和非counter属性-成功")
    @Order(10010)
    public void updateByDto_updateSuccess(){
        long operatorId=5L;
        String containerCode = "CXXX";
        String newVendorCode = "ttt";
        List<InvForTestingPo> invForTestingPos =
                dao.selectList(new QueryWrapper<InvForTestingPo>().lambda().eq(InvForTestingPo::getId,
                        Long.valueOf(2L)));
        assertTrue(invForTestingPos.size()==1);
        InvForTestingPo oldOne =  invForTestingPos.get(0);

        InvForTestingRequestDto invForTestingRequestDto =
                TestDataSuppliers.buildTestDtoFromSimple(TestDataSuppliers.baselineDtoForAllSupplier,
                        dto -> {
                            dto.setVendorCode(newVendorCode);
                            dto.setOperatorId(operatorId);
                            dto.setContainerCode(containerCode);
                        });
        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class);
        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingRequestDto,invForTesting);
        UpdateInstructionResultDto resultDto = updateInstruction.update(invForTesting,null);

        assertTrue(resultDto.isSuccess());
        assertEquals(resultDto.getData().getUpdateRows().size(),1);
        Pair<Long, ChangedProperties> updateOneRowData = resultDto.getData().getUpdateRows().get(0);
        assertEquals(oldOne.getId(), updateOneRowData.getKey());
        ChangedProperties updateData =updateOneRowData.getValue();
        assertEquals(oldOne.getContainerCode(), updateData.getAllChangedProperties().get("containerCode").getMiddle());
        assertEquals(containerCode, updateData.getAllChangedProperties().get("containerCode").getRight());
        assertEquals(oldOne.getVendorCode(), updateData.getAllChangedProperties().get("vendorCode").getMiddle());
        assertEquals(newVendorCode, updateData.getAllChangedProperties().get("vendorCode").getRight());


        List<InvForTestingPo> invForTestingPosNew =
                dao.selectList(new QueryWrapper<InvForTestingPo>().lambda().eq(InvForTestingPo::getId,
                        Long.valueOf(2L)));
        assertTrue(invForTestingPosNew.size()==1);


        InvForTestingPo newOne =  invForTestingPosNew.get(0);

        assertEquals(newOne.getUpdatedBy(),operatorId,"最后更新人没有更新");
        assertEquals(newOne.getVendorCode(),newVendorCode);
        assertEquals(newOne.getContainerCode(),containerCode);
    }


    @Test
    @DisplayName("update-通过其他高优先级维度更新其他维度和非counter属性-成功")
    @Order(10010)
    public void updateByInventoryNo_updateSuccess(){
        String inventoryNo="KC2110282364978404";
        Long tenantId=1L;
        Long id=4L;

        long operatorId=5L;
        String containerCode = "CXXX";
        String newVendorCode = "ttt";
        String entryOrderType = "ot";
        InvForTestingPo oldOne =  dao.selectOne(new LambdaQueryWrapper<InvForTestingPo>()
                .eq(InvForTestingPo::getInventoryNo,inventoryNo).eq(InvForTestingPo::getTenantId,tenantId));

        InvForTestingRequestDto invForTestingRequestDto = new InvForTestingRequestDto();
        invForTestingRequestDto.setInventoryNo(inventoryNo)
                .setContainerCode(containerCode)
                .setVendorCode(newVendorCode)
                .setEntryOrderType(entryOrderType)
                .setOperatorId(operatorId)
                .setTenantId(tenantId);
        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class,InvForTestingModelToDtoMapper.INSTANCE,invForTestingRequestDto);
        UpdateInstructionResultDto resultDto = updateInstruction.update(invForTesting,null);

        assertTrue(resultDto.isSuccess());
        assertEquals(resultDto.getData().getUpdateRows().size(),1);
        Pair<Long, ChangedProperties> updateOneRowData = resultDto.getData().getUpdateRows().get(0);
        assertEquals(oldOne.getId(), updateOneRowData.getKey());
        ChangedProperties updateData =updateOneRowData.getValue();
        assertEquals(oldOne.getContainerCode(), updateData.getAllChangedProperties().get("containerCode").getMiddle());
        assertEquals(containerCode, updateData.getAllChangedProperties().get("containerCode").getRight());
        assertEquals(oldOne.getVendorCode(), updateData.getAllChangedProperties().get("vendorCode").getMiddle());
        assertEquals(newVendorCode, updateData.getAllChangedProperties().get("vendorCode").getRight());


        InvForTestingPo newOne = dao.selectById(id);
        assertEquals(operatorId,newOne.getUpdatedBy(),"最后更新人没有更新");
        assertEquals(newOne.getVendorCode(),newVendorCode);
        assertEquals(newOne.getContainerCode(),containerCode);
        assertEquals(entryOrderType, newOne.getEntryOrderType());
    }

    @Test
    @DisplayName("update-支持map-成功")
    @Order(10020)
    public void updateByMap_updateSuccess(){
        long l = 2L;
        int increaseOnHandQty=100;
        int increaseSuspenseQty=20;
        long operatorId=5L;
//        String containerCode = "cxxx";
        List<InvForTestingPo> invForTestingPos =
                dao.selectList(new QueryWrapper<InvForTestingPo>().lambda().eq(InvForTestingPo::getId,
                        Long.valueOf(l)));
        String newVendorCode = "ttt";
        assertTrue(invForTestingPos.size()==1);

        Map<String,Object> map =  new HashMap<>();
        map.put("id",2L);
        map.put("onHandQty",increaseOnHandQty);
        map.put("suspenseQty", increaseSuspenseQty);
        map.put("updatedBy",operatorId);
//        map.put("containerCode", containerCode);

        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class,map);
//        BeanUtil.copy(invForTestingIncreaseRequestDto, invForTesting);
//        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingIncreaseRequestDto,invForTesting);
        UpdateInstructionResultDto result = updateInstruction.update(invForTesting,null);
        assertTrue(result.isSuccess());

        List<InvForTestingPo> invForTestingPosNew =
                dao.selectList(new QueryWrapper<InvForTestingPo>().lambda().eq(InvForTestingPo::getId,
                        Long.valueOf(2L)));
        assertTrue(invForTestingPosNew.size()==1);
        InvForTestingPo oldOne =  invForTestingPos.get(0);
        InvForTestingPo newOne =  invForTestingPosNew.get(0);

        assertEquals(newOne.getOnHandQty(),increaseOnHandQty);
        assertEquals(newOne.getSuspenseQty(),increaseSuspenseQty);
        assertEquals(newOne.getUpdatedBy(),operatorId);
//        assertEquals(newOne.getVendorCode(),newVendorCode);
//        assertEquals(newOne.getContainerCode(),containerCode);

    }

    /**
     * 指定用uniqueCode这个低优先级的维度，放过来更新inventoryNo
     */
    @Test
    @DisplayName("update 指定高优先级维度")
    @Order(10050)
    public void updateSpecifyIdentfyDimension() {
        String inventoryNo = "KC2110282364978XXX";
        long tenantId = 1L;
        String uniqueCode = "uniqueCode1";
        InvForTestingRequestDto invForTestingRequestDto =  new InvForTestingRequestDto();
        invForTestingRequestDto.setUniqueCode(uniqueCode).setInventoryNo(inventoryNo).setTenantId(tenantId);

        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class, InvForTestingModelToDtoMapper.INSTANCE,invForTestingRequestDto);

        Options options = new Options().add(new OptionForDimension(OptionEnum.IDENTIFY＿DIMENSION, "uniqueCode"));

        UpdateInstructionResultDto resultDto = updateInstruction.update(invForTesting, options);

        assertTrue(resultDto.isSuccess());
        InvForTestingPo invForTestingPo = dao.selectOne(new LambdaQueryWrapper<InvForTestingPo>().eq(InvForTestingPo::getInventoryNo,"KC2110282364978XXX"));
    }



    @Test
    @DisplayName("update 没有更新字段，不更新")
    @Order(12110)
    public void updateWithoutUpdateField_fail() {
        long id = 1L;
        InvForTestingRequestDto invForTestingRequestDto =  new InvForTestingRequestDto();
        invForTestingRequestDto.setId(id);

        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class);
        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingRequestDto,invForTesting);

        TestAssert.assertExceptionCode(FoundationExceptionCode.UPDATE_WITHOUT_UPDATE_PROPERTY,() -> {
            updateInstruction.update(invForTesting,null);
        });
    }

    @Test
    @DisplayName("update 没有更新字段2，不更新")
    public void update_UPDATE_WITHOUT_UPDATE_PROPERTY_fail() {
        InvForTestingRequestDto invForTestingRequestDto =  new InvForTestingRequestDto();

        invForTestingRequestDto.setInventoryNo("KC2110282364978404").setTenantId(1L);

        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class);
        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingRequestDto,invForTesting);

        TestAssert.assertExceptionCode(FoundationExceptionCode.UPDATE_WITHOUT_UPDATE_PROPERTY,() -> {
            updateInstruction.update(invForTesting,null);
        });

    }

    @Test
    @DisplayName("update 重复维度 失败")
    @Order(12120)
    public void updateByDimensionDuplicatedFields_fail() {
        InvForTestingRequestDto invForTestingRequestDto =
                TestDataSuppliers.buildTestDtoFromSimple(TestDataSuppliers.baselineDtoForInventoryDimensionSupplier, dto -> {
                    dto.setUniqueCode("uniqueCodeD");
//                    dto.setInventoryNo("KC21102823649784XX")
//                            .setUniqueCode("uniqueCodeD");
                });


        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class);
        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingRequestDto,invForTesting);

        TestAssert.assertExceptionCode(FoundationExceptionCode.DIMENSION_IN_DB_DUPLICATED,() -> {
            updateInstruction.update(invForTesting,null);
        });
    }


    @Test
    @DisplayName("update 缺失定位维度 失败")
    @Order(12120)
    public void update_WITHOUT_DIMENSION_FOR_IDENTIFY_fail() {
        InvForTestingRequestDto invForTestingRequestDto = new InvForTestingRequestDto();
        invForTestingRequestDto.setLocationCode("XXX").setOnHandQty(1).setTenantId(1L);

        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class,InvForTestingModelToDtoMapper.INSTANCE,invForTestingRequestDto);

        TestAssert.assertExceptionCode(FoundationExceptionCode.UPDATE_WITHOUT_DIMENSION_FOR_IDENTIFY,() -> {
            updateInstruction.update(invForTesting,null);
        });
    }


//    /**
//     * 断言暂时不支持
//     */
//    @Test
//    @DisplayName("update 断言字段和数据库中不匹配 失败")
//    @Order(12120)
//    @Disabled
//    public void updateByAssertFieldNotMatch_updateFail() {
//        InvForTestingRequestDto invForTestingRequestDto =  new InvForTestingRequestDto();
//        invForTestingRequestDto.setInventoryNo("KC2110282364978402").setSkuId("sku3").setTenantId(1L);
//
//        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class);
//        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingRequestDto,invForTesting);
//
//        TestAssert.assertExceptionCode(FoundationExceptionCode.DIMENSION_IN_DB_DUPLICATED,() -> {
//            updateInstruction.update(invForTesting,null);
//        });
//    }

    @Test
    @DisplayName("update 不可变的不可更新 失败")
    @Order(12120)
    public void update_ImmutableField_Fail() {
        String ownerCode = "OwnerXXX";
        String sku = "skuXXX";
        long id = 2L;
        InvForTestingRequestDto invForTestingRequestDto =  new InvForTestingRequestDto();
        invForTestingRequestDto.setOwnerCode(ownerCode).setSkuId(sku).setId(id);

        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class, InvForTestingModelToDtoMapper.INSTANCE,invForTestingRequestDto);
        BaseException baseException =
                TestAssert.assertExceptionCode(FoundationExceptionCode.UPDATE_IMMUTABLE_PROPERTY_NOT_ALLOW, () -> {
            UpdateInstructionResultDto result = updateInstruction.update(invForTesting, null);
        });
        log.info(baseException.getException().getMessage());

    }

    @Test
    @DisplayName("update 请求中属性为Null 不更新该属性")
    @Order(12210)
    public void update_RequestFieldWithNullValue_NotUpdate() {
        long id = 1L;
        String skuId = "skuXX";
        String locationCode = null;
        InvForTestingRequestDto invForTestingRequestDto =  new InvForTestingRequestDto();
        invForTestingRequestDto.setSkuId(skuId).setId(id).setLocationCode(locationCode);

        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class);
        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingRequestDto,invForTesting);
        UpdateInstructionResultDto resultDto = updateInstruction.update(invForTesting, null);
        assertTrue(resultDto.isSuccess());

        InvForTestingPo invForTestingPo = dao.selectById(id);

        assertEquals(skuId,invForTestingPo.getSkuId());
        assertNotNull(invForTestingPo.getOwnerCode());
        assertNotNull(invForTestingPo.getLocationCode());
        assertNotNull(invForTestingPo.getContainerCode());

        //0 will not update
        assertTrue(StringUtils.isNotBlank(invForTestingPo.getLocationCode()));

    }

    @Test
    @DisplayName("update 请求中属性为“” 不更新该属性")
    @Order(12220)
    public void update_RequestFieldWithEmptyString_NotUpdate() {
        long id = 1L;
        String locationCode = "";
        String skuId = "skuXX";
        InvForTestingRequestDto invForTestingRequestDto =  new InvForTestingRequestDto();
        invForTestingRequestDto.setSkuId(skuId).setId(id).setLocationCode(locationCode);

        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class);
        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingRequestDto,invForTesting);
        UpdateInstructionResultDto resultDto = updateInstruction.update(invForTesting, null);
        assertTrue(resultDto.isSuccess());

        InvForTestingPo invForTestingPo = dao.selectById(id);

        assertEquals(skuId,invForTestingPo.getSkuId());
        assertNotNull(invForTestingPo.getOwnerCode());
        assertNotNull(invForTestingPo.getLocationCode());
        assertNotNull(invForTestingPo.getContainerCode());

        //0 will not update
        assertNotEquals(invForTestingPo.getLocationCode(),locationCode);
    }

    @Test
    @DisplayName("update 请求中属性为0L 不更新该属性")
    @Order(12230)
    public void update_RequestFieldWithZero_NotUpdate() {
        long id = 1L;
        long operatorId = 9L;
        InvForTestingRequestDto invForTestingRequestDto =  new InvForTestingRequestDto();
        invForTestingRequestDto.setId(id).setOnHandQty(0).setLocationCode("SH01-XXXX").setOperatorId(operatorId);

        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class,InvForTestingModelToDtoMapper.INSTANCE,invForTestingRequestDto);

        UpdateInstructionResultDto resultDto = updateInstruction.update(invForTesting);
        assertTrue(resultDto.isSuccess());

        InvForTestingPo invForTestingPo = dao.selectById(id);
        //skuId still update to ""
        assertNotEquals(0,invForTestingPo.getOnHandQty());
        assertEquals("SH01-XXXX",invForTestingPo.getLocationCode());

    }


}
