package com.root.controller;

import com.root.resource.TheaterResource;
import com.root.service.TheaterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/theater")
@Slf4j
public class TheaterController {
    @Autowired
    private TheaterService theaterService;

    @PostMapping("/add")
    public ResponseEntity<TheaterResource> addTheater(@RequestBody TheaterResource theaterResource) {
        return ResponseEntity.ok(theaterService.addTheater(theaterResource));
    }


    @GetMapping("/{id}")
    public ResponseEntity<TheaterResource> getTheater(@PathVariable(name = "id") @Min(value = 1, message = "Theater Id Cannot be -ve") Long id) {
        return ResponseEntity.ok(theaterService.getTheater(id));
    }
}
