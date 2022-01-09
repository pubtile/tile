package com.pubtile.foundation.invfortesting;

import com.pubtile.foundation.domain.model.meta.ModelDimensionMeta;
import com.pubtile.foundation.domain.model.meta.ModelMeta;
import com.pubtile.foundation.domain.model.meta.ThreeState;
import com.pubtile.foundation.invfortesting.testdata.TestDataSuppliers;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.pubtile.foundation.invfortesting.testdata.TestDataSuppliers.buildTestModelFromRequest;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BuildMetaTest {

    //id
    //inventoryNo
    //inventoryDimension
    //unicode
    @Test
    @DisplayName("buildMeta request包含全部维度 可以更新-可以插入")
    @Order(1000)
    public void allDimesionProvied_canUpdate() {
        ModelMeta modelMeta = buildTestModelFromRequest(TestDataSuppliers.baselineDtoForAllSupplier, increaseInvForTestingDto -> {
        }).getMeta();
        boolean isUpdateAbleFromRequest=true;
        List<String> expectedDimensionNameForAssert = Arrays.asList("inventoryNo", "inventoryDimension", "uniqueCode");
        String expectedDimensionNameForUpdate = "id";
        assertMeta(modelMeta, isUpdateAbleFromRequest, expectedDimensionNameForUpdate , expectedDimensionNameForAssert);
        assertTrue(modelMeta.isInsertAbleFromRequest());
    }


    @Test
    @DisplayName("buildMeta request包含自动生成的空值， 可以更新-可以插入")
    @Order(1010)
    public void autoGenerateFieldWithValue_canInsert() {
        ModelMeta modelMeta = buildTestModelFromRequest(TestDataSuppliers.baselineDtoForAllSupplier, increaseInvForTestingDto -> {
            increaseInvForTestingDto.setId(0l);
        }).getMeta();
        boolean isUpdateAbleFromRequest=true;
        List<String> expectedDimensionNameForAssert = Arrays.asList( "inventoryDimension", "uniqueCode");
        String expectedDimensionNameForUpdate = "inventoryNo";
        assertMeta(modelMeta, isUpdateAbleFromRequest, expectedDimensionNameForUpdate , expectedDimensionNameForAssert);
        assertTrue(modelMeta.isInsertAbleFromRequest());
    }
    @Test
    @DisplayName("buildMeta request包含可为空的空值， 可以更新-可以插入")
    @Order(1020)
    public void blankableWithNull_canInsert() {
        ModelMeta modelMeta = buildTestModelFromRequest(TestDataSuppliers.baselineDtoForAllSupplier, increaseInvForTestingDto -> {
            increaseInvForTestingDto.setId(0l).setContainerCode(null);
        }).getMeta();
        boolean isUpdateAbleFromRequest=true;
        List<String> expectedDimensionNameForAssert = Arrays.asList( "inventoryDimension", "uniqueCode");
        String expectedDimensionNameForUpdate = "inventoryNo";
        assertMeta(modelMeta, isUpdateAbleFromRequest, expectedDimensionNameForUpdate , expectedDimensionNameForAssert);
        assertTrue(modelMeta.isInsertAbleFromRequest());
    }


    @Test
    @DisplayName("buildMeta request包含自动生成的非必须的， 可以插入")
    public void autoGenerateFieldWithNull_canInsert() {
        ModelMeta modelMeta = buildTestModelFromRequest(TestDataSuppliers.baselineDtoForAllSupplier, increaseInvForTestingDto -> {
            increaseInvForTestingDto.setUniqueCode(null);
        }).getMeta();
        assertTrue(modelMeta.isInsertAbleFromRequest());
    }

//    @Test
//    @DisplayName("buildMeta request包含一个维度中所有非blankable的属性,但是缺乏其他维度的属性 可更新-不可插入")
//    @Disabled
//    //TODO 需要新建个model,把InventoryNumber设为不是autogenerate
//    public void multipleDimensionFields() {
//        ModelMeta modelMeta = buildTestModelFromRequest(TestDataSuppliers.baselineDtoForInventoryDimensionSupplier, increaseInvForTestingDto -> {
//        }).getMeta();
//        boolean isUpdateAbleFromRequest=true;
//        List<String> expectedDimensionNameForAssert = Arrays.asList();
//        String expectedDimensionNameForUpdate = "inventoryDimension";
//        assertMeta(modelMeta, isUpdateAbleFromRequest, expectedDimensionNameForUpdate , expectedDimensionNameForAssert);
//        assertFalse(modelMeta.isInsertAbleFromRequest(),"期望不能insert,");
//    }


    @Test
    @DisplayName("buildMeta long型为0视为NULL 该维度失效")
    public void longWithZero_ExpectWithNull() {
        ModelMeta modelMeta = buildTestModelFromRequest(TestDataSuppliers.baselineDtoForAllSupplier, increaseInvForTestingDto -> {
            increaseInvForTestingDto.setId(0L);
        }).getMeta();

        boolean isUpdateAbleFromRequest=true;
        List<String> expectedDimensionNameForAssert = Arrays.asList("inventoryDimension", "uniqueCode");
        String expectedDimensionNameForUpdate = "inventoryNo";
        assertMeta(modelMeta, isUpdateAbleFromRequest, expectedDimensionNameForUpdate , expectedDimensionNameForAssert);

//
//        Map<String, ModelDimension> modelDimensionMap = modelMeta.getDimensionMap();
//        assertEquals(modelDimensionMap.get("id").getCanIdentify(),ThreeState.FALSE);
//
//        assertTrue(modelMeta.isUpdateAbleFromRequest());
//        assertEquals("id",modelMeta.getDimensionForUpdate().getModelDimensionDefinition().getName());
    }

    @Test
    @DisplayName("buildMeta request包含一个维度的部分属性(前) 不可更新")
    public void multipleDimensionFieldsMissingAtFirst_dimensionBecomePartial() {
        ModelMeta modelMeta = buildTestModelFromRequest(TestDataSuppliers.baselineDtoForInventoryDimensionSupplier, increaseInvForTestingDto -> {
            increaseInvForTestingDto.setSkuId(null);
        }).getMeta();

        Map<String, ModelDimensionMeta> modelDimensionMap = modelMeta.getDimensionMetaMap();
        assertEquals(modelDimensionMap.get("inventoryDimension").getCanIdentify(),ThreeState.PARTIAL);

        boolean isUpdateAbleFromRequest=false;
        List<String> expectedDimensionNameForAssert = Arrays.asList("inventoryDimension");
        String expectedDimensionNameForUpdate = null;
        assertMeta(modelMeta, isUpdateAbleFromRequest, expectedDimensionNameForUpdate , expectedDimensionNameForAssert);
    }

    @Test
    @DisplayName("buildMeta request包含一个维度的部分属性(中) 不可更新")
    public void multipleDimensionFieldsMissingAtMiddle_dimensionBecomePartial() {
        ModelMeta modelMeta = buildTestModelFromRequest(TestDataSuppliers.baselineDtoForInventoryDimensionSupplier, increaseInvForTestingDto -> {
            increaseInvForTestingDto.setLocationCode(null);
        }).getMeta();
        Map<String, ModelDimensionMeta> modelDimensionMap = modelMeta.getDimensionMetaMap();
        assertEquals(modelDimensionMap.get("inventoryDimension").getCanIdentify(),ThreeState.PARTIAL);

        boolean isUpdateAbleFromRequest=false;
        List<String> expectedDimensionNameForAssert = Arrays.asList("inventoryDimension");
        String expectedDimensionNameForUpdate = null;
        assertMeta(modelMeta, isUpdateAbleFromRequest, expectedDimensionNameForUpdate , expectedDimensionNameForAssert);
    }


    @Test
    @DisplayName("buildMeta request包含一个维度的部分属性(后) 不可更新")
    public void multipleDimensionFieldsMissingAtLast_dimensionBecomePartial() {
        ModelMeta modelMeta = buildTestModelFromRequest(TestDataSuppliers.baselineDtoForInventoryDimensionSupplier, increaseInvForTestingDto -> {
            increaseInvForTestingDto.setOwnerCode(null);
        }).getMeta();
        Map<String, ModelDimensionMeta> modelDimensionMap = modelMeta.getDimensionMetaMap();
        assertEquals(modelDimensionMap.get("inventoryDimension").getCanIdentify(),ThreeState.PARTIAL);

        boolean isUpdateAbleFromRequest=false;
        List<String> expectedDimensionNameForAssert = Arrays.asList("inventoryDimension");
        String expectedDimensionNameForUpdate = null;
        assertMeta(modelMeta, isUpdateAbleFromRequest, expectedDimensionNameForUpdate , expectedDimensionNameForAssert);
    }


    @Test
    @DisplayName("buildMeta request包含一个维度的部分属性(parent属性) 不可更新")
    public void multipleDimensionFieldsMissingAtParent_dimensionBecomePartial() {
        ModelMeta modelMeta = buildTestModelFromRequest(TestDataSuppliers.baselineDtoForInventoryDimensionSupplier, increaseInvForTestingDto -> {
//            increaseInvForTestingDto.setVendorCode(null);
            increaseInvForTestingDto.setTenantId(0L);
        }).getMeta();
        Map<String, ModelDimensionMeta> modelDimensionMap = modelMeta.getDimensionMetaMap();
        assertEquals(modelDimensionMap.get("inventoryDimension").getCanIdentify(),ThreeState.PARTIAL);

        boolean isUpdateAbleFromRequest=false;
        List<String> expectedDimensionNameForAssert = Arrays.asList("inventoryDimension");
        String expectedDimensionNameForUpdate = null;
        assertMeta(modelMeta, isUpdateAbleFromRequest, expectedDimensionNameForUpdate , expectedDimensionNameForAssert);

    }

    @Test
    @DisplayName("buildMeta request指定的维度包含一个bankable的属性为null 可更新")
    public void multipleDimensionFieldsMissingWithNullable_canUpdate() {
        ModelMeta modelMeta = buildTestModelFromRequest(TestDataSuppliers.baselineDtoForInventoryDimensionSupplier, increaseInvForTestingDto -> {
            increaseInvForTestingDto.setContainerCode(null);
        }).getMeta();
        Map<String, ModelDimensionMeta> modelDimensionMap = modelMeta.getDimensionMetaMap();
        assertEquals(modelDimensionMap.get("inventoryDimension").getCanIdentify(),ThreeState.TRUE);

        boolean isUpdateAbleFromRequest=true;
        List<String> expectedDimensionNameForAssert = Arrays.asList();
        String expectedDimensionNameForUpdate = "inventoryDimension";
        assertMeta(modelMeta, isUpdateAbleFromRequest, expectedDimensionNameForUpdate , expectedDimensionNameForAssert);
    }

    @Test
    @DisplayName("buildMeta request包含的维度都是有部分属性 不能更新")
    public void multipleDimensionFieldsMissingOnExcludeNameField_becomePartial() {
        ModelMeta modelMeta = buildTestModelFromRequest(TestDataSuppliers.baselineDtoForAllSupplier, increaseInvForTestingDto -> {
            increaseInvForTestingDto.setId(0L);
            increaseInvForTestingDto.setTenantId(0L);
        }).getMeta();

        boolean isUpdateAbleFromRequest=false;
        List<String> expectedDimensionNameForAssert = Arrays.asList("inventoryNo", "inventoryDimension", "uniqueCode");
        String expectedDimensionNameForUpdate = null;
        assertMeta(modelMeta, isUpdateAbleFromRequest, expectedDimensionNameForUpdate , expectedDimensionNameForAssert);

        Map<String, ModelDimensionMeta> modelDimensionMap = modelMeta.getDimensionMetaMap();
        assertEquals(modelDimensionMap.get("inventoryNo").getCanIdentify(),ThreeState.PARTIAL);
        assertEquals(modelDimensionMap.get("inventoryDimension").getCanIdentify(),ThreeState.PARTIAL);
        assertEquals(modelDimensionMap.get("uniqueCode").getCanIdentify(),ThreeState.PARTIAL);
    }


    private void assertMeta(ModelMeta modelMeta, boolean isUpdateAbleFromRequest, String expectedDimensionNameForUpdate, List<String> expectedDimensionNameForAssert) {
        assertEquals(isUpdateAbleFromRequest, modelMeta.isUpdateAbleFromRequest());
        if (expectedDimensionNameForUpdate == null){
            assertNull(modelMeta.getDimensionForIdentify());
        }else{
            assertEquals(expectedDimensionNameForUpdate, modelMeta.getDimensionForIdentify().getName());
        }
        assertEquals(expectedDimensionNameForAssert.size(), modelMeta.getDimensionsForAssert().stream().filter(modelDimension -> {
            return expectedDimensionNameForAssert.contains(modelDimension.getName());
        }).count());
    }
}
