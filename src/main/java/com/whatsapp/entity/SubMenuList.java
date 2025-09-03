package com.whatsapp.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class SubMenuList extends BaseModel {
    private String name;
    private String url;
    private String image;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private MenuList menuList;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
