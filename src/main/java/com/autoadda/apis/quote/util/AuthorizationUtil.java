package com.autoadda.apis.quote.util;

import com.autoadda.apis.quote.exception.UnauthorizedAccessException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public final class AuthorizationUtil {

    private static final Set<String> VENDOR_ROLES = Set.of(
            "COMPANY_OWNER",
            "GARAGE_OWNER",
            "SPARE_PART_OWNER",
            "OTHER_SERVICE_OWNER"
    );

    private AuthorizationUtil() {
    }

    /** True when the roles header contains ADMIN, in any of the accepted formats. */
    public static boolean isAdmin(String userRoles) {
        return hasRole(userRoles, "ADMIN");
    }

    public static void requireAdmin(String userRoles) {
        if (!hasRole(userRoles, "ADMIN")) {
            throw new UnauthorizedAccessException("Admin access required");
        }
    }

    public static void requireVendor(String userRoles) {
        if (!hasRole(userRoles, "ADMIN") && !hasAnyVendorRole(userRoles)) {
            throw new UnauthorizedAccessException("Vendor access required");
        }
    }

    public static void requireAdminOrOwner(String userRoles, Integer userId, Integer resourceOwnerId) {
        if (hasRole(userRoles, "ADMIN")) {
            return;
        }
        if (userId == null || !userId.equals(resourceOwnerId)) {
            throw new UnauthorizedAccessException("Access denied");
        }
    }

    public static void requireVendorOrOwner(String userRoles, Integer userId, Integer resourceOwnerId) {
        if (hasRole(userRoles, "ADMIN")) {
            return;
        }
        if (!hasAnyVendorRole(userRoles)) {
            throw new UnauthorizedAccessException("Vendor access required");
        }
        if (userId == null || !userId.equals(resourceOwnerId)) {
            throw new UnauthorizedAccessException("Access denied to this resource");
        }
    }

    private static boolean hasRole(String userRoles, String role) {
        return parseRoles(userRoles).stream()
                .anyMatch(r -> r.equalsIgnoreCase(role));
    }

    private static boolean hasAnyVendorRole(String userRoles) {
        return parseRoles(userRoles).stream()
                .map(String::toUpperCase)
                .anyMatch(VENDOR_ROLES::contains);
    }

    private static List<String> parseRoles(String userRoles) {
        if (userRoles == null || userRoles.isBlank()) {
            return Collections.emptyList();
        }

        String trimmed = userRoles.trim();

        // Handle JSON array format: ["ROLE_ADMIN","ROLE_CUSTOMER"]
        if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
            // Remove brackets and quotes, split by comma
            String content = trimmed.substring(1, trimmed.length() - 1);
            if (content.isBlank()) {
                return Collections.emptyList();
            }
            return Arrays.stream(content.split(","))
                    .map(String::trim)
                    .map(r -> r.replace("\"", ""))  // Remove quotes
                    .map(AuthorizationUtil::stripRolePrefix)
                    .toList();
        }

        // Fallback: comma-separated format for backwards compatibility
        return Arrays.stream(userRoles.split(","))
                .map(String::trim)
                .map(AuthorizationUtil::stripRolePrefix)
                .toList();
    }

    private static String stripRolePrefix(String role) {
        if (role.toUpperCase().startsWith("ROLE_")) {
            return role.substring(5);
        }
        return role;
    }
}
