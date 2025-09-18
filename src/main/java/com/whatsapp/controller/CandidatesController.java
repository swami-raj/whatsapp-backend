package com.whatsapp.controller;

import com.whatsapp.dto.request.CandidatesRequest;
import com.whatsapp.dto.request.CountryRequest;
import com.whatsapp.dto.response.CandidatesResponse;
import com.whatsapp.dto.response.CountryResponse;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.repository.ServiceAccessor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("candidates")
public class CandidatesController {
    @PostMapping("add")
    ResponseDto<CandidatesResponse> addCandidate(@RequestBody CandidatesRequest candidatesRequest) {
        return ServiceAccessor.getCandidatesService().addCandidate(candidatesRequest );
    }

    @GetMapping("get-by-id/{id}")
    ResponseDto<CandidatesResponse> getById(@PathVariable Long id) {
        return ServiceAccessor.getCandidatesService().getCandidateById(id);
    }

    @GetMapping("get-all")
    ResponseDto<List<CandidatesResponse>> getAll() {
        return ServiceAccessor.getCandidatesService().getAllCandidate();
    }

    @PutMapping("update")
    ResponseDto<CandidatesResponse> updateCandidate(@RequestBody CandidatesRequest candidatesRequest) {
        return ServiceAccessor.getCandidatesService().updateCandidate(candidatesRequest);
    }

    @DeleteMapping("delete/{id}")
    ResponseDto<String> deleteDepartment(@PathVariable Long id) {
        return ServiceAccessor.getCandidatesService().deleteCandidate(id);
    }

    @PostMapping("/upload-single")
    public ResponseDto<String> uploadDocument(@RequestParam("file") MultipartFile file, @RequestParam Long candidateId) {
        return ServiceAccessor.getCandidatesService().uploadDocument(file, candidateId);
    }

}
