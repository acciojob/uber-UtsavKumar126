package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.CabRepository;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;
	@Autowired
	CabRepository cabRepository;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		customerRepository2.deleteById(customerId);
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		Customer customer=customerRepository2.findById(customerId).get();
		List<Driver>driverList=driverRepository2.findAll();

		Driver available=null;
		for (Driver driver : driverList) {
			if (driver.getCab().getAvailable()) {
				if(available == null || driver.getDriverId() < available.getDriverId()){
					available=driver ;
				}
			}
		}
		if(available==null)throw new Exception("No cab available!");


		available.getCab().setAvailable(false);
		TripBooking trip=new TripBooking();
		trip.setFromLocation(fromLocation);
		trip.setToLocation(toLocation);
		trip.setDistanceInKm(distanceInKm);
		trip.setBill((available.getCab().getPerKmRate())*distanceInKm);
		trip.setCustomer(customer);
		trip.setDriver(available);
		available.getTripBookingList().add(trip);
		customer.getTripBookingList().add(trip);
		trip.setStatus(TripStatus.CONFIRMED);
		//Avoid using SQL query
		customerRepository2.save(customer);
		driverRepository2.save(available);

		return trip;

	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking=tripBookingRepository2.findById(tripId).get();


		tripBooking.setStatus(TripStatus.CANCELED);
		tripBooking.setBill(0);
		Cab cab=tripBooking.getDriver().getCab();
		cab.setAvailable(true);

		cabRepository.save(cab);
		tripBookingRepository2.save(tripBooking);

	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking=tripBookingRepository2.findById(tripId).get();

		tripBooking.setStatus(TripStatus.COMPLETED);
		Cab cab=tripBooking.getDriver().getCab();
		cab.setAvailable(true);

		cabRepository.save(cab);
		tripBookingRepository2.save(tripBooking);
	}
}
