package com.al.qdt.rps.qry.domain.entities;

import com.al.qdt.common.domain.base.BaseEntity;
import com.al.qdt.common.domain.enums.Hand;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "GAME", indexes = {@Index(name = "IDX_USER_ID", columnList = "USER_ID")})
public class Game extends BaseEntity {
    public static final String USER_ID_MUST_NOT_BE_NULL = "User id must not be null";
    public static final String HAND_MUST_NOT_BE_NULL = "Hand must not be null";

    @NotNull(message = USER_ID_MUST_NOT_BE_NULL)
    @Column(name = "USER_ID", columnDefinition = "VARBINARY(16)", nullable = false, updatable = false)
    private UUID userId;

    @NotNull(message = HAND_MUST_NOT_BE_NULL)
    @Enumerated(STRING)
    @Column(name = "HAND")
    private Hand hand;

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Game)) return false;
        final var other = (Game) o;
        if (!other.canEqual(this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null && other$id == null) return false;
        return Objects.equals(this$id, other$id);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Game;
    }

    @Override
    public int hashCode() {
        final var PRIME = 31;
        var result = 1;
        final var $id = super.getId();
        return result * PRIME + (($id == null) ? 0 : $id.hashCode());
    }
}
