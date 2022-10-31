package com.example.demo.service;

import com.example.demo.model.Point;
import com.example.demo.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class PointConcurrentService {
    @Autowired
    private PointRepository pointRepository;

    private final ReentrantLock lock = new ReentrantLock();

    private Set<String> used;
    private long timeoutMs;

    public PointConcurrentService() {
        this.used = new HashSet<String>();
        timeoutMs = 5000;
    }

    @Transactional
    public void updatePoint(Long value) {
        System.out.println("value: " + value);
        Point point = pointRepository.findById(1L).orElse(null);
        point.update(value);
    }


    public void update(String use, Long decrease) throws Exception {
            lock.lock();

            try {
                Point point = pointRepository.findById(1L).orElse(null);
                Long p = point.getPoint();
                point.update(p - 10);
                pointRepository.save(point);
            } finally {
                lock.unlock();
            }
    }

    public synchronized Long getPoint(String use) throws Exception {
        if (used.contains(use)) {
            System.out.println("사용중");
            long timeoutExpiredMs = System.currentTimeMillis() + timeoutMs;
            while (used.contains(use)) {
                long wait = timeoutExpiredMs - System.currentTimeMillis();
                if (wait <= 0) {
                    throw new TimeoutException();
                }
            }
        }
        used.add(use);
        System.out.println("상태변경");
        Point point = pointRepository.findById(1L).orElse(null);
        return point.getPoint();
    }

    @Transactional
    public void free(String use) {
        System.out.println("free");
        used.remove(use);
    }

    @Transactional
    public void init() {
        Point point = pointRepository.findById(1L).orElse(null);
        point.update(1000L);
        used.clear();
    }
}