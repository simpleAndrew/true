package org.andrew.truecall.web.filter;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Just filter to ensure all requests have userId query param.
 */
public class UserIdRequiredFilter implements ContainerRequestFilter {

    private static final String USER_QUERY_PARAM = "userId";

    private static final String ERROR_MESSAGE = USER_QUERY_PARAM + " must be specified.";

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        String first = request.getQueryParameters().getFirst(USER_QUERY_PARAM);

        if(first == null || first.isEmpty()) {
            IllegalArgumentException cause = new IllegalArgumentException(ERROR_MESSAGE);
            throw new WebApplicationException(cause, Response.Status.BAD_REQUEST);
        }

        return request;
    }
}
