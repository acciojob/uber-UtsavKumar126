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
		List<Driver> drivers = driverRepository2.findAll();
		Driver driver = null;

		for(Driver driver1:drivers){
			if(driver1.getCab().getAvailable()){
				if(driver == null || driver1.getDriverId() < driver.getDriverId()) driver = driver1;
			}
		}

		if(driver == null) throw new Exception("No cab available!");

		Customer customer = customerRepository2.findById(customerId).get();


		TripBooking tripBooking = new TripBooking();
		tripBooking.setCustomer(customer);
		tripBooking.setDistanceInKm(distanceInKm);
		tripBooking.setFromLocation(fromLocation);
		tripBooking.setToLocation(toLocation);
		tripBooking.setStatus(TripStatus.CONFIRMED);
		tripBooking.setDriver(driver);
		driver.getCab().setAvailable(false);

		//List<TripBooking> driverTrips =
		customer.getTripBookingList().add(tripBooking);
		driver.getTripBookingList().add(tripBooking);

		customerRepository2.save(customer);
		driverRepository2.save(driver);

		return  tripBooking;

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

		Cab cab=tripBooking.getDriver().getCab();
		tripBooking.setStatus(TripStatus.COMPLETED);
		tripBooking.setBill((cab.getPerKmRate())*tripBooking.getDistanceInKm());
		cab.setAvailable(true);

		cabRepository.save(cab);
		tripBookingRepository2.save(tripBooking);
	}
}
