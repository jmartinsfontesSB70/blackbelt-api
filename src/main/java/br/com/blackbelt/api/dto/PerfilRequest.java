package br.com.blackbelt.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record PerfilRequest(

        @NotBlank
        String nome,

        @NotNull
        Set<Long> permissaoIds

) {
}