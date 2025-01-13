package com.example.demo.mappers;

import com.example.demo.dtos.ContestDto;
import com.example.demo.models.Contest;

import java.util.ArrayList;
import java.util.List;

public class ContestMapperImpl implements ContestMapper{
    @Override
    public ContestDto convertToDto(Contest contest) {
        ContestDto contestDto = ContestDto.builder()
                .id(contest.getId().toString())
                .name(contest.getName())
                .owner(contest.getOwner().getId().toString())
                .durationInSeconds(contest.getDurationInSeconds())
                .startMessage(contest.getStartMessage())
                .endMessage(contest.getEndMessage())
                .createdAt(contest.getCreatedAt().toString())
                .updatedAt(contest.getUpdatedAt().toString())
                .isStarted(contest.getIsStarted())
                .isStopped(contest.getIsStopped())
                .build();

        return contestDto;
    }

    @Override
    public List<ContestDto> convertToDto(List<Contest> contestList) {
        List<ContestDto> listDto = new ArrayList<>();

        for(Contest contest: contestList){
            ContestDto contestDto = ContestDto.builder()
                    .id(contest.getId().toString())
                    .name(contest.getName())
                    .owner(contest.getOwner().getId().toString())
                    .durationInSeconds(contest.getDurationInSeconds())
                    .startMessage(contest.getStartMessage())
                    .endMessage(contest.getEndMessage())
                    .createdAt(contest.getCreatedAt().toString())
                    .updatedAt(contest.getUpdatedAt().toString())
                    .isStarted(contest.getIsStarted())
                    .isStopped(contest.getIsStopped())
                    .build();

            listDto.add(contestDto);
        }

        return listDto;
    }
}
