package com.zhukowez.controller;

import com.zhukowez.configuration.DatabaseProperties;
import com.zhukowez.domain.Athlete;
import com.zhukowez.repository.impl.AthleteRepositoryImpl;
import com.zhukowez.service.AthleteService;
import com.zhukowez.service.AthleteServiceImpl;
import com.zhukowez.util.RandomValuesGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class FrontController extends HttpServlet {

    @Autowired
    private final AthleteService athleteService = new AthleteServiceImpl(new AthleteRepositoryImpl(new DatabaseProperties()), new RandomValuesGenerator());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doRequest(req, resp);
    }

    private void doRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/hello");
        if (dispatcher != null) {
            System.out.println("Forward will be done!");
            System.out.println("We are processing user request");

            List<Athlete> athletes = athleteService.findAll();

            String collect = athletes.stream().map(Athlete::getName).collect(Collectors.joining(","));

            req.setAttribute("userName", collect);
            req.setAttribute("users", athletes);

            dispatcher.forward(req, resp);
        }
    }
}
