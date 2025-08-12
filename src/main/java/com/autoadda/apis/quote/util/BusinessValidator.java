package com.autoadda.apis.quote.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autoadda.apis.quote.entity.AuthenticationDetails;
import com.autoadda.apis.quote.entity.Customer;
import com.autoadda.apis.quote.entity.Garage;
import com.autoadda.apis.quote.exception.CustomerNotFoundException;
import com.autoadda.apis.quote.exception.GarageNotFoundException;
import com.autoadda.apis.quote.exception.MemberAlreadyExistException;
import com.autoadda.apis.quote.exception.MemberNotFoundException;
import com.autoadda.apis.quote.repository.AuthenticationDetailsRepository;
import com.autoadda.apis.quote.repository.CustomerRepository;
import com.autoadda.apis.quote.repository.GarageRepository;
import com.autoadda.apis.quote.repository.TechnicianRepository;

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