package com.al.qdt.common.domain.base;

import com.al.qdt.cqrs.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@JsonIgnoreProperties(
        value = {"version"},
        allowGetters = true
)
public abstract class BaseEntity extends AbstractEntity {
    public static final String ID_MUST_NOT_BE_NULL = "Identification must not be null";

    @NotNull(message = ID_MUST_NOT_BE_NULL)
    @Id
    @Column(name = "ID", columnDefinition = "VARBINARY(16)", nullable = false, updatable = false)
    protected UUID id;

    @Version
    @Column(name = "VERSION")
    private Integer version;
}
