package org.andrew.truecall.app.scheduled;

import org.andrew.truecall.dao.UserStatisticsBatchDao;

import java.util.List;

public class ScheduledCleaner implements Runnable {

    private final List<UserStatisticsBatchDao> shardsToManage;
    private final UserStatisticsBatchDao dump;
    private final Long criticalLimit;
    private final int delayInDays;

    public ScheduledCleaner(List<UserStatisticsBatchDao> shardsToManage, UserStatisticsBatchDao dump, Long criticalLimit, int delayInDays) {
        this.shardsToManage = shardsToManage;
        this.dump = dump;
        this.criticalLimit = criticalLimit;
        this.delayInDays = delayInDays;
    }

    @Override
    public void run() {

        for (UserStatisticsBatchDao userStatisticsBatchDao : shardsToManage) {
            new DumpJob(criticalLimit, dump, userStatisticsBatchDao, delayInDays).run();
        }
    }
}
