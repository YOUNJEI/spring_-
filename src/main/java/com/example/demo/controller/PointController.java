package com.example.demo.controller;

import com.example.demo.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointController {
    @Autowired
    private PointService pointService;

    @GetMapping("/create/{name}")
    public Long createPoint(@PathVariable("name") String name) {
        return pointService.createPoint(name);
    }
}
