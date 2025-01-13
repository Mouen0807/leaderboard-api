package com.example.demo.controllers;

import com.example.demo.dtos.ContestDto;
import com.example.demo.models.ApiResponse;
import com.example.demo.services.ContestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class ContestController {
    private static final Logger logger = LoggerFactory.getLogger(ContestController.class);
    private final ContestService ContestService;

    public ContestController(ContestService ContestService) {
        this.ContestService = ContestService;
    }

    @PostMapping("/contest/create")
    public ResponseEntity<?> createContest(@RequestBody ContestDto contestDto) {
        logger.info("Attempt to create contest with name: {} ", contestDto.getName());
        Optional<ContestDto> optContestDto  = ContestService.createContest(contestDto);

        if(!optContestDto.isPresent()){
            ApiResponse apiResponse = ApiResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .message("Owner of contest don't exist")
                    .build();

            logger.info("contest is not created");
            return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.BAD_REQUEST);
        }

        logger.info("contest is created");
        ApiResponse apiResponse = ApiResponse.builder()
                .code(HttpStatus.OK.toString())
                .message("Contest is created")
                .data(optContestDto.get())
                .build();

        return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
    }
}
