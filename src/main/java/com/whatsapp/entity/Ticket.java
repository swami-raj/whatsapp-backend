package com.whatsapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.joda.time.DateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Ticket extends BaseModel{
    @ManyToOne
    @JoinColumn(name = "company_id" )
    private Company company;

    @ManyToOne
    @JoinColumn(name = "candidates_id" )
    private Candidates candidate;

    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id" )
    private User user;

    private String remarks;

    private DateTime nextFollowUpDate;

    private String ticketId;


}
