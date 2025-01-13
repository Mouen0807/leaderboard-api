package com.example.demo.mappers;

import com.example.demo.dtos.ContestDto;
import com.example.demo.models.Contest;

import java.util.List;

public interface ContestMapper {
    public ContestDto convertToDto(Contest contest);
    public List<ContestDto> convertToDto(List<Contest> contestList);
}
