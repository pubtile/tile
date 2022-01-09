package com.pubtile.foundation.invfortesting.testdata;

import com.pubtile.foundation.invfortesting.domain.command.increase.InvForTesting2RequestDto;
import com.pubtile.foundation.invfortesting.domain.model.InvForTesting2;
import com.pubtile.foundation.invfortesting.domain.model.InvForTesting2ModelToDtoMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class InvForTesting2TestDataSuppliers {
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public static BaselineDtoForAllSupplier baselineDtoForAllSupplier = new BaselineDtoForAllSupplier();
    public static BaselineDtoForInventoryDimensionSupplier baselineDtoForInventoryDimensionSupplier = new BaselineDtoForInventoryDimensionSupplier();

    public static class BaselineDtoForAllSupplier implements Supplier<InvForTesting2RequestDto> {
        @Override
        public InvForTesting2RequestDto get() {
            try {
                Date date = formatter.parse("2021-06-17");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            InvForTesting2RequestDto invForTesting2RequestDto = new InvForTesting2RequestDto();
            invForTesting2RequestDto
                    .setId(2L)
                    .setInventoryNo("RECEIVE_DOCK")
                    .setSkuId("SKU1")
                    .setLocationCode("SH01-A-1-1")
                    .setContainerCode("C01")
                    .setOwnerCode("owner1")
                    .setUniqueCode("uniqueCode1")
                    .setEntryOrderType("relOrderCode1")
                    .setOnHandQty(1)
                    .setVendorCode("vendor1")
                    .setOperatorId(1L)
                    .setTenantId(1L);
            return invForTesting2RequestDto;
        }
    }

    public static class BaselineDtoForInventoryDimensionSupplier implements Supplier<InvForTesting2RequestDto> {
        @Override
        public InvForTesting2RequestDto get() {
            try {
                Date date = formatter.parse("2021-06-17");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            InvForTesting2RequestDto invForTesting2RequestDto = new InvForTesting2RequestDto();

            invForTesting2RequestDto
                    .setSkuId("SKU1")
                    .setLocationCode("SH01-A-1-1")
                    .setContainerCode("C01")
                    .setOwnerCode("owner1")
                    .setUniqueCode("")
                    .setEntryOrderType("relOrderCode1")
                    .setOnHandQty(1)
                    .setVendorCode("vendor1")
                    .setOperatorId(1L)
                    .setTenantId(1L);

            return invForTesting2RequestDto;
        }
    }

    public static InvForTesting2RequestDto buildTestDtoFromSimple(Supplier<InvForTesting2RequestDto> supplier, Consumer<InvForTesting2RequestDto> Consumer) {
        InvForTesting2RequestDto requestDto = supplier.get();
        Consumer.accept(requestDto);
        return requestDto;
    }


    public static InvForTesting2 buildTestModelFromRequest(Supplier<InvForTesting2RequestDto> supplier, Consumer<InvForTesting2RequestDto> Consumer) {
        InvForTesting2RequestDto requestDto = supplier.get();
        Consumer.accept(requestDto);
        InvForTesting2 InvForTesting2 = InvForTesting2ModelToDtoMapper.INSTANCE.dtoToModel(requestDto);
        return InvForTesting2;
    }
//
//    protected void convert(InvForTesting2IncreaseRequestDto dto, InvForTesting2 model) {
//        Converter.INSTANCE.updateModel(dto,model);
//    }
//
////    @Mapper
////    public static abstract class Converter
////    {
////        public static Converter INSTANCE = Mappers.getMapper(Converter.class);
////
////        @Mapping(target = "meta",ignore = true)
////        @Mapping(target = "createdBy",ignore = true)
////        @Mapping(target = "revision",ignore = true)
////        @Mapping(target = "createdTime",ignore = true)
////        @Mapping(target = "updatedBy",ignore = true)
////        @Mapping(target = "updatedTime",ignore = true)
////        @Mapping(target = "fromPo",ignore = true)
////        public abstract InvForTesting2 toModel(InvForTesting2IncreaseRequestDto dto);
////
////        @Mapping(source = "operatorId",target = "updatedBy")
////        public abstract void updateModel(InvForTesting2IncreaseRequestDto dto, @MappingTarget InvForTesting2 model);
////    }
}
