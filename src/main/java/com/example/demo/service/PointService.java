package com.example.demo.service;

import com.example.demo.model.Point;
import com.example.demo.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {
    @Autowired
    private PointRepository pointRepository;

    @Transactional
    public Long createPoint(String name) {
        Point point = pointRepository.save(Point.builder()
                .name(name)
                .point(100L).build());
        return point.getId();
    }

    @Transactional
    public void updatePoint(Long value) {
        Point point = pointRepository.findById(1L).orElse(null);
        point.update(value);
    }

    @Transactional
    public Long getPoint() {
        Point point = pointRepository.findById(1L).orElse(null);
        return point.getPoint();
    }

    @Transactional
    public void init() {
        Point point = pointRepository.findById(1L).orElse(null);
        point.update(100L);
    }
}
