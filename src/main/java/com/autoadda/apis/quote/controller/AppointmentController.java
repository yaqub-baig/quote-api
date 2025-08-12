package com.autoadda.apis.quote.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.autoadda.apis.quote.entity.AppointmentDetails;
import com.autoadda.apis.quote.exception.GarageNotFoundException;
import com.autoadda.apis.quote.request.AppointmentStatusRequest;
import com.autoadda.apis.quote.request.CustomerAppointmentRequest;
import com.autoadda.apis.quote.response.AppointmentResponse;
import com.autoadda.apis.quote.response.GenericResponse;
import com.autoadda.apis.quote.service.AppointmentService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

	private final AppointmentService appointmentService;

	@PostMapping
	public ResponseEntity<GenericResponse> createCustomerAppointment(@RequestBody CustomerAppointmentRequest request) {
		AppointmentDetails appointment = appointmentService.createCustomerAppointment(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(GenericResponse.success(appointment));
	}

//	@GetMapping("/customer/{customerId}")
//	public ResponseEntity<GenericResponse> getCustomerAppointments(@PathVariable Integer customerId,
//			@RequestParam(required = false) Status status) throws CustomerNotFoundException {
//		List<AppointmentDetails> appointments = appointmentService.getAppointmentsByCustomer(customerId, status);
//		return ResponseEntity.ok(GenericResponse.success(appointments));
//	}

	@GetMapping("/garage/{garageId}")
	public ResponseEntity<GenericResponse> getGarageAppointments(@PathVariable Integer garageId,
			@RequestParam(required = false) String status) throws GarageNotFoundException {
		List<AppointmentDetails> appointments = appointmentService.getAppointmentsByGarage(garageId, status);
		return ResponseEntity.ok(GenericResponse.success(appointments));
	}

	@PutMapping("/status")
	public ResponseEntity<GenericResponse> updateAppointmentStatus(@RequestBody AppointmentStatusRequest request) {
		AppointmentDetails updated = appointmentService.updateAppointmentStatus(request);
		return ResponseEntity.ok(GenericResponse.success("Appointment status updated"));
	}

	@GetMapping("/{appointmentId}")
	public ResponseEntity<GenericResponse> getAppointmentDetails(@PathVariable Integer appointmentId) {
		AppointmentResponse response = appointmentService.getAppointmentsDetails(appointmentId);
		return ResponseEntity.ok(GenericResponse.success(response));
	}
}
