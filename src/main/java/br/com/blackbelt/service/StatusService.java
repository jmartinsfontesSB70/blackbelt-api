package br.com.blackbelt.service;

import br.com.blackbelt.domain.StatusApi;
import org.springframework.stereotype.Service;

@Service
public class StatusService {

    public StatusApi obterStatus() {

        return new StatusApi(
                "ONLINE",
                "BlackBelt API",
                "1.0.0"
        );
    }
}