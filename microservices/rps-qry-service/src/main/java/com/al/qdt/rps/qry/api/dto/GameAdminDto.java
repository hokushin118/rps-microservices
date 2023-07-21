package com.al.qdt.rps.qry.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Value
@Builder
@JsonDeserialize(builder = GameAdminDto.GameAdminDtoBuilder.class)
public class GameAdminDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    String id;

    @NotBlank
    @JsonProperty("user_id")
    String userId;

    @NotBlank
    String hand;

    @JsonPOJOBuilder(withPrefix = "")
    public static class GameAdminDtoBuilder {
    }
}
