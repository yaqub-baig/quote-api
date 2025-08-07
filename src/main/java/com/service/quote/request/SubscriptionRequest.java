package com.service.quote.request;

import lombok.Data;

@Data
public class SubscriptionRequest {
    private Integer ownerId;
    private Integer planId;
}
