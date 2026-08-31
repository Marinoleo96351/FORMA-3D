import { createContext, useContext } from "react";

// Contexto e hook do login ficam aqui, separados do provedor, para o arquivo
// do provedor exportar so um componente (regra only-export-components do lint).
export const CHAVE_TOKEN = "forma3d_token";

export const AutenticacaoContexto = createContext(null);

export function useAutenticacao() {
  const contexto = useContext(AutenticacaoContexto);
  if (!contexto) {
    throw new Error("useAutenticacao precisa estar dentro de <AutenticacaoProvedor>.");
  }
  return contexto;
}

export function lerTokenSalvo() {
  try {
    return localStorage.getItem(CHAVE_TOKEN);
  } catch {
    return null; // navegador em modo privado ou storage bloqueado
  }
}
