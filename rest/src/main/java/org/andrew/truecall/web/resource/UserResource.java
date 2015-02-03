package org.andrew.truecall.web.resource;

import com.google.common.collect.Lists;
import io.dropwizard.jersey.params.LongParam;
import org.andrew.truecall.dao.UserStatisticsBatchDao;
import org.andrew.truecall.representation.User;
import org.andrew.truecall.representation.UserView;
import org.andrew.truecall.representation.UserViewStatistics;
import org.andrew.truecall.service.UserService;
import org.joda.time.DateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserService userService;

    /**
     * This dao is used just for testing purposes to ensure dumped data is safe and cool
     */
    private final UserStatisticsBatchDao dump;

    public UserResource(UserService userService, UserStatisticsBatchDao dump) {
        this.userService = userService;
        this.dump = dump;
    }

    @GET
    @Path("{requestedUserId}")
    public User readUser(@QueryParam("userId") LongParam currentUserId,
                            @PathParam("requestedUserId") LongParam userToViewId) {

        return userService.getUser(userToViewId.get(), currentUserId.get());
    }

    @GET
    @Path("{requestedUserId}/read-statistics")
    public UserViewStatistics getStatistics(@QueryParam("userId") LongParam currentUserId,
                                   @PathParam("requestedUserId") LongParam userToViewId) {

        return userService.getViewStatistics(userToViewId.get());
    }


    /**
     * Just a test method to read dump data
     * @return all dump content - assuming you're using this app just for play
     */
    @GET
    @Path("/read-statistics/dump")
    public UserViewStatistics getDump() {
        Iterable<UserView> userViews = dump.readTail(DateTime.now());
        return new UserViewStatistics(null, Lists.newArrayList(userViews));
    }


}
