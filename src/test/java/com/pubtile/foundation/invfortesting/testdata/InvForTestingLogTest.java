//package com.pubtile.foundation.invfortesting;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.pubtile.base.dto.BaseInnerResultDto;
//import com.pubtile.bc.operationlog.domain.service.LogBusiness;
//import com.pubtile.foundation.domain.model.BaseModelV3;
//import com.pubtile.foundation.domain.model.ModelFactory;
//import com.pubtile.foundation.domain.model.instruction.BaseUpdateInstruction;
//import com.pubtile.foundation.domain.model.instruction.Instruction;
//import com.pubtile.foundation.domain.model.instruction.UpdateInstruction;
//import com.pubtile.foundation.domain.model.instruction.interceptor.HandlerInterceptor;
//import com.pubtile.foundation.domain.model.instruction.interceptor.InterceptorRegistry;
//import com.pubtile.foundation.domain.service.command.dto.result.IncreaseCommandResultDto;
//import com.pubtile.foundation.invfortesting.domain.command.increase.InvForTestIngIncreaseInstruction;
//import com.pubtile.foundation.invfortesting.domain.command.increase.InvForTestingIncreaseRequestDto;
//import com.pubtile.foundation.invfortesting.domain.model.InvForTesting;
//import com.pubtile.foundation.invfortesting.infrastructure.mybatis.mapper.InvForTestingMapper;
//import com.pubtile.foundation.invfortesting.infrastructure.po.InvForTestingPo;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.tuple.Pair;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.event.annotation.BeforeTestClass;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.lang.reflect.Field;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = InvForTestingApplication.class)
//@Slf4j
//@Transactional
//public class InvForTestingLogTest {
//    @Autowired
//    BaseUpdateInstruction<InvForTesting> updateInstruction;
//
//    @Autowired
//    InvForTestIngIncreaseInstruction invForTestIngIncreaseCommand;
//
//    @Autowired
//    InvForTestingMapper dao;
//
//    @Autowired
//    LogBusiness lb;
//    @Autowired
//    InterceptorRegistry interceptorRegistry;
//
//    @Test
//    @Disabled
//    public void logByInterceptor() throws IllegalAccessException {
//
//        interceptorRegistry.addInterceptor(new HandlerInterceptor() {
//            @Override
//            public boolean preHandle(BaseModelV3 request, BaseInnerResultDto response, Instruction handler) {
//                return HandlerInterceptor.super.preHandle(request, response, handler);
//            }
//
//            @Override
//            public void postHandle(BaseModelV3 request, BaseInnerResultDto response, Instruction handler) {
//                if (handler instanceof UpdateInstruction){
//                    Map<Field, Pair> updateInformation = (Map<Field, Pair>) response.getData();
//
//                    lb.append("wms", request.getClass().getSimpleName(),"31", request.getUpdatedBy()+"", "update", "Update a Task", null, null, updateInformation, null);
//                }
//                HandlerInterceptor.super.postHandle(request, response, handler);
//            }
//        });
//
//        int increaseOnHandQty = 10;
//        int increaseSuspenseQty = 1;
//        long operatorId = 3L;
//
//        List<InvForTestingPo> invForTestingPos =
//                dao.selectList(new QueryWrapper<InvForTestingPo>().lambda().eq(InvForTestingPo::getId,
//                        Long.valueOf(2L)));
//        assertTrue(invForTestingPos.size() == 1);
//
//        InvForTestingIncreaseRequestDto invForTestingIncreaseRequestDto =
//                TestDataSuppliers.buildTestDtoFromSimple(TestDataSuppliers.baselineDtoForAllSupplier,
//                        dto -> {
//                            dto.setOnHandQty(increaseOnHandQty);
//                            dto.setSuspenseQty(increaseSuspenseQty);
//                            dto.setOperatorId(operatorId);
//                        });
//
//        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class);
//        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingIncreaseRequestDto, invForTesting);
//        IncreaseCommandResultDto result = invForTestIngIncreaseCommand.increase(invForTesting);
//        assertTrue(result.isSuccess());
//        assertFalse(result.isInserted());
//        assertTrue(result.isUpdated());
//        assertNotEquals(result.getId(), 0L);
//
//        List<InvForTestingPo> invForTestingPosNew =
//                dao.selectList(new QueryWrapper<InvForTestingPo>().lambda().eq(InvForTestingPo::getId,
//                        Long.valueOf(2L)));
//        assertTrue(invForTestingPosNew.size() == 1);
//        InvForTestingPo oldOne = invForTestingPos.get(0);
//        InvForTestingPo newOne = invForTestingPosNew.get(0);
//
//        assertEquals(newOne.getOnHandQty(), oldOne.getOnHandQty() + increaseOnHandQty);
//        assertEquals(newOne.getSuspenseQty(), oldOne.getSuspenseQty() + increaseSuspenseQty);
//        assertEquals(newOne.getUpdatedBy(), operatorId);
//    }
//
//
//    @Test
//    public void logIncreaseInsert(){
//
//        interceptorRegistry.addInterceptor(new HandlerInterceptor() {
//            @Override
//            public boolean preHandle(BaseModelV3 request, BaseInnerResultDto response, Instruction handler) {
//                return HandlerInterceptor.super.preHandle(request, response, handler);
//            }
//
//            @Override
//            public void postHandle(BaseModelV3 request, BaseInnerResultDto response, Instruction handler) {
//                if (handler instanceof UpdateInstruction){
//                    Map<Field, Pair> updateInformation = (Map<Field, Pair>) response.getData();
//
//                    lb.append("wms", request.getClass().getSimpleName(),"31", request.getUpdatedBy()+"", "update", "Update a Task", null, null, updateInformation, null);
//                }
//                HandlerInterceptor.super.postHandle(request, response, handler);
//            }
//        });
//
//
//        InvForTestingIncreaseRequestDto invForTestingIncreaseRequestDto =
//                TestDataSuppliers.buildTestDtoFromSimple(TestDataSuppliers.baselineDtoForInventoryDimensionSupplier, dto -> {
//                    dto.setContainerCode("C02");
//                    dto.setInventoryNo("INV_NO_NEW1");
//                });
//
//        InvForTesting invForTesting = ModelFactory.getModelBean(InvForTesting.class);
//        InvForTestingModelToDtoMapper.INSTANCE.dtoIntoModel(invForTestingIncreaseRequestDto,invForTesting);
//        IncreaseCommandResultDto result = invForTestIngIncreaseCommand.increase(invForTesting);
//        assertTrue(result.isSuccess());
//        assertTrue(result.isInserted());
//        assertFalse(result.isUpdated());
//        assertNotEquals(result.getId(),0L);
//
//    }
//
//}
