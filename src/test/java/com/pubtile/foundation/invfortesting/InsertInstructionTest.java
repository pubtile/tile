package com.pubtile.foundation.invfortesting;

import com.pubtile.foundation.FoundationExceptionCode;
import com.pubtile.foundation.domain.instruction.insert.InstructionInsertResultDto;
import com.pubtile.foundation.domain.model.ModelFactory;
import com.pubtile.foundation.infrastructure.po.BasePo;
import com.pubtile.foundation.invfortesting.app.InvForTestingApplication;
import com.pubtile.foundation.invfortesting.domain.command.increase.InvForTestingRequestDto;
import com.pubtile.foundation.invfortesting.domain.model.InvForTesting;
import com.pubtile.foundation.invfortesting.domain.model.InvForTestingModelToDtoMapper;
import com.pubtile.foundation.invfortesting.infrastructure.mybatis.mapper.InvForTestingMapper;
import com.pubtile.foundation.invfortesting.infrastructure.po.InvForTestingPo;
import com.pubtile.foundation.invfortesting.testdata.TestDataSuppliers;
import com.pubtile.foundation.test.TestAssert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/***
 * @author jiayan
 * @version 0.6.17 2021-12-30
 * @since 0.6.17 2021-12-30
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = InvForTestingApplication.class)
@Slf4j
@Transactional
@DisplayName("InsertInstruction测试")
public class InsertInstructionTest {
    @Autowired
    InvForTestingMapper dao;


    @DisplayName("所有必须值都含有，并且与数据库唯独不重复-成功")
    @Test
    public void allRequiredProperty_success(){
        String locationCode = "SH1XX";
        Long operatorId = 89L;

        InvForTestingRequestDto invForTestingRequestDto =
                TestDataSuppliers.buildTestDtoFromSimple(TestDataSuppliers.baselineDtoForAllSupplier,
                        dto -> {
                            //自动生成的都设为空
                            dto.setId(null);
                            dto.setInventoryNo(null);
                            //不唯一
                            dto.setUniqueCode("");
                            dto.setLocationCode(locationCode);
                            dto.setOperatorId(operatorId);
                        });
        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class, InvForTestingModelToDtoMapper.INSTANCE,invForTestingRequestDto);
        InstructionInsertResultDto resultDto = invForTesting.instructionInsert();
        assertTrue(resultDto.isSuccess());
        assertTrue(resultDto.getData().isInserted(),"应该是插入");
        assertFalse(resultDto.getData().isUpdated(),"应该不是更新");
        BasePo insertPo = resultDto.getData().getInsertRows().get(0);
        Long newId=insertPo.getId();

        assertTrue(newId>0);
//        assertTrue(StringUtils.isBlank(insertPo.get));
        InvForTestingPo invNew = dao.selectById(newId);
        assertInsertData(invForTestingRequestDto, invNew);

//        assertEquals(invForTestingRequestDto.getSkuId(),invNew.getSkuId(),"属性赋值");
//        assertEquals(invForTestingRequestDto.getVendorCode(),invNew.getVendorCode(),"属性赋值");
//        assertEquals(invForTestingRequestDto.getContainerCode(),invNew.getContainerCode(),"属性赋值");
//        assertEquals(invForTestingRequestDto.getOwnerCode(),invNew.getOwnerCode(),"属性赋值");
//        assertEquals(invForTestingRequestDto.getUniqueCode(),invNew.getUniqueCode(),"属性赋值");
//        assertEquals(invForTestingRequestDto.getTenantId(),invNew.getTenantId(),"属性赋值");
//        assertEquals(invForTestingRequestDto.getOperatorId(),invNew.getUpdatedBy(),"属性赋值");
//        assertEquals(invForTestingRequestDto.getOperatorId(),invNew.getCreatedBy(),"属性赋值");
//
//        assertEquals(invForTestingRequestDto.getOnHandQty(),invNew.getOnHandQty(),"属性赋值");
//        assertEquals(invForTestingRequestDto.getAllocatedQty(),invNew.getAllocatedQty(),"属性赋值");
//        assertEquals(invForTestingRequestDto.getSuspenseQty(),invNew.getSuspenseQty(),"属性赋值");
//        assertEquals(invForTestingRequestDto.getInTransitQty(),invNew.getInTransitQty(),"属性赋值");
//        assertEquals(invForTestingRequestDto.getEntryOrderType(),invNew.getEntryOrderType(),"属性赋值");

//        TestAssert.assertRecently(invNew.getCreatedTime(),"创建时间");
//        TestAssert.assertRecently(invNew.getUpdatedTime(),"更新时间");
//        assertEquals(1,invNew.getRevision(),"版本号");
    }



    @Test
    @DisplayName("包含自动生成的值-也成功")
    public void withAutoGenerateProperty_success(){
        String locationCode = "SH1XX";
        Long operatorId = 89L;

        InvForTestingRequestDto invForTestingRequestDto =
                TestDataSuppliers.buildTestDtoFromSimple(TestDataSuppliers.baselineDtoForAllSupplier,
                        dto -> {
                            //自动生成的都设为空
                            dto.setId(null);
                            dto.setInventoryNo("INVXXX");
                            //不唯一
                            dto.setUniqueCode("");
                            dto.setLocationCode(locationCode);
                            dto.setOperatorId(operatorId);
                        });
        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class, InvForTestingModelToDtoMapper.INSTANCE,invForTestingRequestDto);
        InstructionInsertResultDto resultDto = invForTesting.instructionInsert();
        assertTrue(resultDto.isSuccess());
        assertTrue(resultDto.getData().isInserted(),"应该是插入");
        assertFalse(resultDto.getData().isUpdated(),"应该不是更新");
        BasePo insertPo = resultDto.getData().getInsertRows().get(0);
        Long newId=insertPo.getId();
        assertTrue(newId>0);

        InvForTestingPo invNew = dao.selectById(newId);

        assertInsertData(invForTestingRequestDto, invNew);
    }

    @Test
    @DisplayName("bankable维度维度属性为空-成功")
    public void withBankablePropertySetToNull_success(){
        String locationCode = "SH1XX";
        Long operatorId = 89L;

        InvForTestingRequestDto invForTestingRequestDto =
                TestDataSuppliers.buildTestDtoFromSimple(TestDataSuppliers.baselineDtoForAllSupplier,
                        dto -> {
                            //自动生成的都设为空
                            dto.setId(null);
                            dto.setInventoryNo(null);
                            //不唯一
                            dto.setUniqueCode("XXX");
                            dto.setContainerCode("");
                            dto.setLocationCode(locationCode);
                            dto.setOperatorId(operatorId);
                        });
        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class, InvForTestingModelToDtoMapper.INSTANCE,invForTestingRequestDto);
        InstructionInsertResultDto resultDto = invForTesting.instructionInsert();
        assertTrue(resultDto.isSuccess());
        assertTrue(resultDto.getData().isInserted(),"应该是插入");
        assertFalse(resultDto.getData().isUpdated(),"应该不是更新");
        Long newId=resultDto.getData().getInsertRows().get(0).getId();
        assertTrue(newId>0);

        InvForTestingPo invNew = dao.selectById(newId);

        assertInsertData(invForTestingRequestDto, invNew);
    }

    @Test
    @DisplayName("维度不全-失败")
    public void withoutRequiredProperty_fail(){
        InvForTestingRequestDto invForTestingRequestDto = new InvForTestingRequestDto();
        //没有设置其他LocationCode等其他属性。
        invForTestingRequestDto.setOwnerCode("ownXXX").setUniqueCode("uniXXX").setTenantId(1L).setOperatorId(21L);
        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class, InvForTestingModelToDtoMapper.INSTANCE,invForTestingRequestDto);
        TestAssert.assertExceptionCode(FoundationExceptionCode.INSERT_DIMENSION_REQUIRED,() -> {
            invForTesting.instructionInsert();});
    }

    @Test
    @DisplayName("数据库不可为空-失败数据库异常")
    public void dbNullableCheck_fail(){
        InvForTestingRequestDto invForTestingRequestDto =
                TestDataSuppliers.buildTestDtoFromSimple(TestDataSuppliers.baselineDtoForInventoryDimensionSupplier,
                        dto -> {
                            dto.setLocationCode("XXXXX");
                            dto.setUniqueCode(null);
                        });

        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class, InvForTestingModelToDtoMapper.INSTANCE,invForTestingRequestDto);

        assertThrows(Exception.class,() -> {
            invForTesting.instructionInsert();
        });

    }

    @Test
    @DisplayName("维度重复-失败")
    public void dimensionExist_fail(){
        String locationCode = "SH1XX";
        Long operatorId = 39L;
        InvForTestingRequestDto invForTestingRequestDto =
                TestDataSuppliers.buildTestDtoFromSimple(TestDataSuppliers.baselineDtoForAllSupplier,
                        dto -> {
                            //自动生成的都设为空
                            dto.setId(null);
                            dto.setInventoryNo("INVXXX");
                            dto.setOperatorId(operatorId);
                        });
        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class, InvForTestingModelToDtoMapper.INSTANCE,invForTestingRequestDto);
        TestAssert.assertExceptionCode(FoundationExceptionCode.INSERT_EXISTS_DIMENSION,() -> {
            invForTesting.instructionInsert();});
    }


    public static void assertInsertData(InvForTestingRequestDto invForTestingRequestDto, InvForTestingPo invNew) {
        TestAssert.assertEqualsObject(invForTestingRequestDto, invNew,new String[]{"id","inventoryNo"},(expectedObject, actualObject) -> {
            assertEquals(invForTestingRequestDto.getOperatorId(), invNew.getCreatedBy(),"属性赋值");
            TestAssert.assertRecently(invNew.getCreatedTime(),"创建时间");
            assertEquals(invForTestingRequestDto.getOperatorId(), invNew.getUpdatedBy(),"属性赋值");
            TestAssert.assertRecently(invNew.getUpdatedTime(),"更新时间");
            assertEquals(1, invNew.getRevision(),"版本号");
        });
    }
}
