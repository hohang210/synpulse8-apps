package com.oliver.apiGateway.service;

import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.tenancy.domain.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface UserService {
    /**
     * Creates a new user and associated the user with some basic roles,
     * such as creating account and so on.
     * <p>
     * Will return null if current username is taken.
     *
     * @param username {String} An user's username passed from frontend.
     * @param password {String} An encrypted password passed from frontend.
     *
     * @return {User} Returns a newly created `User` object.
     *
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    User createUserWithBasicRole(
            String username,
            String password
    ) throws ValidationException, ConflictException;
}
