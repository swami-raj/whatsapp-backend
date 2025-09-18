package com.whatsapp.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Candidates extends BaseModel{
    private String name;
    private String email;
    private String phone;
    private String experience;
    private String currentCTC;
    private String expectedCTC;
    private String noticePeriod;
    private String skills;
    private String resumeLink;
    private String status;
    private String linkedInProfile;
    private String notes;
}
