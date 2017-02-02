package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.container.*;
import java.io.IOException;


@PreMatching
public class UserFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger logger = LoggerFactory.getLogger("UserLogger");

    @Inject
    private UserService userService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        logger.debug("some request log");
        userService.logUser(requestContext.getHeaderString("username"));
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        logger.debug("some response log");
    }
}
