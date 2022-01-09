package com.pubtile.foundation.invfortesting.testdata;

import com.pubtile.foundation.domain.model.ModelMetaManager;
import com.pubtile.foundation.domain.model.meta.ModelMeta;
import com.pubtile.foundation.invfortesting.domain.command.increase.InvForTestingRequestDto;
import com.pubtile.foundation.invfortesting.domain.model.InvForTesting;
import com.pubtile.foundation.invfortesting.domain.model.InvForTestingModelToDtoMapper;

import java.text.SimpleDateFormat;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class TestDataSuppliers {
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public static BaselineDtoForAllSupplier baselineDtoForAllSupplier = new BaselineDtoForAllSupplier();
    public static BaselineDtoForInventoryDimensionSupplier baselineDtoForInventoryDimensionSupplier = new BaselineDtoForInventoryDimensionSupplier();

    public static class BaselineDtoForAllSupplier implements Supplier<InvForTestingRequestDto> {
        @Override
        public InvForTestingRequestDto get() {
            InvForTestingRequestDto invForTestingRequestDto = new InvForTestingRequestDto();
            invForTestingRequestDto
                    .setId(2L)
                    .setInventoryNo("KC2110282364978402")
                    .setSkuId("SKU2")
                    .setLocationCode("RECEIVE_DOCK")
                    .setContainerCode("C01")
                    .setOwnerCode("owner1")
                    .setUniqueCode("uniqueCode1")
                    .setEntryOrderType("CGRK")
                    .setOnHandQty(1)
                    .setVendorCode("vendor1")
                    .setOperatorId(3L)
                    .setTenantId(1L);

            return invForTestingRequestDto;
        }
    }

    public static class BaselineDtoForInventoryDimensionSupplier implements Supplier<InvForTestingRequestDto> {
        @Override
        public InvForTestingRequestDto get() {
            InvForTestingRequestDto invForTestingRequestDto = new InvForTestingRequestDto();
            invForTestingRequestDto
                    .setSkuId("SKU2")
                    .setLocationCode("RECEIVE_DOCK")
                    .setContainerCode("C01")
                    .setOwnerCode("owner1")
                    .setEntryOrderType("CGRK")
                    .setOnHandQty(1)
                    .setVendorCode("vendor1")
                    .setOperatorId(4L)
                    .setTenantId(1L);
            return invForTestingRequestDto;
        }
    }

    /**
     *
     */
    public static InvForTestingRequestDto buildTestDtoFromSimple(Supplier<InvForTestingRequestDto> supplier, Consumer<InvForTestingRequestDto> Consumer) {
        InvForTestingRequestDto requestDto = supplier.get();
        Consumer.accept(requestDto);
        return requestDto;
    }


    public static InvForTesting buildTestModelFromRequest(Supplier<InvForTestingRequestDto> supplier, Consumer<InvForTestingRequestDto> Consumer) {
        InvForTestingRequestDto requestDto = supplier.get();
        Consumer.accept(requestDto);
        InvForTesting invForTesting = InvForTestingModelToDtoMapper.INSTANCE.dtoToModel(requestDto);
        ModelMeta modelMeta = ModelMetaManager.buildMeta(invForTesting);
        return invForTesting;
    }

//    protected void convert(InvForTestingRequestDto dto, InvForTesting model) {
//        Converter.INSTANCE.updateModel(dto,model);
//    }
//
//    @Mapper
//    public static abstract class Converter
//    {
//        public static Converter INSTANCE = Mappers.getMapper(Converter.class);
//
//        @Mapping(target = "meta",ignore = true)
//        @Mapping(target = "fromPo",ignore = true)
//        @Mapping(target = "createdBy",ignore = true)
//        @Mapping(target = "createdTime",ignore = true)
//        @Mapping(target = "updatedTime",ignore = true)
//        @Mapping(target = "revision",ignore = true)
//        @Mapping(source = "operatorId",target = "updatedBy")
//        public abstract InvForTesting toModel(InvForTestingRequestDto dto);
//
//        @Mapping(source = "operatorId",target = "updatedBy")
//        public abstract void updateModel(InvForTestingRequestDto dto, @MappingTarget InvForTesting model);
//
//        @Mapping(source = "operatorId",target = "updatedBy")
//        public abstract void updateModel(InvForTestingRequestDto dto, @MappingTarget InvForTestingDimensionPropertyWrong model);
//
//    }
}
