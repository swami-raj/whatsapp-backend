package com.whatsapp.services;

import com.whatsapp.dto.request.TicketRequest;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.TicketResponse;

import java.util.List;

public interface TicketService {
    ResponseDto<TicketResponse> addTicket(TicketRequest ticketRequest);

    ResponseDto<TicketResponse> getTicketById(Long id);

    ResponseDto<List<TicketResponse>> getAllTicket();

    ResponseDto<TicketResponse> updateTicket(TicketRequest ticketRequest);

    ResponseDto<String> deleteTicket(Long id);
}
