package com.pubtile.foundation.invfortesting;

import com.pubtile.foundation.FoundationExceptionCode;
import com.pubtile.foundation.domain.model.ModelFactory;
import com.pubtile.foundation.domain.instruction.increase.BaseIncreaseInstruction;
import com.pubtile.foundation.invfortesting.app.InvForTestingApplication;
import com.pubtile.foundation.invfortesting.domain.command.increase.InvForTestIng2IncreaseInstruction;
import com.pubtile.foundation.invfortesting.domain.command.increase.InvForTestingRequestDto;
import com.pubtile.foundation.invfortesting.domain.model.InvForTestingDimensionPropertyWrong;
import com.pubtile.foundation.invfortesting.infrastructure.mybatis.mapper.InvForTesting2Mapper;
import com.pubtile.foundation.invfortesting.testdata.TestDataSuppliers;
import com.pubtile.foundation.test.TestAssert;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = InvForTestingApplication.class)
@Slf4j
@Transactional
public class IncreaseInstructionWithWrongConfigurationTest {
    @Autowired
    InvForTestIng2IncreaseInstruction InvForTesting2IncreaseCommand;

    @Autowired
    BaseIncreaseInstruction<InvForTestingDimensionPropertyWrong> InvForTestingDimensionPropertyWrongIncreaseInstruction;

    @Autowired
    InvForTesting2Mapper dao;

    @Test
    @DisplayName("一个属性定义既定义了ValueProperty,又定义了DimensionProperty ")
//    @Disabled
    public void definition_duplicationProperty() {
        InvForTestingRequestDto invForTestingRequestDto =
                TestDataSuppliers.buildTestDtoFromSimple(TestDataSuppliers.baselineDtoForInventoryDimensionSupplier, dto -> {
                });

        InvForTestingDimensionPropertyWrong invForTesting = ModelFactory.getModelBean(InvForTestingDimensionPropertyWrong.class);
//        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingRequestDto,invForTesting);
        TestAssert.assertExceptionCode(FoundationExceptionCode.DEFINE_PROPERTY_DUPLICATED,() -> {
            InvForTestingDimensionPropertyWrongIncreaseInstruction.increase(invForTesting);
        });

    }

}
