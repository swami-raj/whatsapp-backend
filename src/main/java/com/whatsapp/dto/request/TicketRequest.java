package com.whatsapp.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketRequest {
    private Long id;
    private Long candidateId;
    private Long userId;
    private String status;
    private String remarks;
    private String nextFollowUpDate;

}
