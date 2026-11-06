package br.com.blackbelt.config;

import br.com.blackbelt.service.CargaLocalidadesService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CargaLocalidadesRunner {

    @Bean
    public ApplicationRunner carregarLocalidades(
            CargaLocalidadesService service) {

        return args -> service.importarLocalidades();
    }
}