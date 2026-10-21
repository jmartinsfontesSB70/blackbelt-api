package br.com.blackbelt.service;

import br.com.blackbelt.api.dto.AcademiaRequest;
import br.com.blackbelt.api.dto.AcademiaResponse;
import br.com.blackbelt.api.mapper.AcademiaMapper;
import br.com.blackbelt.domain.model.Academia;
import br.com.blackbelt.domain.repository.AcademiaRepository;
import br.com.blackbelt.exception.EntidadeConflitoException;
import br.com.blackbelt.exception.EntidadeNaoEncontradaException;
import br.com.blackbelt.service.storage.FotoStorage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class AcademiaService {

    private static final int DIAS_AVALIACAO = 14;

    private final AcademiaRepository academiaRepository;
    private final AcademiaMapper academiaMapper;
    private final FotoStorage fotoStorage;

    public AcademiaService(
            AcademiaRepository academiaRepository,
            AcademiaMapper academiaMapper,
            FotoStorage fotoStorage) {

        this.academiaRepository = academiaRepository;
        this.academiaMapper = academiaMapper;
        this.fotoStorage = fotoStorage;
    }

    @Transactional
    public AcademiaResponse cadastrar(
            AcademiaRequest request,
            MultipartFile logo) {

        if (academiaRepository.count() > 0) {
            throw new EntidadeConflitoException(
                    "Já existe uma academia cadastrada nesta instalação."
            );
        }

        Academia academia = academiaMapper.toEntity(request);

        LocalDate dataCadastro = LocalDate.now();

        academia.setId(null);
        academia.setDataCadastro(dataCadastro);
        academia.setDataExpiracao(
                dataCadastro.plusDays(DIAS_AVALIACAO)
        );

        Academia academiaSalva = academiaRepository.save(academia);

        if (logo != null && !logo.isEmpty()) {
            try {
                String logoKey = fotoStorage.salvar(
                        "academias",
                        academiaSalva.getId(),
                        logo
                );

                academiaSalva.setLogoKey(logoKey);

                academiaSalva = academiaRepository.save(academiaSalva);

            } catch (IOException e) {
                throw new IllegalStateException(
                        "Não foi possível salvar o logotipo da academia.",
                        e
                );
            }
        }

        return academiaMapper.toResponse(academiaSalva);
    }

    @Transactional(readOnly = true)
    public AcademiaResponse buscar() {

        Academia academia = academiaRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Nenhuma academia cadastrada."
                ));

        return academiaMapper.toResponse(academia);
    }

    @Transactional
    public AcademiaResponse atualizarLogo(MultipartFile logo) {

        if (logo == null || logo.isEmpty()) {
            throw new IllegalArgumentException(
                    "Selecione uma imagem para a logo da academia."
            );
        }

        String tipo = logo.getContentType();

        if (tipo == null ||
                !(tipo.equalsIgnoreCase("image/jpeg")
                        || tipo.equalsIgnoreCase("image/png"))) {
            throw new IllegalArgumentException(
                    "A logo deve estar no formato JPG ou PNG."
            );
        }

        Academia academia = academiaRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Nenhuma academia cadastrada."
                ));

        try {
            String logoKey = fotoStorage.salvar(
                    "academias",
                    academia.getId(),
                    logo
            );

            academia.setLogoKey(logoKey);

            Academia academiaAtualizada =
                    academiaRepository.save(academia);

            return academiaMapper.toResponse(academiaAtualizada);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Não foi possível atualizar a logo da academia.",
                    e
            );
        }
    }

    @Transactional
    public AcademiaResponse atualizar(AcademiaRequest request) {

        Academia academia = academiaRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Nenhuma academia cadastrada."
                ));

        academia.setNome(request.getNome());
        academia.setResponsavel(request.getResponsavel());
        academia.setCnpj(request.getCnpj());

        if (request.getEndereco() != null) {
            academia.setEndereco(
                    academiaMapper.toEntity(request).getEndereco()
            );
        }

        Academia academiaAtualizada =
                academiaRepository.save(academia);

        return academiaMapper.toResponse(academiaAtualizada);
    }

    @Transactional(readOnly = true)
    public boolean existeAcademia() {
        return academiaRepository.count() > 0;
    }

    @Transactional(readOnly = true)
    public boolean avaliacaoValida() {

        return academiaRepository.findAll()
                .stream()
                .findFirst()
                .map(academia ->
                        LocalDate.now().isBefore(
                                academia.getDataExpiracao()
                        )
                )
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public byte[] buscarLogo() {
        Academia academia = academiaRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Nenhuma academia cadastrada."
                ));

        if (academia.getLogoKey() == null || academia.getLogoKey().isBlank()) {
            throw new EntidadeNaoEncontradaException(
                    "A academia ainda não possui uma logo cadastrada."
            );
        }

        try {
            return fotoStorage.buscar(academia.getLogoKey());
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Não foi possível carregar a logo da academia.", e
            );
        }
    }

}
