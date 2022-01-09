package com.pubtile.foundation.invfortesting;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pubtile.foundation.FoundationExceptionCode;
import com.pubtile.foundation.domain.model.ModelFactory;
import com.pubtile.foundation.domain.instruction.ChangedProperties;
import com.pubtile.foundation.domain.instruction.increase.IncreaseInstructionResultDto;
import com.pubtile.foundation.domain.instruction.insert.InstructionInsertResultDto;
import com.pubtile.foundation.infrastructure.po.BasePo;
import com.pubtile.foundation.invfortesting.app.InvForTestingApplication;
import com.pubtile.foundation.invfortesting.domain.command.increase.InvForTestIngIncreaseInstruction;
import com.pubtile.foundation.invfortesting.domain.command.increase.InvForTestingRequestDto;
import com.pubtile.foundation.invfortesting.domain.model.InvForTesting;
import com.pubtile.foundation.invfortesting.domain.model.InvForTestingModelToDtoMapper;
import com.pubtile.foundation.invfortesting.infrastructure.mybatis.mapper.InvForTestingMapper;
import com.pubtile.foundation.invfortesting.infrastructure.po.InvForTestingPo;
import com.pubtile.foundation.invfortesting.testdata.TestDataSuppliers;
import com.pubtile.foundation.test.TestAssert;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = InvForTestingApplication.class)
@Slf4j
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//每个方法10000个
@DisplayName("increase")
public class IncreaseInstructionTest {
    @Autowired
    InvForTestIngIncreaseInstruction invForTestIngIncreaseCommand;

    @Autowired
    InvForTestingMapper dao;

