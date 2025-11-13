package com.autoadda.apis.quote.clients;

import com.autoadda.apis.quote.response.VehicleDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "vehicle-api", url = "https://vehicle-api-zq5p.onrender.com/api/v1")
public interface VehicleClient {
    @GetMapping("/vehicles/isc/{id}")
    public VehicleDetailResponse getVehicleDetailsById(@PathVariable Integer id);

}