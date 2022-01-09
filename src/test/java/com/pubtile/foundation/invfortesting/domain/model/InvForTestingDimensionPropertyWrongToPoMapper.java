package com.pubtile.foundation.invfortesting.domain.model;

import com.pubtile.foundation.domain.model.BaseModelToBasePoMapConfig;
import com.pubtile.foundation.domain.model.converter.ModelToPoConverter;
import com.pubtile.foundation.invfortesting.infrastructure.po.InvForTestingPo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author jiayan
 * @version 0.6.17 2021-12-29
 * @since 0.6.17 2021-12-29
 */
@Mapper(config = BaseModelToBasePoMapConfig.class)
public interface InvForTestingDimensionPropertyWrongToPoMapper extends ModelToPoConverter<InvForTestingDimensionPropertyWrong, InvForTestingPo> {
    InvForTestingDimensionPropertyWrongToPoMapper INSTANCE = Mappers.getMapper(InvForTestingDimensionPropertyWrongToPoMapper.class);

}
