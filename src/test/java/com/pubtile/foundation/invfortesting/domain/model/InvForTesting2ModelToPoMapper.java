package com.pubtile.foundation.invfortesting.domain.model;

import com.pubtile.foundation.domain.model.BaseModelToBasePoMapConfig;
import com.pubtile.foundation.domain.model.converter.ModelToPoConverter;
import com.pubtile.foundation.invfortesting.infrastructure.po.InvForTesting2Po;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author jiayan
 * @version 0.6.17 2021-12-29
 * @since 0.6.17 2021-12-29
 */
@Mapper(config = BaseModelToBasePoMapConfig.class)
public interface InvForTesting2ModelToPoMapper extends ModelToPoConverter<InvForTesting2, InvForTesting2Po> {
    InvForTesting2ModelToPoMapper INSTANCE = Mappers.getMapper(InvForTesting2ModelToPoMapper.class);


}
