package com.zhukowez.service;

import com.zhukowez.domain.Athlete;
import com.zhukowez.repository.AthleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;

import java.util.List;

@Service
public class AthleteServiceImpl implements AthleteService {

    private final AthleteRepository athleteRepository;

    @Autowired
    public AthleteServiceImpl(AthleteRepository athleteRepository) {
        this.athleteRepository = athleteRepository;
    }

    public void searchAthlete(String name, String surname) {
        List<Athlete> athletes = athleteRepository.findAthletesByNameAndSurname(name, surname);
        if (athletes.isEmpty()) {
            System.out.println("Не найдено атлетов с именем '" + name + "' и фамилией '" + surname + "'.");
        } else {
            System.out.println("Найдены атлеты с именем '" + name + "' и фамилией '" + surname + "':");
            for (Athlete a : athletes) {
                System.out.println("ID: " + a.getId() + ", Имя: " + a.getName() + ", Фамилия: " + a.getSurname() + ", Вес: " + a.getWeight());
            }
        }
    }

    @Override
    public Athlete findById(Long id) {
        return athleteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Athlete not found with id: " + id));
    }


    @Override
    public Athlete findOne(Long id) {
        return null;
    }

    @Override
    public List<Athlete> findAll() {
        return athleteRepository.findAll();
    }

    @Override
    public Athlete create(Athlete object) {
        if (object.getWeight() > 90) {
            throw new RuntimeException("Something wrong!");
        }

        return athleteRepository.create(object);
    }

    @Override
    public Athlete update(Athlete object) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
