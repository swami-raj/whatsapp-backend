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
public class MenuViewRole extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private MenuList menuList;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "sub_menu_id")
    private SubMenuList subMenuList;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
