package br.com.forma3d.api.seguranca.dto;

import jakarta.validation.constraints.NotBlank;

/** Corpo do POST /api/auth/login. */
public record LoginRequest(
    @NotBlank(message = "Informe o e-mail.")
    String email,

    @NotBlank(message = "Informe a senha.")
    String senha
) {}
