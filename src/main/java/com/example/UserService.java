package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UserService {
    private static final Logger logger = LoggerFactory.getLogger("UserLogger");

    public void logUser(String user) {
        logger.debug("Process user: {}", user);
    }

}
