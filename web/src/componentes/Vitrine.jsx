import { useMemo, useState } from "react";
import CartaoProduto from "./CartaoProduto";
import "./Vitrine.css";

export default function Vitrine({ produtos, carregando, erro }) {
  const categorias = useMemo(
    () => ["Tudo", ...new Set(produtos.map((p) => p.categoria))],
    [produtos]
  );
  const [categoriaAtiva, setCategoriaAtiva] = useState("Tudo");

  const lista =
    categoriaAtiva === "Tudo"
      ? produtos
      : produtos.filter((p) => p.categoria === categoriaAtiva);

  return (
    <section id="produtos">
      <div className="wrap">
        <span className="cruz" style={{ top: "-8px", left: "22px" }} aria-hidden="true">+</span>
        <div className="cabeca">
          <span className="olho">01 — Vitrine</span>
          <h2>Peças prontas para pedir</h2>
          <p>Compre pela Shopee ou fale direto para pagar no Pix.</p>
        </div>

        {!carregando && !erro && produtos.length > 0 && (
          <div className="filtros" role="group" aria-label="Filtrar por categoria">
            {categorias.map((c) => (
              <button
                key={c}
                type="button"
                className="chip"
                aria-pressed={c === categoriaAtiva}
                onClick={() => setCategoriaAtiva(c)}
              >
                {c}
              </button>
            ))}
          </div>
        )}

        <div className="grade">
          {carregando && <p className="vazio">Carregando peças...</p>}
          {!carregando && erro && <p className="vazio">{erro}</p>}
          {!carregando && !erro && produtos.length === 0 && (
            <p className="vazio">Nenhuma peça cadastrada ainda.</p>
          )}
          {!carregando && !erro && produtos.length > 0 && lista.length === 0 && (
            <p className="vazio">Nenhuma peça nesta categoria por enquanto.</p>
          )}
          {!carregando &&
            !erro &&
            lista.map((p) => <CartaoProduto key={p.id} produto={p} />)}
        </div>
      </div>
    </section>
  );
}
