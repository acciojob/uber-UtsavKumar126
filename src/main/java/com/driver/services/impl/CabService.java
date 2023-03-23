package com.driver.services.impl;

import com.driver.Dto.CabAddReqDto;
import com.driver.model.Cab;
import com.driver.model.Driver;
import com.driver.repository.CabRepository;
import com.driver.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CabService {

    @Autowired
    DriverRepository driverRepository;
    @Autowired
    CabRepository cabRepository;
    public String addCab(CabAddReqDto cabAddReqDto) {
        Driver cabdriver= driverRepository.findById(cabAddReqDto.getDriverId()).get();
        Cab cab =new Cab();
        cab.setPerKmRate(cab.getPerKmRate());
        cab.setAvailable(true);
        cab.setDriver(cabdriver);

        driverRepository.save(cabdriver);

        return "Driver added successfully";
    }
}
