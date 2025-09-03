package com.whatsapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseModel implements Serializable {

    @Serial private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected boolean isActive;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User updatedBy;

    @PrePersist
    protected void prePersist() {
        createdOn = new Date();
        updatedOn = new Date();
        isActive = true;
    }

    @PreUpdate
    protected void preUpdate() {

        this.updatedOn = new Date();
    }
}
