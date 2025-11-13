package com.autoadda.apis.quote.clients;

import com.autoadda.apis.quote.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "identity-api", url = "https://identity-api-wbad.onrender.com/api/v1")
public interface AuthClient {
    @GetMapping("/auth/{id}")
    public UserResponse getUser(@PathVariable Integer id);

}