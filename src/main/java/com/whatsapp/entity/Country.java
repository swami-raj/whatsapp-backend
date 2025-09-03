package com.whatsapp.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Country extends BaseModel{
    private String name;
    private String code;
}
