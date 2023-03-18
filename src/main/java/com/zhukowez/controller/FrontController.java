package com.zhukowez.controller;

import com.zhukowez.domain.Athlete;
import com.zhukowez.service.AthleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class FrontController {

    private final AthleteService athleteService;

    @Autowired
    public FrontController(AthleteService athleteService) {
        this.athleteService = athleteService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String doRequest(Model model) {
        List<Athlete> athletes = athleteService.findAll();
        String collect = athletes.stream().map(Athlete::getName).collect(Collectors.joining(","));
        model.addAttribute("athleteName", collect);
        model.addAttribute("athletes", athletes);
        return "hello";
    }
}


/*@Controller
public class FrontController extends HttpServlet {

    private final AthleteService athleteService;

    @Autowired
    public FrontController(AthleteService athleteService) {
        this.athleteService = athleteService;
    }

    protected void doRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/hello");
        if (dispatcher != null) {
            System.out.println("Forward will be done!");
            System.out.println("We are processing user request");

            List<Athlete> athletes = athleteService.findAll();

            String collect = athletes.stream().map(Athlete::getName).collect(Collectors.joining(","));

            req.setAttribute("athleteName", collect);
            req.setAttribute("athletes", athletes);

            dispatcher.forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doRequest(req, resp);
    }


}*/
