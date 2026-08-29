package br.com.forma3d.api.imagem;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Envia a imagem para a Cloudinary e devolve a secure_url.
 *
 * Sem CLOUDINARY_URL configurada a aplicacao ainda sobe, mas a rota de upload
 * responde 503 ate a variavel ser definida (ver secao 8 da especificacao).
 */
@Service
public class CloudinaryArmazenamento implements ArmazenamentoDeImagem {

    private static final Logger log = LoggerFactory.getLogger(CloudinaryArmazenamento.class);

    private static final Set<String> TIPOS_ACEITOS =
        Set.of("image/jpeg", "image/png", "image/webp", "image/gif");

    private final Cloudinary cloudinary;
    private final String pasta;

    public CloudinaryArmazenamento(
        @Value("${cloudinary.url:}") String cloudinaryUrl,
        @Value("${app.upload.pasta}") String pasta
    ) {
        this.pasta = pasta;
        if (cloudinaryUrl == null || cloudinaryUrl.isBlank()) {
            this.cloudinary = null;
            log.warn("CLOUDINARY_URL nao definida; a rota /api/admin/upload vai responder 503.");
        } else {
            this.cloudinary = new Cloudinary(cloudinaryUrl);
        }
    }

    @Override
    public String salvar(MultipartFile arquivo) {
        if (cloudinary == null) {
            throw new ArmazenamentoIndisponivelException(
                "Envio de imagem ainda nao configurado. Tente de novo mais tarde.");
        }
        if (arquivo == null || arquivo.isEmpty()) {
            throw new ArquivoInvalidoException("Envie um arquivo de imagem.");
        }
        String tipo = arquivo.getContentType();
        if (tipo == null || !TIPOS_ACEITOS.contains(tipo.toLowerCase())) {
            throw new ArquivoInvalidoException("Formato nao aceito. Use JPG, PNG, WEBP ou GIF.");
        }

        try {
            Map<?, ?> resultado = cloudinary.uploader().upload(
                arquivo.getBytes(),
                ObjectUtils.asMap("folder", pasta, "resource_type", "image"));
            Object url = resultado.get("secure_url");
            if (url == null) {
                throw new ArmazenamentoIndisponivelException(
                    "Nao foi possivel enviar a imagem. Tente de novo.");
            }
            return url.toString();
        } catch (IOException e) {
            log.error("Falha ao enviar imagem para a Cloudinary", e);
            throw new ArmazenamentoIndisponivelException(
                "Nao foi possivel enviar a imagem. Tente de novo.");
        }
    }
}
