// Redimensiona e recomprime a foto no proprio navegador antes de enviar (dia 4 do plano).
// Foto crua de celular tem varios MB e deixa o site lento justamente no celular.
// Feito so com canvas, sem dependencia (o plano pede visual sem bibliotecas prontas).
const LARGURA_MAX = 1000;
const QUALIDADE_JPEG = 0.82;

export async function comprimirImagem(arquivo) {
  if (!arquivo || !arquivo.type.startsWith("image/")) {
    throw new Error("Escolha um arquivo de imagem.");
  }

  const fonte = await carregarImagem(arquivo);
  const larguraOriginal = fonte.width || fonte.naturalWidth;
  const alturaOriginal = fonte.height || fonte.naturalHeight;

  const escala = Math.min(1, LARGURA_MAX / larguraOriginal);
  const largura = Math.round(larguraOriginal * escala);
  const altura = Math.round(alturaOriginal * escala);

  const canvas = document.createElement("canvas");
  canvas.width = largura;
  canvas.height = altura;
  canvas.getContext("2d").drawImage(fonte, 0, 0, largura, altura);
  if (typeof fonte.close === "function") fonte.close();

  const blob = await new Promise((resolve, reject) => {
    canvas.toBlob(
      (resultado) =>
        resultado ? resolve(resultado) : reject(new Error("Nao foi possivel processar a imagem.")),
      "image/jpeg",
      QUALIDADE_JPEG
    );
  });

  // Se recomprimir nao ajudou (imagem ja pequena), envia o arquivo original.
  if (blob.size >= arquivo.size) return arquivo;

  const nome = (arquivo.name || "foto").replace(/\.[^.]+$/, "") + ".jpg";
  return new File([blob], nome, { type: "image/jpeg" });
}

async function carregarImagem(arquivo) {
  if ("createImageBitmap" in window) {
    try {
      return await createImageBitmap(arquivo);
    } catch {
      // alguns formatos falham aqui; cai no caminho com <img>
    }
  }

  const url = URL.createObjectURL(arquivo);
  try {
    const img = new Image();
    img.src = url;
    await img.decode();
    return img;
  } finally {
    URL.revokeObjectURL(url);
  }
}
