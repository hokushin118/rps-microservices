package com.al.qdt.rps.qry.api.dto;

import com.al.qdt.common.api.dto.PagingDto;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
@Builder
public class GameAdminPagedResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    List<GameAdminDto> games;

    PagingDto paging;
}
