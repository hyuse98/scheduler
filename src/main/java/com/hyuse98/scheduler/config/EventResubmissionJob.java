package com.hyuse98.scheduler.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.IncompleteEventPublications;
import org.springframework.modulith.events.ResubmissionOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EventResubmissionJob {

    private static final Logger LOG = LoggerFactory.getLogger(EventResubmissionJob.class);

    private final IncompleteEventPublications incompleteEvents;

    public EventResubmissionJob(IncompleteEventPublications incompleteEvents) {
        this.incompleteEvents = incompleteEvents;
    }

    @Scheduled(fixedRate = 60000)
    public void resubmitFailedEvents() {
        LOG.info("Searching for failed outbox events...");

        incompleteEvents.resubmitIncompletePublications(ResubmissionOptions.defaults());

        LOG.info("Searching Concluded!");
    }
}
