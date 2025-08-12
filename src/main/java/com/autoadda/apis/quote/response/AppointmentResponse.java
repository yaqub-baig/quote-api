package com.autoadda.apis.quote.response;

import java.util.List;

import com.autoadda.apis.quote.entity.AppointmentDetails;
import com.autoadda.apis.quote.entity.AppointmentServices;
import com.autoadda.apis.quote.entity.JobCard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppointmentResponse {
	private AppointmentDetails appointment;
	private List<AppointmentServices> services;
	private List<JobCard> jobcards;

}
