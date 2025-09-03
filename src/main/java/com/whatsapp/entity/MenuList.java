package com.whatsapp.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class MenuList extends BaseModel {
    private String name;
    private String url;
    private String image;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
