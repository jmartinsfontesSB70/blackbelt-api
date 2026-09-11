package br.com.blackbelt.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ResendEmailService {

    private final Resend resend;
    private final String webUrl;

    public ResendEmailService(
            @Value("${resend.api-key}") String apiKey,
            @Value("${blackbelt.web-url}") String webUrl) {

        this.resend = new Resend(apiKey);
        this.webUrl = webUrl;
    }

    public void enviarRecuperacaoSenha(
            String email,
            String username,
            String token,
            String client) {

        String linkRedefinicao;

        if ("mobile".equalsIgnoreCase(client)) {
            linkRedefinicao =
                    "blackbeltmobile://redefinir-senha?token=" + token;
        } else {
            linkRedefinicao =
                    webUrl + "/redefinir-senha?token=" + token;
        }

        String html = """
                <h2>BlackBelt - Recuperação de senha</h2>

                <p>Olá, %s.</p>

                <p>Recebemos uma solicitação para redefinição da sua senha.</p>

                <p>
                    Clique no botão abaixo para criar uma nova senha:
                </p>

                <p>
                    <a href="%s"
                       style="
                           display: inline-block;
                           padding: 12px 20px;
                           background-color: #dc2626;
                           color: #ffffff;
                           text-decoration: none;
                           border-radius: 8px;
                           font-weight: bold;
                       ">
                        Redefinir minha senha
                    </a>
                </p>

                <p>Esse link é válido por 30 minutos.</p>

                <p>
                    Se você não solicitou essa recuperação, ignore este e-mail.
                </p>

                <p>
                    Atenciosamente,<br>
                    Equipe BlackBelt
                </p>
                """.formatted(username, linkRedefinicao);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("BlackBelt <noreply@blackbeltgestao.com.br>")
                .to(email)
                .subject("BlackBelt - Recuperação de senha")
                .html(html)
                .build();

        try {
            resend.emails().send(params);
        } catch (ResendException e) {
            throw new RuntimeException(
                    "Não foi possível enviar o e-mail de recuperação.",
                    e
            );
        }
    }
}