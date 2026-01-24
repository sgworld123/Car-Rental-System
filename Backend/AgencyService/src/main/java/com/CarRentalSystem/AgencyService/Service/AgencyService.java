package com.CarRentalSystem.AgencyService.Service;

import com.CarRentalSystem.AgencyService.Dto.RequestDto;
import com.CarRentalSystem.AgencyService.Dto.ResponseDto;
import com.CarRentalSystem.AgencyService.Model.Agency;
import com.CarRentalSystem.AgencyService.Repository.AgencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgencyService {
    @Autowired
    private AgencyRepository agencyRepository;

    //register agency
    public ResponseDto registerAgency(RequestDto requestDto)
    {
        Agency agency = Agency.builder()
                .name(requestDto.getName())
                .cities(requestDto.getCities())
                .email(requestDto.getEmail())
                .phone(requestDto.getPhone())
                .vehicleInfo(requestDto.getVehicleInfo())
                .password(requestDto.getPassword()).build();
        agencyRepository.save(agency);

        return ResponseDto.builder()
                .email(agency.getEmail())
                .name(agency.getName())
                .phone((agency.getPhone()))
                .build();
    }

}
