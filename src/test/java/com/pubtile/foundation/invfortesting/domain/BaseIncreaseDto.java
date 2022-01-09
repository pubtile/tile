package com.pubtile.foundation.invfortesting.domain;

import com.pubtile.foundation.BaseRequestDto;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 *
 * @author jiayan
 * @version 0.6.17 2021-11-06
 * @since 0.6.17 2021-11-06
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class BaseIncreaseDto extends BaseRequestDto {

}