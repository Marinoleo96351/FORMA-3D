import { Link } from "react-router-dom";
import { useAutenticacao } from "./autenticacao";
import "./LayoutAdmin.css";

// Moldura comum das telas internas: cabecalho com logo, atalho para o site e Sair.
export default function LayoutAdmin({ titulo, acao, children }) {
  const { sair } = useAutenticacao();

  return (
    <div className="admin">
      <header className="admin-topo">
        <div className="admin-barra">
          <Link to="/admin" className="admin-logo">forma <span>3d</span></Link>
          <div className="admin-topo-dir">
            <Link to="/admin/aparencia" className="admin-link">Aparência do site</Link>
            <a href="/" className="admin-link" target="_blank" rel="noopener noreferrer">Ver o site</a>
            <button type="button" className="admin-sair" onClick={sair}>Sair</button>
          </div>
        </div>
      </header>

      <main className="admin-conteudo">
        <div className="admin-cabeca">
          <h1>{titulo}</h1>
          {acao}
        </div>
        {children}
      </main>
    </div>
  );
}
