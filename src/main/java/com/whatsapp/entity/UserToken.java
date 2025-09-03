package com.whatsapp.entity;

import jakarta.persistence.Column;
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
public class UserToken extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 5000)
    private String token;
}
