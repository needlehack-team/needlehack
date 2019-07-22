package org.needlehack.collector.infrastructure.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchScheduler {

    private final static Logger log = LoggerFactory.getLogger(BatchScheduler.class);


//    @Autowired
    private SimpleJobLauncher jobLauncher;

//    @Autowired
    private Job job;

    @Scheduled(cron = "${feed-collector.cron}")
    public void perform() throws JobExecutionException {

        log.info("Job [{}] Started", job.getName());

        final JobParameters param = new JobParametersBuilder().addLong("JobID", System.nanoTime())
                .toJobParameters();

        final JobExecution execution = jobLauncher.run(job, param);

        log.info("Job [{}] finished with status : {}", job.getName(), execution.getStatus());
    }

}
