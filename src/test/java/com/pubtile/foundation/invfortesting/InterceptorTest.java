package com.pubtile.foundation.invfortesting;

import com.pubtile.foundation.domain.instruction.interceptor.option.OptionEnum;
import com.pubtile.foundation.domain.instruction.interceptor.option.OptionForProperty;
import com.pubtile.foundation.domain.instruction.interceptor.option.Options;
import com.pubtile.foundation.domain.instruction.update.UpdateInstructionResultDto;
import com.pubtile.foundation.domain.model.ModelFactory;
import com.pubtile.foundation.invfortesting.app.InvForTestingApplication;
import com.pubtile.foundation.invfortesting.domain.command.increase.InvForTestingRequestDto;
import com.pubtile.foundation.invfortesting.domain.model.InvForTesting;
import com.pubtile.foundation.invfortesting.domain.model.InvForTestingModelToDtoMapper;
import com.pubtile.foundation.invfortesting.infrastructure.mybatis.mapper.InvForTestingMapper;
import com.pubtile.foundation.invfortesting.infrastructure.po.InvForTestingPo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = InvForTestingApplication.class)
@Slf4j
@Transactional
@DisplayName("interceptor 测试用例")
public class InterceptorTest {

    @Autowired
    InvForTestingMapper dao;


    @Test
    @DisplayName("update 请求中属性为“”，并强制更新该属性 更新该属性")
    @Order(12320)
    public void updateForce_requestFieldWithEmptyString_success() {
        long id = 1L;
        String skuId = "";
        InvForTestingRequestDto invForTestingRequestDto =  new InvForTestingRequestDto();
        invForTestingRequestDto.setSkuId(skuId).setId(id);

        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class);
        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingRequestDto,invForTesting);
        Options instructionConfiguration = new Options();

        instructionConfiguration.add(new OptionForProperty(OptionEnum.UPDATE_FORCE,"skuId"));
        UpdateInstructionResultDto resultDto = invForTesting.instructionUpdate(instructionConfiguration);
        assertTrue(resultDto.isSuccess());

        InvForTestingPo invForTestingPo = dao.selectById(id);

        //skuId still update to ""
        assertEquals(skuId,invForTestingPo.getSkuId());
        assertNotNull(invForTestingPo.getOwnerCode());
        assertNotNull(invForTestingPo.getLocationCode());
        assertNotNull(invForTestingPo.getContainerCode());
    }

    @Test
    @DisplayName("update 请求中属性为0L，并强制更新该属性 更新该属性")
    public void updateForce_requestFieldWithZeroForce_success() {
        long id = 1L;
        long operatorId = 0L;
        InvForTestingRequestDto invForTestingRequestDto =  new InvForTestingRequestDto();
        invForTestingRequestDto.setId(id).setOperatorId(operatorId);

        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class);
        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingRequestDto,invForTesting);
        Options instructionConfiguration = new Options();

        instructionConfiguration.add(new OptionForProperty(OptionEnum.UPDATE_FORCE ,"updatedBy" ));
        UpdateInstructionResultDto resultDto = invForTesting.instructionUpdate(instructionConfiguration);
        assertTrue(resultDto.isSuccess());

        InvForTestingPo invForTestingPo = dao.selectById(id);

        //skuId still update to ""
        assertEquals(operatorId,invForTestingPo.getUpdatedBy());
        assertNotNull(invForTestingPo.getOwnerCode());
        assertNotNull(invForTestingPo.getLocationCode());
        assertNotNull(invForTestingPo.getContainerCode());
    }


//
//    /**
//     * TODO mybatis update null.
//     */
//    @Test
//    @DisplayName("update 请求中属性为Null，并强制更新该属性 更新该属性")
////    @Order(12310)
//    @Disabled
//    public void update_RequestFieldWithNullValueForce_Update() {
//        long id = 1L;
//        String skuId = null;
//        String locationCode = "XX";
//        InvForTestingRequestDto invForTestingRequestDto =  new InvForTestingRequestDto();
//        invForTestingRequestDto.setLocationCode(locationCode).setSkuId(skuId).setId(id);
//
//        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class);
//        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingRequestDto,invForTesting);
//        Options instructionConfiguration = new Options();
//
//        instructionConfiguration.add(new OptionForProperty( OptionsEnum.UPDATE_FORCE, "skuId"));
//        UpdateCommandResultDto resultDto = updateInstruction.update(invForTesting, null, instructionConfiguration);
//        assertTrue(resultDto.isSuccess());
//
//        InvForTestingPo invForTestingPo = dao.selectById(id);
//
//
//        assertNull(invForTestingPo.getSkuId());
//        assertNotNull(invForTestingPo.getOwnerCode());
//        assertEquals(locationCode,invForTestingPo.getLocationCode());
//        assertNotNull(invForTestingPo.getContainerCode());
//    }

    //不可变的值，失败

}
