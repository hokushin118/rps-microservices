package com.al.qdt.score.qry.domain.mappers;

import com.al.qdt.common.domain.mappers.CommonConfig;
import com.al.qdt.rps.grpc.v1.common.SortingOrder;
import com.al.qdt.rps.grpc.v1.dto.ScoreAdminDto;
import com.al.qdt.rps.grpc.v1.dto.ScoreDto;
import com.al.qdt.score.qry.domain.entities.Score;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;

import java.util.UUID;

@Mapper(config = CommonConfig.class)
public interface ScoreProtoMapper {

    /**
     * Converts score entity object {@link Score} to score dto object {@link ScoreDto}.
     *
     * @param score score entity
     * @return score data transfer object
     */
    ScoreDto toScoreDto(Score score);

    /**
     * Converts score entity object {@link Score} to score admin dto object {@link ScoreAdminDto}.
     *
     * @param score score entity
     * @return score admin data transfer object
     */
    ScoreAdminDto toScoreAdminDto(Score score);

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
        return value.toString();
    }
}
