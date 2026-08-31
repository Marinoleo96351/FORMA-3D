import { useState } from "react";
import { Navigate, useLocation, useNavigate } from "react-router-dom";
import { useAutenticacao } from "../autenticacao";
import "./Login.css";

export default function Login() {
  const { autenticado, entrar } = useAutenticacao();
  const navegar = useNavigate();
  const local = useLocation();
  const destino = local.state?.de || "/admin";

  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");
  const [enviando, setEnviando] = useState(false);

  if (autenticado) return <Navigate to={destino} replace />;

  async function aoEnviar(evento) {
    evento.preventDefault();
    setErro("");
    setEnviando(true);
    try {
      await entrar(email.trim(), senha);
      navegar(destino, { replace: true });
    } catch (falha) {
      setErro(falha.message || "Nao foi possivel entrar. Tente de novo.");
      setEnviando(false);
    }
  }

  function aoDigitar(definir) {
    return (evento) => {
      definir(evento.target.value);
      if (erro) setErro("");
    };
  }

  return (
    <main className="login-tela">
      <form className="login-caixa" onSubmit={aoEnviar}>
        <a href="/" className="login-logo">forma <span>3d</span></a>
        <h1>Painel</h1>
        <p className="login-ajuda">Entre para cuidar do catalogo.</p>

        <div className="campo">
          <label htmlFor="email">E-mail</label>
          <input
            id="email"
            type="email"
            autoComplete="username"
            value={email}
            onChange={aoDigitar(setEmail)}
            required
            autoFocus
          />
        </div>

        <div className="campo">
          <label htmlFor="senha">Senha</label>
          <input
            id="senha"
            type="password"
            autoComplete="current-password"
            value={senha}
            onChange={aoDigitar(setSenha)}
            required
          />
        </div>

        {erro && <p className="login-erro" role="alert">{erro}</p>}

        <button type="submit" className="btn btn-cheio" disabled={enviando}>
          {enviando ? "Entrando..." : "Entrar"}
        </button>
      </form>
    </main>
  );
}
