package com.zhukowez.controller.mvc;

import com.zhukowez.controller.requests.AthleteCreateRequest;
import com.zhukowez.domain.Athlete;
import com.zhukowez.service.AthleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/athletes")
@RequiredArgsConstructor
public class AthleteRestController {

    private final AthleteService athleteService;

    @GetMapping
    public ResponseEntity<Object> getAllAthletes() {
        List<Athlete> athletes = athleteService.findAll();
        return new ResponseEntity<>(athletes, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Athlete> createUser(@RequestBody AthleteCreateRequest request) {

        Athlete build = Athlete.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .birthDate(request.getBirthDate())
                .height(request.getHeight())
                .weight(request.getWeight())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .build();

        Athlete createdUser = athleteService.create(build);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }


}
