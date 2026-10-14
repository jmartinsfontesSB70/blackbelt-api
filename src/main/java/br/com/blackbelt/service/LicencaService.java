package br.com.blackbelt.service;

import br.com.blackbelt.api.dto.LicencaTokenGeradoResponse;
import br.com.blackbelt.domain.model.Licenca;
import br.com.blackbelt.domain.repository.LicencaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class LicencaService {

    private static final SecureRandom SECURE_RANDOM =
            new SecureRandom();

    private final LicencaRepository licencaRepository;

    public LicencaService(LicencaRepository licencaRepository) {
        this.licencaRepository = licencaRepository;
    }

    @Transactional
    public LicencaTokenGeradoResponse gerarToken(int duracaoDias) {

        if (duracaoDias < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A duração deve ser de pelo menos 1 dia."
            );
        }

        String token;
        String tokenHash;

        do {
            token = gerarTokenAleatorio();
            tokenHash = calcularHash(token);
        } while (licencaRepository.existsByTokenHash(tokenHash));

        LocalDate hoje = LocalDate.now();

        Licenca licenca = new Licenca();
        licenca.setTokenHash(tokenHash);
        licenca.setDuracaoDias(duracaoDias);
        licenca.setDataGeracao(hoje);

        licencaRepository.save(licenca);

        // O token original é retornado somente nesta operação.
        return new LicencaTokenGeradoResponse(
                token,
                duracaoDias,
                hoje
        );
    }

    @Transactional
    public Licenca ativar(String tokenInformado) {

        if (tokenInformado == null || tokenInformado.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Informe o token de ativação."
            );
        }

        String tokenHash = calcularHash(tokenInformado.trim());

        Licenca licenca = licencaRepository
                .findByTokenHash(tokenHash)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Token inválido ou já utilizado."
                ));

        if (licenca.getDataAtivacao() != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Token inválido ou já utilizado."
            );
        }

        LocalDate hoje = LocalDate.now();

        // Se já existe uma licença válida, a nova começa
        // quando a atual terminar, evitando perder dias.
        LocalDate dataInicio = licencaRepository
                .findTopByDataExpiracaoAfterOrderByDataExpiracaoDesc(
                        hoje
                )
                .map(Licenca::getDataExpiracao)
                .filter(data -> data.isAfter(hoje))
                .orElse(hoje);

        licenca.setDataAtivacao(hoje);
        licenca.setDataInicio(dataInicio);
        licenca.setDataExpiracao(
                dataInicio.plusDays(licenca.getDuracaoDias())
        );

        return licencaRepository.save(licenca);
    }

    @Transactional(readOnly = true)
    public boolean licencaValida() {

        LocalDate hoje = LocalDate.now();

        return licencaRepository
                .findFirstByDataInicioLessThanEqualAndDataExpiracaoAfter(
                        hoje,
                        hoje
                )
                .isPresent();
    }

    private String gerarTokenAleatorio() {

        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    private String calcularHash(String token) {

        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "Não foi possível gerar o hash do token.",
                    e
            );
        }
    }
}