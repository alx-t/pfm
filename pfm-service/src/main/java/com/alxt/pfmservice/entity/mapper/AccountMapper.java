package com.alxt.pfmservice.entity.mapper;

import com.alxt.pfmservice.entity.Account;
import com.alxt.pfmservice.entity.dto.AccountDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AccountMapper {

    Account toEntity(AccountDTO accountDTO);
    AccountDTO toDTO(Account account);
}
