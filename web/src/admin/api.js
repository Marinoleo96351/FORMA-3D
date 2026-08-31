// Cliente HTTP do painel. Fala com a API do Spring (rotas da secao 5 do plano).
// O endereco vem de VITE_API_URL; no desenvolvimento local cai no 8080.
const BASE = import.meta.env.VITE_API_URL || "http://localhost:8080";

export class ErroDaApi extends Error {
  constructor(mensagem, status) {
    super(mensagem);
    this.name = "ErroDaApi";
    this.status = status;
  }
}

async function mensagemDoErro(resposta) {
  try {
    const corpo = await resposta.json();
    if (corpo && typeof corpo.mensagem === "string") return corpo.mensagem;
  } catch {
    // corpo vazio ou nao-JSON: cai na mensagem generica
  }
  return "Nao foi possivel concluir. Tente de novo.";
}

/**
 * Faz uma requisicao a API e ja devolve o JSON pronto.
 * @param {string} caminho  ex.: "/api/admin/produtos"
 * @param {object} opcoes   { token, metodo, corpo, formData }
 * @throws {ErroDaApi} com .status e .message em portugues
 */
export async function pedir(caminho, opcoes = {}) {
  const { token, metodo = "GET", corpo, formData } = opcoes;
  const cabecalhos = {};
  if (token) cabecalhos.Authorization = `Bearer ${token}`;

  let body;
  if (formData) {
    body = formData; // o navegador define o Content-Type com o boundary
  } else if (corpo !== undefined) {
    cabecalhos["Content-Type"] = "application/json";
    body = JSON.stringify(corpo);
  }

  let resposta;
  try {
    resposta = await fetch(`${BASE}${caminho}`, { method: metodo, headers: cabecalhos, body });
  } catch {
    throw new ErroDaApi("Nao foi possivel conectar ao servidor. Tente de novo.", 0);
  }

  if (!resposta.ok) {
    throw new ErroDaApi(await mensagemDoErro(resposta), resposta.status);
  }

  if (resposta.status === 204) return null;
  const texto = await resposta.text();
  return texto ? JSON.parse(texto) : null;
}
