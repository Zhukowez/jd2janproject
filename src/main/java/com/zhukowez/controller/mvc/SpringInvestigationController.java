package com.zhukowez.controller.mvc;

import com.zhukowez.domain.Athlete;
import com.zhukowez.repository.AthleteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/spring")
@RequiredArgsConstructor
public class SpringInvestigationController {

    private final List<AthleteRepository> repositoryList;


    @GetMapping("/info")
    public void getSpringInfo() {

        for (AthleteRepository athleteRepository : repositoryList) {
            if (athleteRepository.support("jdbc")) {
                for (Athlete athlete : athleteRepository.findAll()) {
                    System.out.println(athlete);
                }
            }
        }

    }
}
