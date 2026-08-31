package br.com.forma3d.api.imagem;

import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

/**
 * Cliente S3 apontado para o endpoint do Cloudflare R2.
 * Todos os valores vem do ambiente (arquivo api/.env, fora do repositorio) — nada hardcoded.
 * Sem credencial preenchida o bean ainda sobe, mas o envio de imagem falha com mensagem clara.
 */
@Configuration
public class R2Config {

    private static final Logger log = LoggerFactory.getLogger(R2Config.class);

    @Value("${r2.access-key-id:}")
    private String accessKeyId;

    @Value("${r2.secret-access-key:}")
    private String secretAccessKey;

    @Value("${r2.endpoint:}")
    private String endpoint;

    @Bean
    public S3Client s3Client() {
        boolean configurado = !accessKeyId.isBlank() && !secretAccessKey.isBlank() && !endpoint.isBlank();
        if (!configurado) {
            log.warn("R2 nao configurado: preencha R2_ACCESS_KEY_ID, R2_SECRET_ACCESS_KEY e "
                + "R2_ENDPOINT no arquivo api/.env. O envio de imagem vai falhar ate la.");
        }

        AwsBasicCredentials credenciais = AwsBasicCredentials.create(
            accessKeyId.isBlank() ? "r2-nao-configurado" : accessKeyId,
            secretAccessKey.isBlank() ? "r2-nao-configurado" : secretAccessKey);

        return S3Client.builder()
            .endpointOverride(URI.create(endpoint.isBlank()
                ? "https://r2-nao-configurado.invalid"
                : endpoint))
            .credentialsProvider(StaticCredentialsProvider.create(credenciais))
            .region(Region.of("auto"))
            .httpClientBuilder(UrlConnectionHttpClient.builder())
            .serviceConfiguration(S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build())
            .build();
    }
}
