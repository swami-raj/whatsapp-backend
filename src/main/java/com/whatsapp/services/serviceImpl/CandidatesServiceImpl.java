package com.whatsapp.services.serviceImpl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.whatsapp.dto.request.CandidatesRequest;
import com.whatsapp.dto.response.CandidatesResponse;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.UserDetailResponse;
import com.whatsapp.entity.Candidates;
import com.whatsapp.entity.User;
import com.whatsapp.repository.RepositoryAccessor;
import com.whatsapp.services.CandidatesService;
import com.whatsapp.utils.CommonUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.jni.FileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CandidatesServiceImpl implements CandidatesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CandidatesServiceImpl.class);

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${bucket.name}")
    private String bucketName;

    @Value("${bucket.base.url}")
    private String bucketBaseUrl;

    @Override
    public ResponseDto<CandidatesResponse> addCandidate(CandidatesRequest candidatesRequest) {
        LOGGER.info("[CandidatesServiceImpl >> addCandidate] Adding new country: {}", candidatesRequest.getName());
        ResponseDto<CandidatesResponse> response = new ResponseDto<>();
        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> optionalUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(userDetailResponse.getId(), true);
            Candidates candidates = Candidates.builder()
                    .name(candidatesRequest.getName())
                    .email(candidatesRequest.getEmail())
                    .phone(candidatesRequest.getPhone())
                    .notes(candidatesRequest.getNotes())
                    .expectedCTC(candidatesRequest.getExpectedCTC())
                    .experience(candidatesRequest.getExperience())
                    .linkedInProfile(candidatesRequest.getLinkedInProfile())
                    .noticePeriod(candidatesRequest.getNoticePeriod())
                    .status(candidatesRequest.getStatus())
                    .currentCTC(candidatesRequest.getCurrentCTC())
                    .skills(candidatesRequest.getSkills())
                    .company(optionalUser.get().getCompany())
                    .build();
            candidates.setCreatedBy(optionalUser.get());
            RepositoryAccessor.getCandidatesRepository().save(candidates);
            response.setData(mapToResponse(candidates));
            response.setCode(1);
            response.setMessage("candidates added successfully");

        }catch (Exception e){
            LOGGER.error("[CandidatesServiceImpl >> addCandidate] Exception occurred while adding candidates", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<CandidatesResponse> getCandidateById(Long id) {
        LOGGER.info("[CandidatesServiceImpl >> getCandidateById] get candidates: {}", id);
        ResponseDto<CandidatesResponse> response = new ResponseDto<>();
        try {
            Optional<Candidates> optionalCandidates = RepositoryAccessor.getCandidatesRepository().findByIdAndIsActive(id, true);
            if (optionalCandidates.isEmpty()) {
                response.setCode(0);
                response.setMessage("candidates not found.");
                return response;
            }
            response.setData(mapToResponse(optionalCandidates.get()));
            response.setCode(1);
            response.setMessage("candidates fetched successfully");
        } catch (Exception e) {
            LOGGER.error("[CandidatesServiceImpl >> getCandidateById] Exception occurred while fetching candidates", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<List<CandidatesResponse>> getAllCandidate() {
        LOGGER.info("[CandidatesServiceImpl >> getAllCandidate] Fetching all candidates");
        ResponseDto<List<CandidatesResponse>> response = new ResponseDto<>();
        try {
            List<Candidates> candidatesList = RepositoryAccessor.getCandidatesRepository().findByIsActive(true);
            if (candidatesList.isEmpty()) {
                response.setCode(0);
                response.setMessage("candidates not found.");
                return response;
            }
            response.setData(candidatesList.stream().map(this::mapToResponse).toList());
            response.setCode(1);
            response.setMessage("candidates fetched successfully");
        } catch (Exception e) {
            LOGGER.error("[CandidatesServiceImpl >> getAllCandidate] Exception occurred while fetching candidates", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<CandidatesResponse> updateCandidate(CandidatesRequest candidatesRequest) {
        LOGGER.info("[CandidatesServiceImpl >> updateCandidate] Updating candidates: {}", candidatesRequest.getName());
        ResponseDto<CandidatesResponse> response = new ResponseDto<>();
        try {
            Optional<Candidates> optionalCandidates = RepositoryAccessor.getCandidatesRepository().findByIdAndIsActive(candidatesRequest.getId(), true);
            if (optionalCandidates.isEmpty()) {
                response.setCode(0);
                response.setMessage("candidates not found.");
                return response;
            }
            Candidates candidates = optionalCandidates.get();
            candidates.setName(candidatesRequest.getName());
            candidates.setEmail(candidatesRequest.getEmail());
            candidates.setPhone(candidatesRequest.getPhone());
            candidates.setNotes(candidatesRequest.getNotes());
            candidates.setExpectedCTC(candidatesRequest.getExpectedCTC());
            candidates.setExperience(candidatesRequest.getExperience());
            candidates.setLinkedInProfile(candidatesRequest.getLinkedInProfile());
            candidates.setNoticePeriod(candidatesRequest.getNoticePeriod());
            candidates.setStatus(candidatesRequest.getStatus());
            candidates.setCurrentCTC(candidatesRequest.getCurrentCTC());
            candidates.setSkills(candidatesRequest.getSkills());
            RepositoryAccessor.getCandidatesRepository().save(candidates);
            response.setData(mapToResponse(candidates));
            response.setCode(1);
            response.setMessage("candidates updated successfully");
        } catch (Exception e) {
            LOGGER.error("[CandidatesServiceImpl >> updateCandidate] Exception occurred while updating candidates", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<String> deleteCandidate(Long id) {
        LOGGER.info("[CandidatesServiceImpl >> deleteCandidate] Deleting candidates by id: {}", id);
        ResponseDto<String> response = new ResponseDto<>();
        try {
            Optional<Candidates> optionalCandidates = RepositoryAccessor.getCandidatesRepository().findByIdAndIsActive(id, true);
            if (optionalCandidates.isEmpty()) {
                response.setCode(0);
                response.setMessage("candidates not found.");
                return response;
            }
            Candidates candidates = optionalCandidates.get();
            candidates.setActive(false);
            RepositoryAccessor.getCandidatesRepository().save(candidates);
            response.setCode(1);
            response.setMessage("candidates deleted successfully");
        } catch (Exception e) {
            LOGGER.error("[CandidatesServiceImpl >> deleteCandidate] Exception occurred while deleting candidates", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<String> uploadDocument(MultipartFile file, Long candidateId) {
        LOGGER.info("[CandidatesServiceImpl >> uploadDocument] Uploading file: {}", file.getOriginalFilename());
        ResponseDto<String> response = new ResponseDto<>();
        UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(userDetailResponse.getId(), true);
        if (optionalUser.isEmpty()) {
            response.setCode(0);
            response.setMessage("User not found");
            return response;
        }

        if (file == null || file.isEmpty()) {
            response.setCode(0);
            response.setMessage("Please upload a valid file.");
            return response;
        }

        if (candidateId == null) {
            response.setCode(0);
            response.setMessage("Candidate ID is required.");
            return response;
        }

        Optional<Candidates> optionalCandidates = RepositoryAccessor.getCandidatesRepository().findByIdAndIsActive(candidateId, true);
        if (optionalCandidates.isEmpty()) {
            response.setCode(0);
            response.setMessage("Invalid candidate ID");
            return response;
        }

        Candidates candidate = optionalCandidates.get();

        try {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String UUIDFileName = UUID.randomUUID() + "." + extension;

            File convertedFile = CommonUtils.convertMultiPartToFile(file);
            String s3Key = "documents/" + candidateId + "/" + UUIDFileName;
            String fileUrl = bucketBaseUrl + s3Key;

            amazonS3.putObject(new PutObjectRequest(bucketName, s3Key, convertedFile).withCannedAcl(CannedAccessControlList.PublicRead));
            convertedFile.delete();

            candidate.setResumeLink(fileUrl);
            RepositoryAccessor.getCandidatesRepository().save(candidate);

            response.setData(fileUrl);
            response.setCode(1);
            response.setMessage("File uploaded and saved successfully.");

        } catch (Exception e) {
            LOGGER.error("[CandidatesServiceImpl >> uploadDocument] Error uploading file: {}", e.getMessage(), e);
            response.setCode(0);
            response.setMessage("Something went wrong. Please try again.");
        }

        return response;
    }


    private CandidatesResponse mapToResponse(Candidates candidates) {
        return CandidatesResponse.builder()
                .id(candidates.getId())
                .name(candidates.getName())
                .email(candidates.getEmail())
                .phone(candidates.getPhone())
                .notes(candidates.getNotes())
                .expectedCTC(candidates.getExpectedCTC())
                .experience(candidates.getExperience())
                .linkedInProfile(candidates.getLinkedInProfile())
                .noticePeriod(candidates.getNoticePeriod())
                .status(candidates.getStatus())
                .currentCTC(candidates.getCurrentCTC())
                .skills(candidates.getSkills())
                .resumeLink(candidates.getResumeLink())
                .build();
    }
}
