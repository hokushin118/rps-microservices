package com.al.qdt.rps.qry.domain.mappers;

import com.al.qdt.common.domain.mappers.CommonConfig;
import com.al.qdt.rps.grpc.v1.common.SortingOrder;
import com.al.qdt.rps.grpc.v1.dto.GameAdminDto;
import com.al.qdt.rps.grpc.v1.dto.GameAdminDto;
import com.al.qdt.rps.grpc.v1.dto.GameDto;
import com.al.qdt.rps.qry.domain.entities.Game;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;

import java.util.UUID;

@Mapper(config = CommonConfig.class)
public interface GameProtoMapper {

    /**
     * Converts game entity object {@link Game} to game dto object {@link GameDto}.
     *
     * @param game game entity
     * @return game data transfer object
     */
    GameDto toGameDto(Game game);

    /**
     * Converts game entity object {@link Game} to game admin dto object {@link GameAdminDto}.
     *
     * @param game game entity
     * @return game admin data transfer object
     */
    GameAdminDto toGameAdminDto(Game game);

    /**
     * Converts enum {@link SortingOrder} to enum {@link com.al.qdt.cqrs.queries.SortingOrder}.
     *
     * @param sortingOrder enum
     * @return converted enum
     */
    @ValueMapping(source = "ASC", target = "ASC")
    @ValueMapping(source = "DESC", target = "DESC")
    @ValueMapping(source = MappingConstants.ANY_REMAINING, target = "ASC")
    com.al.qdt.cqrs.queries.SortingOrder toSortingOrder(SortingOrder sortingOrder);

    /**
     * Converts from {@link UUID} to {@link String}.
     *
     * @param value input object
     * @return converted object
     */
    default String map(UUID value) {
        return String.valueOf(value);
    }
}
