package br.com.forma3d.api.produto.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/** Corpo de criacao e atualizacao de produto. Ordem e ativo assumem o padrao quando ausentes. */
public record ProdutoRequest(
    @NotBlank(message = "O nome e obrigatorio.")
    @Size(max = 80, message = "O nome deve ter no maximo 80 caracteres.")
    String nome,

    @NotBlank(message = "A categoria e obrigatoria.")
    @Size(max = 60, message = "A categoria deve ter no maximo 60 caracteres.")
    String categoria,

    @NotNull(message = "O preco e obrigatorio.")
    @DecimalMin(value = "0.01", message = "O preco deve ser maior que zero.")
    @Digits(integer = 8, fraction = 2, message = "O preco deve ter no maximo 8 digitos e 2 casas decimais.")
    BigDecimal preco,

    @Size(max = 120, message = "A observacao deve ter no maximo 120 caracteres.")
    String observacao,

    String fotoUrl,

    String linkShopee,

    Integer ordem,

    Boolean ativo
) {}
