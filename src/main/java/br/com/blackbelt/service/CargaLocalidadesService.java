package br.com.blackbelt.service;

import br.com.blackbelt.domain.model.Cidade;
import br.com.blackbelt.domain.model.Uf;
import br.com.blackbelt.domain.repository.CidadeRepository;
import br.com.blackbelt.domain.repository.UfRepository;
import br.com.blackbelt.infrastructure.client.IbgeClient;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CargaLocalidadesService {

    private final UfRepository ufRepository;
    private final CidadeRepository cidadeRepository;
    private final IbgeClient ibgeClient;

    public CargaLocalidadesService(
            UfRepository ufRepository,
            CidadeRepository cidadeRepository,
            IbgeClient ibgeClient) {
        this.ufRepository = ufRepository;
        this.cidadeRepository = cidadeRepository;
        this.ibgeClient = ibgeClient;
    }

    @Transactional
    public void importarLocalidades() {

        if (ufRepository.count() > 0) {
            System.out.println("Localidades já cadastradas.");
            return;
        }

        var ufs = ibgeClient.buscarUfs();

        if (ufs == null || ufs.isEmpty()) {
            throw new IllegalStateException(
                    "Não foi possível carregar as UFs do IBGE.");
        }

        for (var ibgeUf : ufs) {

            Uf uf = new Uf();
            uf.setSigla(ibgeUf.sigla());
            uf.setNome(ibgeUf.nome());

            uf = ufRepository.save(uf);

            var cidades = ibgeClient.buscarCidades(ibgeUf.id());

            if (cidades == null) {
                throw new IllegalStateException(
                        "Não foi possível carregar municípios de "
                                + ibgeUf.sigla());
            }

            for (var ibgeCidade : cidades) {

                Cidade cidade = new Cidade();
                cidade.setNome(ibgeCidade.nome());
                cidade.setCodigoIbge(ibgeCidade.id().toString());
                cidade.setUf(uf);

                cidadeRepository.save(cidade);
            }

            System.out.println("UF carregada: " + uf.getSigla());
        }

        System.out.println("Carga de localidades concluída!");
    }
}