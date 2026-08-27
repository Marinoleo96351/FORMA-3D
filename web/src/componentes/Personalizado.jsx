import { useRef, useState } from "react";
import { linkZap } from "../utilitarios/zap";
import "./Personalizado.css";

export default function Personalizado() {
  const [ideia, setIdeia] = useState("");
  const [tamanho, setTamanho] = useState("");
  const [quantidade, setQuantidade] = useState(1);
  const [prazo, setPrazo] = useState("Sem pressa");
  const [nome, setNome] = useState("");
  const [erro, setErro] = useState(false);
  const campoIdeia = useRef(null);

  function enviar() {
    const texto = ideia.trim();
    if (!texto) {
      setErro(true);
      campoIdeia.current?.focus();
      return;
    }

    const linhas = [
      "Oi, forma 3d! Quero um pedido personalizado.",
      "",
      "O que eu quero: " + texto,
      "Tamanho: " + (tamanho.trim() || "não sei ainda"),
      "Quantidade: " + quantidade,
      "Prazo: " + prazo,
    ];
    if (nome.trim()) linhas.push("Meu nome: " + nome.trim());

    window.open(linkZap(linhas.join("\n")), "_blank", "noopener");
  }

  return (
    <section id="personalizado">
      <div className="wrap">
        <span className="cruz" style={{ top: "-8px", right: "22px" }} aria-hidden="true">+</span>
        <div className="cabeca">
          <span className="olho">03 — Sob medida</span>
          <h2>Conte o que você imaginou</h2>
          <p>Ao enviar, o WhatsApp abre com a mensagem já escrita. É só apertar enviar.</p>
        </div>
        <div className="form-caixa">
          <div className="campos">
            <div className="campo largo">
              <label htmlFor="ideia">O que você quer que a gente imprima</label>
              <textarea
                id="ideia"
                ref={campoIdeia}
                placeholder="Ex.: um suporte de fone com o nome do meu filho gravado"
                value={ideia}
                onChange={(e) => {
                  setIdeia(e.target.value);
                  if (erro) setErro(false);
                }}
              />
              <span className={`erro${erro ? " on" : ""}`}>Descreva a peça antes de enviar.</span>
            </div>
            <div className="campo">
              <label htmlFor="tamanho">Tamanho aproximado</label>
              <input
                id="tamanho"
                placeholder="Ex.: 10 cm de altura"
                value={tamanho}
                onChange={(e) => setTamanho(e.target.value)}
              />
            </div>
            <div className="campo">
              <label htmlFor="qtd">Quantidade</label>
              <input
                id="qtd"
                type="number"
                min="1"
                value={quantidade}
                onChange={(e) => setQuantidade(e.target.value)}
              />
            </div>
            <div className="campo">
              <label htmlFor="prazo">Quando você precisa</label>
              <select id="prazo" value={prazo} onChange={(e) => setPrazo(e.target.value)}>
                <option>Sem pressa</option>
                <option>Nas próximas 2 semanas</option>
                <option>Nesta semana</option>
                <option>É urgente</option>
              </select>
            </div>
            <div className="campo">
              <label htmlFor="nome">Seu nome</label>
              <input
                id="nome"
                placeholder="Como podemos te chamar"
                value={nome}
                onChange={(e) => setNome(e.target.value)}
              />
            </div>
          </div>
          <div className="rodape-form">
            <button type="button" className="btn btn-zap" onClick={enviar}>
              Abrir no WhatsApp
            </button>
            <span className="dica">Nada é enviado por este site — a conversa acontece no WhatsApp.</span>
          </div>
        </div>
      </div>
    </section>
  );
}
