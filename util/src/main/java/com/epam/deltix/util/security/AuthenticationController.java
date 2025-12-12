package com.epam.deltix.util.security;

import java.security.Principal;

/**
 * Authentication Controller Service
 */
public interface AuthenticationController {

    /**
     * Performs authentication for the given principal
     * @param name principal name
     * @param pass principal password
     * @return Principal object if authentication succeed. Otherwise null.
     */
    Principal   authenticate(String name, String pass);
}