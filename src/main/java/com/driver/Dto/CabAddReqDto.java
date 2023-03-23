package com.driver.Dto;

public class CabAddReqDto {

    private int perKmRate;
    private Boolean isAvailable;
    private int driverId;

    public CabAddReqDto() {
    }

    public CabAddReqDto(int perKmRate, Boolean isAvailable, int driverId) {
        this.perKmRate = perKmRate;
        this.isAvailable = isAvailable;
        this.driverId = driverId;
    }

    public int getPerKmRate() {
        return perKmRate;
    }

    public void setPerKmRate(int perKmRate) {
        this.perKmRate = perKmRate;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }
}
