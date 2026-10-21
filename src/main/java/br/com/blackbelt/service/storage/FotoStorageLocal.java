package br.com.blackbelt.service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FotoStorageLocal implements FotoStorage {

    private final Path diretorioBase;

    public FotoStorageLocal(
            @Value("${blackbelt.storage.fotos-diretorio}")
            String diretorio) {

        this.diretorioBase = Paths.get(diretorio);

        try {
            Files.createDirectories(this.diretorioBase);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Não foi possível criar o diretório de armazenamento de fotos.",
                    e
            );
        }
    }

    @Override
    public String salvar(
            String pasta,
            Long id,
            MultipartFile arquivo) throws IOException {

        Path diretorio = diretorioBase
                .resolve(pasta)
                .resolve(id.toString());

        Files.createDirectories(diretorio);

        String nomeArquivo = "foto.jpg";
        Path destino = diretorio.resolve(nomeArquivo);

        Path temporario = Files.createTempFile(
                diretorio,
                "foto-",
                ".tmp"
        );

        try {
            arquivo.transferTo(temporario);

            try {
                Files.move(
                        temporario,
                        destino,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE
                );
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(
                        temporario,
                        destino,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }
        } finally {
            Files.deleteIfExists(temporario);
        }

        return pasta + "/" + id + "/" + nomeArquivo;
    }

    @Override
    public byte[] buscar(String fotoKey) throws IOException {
        Path arquivo = diretorioBase.resolve(fotoKey).normalize();

        if (!arquivo.startsWith(diretorioBase.normalize())) {
            throw new IOException("Caminho de imagem inválido.");
        }

        return Files.readAllBytes(arquivo);
    }

    @Override
    public void excluir(String fotoKey) throws IOException {
        Path arquivo = diretorioBase.resolve(fotoKey).normalize();

        if (!arquivo.startsWith(diretorioBase.normalize())) {
            throw new IOException("Caminho de imagem inválido.");
        }

        Files.deleteIfExists(arquivo);
    }
}
