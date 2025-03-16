package br.com.natcorpbr.batidaPonto;

import br.com.natcorpbr.batidaPonto.service.BatidaPontoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan("br.com.natcorpbr.batidaPonto.service")
@EnableScheduling // Habilita o Scheduler do Spring Boot
public class BatidaPontoApplication {

	private static final Logger logger = LoggerFactory.getLogger(BatidaPontoApplication.class);

	public static void main(String[] args) {


		logger.info("🔵 Aplicação iniciando...");

		try {
			ApplicationContext context = SpringApplication.run(BatidaPontoApplication.class, args);
			logger.info("Aplicação em execução!");

			BatidaPontoService service = context.getBean(BatidaPontoService.class);
			service.baterPonto();
		} catch (Exception e) {
			logger.error("Erro ao iniciar a aplicação!", e);
		}

	}

}
