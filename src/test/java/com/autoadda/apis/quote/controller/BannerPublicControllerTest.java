package com.autoadda.apis.quote.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class BannerPublicControllerTest {

    @Test
    void clientIpIsTheLastForwardedEntry() {
        MockHttpServletRequest r = new MockHttpServletRequest();
        r.setRemoteAddr("127.0.0.1");
        assertEquals("127.0.0.1", BannerPublicController.clientIp(r));
        r.addHeader("X-Forwarded-For", "198.51.100.1, 203.0.113.7");
        // The client wrote the first entry; Apache appended the real address last.
        assertEquals("203.0.113.7", BannerPublicController.clientIp(r));
    }
}
