package com.CarRentalSystem.AgencyService.Controller;

import com.CarRentalSystem.AgencyService.Dto.RequestDto;
import com.CarRentalSystem.AgencyService.Dto.ResponseDto;
import com.CarRentalSystem.AgencyService.Service.AgencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agency")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private AgencyService agencyService;

    @GetMapping("/register")
    public ResponseDto registerAgency(@RequestBody RequestDto requestDto)
    {
        return agencyService.registerAgency(requestDto);
    }
}
