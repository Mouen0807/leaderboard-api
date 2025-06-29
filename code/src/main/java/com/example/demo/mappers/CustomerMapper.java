package com.example.demo.mappers;

import com.example.demo.dtos.CustomerDto;
import com.example.demo.models.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CustomerDetailsMapper.class, MapperHelper.class})
public interface CustomerMapper {
    @Mapping(source = "role", target = "role", qualifiedByName = "mapRoleToString")
    @Mapping(source = "id", target = "id", qualifiedByName = "uuidToString")
    public CustomerDto convertToDto(Customer customer);

    @Mapping(target = "role", ignore = true)
    @Mapping(source = "id", target = "id", qualifiedByName = "stringToUuid")
    public Customer convertToEntity(CustomerDto customerDto);


}
