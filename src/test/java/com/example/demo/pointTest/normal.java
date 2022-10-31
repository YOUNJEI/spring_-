package com.example.demo.pointTest;

import com.example.demo.service.PointService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class normal {

    @Autowired
    private PointService pointService;

    @BeforeEach
    public void init() {
        pointService.init();
    }

    @Test
    public void 동시에_10개_요청() throws Exception {
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    Long point = pointService.getPoint();
                    pointService.updatePoint(point - 10);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        Long p = pointService.getPoint();
        Assertions.assertThat(p).isEqualTo(0L);
    }
}
