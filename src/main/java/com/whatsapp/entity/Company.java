package com.whatsapp.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Company extends BaseModel{
    private String name;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String state;
    private String city;
    private String pinCode;
    private String logo;

}
