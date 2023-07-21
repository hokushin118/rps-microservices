package com.al.qdt.score.qry.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = ScoreAdminDto.ScoreAdminDtoBuilder.class)
public class ScoreAdminDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    String id;

    @NotBlank
    String userId;

    @NotBlank
    String winner;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ScoreAdminDtoBuilder {
    }
}
