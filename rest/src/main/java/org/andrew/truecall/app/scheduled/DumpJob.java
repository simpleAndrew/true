package org.andrew.truecall.app.scheduled;

import org.andrew.truecall.dao.UserStatisticsBatchDao;
import org.andrew.truecall.representation.UserView;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to clean up Statistics DB when required.
 *
 * For now it's based on number of records that are out of defined date.
 * Those outdated records are then copied to Dump storage and removed from active table.
 */
public class DumpJob implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DumpJob.class);

    private final UserStatisticsBatchDao source;
    private final UserStatisticsBatchDao target;
    private final int delayInDays;
    private final Long criticalSize;

    public DumpJob(Long criticalSize, UserStatisticsBatchDao target, UserStatisticsBatchDao source, int delayInDays) {
        this.criticalSize = criticalSize;
        this.target = target;
        this.source = source;
        this.delayInDays = delayInDays;
    }

    @Override
    public void run() {

        LOGGER.info("DumpJob is starting");
        DateTime theEndDate = DateTime.now().minusDays(delayInDays);

        LOGGER.info("To clean from: {}", theEndDate);
        Long tailSize = source.measureTail(theEndDate);

        LOGGER.info("Found {} items to move...", tailSize);

        if(tailSize >= criticalSize) {

            LOGGER.info("Tail is over limit - start dumping...");

            Iterable<UserView> userViews = source.readTail(theEndDate);
            LOGGER.info("Tail is read in iterable...");

            //Here is better to start app-managed transaction

            target.insertAll(userViews);
            LOGGER.info("Tail is inserted into dump...");

            source.clearTail(theEndDate);
            LOGGER.info("Stat table is truncated.");

            //Here transaction should be closed and committed

        } else {
            LOGGER.info("No reason to clean...");
        }
    }
}
