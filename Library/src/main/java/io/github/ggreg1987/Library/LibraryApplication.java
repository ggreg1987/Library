package io.github.ggreg1987.Library;

import io.github.ggreg1987.Library.domain.rest.service.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class LibraryApplication {

	/*
	@Autowired
	private EmailService emailService;
	@Bean
	public CommandLineRunner runner() {
		return args -> {
			List<String> mails = Arrays.asList("api-java.e968582968e-aa5996+1@inbox.mailtrap.io");
			emailService.sendMails("Testing mail service",mails);
			System.out.println("Sending mail successful");
		};
	}
	*/

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}
}
