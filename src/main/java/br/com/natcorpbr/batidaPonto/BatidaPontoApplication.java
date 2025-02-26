package br.com.natcorpbr.batidaPonto;

import br.com.natcorpbr.batidaPonto.service.BatidaPontoService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan("br.com.natcorpbr.batidaPonto.service")
@EnableScheduling // Habilita o Scheduler do Spring Boot
public class BatidaPontoApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(BatidaPontoApplication.class, args);

		BatidaPontoService service = context.getBean(BatidaPontoService.class);
		service.baterPonto();
	}

}
