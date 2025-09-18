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

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
