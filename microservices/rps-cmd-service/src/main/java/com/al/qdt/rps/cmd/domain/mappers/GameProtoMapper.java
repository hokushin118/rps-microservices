package com.al.qdt.rps.cmd.domain.mappers;

import com.al.qdt.common.domain.mappers.CommonConfig;
import com.al.qdt.common.domain.mappers.ConverterMapper;
import com.al.qdt.rps.cmd.api.commands.PlayGameCommand;
import com.al.qdt.rps.grpc.v1.dto.GameDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(config = CommonConfig.class)
public interface GameProtoMapper extends ConverterMapper {

    @Mapping(target = "hand", expression = "java(enumConverter(gameDto.getHand()))")
    PlayGameCommand fromDto(GameDto gameDto);

    /**
     * Converts from {@link String} to {@link UUID}.
     *
     * @param value input object
     * @return converted object
     */
    default UUID map(String value) {
        return UUID.fromString(value);
    }
}
