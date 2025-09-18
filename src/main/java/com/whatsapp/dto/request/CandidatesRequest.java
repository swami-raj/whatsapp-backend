package com.whatsapp.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandidatesRequest {
    private Long id;
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
