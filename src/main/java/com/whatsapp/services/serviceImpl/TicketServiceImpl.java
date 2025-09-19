package com.whatsapp.services.serviceImpl;

import com.whatsapp.dto.request.TicketRequest;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.TicketResponse;
import com.whatsapp.dto.response.UserDetailResponse;
import com.whatsapp.entity.Candidates;
import com.whatsapp.entity.Company;
import com.whatsapp.entity.Ticket;
import com.whatsapp.entity.User;
import com.whatsapp.repository.RepositoryAccessor;
import com.whatsapp.services.TicketService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketServiceImpl.class);

    @Override
    public ResponseDto<TicketResponse> addTicket(TicketRequest ticketRequest) {
        LOGGER.info("[TicketServiceImpl >> addTicket] Adding new ticket: {}", ticketRequest.getCandidateId());
        ResponseDto<TicketResponse> response = new ResponseDto<>();
        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> optionalUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(userDetailResponse.getId(), true);
            if (optionalUser.isEmpty()) {
                response.setCode(0);
                response.setMessage("user not found.");
                return response;
            }

            Optional<User> userOptional = RepositoryAccessor.getUserRepository().findByIdAndIsActive(ticketRequest.getUserId(), true);
            if (userOptional.isEmpty()) {
                response.setCode(0);
                response.setMessage("user not found.");
                return response;
            }

            Optional<Candidates> optionalCandidates = RepositoryAccessor.getCandidatesRepository().findByIdAndIsActive(ticketRequest.getCandidateId(), true);
            if (optionalCandidates.isEmpty()) {
                response.setCode(0);
                response.setMessage("candidate not found.");
                return response;
            }

            Company company = optionalUser.get().getCompany();
            String prefix = company.getName().substring(0, 2).toUpperCase();
            Optional<Ticket> lastTicketOpt = RepositoryAccessor.getTicketRepository().findTopByCompanyOrderByIdDesc(company);

            String ticketId;
            if (lastTicketOpt.isPresent()) {
                String lastTicketId = lastTicketOpt.get().getTicketId();
                String numberPart = lastTicketId.substring(2);
                int newNumber = Integer.parseInt(numberPart) + 1;
                ticketId = prefix + String.format("%02d", newNumber);
            } else {
                ticketId = prefix + "01";
            }

            Ticket ticket = Ticket.builder()
                    .user(userOptional.get())
                    .candidate(optionalCandidates.get())
                    .remarks(ticketRequest.getRemarks())
                    .status(ticketRequest.getStatus())
                    .company(company)
                    .nextFollowUpDate(DateTime.parse(ticketRequest.getNextFollowUpDate()))
                    .ticketId(ticketId)
                    .build();

            ticket.setCreatedBy(optionalUser.get());
            RepositoryAccessor.getTicketRepository().save(ticket);

            response.setData(mapToResponse(ticket));
            response.setCode(1);
            response.setMessage("ticket added successfully");

        } catch (Exception e) {
            LOGGER.error("[TicketServiceImpl >> addTicket] Exception occurred while adding ticket", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }


    @Override
    public ResponseDto<TicketResponse> getTicketById(Long id) {
        LOGGER.info("[TicketServiceImpl >> getTicketById] get ticket: {}", id);
        ResponseDto<TicketResponse> response = new ResponseDto<>();
        try {
            Optional<Ticket> optionalTicket = RepositoryAccessor.getTicketRepository().findByIdAndIsActive(id, true);
            if(optionalTicket.isEmpty()){
                response.setCode(0);
                response.setMessage("ticket not found.");
                return response;
            }
            response.setData(mapToResponse(optionalTicket.get()));
            response.setCode(1);
            response.setMessage("ticket fetched successfully");
        }catch (Exception e){
            LOGGER.error("[TicketServiceImpl >> getTicketById] Exception occurred while fetching ticket", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<List<TicketResponse>> getAllTicket() {
        LOGGER.info("[TicketServiceImpl >> getAllTicket] get all tickets");
        ResponseDto<List<TicketResponse>> response = new ResponseDto<>();
        try {
            List<Ticket> tickets = RepositoryAccessor.getTicketRepository().findAllByIsActive(true);
            if(tickets.isEmpty()){
                response.setCode(0);
                response.setMessage("tickets not found.");
                return response;
            }
            response.setData(tickets.stream().map(this::mapToResponse).toList());
            response.setCode(1);
            response.setMessage("tickets fetched successfully");
        }catch (Exception e){
            LOGGER.error("[TicketServiceImpl >> getAllTicket] Exception occurred while fetching tickets", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<TicketResponse> updateTicket(TicketRequest ticketRequest) {
        LOGGER.info("[TicketServiceImpl >> updateTicket] Updating ticket: {}", ticketRequest.getId());
        ResponseDto<TicketResponse> response = new ResponseDto<>();
        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> optionalUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(userDetailResponse.getId(), true);
            if(optionalUser.isEmpty()){
                response.setCode(0);
                response.setMessage("user not found.");
                return response;
            }
            Optional<User> userOptional = RepositoryAccessor.getUserRepository().findByIdAndIsActive(ticketRequest.getUserId(), true);
            if(userOptional.isEmpty()){
                response.setCode(0);
                response.setMessage("user not found.");
                return response;
            }
            Optional<Candidates> optionalCandidates = RepositoryAccessor.getCandidatesRepository().findByIdAndIsActive(ticketRequest.getCandidateId(), true);
            if(optionalCandidates.isEmpty()){
                response.setCode(0);
                response.setMessage("candidate not found.");
                return response;
            }
            Optional<Ticket> optionalTicket = RepositoryAccessor.getTicketRepository().findByIdAndIsActive(ticketRequest.getId(), true);
            if(optionalTicket.isEmpty()){
                response.setCode(0);
                response.setMessage("ticket not found.");
                return response;
            }
            Ticket ticket = optionalTicket.get();
            ticket.setUser(userOptional.get());
            ticket.setCandidate(optionalCandidates.get());
            ticket.setRemarks(ticketRequest.getRemarks());
            ticket.setStatus(ticketRequest.getStatus());
            ticket.setNextFollowUpDate(DateTime.parse(ticketRequest.getNextFollowUpDate()));
            ticket.setUpdatedBy(optionalUser.get());
            RepositoryAccessor.getTicketRepository().save(ticket);
            response.setData(mapToResponse(ticket));
            response.setCode(1);
            response.setMessage("ticket updated successfully");

        }catch (Exception e){
            LOGGER.error("[TicketServiceImpl >> updateTicket] Exception occurred while updating ticket", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<String> deleteTicket(Long id) {
        LOGGER.info("[TicketServiceImpl >> deleteTicket] Deleting ticket: {}", id);
        ResponseDto<String> response = new ResponseDto<>();
        try {
            Optional<Ticket> optionalTicket = RepositoryAccessor.getTicketRepository().findByIdAndIsActive(id, true);
            if(optionalTicket.isEmpty()){
                response.setCode(0);
                response.setMessage("ticket not found.");
                return response;
            }
            Ticket ticket = optionalTicket.get();
            ticket.setActive(false);
            RepositoryAccessor.getTicketRepository().save(ticket);
            response.setCode(1);
            response.setMessage("ticket deleted successfully");
        }catch (Exception e){
            LOGGER.error("[TicketServiceImpl >> deleteTicket] Exception occurred while deleting ticket", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }
    private TicketResponse mapToResponse(Ticket ticket){
        return TicketResponse.builder()
                .id(ticket.getId())
                .candidateId(ticket.getCandidate().getId())
                .candidateName(ticket.getCandidate().getName())
                .userId(ticket.getUser().getId())
                .assignedTo(ticket.getUser().getName())
                .status(ticket.getStatus())
                .remarks(ticket.getRemarks())
                .nextFollowUpDate(ticket.getNextFollowUpDate().toString())
                .ticketId(ticket.getTicketId())
                .build();
    }
}
