package com.autoadda.apis.quote.clients;

import com.autoadda.apis.quote.response.GarageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "garage-api", url = "http://localhost:8020/api/v1")
public interface GarageClient {


    @GetMapping("/garages/{id}")
    public ResponseEntity<GarageResponse> getGarageById(@PathVariable Integer id);
}
