package com.whatsapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Department extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private String name;
}
