package com.whatsapp.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketResponse {
    private Long id;
    private Long candidateId;
    private Long userId;
    private String status;
    private String remarks;
    private String nextFollowUpDate;
    private String candidateName;
    private String assignedTo;
    private String candidateEmail;
    private String candidatePhone;
    private String ticketId;

}
