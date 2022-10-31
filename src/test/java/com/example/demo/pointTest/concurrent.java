package com.example.demo.pointTest;

import com.example.demo.service.PointConcurrentService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class concurrent {
    @Autowired
    private PointConcurrentService pointConcurrentService;

    @BeforeEach
    public void init() {
        pointConcurrentService.init();
    }

    @Test
    public void 동시에_100개_요청() throws Exception {
        final String key = "test";

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    Long point = pointConcurrentService.getPoint(key);
                    pointConcurrentService.updatePoint(point - 10);
                    pointConcurrentService.free(key);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        Long p = pointConcurrentService.getPoint(key);
        Assertions.assertThat(p).isEqualTo(0L);
    }


    @Test
    public void 동시에_100개_요청_update() throws Exception {
        final String key = "test";

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    pointConcurrentService.update(key, 10L);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        Long p = pointConcurrentService.getPoint(key);
        Assertions.assertThat(p).isEqualTo(0L);
    }
}
