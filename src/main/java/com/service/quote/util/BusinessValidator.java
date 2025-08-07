package com.service.quote.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.service.quote.entity.AuthenticationDetails;
import com.service.quote.entity.Customer;
import com.service.quote.entity.Garage;
import com.service.quote.exception.CustomerNotFoundException;
import com.service.quote.exception.GarageNotFoundException;
import com.service.quote.exception.MemberAlreadyExistException;
import com.service.quote.exception.MemberNotFoundException;
import com.service.quote.repository.AuthenticationDetailsRepository;
import com.service.quote.repository.CustomerRepository;
import com.service.quote.repository.GarageRepository;
import com.service.quote.repository.TechnicianRepository;

@Component
public class BusinessValidator {

	@Autowired
	private GarageRepository garageRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TechnicianRepository garageTechnicianRepository;

	@Autowired
	private AuthenticationDetailsRepository authenticationDetailsRepository;

	/**
	 * Validates if the given garage ID exists.
	 *
	 * @param garageId the ID of the garage to validate
	 * @throws GarageNotFoundException if the garage does not exist
	 */

	public void validateUsername(String username) throws MemberAlreadyExistException {
		AuthenticationDetails authData = authenticationDetailsRepository.findByUserName(username);
		if (null != authData) {
			throw new MemberAlreadyExistException("Username already exist");
		}
	}

	public void validateTechnician(Integer technicianId) throws MemberNotFoundException {
		
		if (!garageTechnicianRepository.findById(technicianId).isPresent()) {
			throw new MemberNotFoundException("technician not exist");
		}
	}

	public Garage validateAndGetGarage(Integer garageId) throws GarageNotFoundException {
		return garageRepository.findById(garageId).orElseThrow(() -> new GarageNotFoundException("Garage not found"));
	}

	public Customer validateAndGetCustomer(Integer customerId) throws CustomerNotFoundException {
		return customerRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
	}

}