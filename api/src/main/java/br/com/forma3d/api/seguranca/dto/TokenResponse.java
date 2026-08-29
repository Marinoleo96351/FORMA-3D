package br.com.forma3d.api.seguranca.dto;

/** Resposta do login: o token e por quanto tempo ele vale. */
public record TokenResponse(
    String token,
    String tipo,
    long expiraEmSegundos
) {}
