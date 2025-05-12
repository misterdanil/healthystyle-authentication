package org.healthystyle.authentication.app;

import org.healthystyle.authentication.repository.role.RoleRepository;
import org.healthystyle.authentication.repository.role.opportunity.OpportunityRepository;
import org.healthystyle.model.role.Name;
import org.healthystyle.model.role.Role;
import org.healthystyle.model.role.opportunity.Opportunity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "org.healthystyle.authentication")
@EnableJpaRepositories(basePackages = "org.healthystyle.authentication.repository")
@EntityScan(basePackages = "org.healthystyle.model")
public class Main {
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private OpportunityRepository opportunityRepository;

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	public CommandLineRunner r() {
		return new CommandLineRunner() {

			@Override
			public void run(String... args) throws Exception {
				Opportunity o1 = new Opportunity(org.healthystyle.model.role.opportunity.Name.WATCH_ARTICLES);
//				o1 = opportunityRepository.save(o1);
				
				Role userRole = new Role(Name.USER, o1);
//				roleRepository.save(userRole);
			}
		};
	}
}
