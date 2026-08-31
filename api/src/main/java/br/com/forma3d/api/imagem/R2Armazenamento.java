package br.com.forma3d.api.imagem;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * Guarda a imagem no bucket do Cloudflare R2 e devolve a URL publica.
 * Implementa ArmazenamentoDeImagem; substitui a implementacao anterior via Cloudinary.
 */
@Service
public class R2Armazenamento implements ArmazenamentoDeImagem {

    private static final Logger log = LoggerFactory.getLogger(R2Armazenamento.class);

    private static final Set<String> TIPOS_ACEITOS = Set.of("image/jpeg", "image/png", "image/webp");

    private final S3Client s3;
    private final String bucket;
    private final String urlPublica;

    public R2Armazenamento(
            S3Client s3,
            @Value("${r2.bucket-name:forma3d-imagens}") String bucket,
            @Value("${r2.public-url:}") String urlPublica) {
        this.s3 = s3;
        this.bucket = bucket;
        this.urlPublica = urlPublica == null ? "" : urlPublica.trim();
    }

    @Override
    public String salvar(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new ArquivoInvalidoException("Selecione uma imagem para enviar.");
        }

        String tipo = arquivo.getContentType();
        if (tipo == null || !TIPOS_ACEITOS.contains(tipo.toLowerCase())) {
            throw new ArquivoInvalidoException("Formato nao aceito. Envie uma imagem JPG, PNG ou WebP.");
        }

        if (urlPublica.isBlank()) {
            log.warn("R2_PUBLIC_URL vazio; upload recusado ate configurar o api/.env.");
            throw new ArmazenamentoIndisponivelException("O armazenamento de imagens ainda nao foi configurado.");
        }

        String nome = nomeLimpo(tipo);

        try {
            PutObjectRequest requisicao = PutObjectRequest.builder()
                .bucket(bucket)
                .key(nome)
                .contentType(tipo)
                .build();
            s3.putObject(requisicao, RequestBody.fromBytes(arquivo.getBytes()));
        } catch (IOException | RuntimeException e) {
            log.error("Falha ao enviar imagem para o R2", e);
            throw new ArmazenamentoIndisponivelException("Nao foi possivel enviar a imagem. Tente novamente.");
        }

        return urlPublica.replaceAll("/+$", "") + "/" + nome;
    }

    private String nomeLimpo(String tipo) {
        String extensao = switch (tipo.toLowerCase()) {
            case "image/png" -> "png";
            case "image/webp" -> "webp";
            default -> "jpg";
        };
        return UUID.randomUUID().toString().replace("-", "") + "." + extensao;
    }
}