    @Test
    @DisplayName("缺少定位维度 失败")
    public void increaseByDimensionWithMissingFields_fail() {
        InvForTestingRequestDto invForTestingRequestDto =
                TestDataSuppliers.buildTestDtoFromSimple(TestDataSuppliers.baselineDtoForInventoryDimensionSupplier, dto -> {
                    dto.setLocationCode(null);
                });
        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class);
        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingRequestDto,invForTesting);

        TestAssert.assertExceptionCode(FoundationExceptionCode.UPDATE_WITHOUT_DIMENSION_FOR_IDENTIFY,() -> {
            invForTestIngIncreaseCommand.increase(invForTesting);
        });
    }

    @Test
    @DisplayName("包含定位维度，但是该维度不存在，各个属性全-新增成功")
    public void increaseByNewDimension_insertSuccess() {
        String inventoryNo = "KC21102823649784XX";
        String locationCode = "SH01-A-1-X";
        String uniqueCode = "uniqueCodeX";
        InvForTestingRequestDto invForTestingRequestDto =
                TestDataSuppliers.buildTestDtoFromSimple(TestDataSuppliers.baselineDtoForInventoryDimensionSupplier, dto -> {
                    dto.setInventoryNo(inventoryNo).setLocationCode(locationCode).setUniqueCode(uniqueCode).setOnHandQty(3).setOperatorId(4L).setTenantId(1L);
                });

        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class);
        //InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingRequestDto,invForTesting);
        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingRequestDto,invForTesting);
        IncreaseInstructionResultDto result = invForTestIngIncreaseCommand.increase(invForTesting);
        assertTrue(result.isSuccess());
        assertTrue(result.getData().isInserted());
        assertFalse(result.getData().isUpdated());
        List<BasePo> insertRows = result.getData().getInsertRows();

        assertEquals(insertRows.size(),1L);
        assertNotEquals(0L, insertRows.get(0));

        InvForTestingPo invForTestingNew = dao.selectById(result.getData().getInsertRows().get(0).getId());
        assertEquals(locationCode,invForTestingNew.getLocationCode());
        InsertInstructionTest.assertInsertData(invForTestingRequestDto,invForTestingNew);
    }

    @Test
    @DisplayName("包含定位维度，但是该维度不存在，新维度包含可为空字段-新增成功")
    public void increaseByNewDimension2_insertSuccess() {
        String inventoryNo = "KC21102823649784XX";
        String locationCode = "SH01-A-1-X";
        String uniqueCode = "uniqueCodeX";

        InvForTestingRequestDto invForTestingRequestDto =
                TestDataSuppliers.buildTestDtoFromSimple(TestDataSuppliers.baselineDtoForInventoryDimensionSupplier, dto -> {
                    dto.setInventoryNo(inventoryNo).setLocationCode(locationCode).setContainerCode(null).setUniqueCode(uniqueCode);
                });

        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class);
        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingRequestDto,invForTesting);
        IncreaseInstructionResultDto result = invForTestIngIncreaseCommand.increase(invForTesting);
        assertTrue(result.isSuccess());
        assertTrue(result.getData().isInserted());
        assertFalse(result.getData().isUpdated());
        InvForTestingPo invForTestingNew = dao.selectById(result.getData().getInsertRows().get(0).getId());
        assertEquals(locationCode,invForTestingNew.getLocationCode());
        InsertInstructionTest.assertInsertData(invForTestingRequestDto,invForTestingNew);

    }


    @Test
    @DisplayName("包含定位维度，但是该维度不存在，新增包含可为空维度 成功")
    public void increaseByNewDimension3_insertSuccess() {
        String inventoryNo = "KC21102823649784XX";
        String locationCode = "SH01-A-1-X";

        InvForTestingRequestDto invForTestingRequestDto =
                TestDataSuppliers.buildTestDtoFromSimple(TestDataSuppliers.baselineDtoForInventoryDimensionSupplier, dto -> {
                    dto.setInventoryNo(inventoryNo).setLocationCode(locationCode).setContainerCode(null).setUniqueCode("XXXX");
                });

        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class);
        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingRequestDto,invForTesting);
        IncreaseInstructionResultDto result = invForTestIngIncreaseCommand.increase(invForTesting);
        assertTrue(result.isSuccess());
        assertTrue(result.getData().isInserted());
        assertFalse(result.getData().isUpdated());

        InvForTestingPo invForTestingNew = dao.selectById(result.getData().getInsertRows().get(0).getId());
        assertEquals(locationCode,invForTestingNew.getLocationCode());
        InsertInstructionTest.assertInsertData(invForTestingRequestDto,invForTestingNew);

    }

    @Test
    @DisplayName("包含定位维度，但是该维度不存在，缺乏新增需要的所有维度-失败")
    public void increaseWithMissingDimensionFields_fail() {
        InvForTestingRequestDto invForTestingRequestDto = new InvForTestingRequestDto();
        invForTestingRequestDto
                .setInventoryNo("NEW-InventoryNo")
                .setOnHandQty(1)
                .setTenantId(1L);

        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class);
        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingRequestDto,invForTesting);
        TestAssert.assertExceptionCode(FoundationExceptionCode.INSERT_DIMENSION_REQUIRED,() -> {
            invForTesting.instructionInsert();});
    }

    @Test
    @DisplayName("包含定位维度，但是该维度不存在，其他维度重复-失败")
    public void increaseByDimensionDuplicatedFields_insertFail() {
        InvForTestingRequestDto invForTestingRequestDto =
                TestDataSuppliers.buildTestDtoFromSimple(TestDataSuppliers.baselineDtoForInventoryDimensionSupplier, dto -> {
                    dto.setInventoryNo("KC21102823649784XX")
                            .setUniqueCode("uniqueCode2");
                });


        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class);
        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingRequestDto,invForTesting);
        TestAssert.assertExceptionCode(FoundationExceptionCode.INSERT_EXISTS_DIMENSION, () -> {
            invForTestIngIncreaseCommand.increase(invForTesting);
        });
    }


    @Test
    @DisplayName("包含定位维度id，但是该维度存在，累加更新-成功")
    @Order(13010)
    public void increaseById_updateSuccess(){
        int increaseOnHandQty=10;
        int increaseSuspenseQty=1;
        long operatorId=3L;
        List<InvForTestingPo> invForTestingPos =
                dao.selectList(new QueryWrapper<InvForTestingPo>().lambda().eq(InvForTestingPo::getId,
                        Long.valueOf(2L)));
        assertTrue(invForTestingPos.size()==1);
        InvForTestingPo oldOne =  invForTestingPos.get(0);

        InvForTestingRequestDto invForTestingRequestDto =
                TestDataSuppliers.buildTestDtoFromSimple(TestDataSuppliers.baselineDtoForAllSupplier,
                        dto -> {
                    dto.setOnHandQty(increaseOnHandQty);
                    dto.setSuspenseQty(increaseSuspenseQty);
                    dto.setOperatorId(operatorId);
                        });
        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class);
        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingRequestDto,invForTesting);
        IncreaseInstructionResultDto result = invForTestIngIncreaseCommand.increase(invForTesting);
        assertTrue(result.isSuccess());
        assertFalse(result.getData().isInserted());
        assertTrue(result.getData().isUpdated());

        Pair<Long, ChangedProperties> updateOneRowData = result.getData().getUpdateRows().get(0);
        ChangedProperties updateData = updateOneRowData.getValue();
        assertTrue(updateData.getAllChangedProperties().size()==3);
        assertEquals(oldOne.getOnHandQty(), updateData.getAllChangedProperties().get("onHandQty").getMiddle());
        assertEquals(oldOne.getOnHandQty()+increaseOnHandQty,updateData.getAllChangedProperties().get("onHandQty").getRight());
        assertEquals(oldOne.getSuspenseQty(), updateData.getAllChangedProperties().get("suspenseQty").getMiddle());
        assertEquals(oldOne.getSuspenseQty()+increaseSuspenseQty, updateData.getAllChangedProperties().get("suspenseQty").getRight());
        assertEquals(operatorId,updateData.getAllChangedProperties().get("updatedBy").getRight());

        List<InvForTestingPo> invForTestingPosNew =
                dao.selectList(new QueryWrapper<InvForTestingPo>().lambda().eq(InvForTestingPo::getId,
                        Long.valueOf(2L)));
        assertTrue(invForTestingPosNew.size()==1);

        InvForTestingPo newOne =  invForTestingPosNew.get(0);

        assertEquals(newOne.getOnHandQty(),oldOne.getOnHandQty()+increaseOnHandQty);
        assertEquals(newOne.getSuspenseQty(),oldOne.getSuspenseQty()+increaseSuspenseQty);
        assertEquals(newOne.getUpdatedBy(),operatorId);
    }


    @Test
    @DisplayName("包含定位维度，但是该维度存在，累加更新-成功")
    public void increaseByDimension_updateSuccess(){
        long id=2L;
        int increaseOnHandQty=10;
        int increaseSuspenseQty=1;
        long operatorId=5L;
        List<InvForTestingPo> invForTestingPos =
                dao.selectList(new QueryWrapper<InvForTestingPo>().lambda().eq(InvForTestingPo::getId,
                        Long.valueOf(id)));
        assertTrue(invForTestingPos.size()==1);

        InvForTestingRequestDto invForTestingRequestDto =
                TestDataSuppliers.buildTestDtoFromSimple(TestDataSuppliers.baselineDtoForInventoryDimensionSupplier,
                        dto -> {
                            dto.setOnHandQty(increaseOnHandQty);
                            dto.setSuspenseQty(increaseSuspenseQty);
                            dto.setOperatorId(operatorId);
                        });
        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class);
        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingRequestDto,invForTesting);
        IncreaseInstructionResultDto result = invForTestIngIncreaseCommand.increase(invForTesting);
        assertTrue(result.isSuccess());
        assertFalse(result.getData().isInserted());
        assertTrue(result.getData().isUpdated());

        List<InvForTestingPo> invForTestingPosNew =
                dao.selectList(new QueryWrapper<InvForTestingPo>().lambda().eq(InvForTestingPo::getId,
                        Long.valueOf(id)));
        assertTrue(invForTestingPosNew.size()==1);
        InvForTestingPo oldOne =  invForTestingPos.get(0);
        InvForTestingPo newOne =  invForTestingPosNew.get(0);

        assertEquals(newOne.getOnHandQty(),oldOne.getOnHandQty()+increaseOnHandQty);
        assertEquals(newOne.getSuspenseQty(),oldOne.getSuspenseQty()+increaseSuspenseQty);
        assertEquals(newOne.getUpdatedBy(),operatorId);
    }

    @Test
    @DisplayName("没有counter字段 - 失败")
    public void withoutCounter_fail(){

        InvForTestingRequestDto invForTestingRequestDto = new InvForTestingRequestDto();
        invForTestingRequestDto.setInventoryNo("KC2110282364978410")
                .setLocationCode("XXX")
                .setTenantId(1L);

        InvForTesting modelBean = ModelFactory.getModelBean(InvForTesting.class, InvForTestingModelToDtoMapper.INSTANCE,
                invForTestingRequestDto);

        TestAssert.assertExceptionCode(FoundationExceptionCode.INCREASE_REQUEST_WITHOUT_COUNTER_PROPERTY,() -> {
            modelBean.instructionIncrease();
        });
    }

    @Test
    @DisplayName("既包含counter字段，也包含要修改的Dimension字段 - 失败")
    public void withCounterAndUpdateDimension_fail(){
        InvForTestingRequestDto invForTestingRequestDto = new InvForTestingRequestDto();
        invForTestingRequestDto.setInventoryNo("KC2110282364978410")
                .setLocationCode("XXX")
                .setOnHandQty(1)
                .setTenantId(1L);

        InvForTesting modelBean = ModelFactory.getModelBean(InvForTesting.class, InvForTestingModelToDtoMapper.INSTANCE,
                invForTestingRequestDto);

        TestAssert.assertExceptionCode(FoundationExceptionCode.INCREASE_REQUEST_WITH_BOTH_DIMENSION_AND_COUNTER_PROPERTY,() -> {
            modelBean.instructionIncrease();
        });
    }
}
