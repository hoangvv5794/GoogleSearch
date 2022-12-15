package com.viettel.vtcc.crawler.search.google.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ServiceResponseAnswer {
    private final String FILE_ANSWER_RANDOM = "/answer/not_answer_message.txt";
    private final List<String> listAnswer = new LinkedList<>();

    public ServiceResponseAnswer() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            try {
                List<String> newList = FileUtils.readLines(new File(FILE_ANSWER_RANDOM), "UTF-8");
                newList.forEach(element -> {
                    if (!listAnswer.contains(element)) {
                        listAnswer.add(element);
                    }
                });
                log.info("load random answer {}", listAnswer.size());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }, 0, 5, TimeUnit.MINUTES);
    }

    public String getRandomAnswer() {
        Random rand = new Random();
        return listAnswer.get(rand.nextInt(listAnswer.size()));
    }
}
