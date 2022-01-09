package com.pubtile.foundation.invfortesting.domain.model;

import com.pubtile.foundation.domain.model.BaseModelToDtoMapConfig;
import com.pubtile.foundation.domain.model.converter.ModelToDtoConverter;
import com.pubtile.foundation.invfortesting.domain.command.increase.InvForTesting2RequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author jiayan
 * @version 0.6.17 2021-12-29
 * @since 0.6.17 2021-12-29
 */
@Mapper(config = BaseModelToDtoMapConfig.class)
public interface InvForTesting2ModelToDtoMapper extends ModelToDtoConverter<InvForTesting2,InvForTesting2RequestDto> {
    InvForTesting2ModelToDtoMapper INSTANCE = Mappers.getMapper(InvForTesting2ModelToDtoMapper.class);
}
