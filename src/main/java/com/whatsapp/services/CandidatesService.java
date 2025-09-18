package com.whatsapp.services;

import com.whatsapp.dto.request.CandidatesRequest;
import com.whatsapp.dto.response.CandidatesResponse;
import com.whatsapp.dto.response.ResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CandidatesService {
    ResponseDto<CandidatesResponse> addCandidate(CandidatesRequest candidatesRequest);

    ResponseDto<CandidatesResponse> getCandidateById(Long id);

    ResponseDto<List<CandidatesResponse>> getAllCandidate();

    ResponseDto<CandidatesResponse> updateCandidate(CandidatesRequest candidatesRequest);

    ResponseDto<String> deleteCandidate(Long id);


    ResponseDto<String> uploadDocument(MultipartFile file, Long candidateId);
}
