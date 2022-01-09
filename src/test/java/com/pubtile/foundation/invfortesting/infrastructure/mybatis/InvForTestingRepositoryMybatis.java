package com.pubtile.foundation.invfortesting.infrastructure.mybatis;

import com.pubtile.foundation.domain.repository.IRepository;
import com.pubtile.foundation.invfortesting.domain.model.InvForTesting;
import com.pubtile.foundation.invfortesting.infrastructure.mybatis.mapper.InvForTestingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jiayan
 * @version 0.6.17 2021-11-04
 * @since 0.6.17 2021-11-04
 */
@Repository
@Slf4j
public class InvForTestingRepositoryMybatis implements IRepository<InvForTesting> {
    @Autowired
    InvForTestingMapper invForTestingMapper;

////    @Override
//    public BaseModelV3 findByDimensions(BaseModelV3 inventory, ModelMeta modelMeta) {
//        ModelDimension dimensionForUpdate = modelMeta.getDimensionForUpdate();
//        InvForTestingPo po = null;
//
//        Map<String, Object> conditionMap = new HashMap<>();
//        dimensionForUpdate.getModelDimensionDefinition().getFields().stream()
//                .forEach(modelDimensionFieldDefinition -> {
//                    Field field = modelDimensionFieldDefinition.getField();
//                    field.setAccessible(true);
//                    Object fieldValue = null;
//                    try {
//                        fieldValue = field.get(inventory);
//                    } catch (IllegalAccessException e) {
//                        AssertForBiz.assertWithExceptionCode(false, AssertForBiz.ExceptionInfo.builder()
//                                .domain(DomainInfo.DOMIAN_NAME)
//                                .exceptionCode(CommonExceptionCode.CODE_ERROR)
//                                .cause(e)
//                                .build());
//                    }
//                    AssertForBiz.assertWithExceptionCode(fieldValue!=null, DomainInfo.DOMIAN_NAME,"", CommonExceptionCode.CODE_ERROR_WITH_PLACEHOLDER, "the value of field "+field.getName()+" is null");
//                    //在用维度定位时，即使入参是空，也可以作为查询条件
//                    if (inventory.isBlank(fieldValue) && !modelDimensionFieldDefinition.isBlankAble()){
//                        log.debug("the value of field {}  is null and it is not blankable",field.getName());
//                    } else {
//                        conditionMap.put(modelDimensionFieldDefinition.getColumnName(), fieldValue);
//                    }
//
//                });
//
//        List<InvForTestingPo> invForTestingPos = invForTestingMapper.selectByMap(conditionMap);
//        if (invForTestingPos.size() == 0) {
//            modelMeta.setUpdateAble(false);
//            return null;
//        } else if(invForTestingPos.size() == 1 ){
//            modelMeta.setUpdateAble(true);
//            po=invForTestingPos.get(0);
//        } else {
//            //根据维度查找返回多行,
//            AssertForBiz.assertWithExceptionCode(false, AssertForBiz.ExceptionInfo.builder()
//                            .domain(DomainInfo.DOMIAN_NAME)
//                            .exceptionCode(CommonExceptionCode.STATE_ERROR_WITH_PLACEHOLDER)
//                            .build(),
//                    "the query count for Dimension : " + dimensionForUpdate.getModelDimensionDefinition().getName(),invForTestingPos.size(), "0 or 1。"+conditionMap);
//        }
//
//        return Converter.INSTANCE.toModel(po);
//    }

//    @Override
//    public int insert(InvForTesting model) {
//        return invForTestingMapper.insert(Converter.INSTANCE.fromModel(model));
//    }

//    @Override
//    public InvForTesting findByDimension(Map<String, Object> dimensionCondition, Function<List<InvForTestingPo>,InvForTestingPo> handleResult) {
//        List<InvForTestingPo> invForTestingPos = invForTestingMapper.selectByMap(dimensionCondition);
//        InvForTestingPo po = handleResult.apply(invForTestingPos);
//}
//        if (invForTestingPos.size() == 0) {
//            modelMeta.setUpdateAble(false);
//            return null;
//        } else if(invForTestingPos.size() == 1 ){
//            modelMeta.setUpdateAble(true);
//            po=invForTestingPos.get(0);
//        } else {
//            //根据维度查找返回多行,
//            AssertForBiz.assertWithExceptionCode(false, AssertForBiz.ExceptionInfo.builder()
//                            .domain(DomainInfo.DOMIAN_NAME)
//                            .exceptionCode(CommonExceptionCode.STATE_ERROR_WITH_PLACEHOLDER)
//                            .build(),
//                    "the query count for Dimension : " + dimensionForUpdate.getModelDimensionDefinition().getName(),invForTestingPos.size(), "0 or 1。"+conditionMap);
//        }
//
//        return Converter.INSTANCE.toModel(po);
//    }


}
