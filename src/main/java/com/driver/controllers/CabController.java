package com.driver.controllers;

import com.driver.Dto.CabAddReqDto;
import com.driver.model.Driver;
import com.driver.services.impl.CabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cab")
public class CabController {
    @Autowired
    CabService cabService;
    @PostMapping("/add")
    public String addCab(@RequestBody CabAddReqDto cabAddReqDto){
        return cabService.addCab(cabAddReqDto);
    }
}
