package br.com.blackbelt.service.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FotoStorage {

    String salvar(String pasta, Long id, MultipartFile arquivo) throws IOException;

    byte[] buscar(String fotoKey) throws IOException;

    void excluir(String fotoKey) throws IOException;
}