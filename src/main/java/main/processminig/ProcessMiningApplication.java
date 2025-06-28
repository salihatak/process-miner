package main.processminig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/foos")
@SpringBootApplication
public class ProcessMiningApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcessMiningApplication.class, args);
	}

	@GetMapping
	public String findAll() {
		return "Test";
	}

}






















































