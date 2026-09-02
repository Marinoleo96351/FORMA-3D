package br.com.forma3d.api.configuracao.dto;

import jakarta.validation.constraints.Size;

/**
 * Corpo do PUT /api/admin/configuracao. A foto chega como URL ja hospedada
 * (o upload passa antes pelo POST /api/admin/upload). Vazio limpa a foto.
 */
public record ConfiguracaoRequest(
    @Size(max = 500, message = "A URL da foto deve ter no maximo 500 caracteres.")
    String fotoTopoUrl
) {}
