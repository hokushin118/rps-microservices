package com.al.qdt.common.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
@JsonDeserialize(builder = PagingDto.PagingDtoBuilder.class)
public class PagingDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty(value = "current_page")
    int currentPage;

    @JsonProperty(value = "page_size")
    int pageSize;

    @JsonProperty(value = "total_elements")
    long totalElements;

    @JsonPOJOBuilder(withPrefix = "")
    public static class PagingDtoBuilder {
    }
}
