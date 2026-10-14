package br.com.blackbelt.config;

import br.com.blackbelt.service.AcademiaService;
import br.com.blackbelt.service.LicencaService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class LicencaFilter extends OncePerRequestFilter {

    private final AcademiaService academiaService;
    private final LicencaService licencaService;

    public LicencaFilter(
            AcademiaService academiaService,
            LicencaService licencaService) {

        this.academiaService = academiaService;
        this.licencaService = licencaService;
    }

    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request) {

        String caminho = request.getServletPath();

        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        return caminho.equals("/error")
                || caminho.equals("/api/v1/login")
                || caminho.equals("/api/v1/recuperacao-senha")
                || caminho.equals("/api/v1/recuperacao-senha/redefinir")
                || caminho.equals("/api/v1/academia/status-instalacao")
                || caminho.equals("/api/v1/academia/instalacao")
                || caminho.equals("/api/v1/licenca/ativar")
                || caminho.equals("/api/v1/licenca/tokens");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        Authentication autenticacao =
                SecurityContextHolder.getContext().getAuthentication();

        boolean usuarioAutenticado =
                autenticacao instanceof JwtAuthenticationToken
                        && autenticacao.isAuthenticated();

        boolean avaliacaoValida =
                academiaService.avaliacaoValida();

        boolean licencaValida =
                licencaService.licencaValida();

        if (usuarioAutenticado
                && !avaliacaoValida
                && !licencaValida) {

            response.setStatus(423);
            response.setContentType(
                    MediaType.APPLICATION_JSON_VALUE
            );
            response.setCharacterEncoding("UTF-8");

            response.getWriter().write("""
                    {
                      "codigo": "LICENCA_EXPIRADA",
                      "mensagem": "O período de avaliação expirou e não existe uma licença comercial válida. Ative sua licença para continuar utilizando o BlackBelt."
                    }
                    """);

            return;
        }

        filterChain.doFilter(request, response);
    }
}