package com.pubtile.foundation.invfortesting.domain.model;

import com.pubtile.foundation.domain.model.BaseModelToDtoMapConfig;
import com.pubtile.foundation.domain.model.converter.ModelToDtoConverter;
import com.pubtile.foundation.invfortesting.domain.command.increase.InvForTestingRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author jiayan
 * @version 0.6.17 2021-12-29
 * @since 0.6.17 2021-12-29
 */
@Mapper(config = BaseModelToDtoMapConfig.class)
public interface InvForTestingDimensionPropertyWrongToDtoMapper extends ModelToDtoConverter<InvForTestingDimensionPropertyWrong, InvForTestingRequestDto> {
    InvForTestingDimensionPropertyWrongToDtoMapper INSTANCE = Mappers.getMapper(InvForTestingDimensionPropertyWrongToDtoMapper.class);
}
