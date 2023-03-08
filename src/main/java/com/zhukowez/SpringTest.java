package com.zhukowez;

import com.zhukowez.repository.AthleteRepository;
import com.zhukowez.service.AthleteService;
import com.zhukowez.util.RandomValuesGenerator;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringTest {

    private static final Logger logger = Logger.getLogger(SpringTest.class);

    public static void main(String[] args) {
//        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application-context.xml");
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.zhukowez");

        //Object bean = applicationContext.getBean();
//        AthleteRepository repository = applicationContext.getBean("athleteRepository", AthleteRepository.class);
        AthleteRepository athleteRepository = applicationContext.getBean("athleteRepositoryImpl", AthleteRepository.class);
        AthleteService athleteService = applicationContext.getBean("athleteServiceImpl", AthleteService.class);
        RandomValuesGenerator randomValuesGenerator = applicationContext.getBean("getRandomGenerator", RandomValuesGenerator.class);

        logger.info(athleteRepository.findOne(1L));
        logger.info(athleteService.findAll());
        logger.info(randomValuesGenerator.generateRandomString());
    }
}
