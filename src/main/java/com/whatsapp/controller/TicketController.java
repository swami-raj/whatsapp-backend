package com.whatsapp.controller;

import com.whatsapp.dto.request.CandidatesRequest;
import com.whatsapp.dto.request.TicketRequest;
import com.whatsapp.dto.response.CandidatesResponse;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.TicketResponse;
import com.whatsapp.repository.ServiceAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ticket")
public class TicketController {
    @PostMapping("add")
    ResponseDto<TicketResponse> addTicket(@RequestBody TicketRequest ticketRequest) {
        return ServiceAccessor.getTicketService().addTicket(ticketRequest );
    }

    @GetMapping("get-by-id/{id}")
    ResponseDto<TicketResponse> getById(@PathVariable Long id) {
        return ServiceAccessor.getTicketService().getTicketById(id);
    }

    @GetMapping("get-all")
    ResponseDto<List<TicketResponse>> getAll() {
        return ServiceAccessor.getTicketService().getAllTicket();
    }

    @PutMapping("update")
    ResponseDto<TicketResponse> updateTicket(@RequestBody TicketRequest ticketRequest) {
        return ServiceAccessor.getTicketService().updateTicket(ticketRequest);
    }

    @DeleteMapping("delete/{id}")
    ResponseDto<String> deleteTicket(@PathVariable Long id) {
        return ServiceAccessor.getTicketService().deleteTicket(id);
    }
}
