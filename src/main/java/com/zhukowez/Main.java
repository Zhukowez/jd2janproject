package com.zhukowez;

import com.zhukowez.aspect.MethodInvocationCounterAspect;
import com.zhukowez.configuration.ApplicationConfig;
import com.zhukowez.configuration.DatabaseProperties;
import com.zhukowez.domain.Athlete;
import com.zhukowez.repository.AthleteRepository;
import com.zhukowez.repository.impl.AthleteRepositoryImpl;
import com.zhukowez.repository.rowmapper.AthleteRowMapper;
import com.zhukowez.service.AthleteAggregationService;
import com.zhukowez.service.AthleteService;
import com.zhukowez.service.AthleteServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class Main {

    public static void main(String[] args) {


        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        AthleteRepository athleteRepository = context.getBean(AthleteRepository.class);
        AthleteService athleteService = context.getBean(AthleteService.class);
        AthleteAggregationService athleteAggregationService = context.getBean(AthleteAggregationService.class);

        // Создание атлета
        Athlete newAthlete = Athlete.builder()
                .name("Ivan")
                .surname("Ivanov")
                .birthDate(new Timestamp(System.currentTimeMillis()))
                .height(175.0)
                .weight(75.0)
                .email("Ivan@example.com")
                .phoneNumber("+375294685524")
                .created(new Timestamp(System.currentTimeMillis()))
                .changed(new Timestamp(System.currentTimeMillis()))
                .deleted(false)
                .roleID(1L)
                .build();

        Athlete createdAthlete = athleteRepository.create(newAthlete);
        System.out.println("Создан атлет с ID: " + createdAthlete.getId());

        // Чтение атлета
        Long athleteId = createdAthlete.getId();
        Athlete athlete = athleteRepository.findOne(athleteId);

        if (athlete != null) {
            System.out.println("ID: " + athlete.getId() + ", Имя: " + athlete.getName() + ", Фамилия: " + athlete.getSurname());
        } else {
            System.out.println("Атлет с ID " + athleteId + " не найден.");
        }

        // Обновление атлета
        Athlete athleteToUpdate = athleteRepository.findOne(athleteId);
        if (athleteToUpdate != null) {
            athleteToUpdate.setName("Новое имя");
            athleteToUpdate.setSurname("Новая фамилия");

            Athlete updatedAthlete = athleteRepository.update(athleteToUpdate);
            System.out.println("Атлет обновлен: ID: " + updatedAthlete.getId() + ", Имя: " + updatedAthlete.getName() + ", Фамилия: " + updatedAthlete.getSurname());
        } else {
            System.out.println("Атлет с ID " + athleteId + " не найден.");
        }

        // Удаление атлета
        athleteRepository.delete(athleteId);
        System.out.println("Атлет с ID " + athleteId + " удален.");


        List<Athlete> all = athleteRepository.findAll();

        for (Athlete user : all) {
            System.out.println(user);
        }


        int age = 25; // Замените на желаемый возраст
        List<Athlete> athletes = athleteRepository.findAthletesOlderThan(age);
        athletes.forEach(System.out::println);

        Athlete createdAthleteTestForUpdateEmail = athleteRepository.create(newAthlete);
        System.out.println("//////////////////////////////////////////////");
        System.out.println("Создание нового атлета для проверки процедуры обновления e-mail");
        System.out.println("Создан атлет с ID: " + createdAthleteTestForUpdateEmail.getId());

        // Обновление адреса электронной почты с помощью хранимой процедуры
        athleteRepository.updateEmail(Math.toIntExact(createdAthleteTestForUpdateEmail.getId()), "ivan_updated@example.com");

        // Получение атлета с обновленным адресом электронной почты
        Optional<Athlete> updatedAthlete = athleteRepository.findById(createdAthleteTestForUpdateEmail.getId());
        System.out.println("Updated athlete: " + updatedAthlete);
        System.out.println("//////////////////////////////////////////////");

        // Проверка метода searchAthlete
        athleteService.searchAthlete("Имя", "Фамилия");

        // Проверка метода getStats из AthleteAggregationService
        Map<String, Object> stats = athleteAggregationService.getStats();
        System.out.println("Статистика атлетов:");
        System.out.println("Все атлеты: " + stats.get("allAthletes"));
        System.out.println("Один атлет: " + stats.get("oneAthlete"));

    }
}
