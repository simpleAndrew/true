package org.andrew.truecall.service;

import org.andrew.truecall.dao.UserDao;
import org.andrew.truecall.dao.UserStatisticsDao;
import org.andrew.truecall.representation.User;
import org.andrew.truecall.representation.UserView;
import org.andrew.truecall.representation.UserViewStatistics;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Isolates direct access to Dao and adds statistic recording on read attempts
 */
public class UserService {

    private final int readLimit;
    private final int daysTracked;
    private final UserDao userDao;
    private final UserStatisticsDao userStatisticsDao;


    public UserService(UserStatisticsDao userStatisticsDao, UserDao userDao, int readLimit, int daysTracked) {
        this.userStatisticsDao = userStatisticsDao;
        this.userDao = userDao;
        this.readLimit = readLimit;
        this.daysTracked = daysTracked;
    }

    public User getUser(Long userId, Long requesterId) {
        User user = userDao.getUser(userId);
        userStatisticsDao.addRecord(requesterId, userId, DateTime.now());
        return user;
    }

    public UserViewStatistics getViewStatistics(Long viewedUserId) {
        List<UserView> readRecords = userStatisticsDao.getReadRecords(viewedUserId, readLimit, DateTime.now().minusDays(daysTracked));
        return new UserViewStatistics(viewedUserId, readRecords);
    }
}
