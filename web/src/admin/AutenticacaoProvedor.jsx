import { useCallback, useMemo, useState } from "react";
import { pedir } from "./api";
import { AutenticacaoContexto, CHAVE_TOKEN, lerTokenSalvo } from "./autenticacao";

// Guarda o token do painel e expoe entrar / sair / requisitar para o resto do app.
export default function AutenticacaoProvedor({ children }) {
  const [token, setToken] = useState(lerTokenSalvo);

  const sair = useCallback(() => {
    try {
      localStorage.removeItem(CHAVE_TOKEN);
    } catch {
      // nada a limpar
    }
    setToken(null);
  }, []);

  const entrar = useCallback(async (email, senha) => {
    const dados = await pedir("/api/auth/login", {
      metodo: "POST",
      corpo: { email, senha },
    });
    try {
      localStorage.setItem(CHAVE_TOKEN, dados.token);
    } catch {
      // sem storage: o token vale so enquanto a aba estiver aberta
    }
    setToken(dados.token);
  }, []);

  // Chama a API ja com o token. Se ele nao vale mais, derruba a sessao.
  const requisitar = useCallback(
    async (caminho, opcoes = {}) => {
      try {
        return await pedir(caminho, { ...opcoes, token });
      } catch (falha) {
        if (falha.status === 401) {
          sair();
          falha.message = "Sua sessao expirou. Entre de novo.";
        }
        throw falha;
      }
    },
    [token, sair]
  );

  const valor = useMemo(
    () => ({ token, autenticado: Boolean(token), entrar, sair, requisitar }),
    [token, entrar, sair, requisitar]
  );

  return (
    <AutenticacaoContexto.Provider value={valor}>
      {children}
    </AutenticacaoContexto.Provider>
  );
}
