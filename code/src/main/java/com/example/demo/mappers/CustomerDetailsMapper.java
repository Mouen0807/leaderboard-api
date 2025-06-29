package com.example.demo.mappers;

import com.example.demo.dtos.CustomerDetailsDto;
import com.example.demo.models.CustomerDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { MapperHelper.class })
public interface CustomerDetailsMapper {
    @Mapping(source = "id", target = "id", qualifiedByName = "uuidToString")
    CustomerDetailsDto convertToDto(CustomerDetails customerDetails);

    @Mapping(source = "id", target = "id", qualifiedByName = "stringToUuid")
    CustomerDetails convertToEntity(CustomerDetailsDto customerDetailsDto);

    List<CustomerDetailsDto> convertToDto(List<CustomerDetails> customerDetailsList);
    List<CustomerDetails> convertToEntity(List<CustomerDetailsDto> customerDetailsDtos);
}
