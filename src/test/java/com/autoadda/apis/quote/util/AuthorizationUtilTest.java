package com.autoadda.apis.quote.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.autoadda.apis.quote.exception.UnauthorizedAccessException;

class AuthorizationUtilTest {

    @Test
    void isAdminAcceptsEveryHeaderFormat() {
        assertTrue(AuthorizationUtil.isAdmin("ADMIN"));
        assertTrue(AuthorizationUtil.isAdmin("ROLE_ADMIN"));
        assertTrue(AuthorizationUtil.isAdmin("admin"));
        // What the gateway sends for a user with two roles.
        assertTrue(AuthorizationUtil.isAdmin("ADMIN,GARAGE_OWNER"));
        assertTrue(AuthorizationUtil.isAdmin("GARAGE_OWNER, ROLE_ADMIN"));
        assertTrue(AuthorizationUtil.isAdmin("[\"ROLE_ADMIN\",\"ROLE_CUSTOMER\"]"));
    }

    @Test
    void isAdminRejectsOthers() {
        assertFalse(AuthorizationUtil.isAdmin(null));
        assertFalse(AuthorizationUtil.isAdmin(""));
        assertFalse(AuthorizationUtil.isAdmin("GARAGE_OWNER"));
        assertFalse(AuthorizationUtil.isAdmin("SUPERADMIN"));
        assertFalse(AuthorizationUtil.isAdmin("ROLE_ADMINISTRATOR"));
    }

    @Test
    void requireVendorStillWorks() {
        assertDoesNotThrow(() -> AuthorizationUtil.requireVendor("GARAGE_OWNER"));
        assertDoesNotThrow(() -> AuthorizationUtil.requireVendor("ADMIN,GARAGE_OWNER"));
        assertThrows(UnauthorizedAccessException.class, () -> AuthorizationUtil.requireVendor("CUSTOMER"));
    }
}
