package com.example.demo.services;

import com.example.demo.dtos.ContestDto;
import com.example.demo.mappers.ContestMapperImpl;
import com.example.demo.models.Contest;
import com.example.demo.models.Customer;
import com.example.demo.repositories.ContestRepository;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.utils.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ContestService {
    private static final Logger logger = LoggerFactory.getLogger(ContestService.class);
    private final ContestRepository contestRepository;
    private final CustomerRepository customerRepository;
    private final ContestMapperImpl contestMapperImpl = new ContestMapperImpl();

    public ContestService(ContestRepository contestRepository, CustomerRepository customerRepository) {
        this.contestRepository = contestRepository;
        this.customerRepository = customerRepository;
    }

    public Optional<ContestDto> createContest(ContestDto contestDto){
        try {
            logger.debug("start saving contest {}", contestDto);

            if(!Validator.isValidUUID(contestDto.getOwner())){
                logger.debug("contest owner {} don't exist", contestDto.getOwner());
                return Optional.empty();
            }

            Optional<Customer> customerOpt = customerRepository.findById(UUID.fromString(contestDto.getOwner()));

            if(customerOpt.isPresent()){
                Contest contestToSave = Contest.builder()
                        .name(contestDto.getName())
                        .owner(customerOpt.get())
                        .durationInSeconds(contestDto.getDurationInSeconds())
                        .startMessage(contestDto.getStartMessage())
                        .endMessage(contestDto.getEndMessage())
                        .isStarted(contestDto.getIsStarted())
                        .isStopped(contestDto.getIsStopped())
                        .build();
                Contest contestSaved = contestRepository.save(contestToSave);

                logger.debug("contest saved");
                return Optional.of(contestMapperImpl.convertToDto(contestSaved));
            }else{
                logger.debug("contest owner {} don't exist", contestDto.getOwner());
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Failed to save contest");
            throw new RuntimeException(e.getMessage());
        }
    }


}
