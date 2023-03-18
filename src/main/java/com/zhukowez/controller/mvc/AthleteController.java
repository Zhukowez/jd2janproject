package com.zhukowez.controller.mvc;

import com.zhukowez.controller.requests.SearchCriteria;
import com.zhukowez.domain.Athlete;
import com.zhukowez.service.AthleteService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@RequestMapping("/athlete")
public class AthleteController {

    private static final Logger log = Logger.getLogger(AthleteController.class);

    private final AthleteService athleteService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView findAllAthletes() {

        List<Athlete> athletes = athleteService.findAll();

        String collect = athletes.stream().map(Athlete::getName).collect(Collectors.joining(","));

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("athleteName", collect);
        modelAndView.addObject("athletes", athletes);

        modelAndView.setViewName("hello");

        return modelAndView;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView findAthleteById(@PathVariable String id) {

        Long parsedAthleteId;

        try {
            parsedAthleteId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            log.error("Athlete id: " + id + " cannot be parsed to Long", e);
            parsedAthleteId = 1L;
        }

        Athlete athlete = athleteService.findOne(parsedAthleteId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("athleteName", athlete.getName());
        modelAndView.addObject("athletes", Collections.singletonList(athlete));

        modelAndView.setViewName("hello");

        return modelAndView;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView searchAthleteByParam(@Valid @ModelAttribute SearchCriteria criteria, BindingResult result) {

        System.out.println(result);

        Double parsedWeight;

        try {
            parsedWeight = Double.parseDouble(criteria.getWeight());
        } catch (NumberFormatException e) {
            log.error("Athlete param weight: " + criteria.getWeight() + " cannot be parsed to Double", e);
            parsedWeight = 50D;
        }

        List<Athlete> searchList = athleteService.search(criteria.getQuery(), parsedWeight);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("athleteName", "Search Result");
        modelAndView.addObject("athletes", searchList);

        modelAndView.setViewName("hello");

        return modelAndView;
    }

}
