package com.whatsapp.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandidatesResponse {
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
