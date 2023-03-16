package com.zhukowez;

import com.zhukowez.aspect.MethodInvocationCounterAspect;
import com.zhukowez.configuration.ApplicationConfig;
import com.zhukowez.domain.Athlete;
import com.zhukowez.repository.AthleteRepository;
import com.zhukowez.service.AthleteServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Timestamp;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        AthleteRepository athleteRepository = context.getBean(AthleteRepository.class);

        // Найти атлета с идентификатором
        Long athleteId = 3L;
        Athlete athlete = athleteRepository.findOne(athleteId);


        // Вывести информацию об атлете
        if (athlete != null) {
            System.out.println("ID: " + athlete.getId() + ", Имя: " + athlete.getName() + ", Фамилия: " + athlete.getSurname());
        } else {
            System.out.println("Атлет с ID " + athleteId + " не найден.");
        }

        // Вывести информацию о всех атлетах
        List<Athlete> athletes = athleteRepository.findAll();
        for (Athlete a : athletes) {
            System.out.println("ID: " + a.getId() + ", Имя: " + a.getName() + ", Фамилия: " + a.getSurname());
        }

        Athlete foundAthlete = athleteRepository.findById(athleteId); // Замените 1L на ID атлета, которого вы хотите найти
        System.out.println("ID: " + foundAthlete.getId() +
                ", Имя: " + foundAthlete.getName() + ", Фамилия: " + foundAthlete.getSurname());


        Athlete newAthlete = Athlete.builder()
                .name("Имя")
                .surname("Фамилия")
                .birthDate(new Timestamp(System.currentTimeMillis()))
                .height(170.0)
                .weight(70.0)
                .email("example@example.com")
                .phoneNumber("+123456789")
                .created(new Timestamp(System.currentTimeMillis()))
                .changed(new Timestamp(System.currentTimeMillis()))
                .deleted(false)
                .roleID(1L)
                .build();

        Athlete createdAthlete = athleteRepository.create(newAthlete);
        System.out.println("Создан атлет с ID: " + createdAthlete.getId());


        Athlete athleteToUpdate = athleteRepository.findOne(4L); // Замените athleteId на ID атлета, которого вы хотите обновить
        if (athleteToUpdate != null) {
            athleteToUpdate.setName("Новое имя");
            athleteToUpdate.setSurname("Новая фамилия");

            Athlete updatedAthlete = athleteRepository.update(athleteToUpdate);
            System.out.println("Атлет обновлен: ID: " + updatedAthlete.getId() + ", Имя: " + updatedAthlete.getName() + ", Фамилия: " + updatedAthlete.getSurname());
        } else {
            System.out.println("Атлет с ID " + athleteId + " не найден.");
        }

        Long athleteIdToDelete = 12L;
        athleteRepository.delete(athleteIdToDelete);
        System.out.println("Атлет с ID " + athleteIdToDelete + " удален.");

        double desiredHeight = 180.0;
        List<Athlete> athletesByHeight = athleteRepository.findAllAthletesByHeight(desiredHeight);
        for (Athlete a : athletesByHeight) {
            System.out.println("ID: " + a.getId() + ", Имя: " + a.getName() + ", Фамилия: " + a.getSurname() + ", Рост: " + a.getHeight());
        }

        double desiredWeight = 80.0;
        List<Athlete> athletesByWeight = athleteRepository.findAllAthletesByWeight(desiredWeight);
        for (Athlete a : athletesByWeight) {
            System.out.println("ID: " + a.getId() + ", Имя: " + a.getName() + ", Фамилия: " + a.getSurname() + ", Вес: " + a.getWeight());
        }


        AthleteServiceImpl athleteNameSurname = new AthleteServiceImpl(athleteRepository);
        String nameToSearch = "Имя";
        String surnameToSearch = "Фамилия";
        athleteNameSurname.searchAthlete(nameToSearch, surnameToSearch);


        MethodInvocationCounterAspect methodInvocationCounterAspect = context.getBean(MethodInvocationCounterAspect.class);


        // Call printMethodInvocationCounts() to print the method invocation counts
        methodInvocationCounterAspect.printMethodInvocationCounts();
    }
}
