
package p3.init;

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import p3.jpa.model.Dog;
import p3.jpa.repo.DogRepository;


@Component
public class InitDb4dogs  implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(InitDb4dogs.class);
	@Autowired
	private DogRepository dogRepository;
	
	@Override
	public void run(String... args) throws Exception {
		logger.info("BEF InitDb4dogs will make sure 'dogs' table has rows");
			
		long countOfDogs = dogRepository.count();
		if (countOfDogs == 0) {
			logger.info("InitDbCommandLinerForDogs will initialize 'dogs' table");
			dogRepository.save(new Dog("white", "maltiese"));
			dogRepository.save(new Dog("yellow", "chiwawa"));
			dogRepository.save(new Dog("black", "germanSheppard"));
			dogRepository.save(new Dog("brown", "yorki")); 
		    dogRepository.save(new Dog("milky", "kangai"));
		} else {
			logger.info("InitDb4dogsr will not do anything since 'dogs' table already has rows");
		}
	}
}




