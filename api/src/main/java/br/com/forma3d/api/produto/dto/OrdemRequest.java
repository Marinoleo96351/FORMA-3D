package br.com.forma3d.api.produto.dto;

import jakarta.validation.constraints.NotNull;

/** Corpo do PATCH que mexe so no campo ordem. */
public record OrdemRequest(
    @NotNull(message = "A ordem e obrigatoria.")
    Integer ordem
) {}
