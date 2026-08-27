import "./Topo.css";

export default function Topo() {
  return (
    <section className="topo">
      <div className="piso" aria-hidden="true" />
      <div className="risco" aria-hidden="true" />
      <div className="wrap">
        <span className="cruz" style={{ top: "-14px", left: "22px" }} aria-hidden="true">+</span>
        <span className="cruz" style={{ top: "-14px", right: "22px" }} aria-hidden="true">+</span>
        <div className="topo-grade">
          <div>
            <span className="olho">Impressão 3D · <b>Cornélio Procópio</b></span>
            <h1>A ideia ganha <em>forma</em>.</h1>
            <p className="sub">
              Peças impressas sob medida — decoração, presentes e utilidades. Se não está na
              vitrine, a gente imprime.
            </p>
            <div className="acoes">
              <a className="btn btn-cheio" href="#produtos">Ver a vitrine</a>
              <a className="btn btn-vazio" href="#personalizado">Quero algo personalizado</a>
            </div>
            <div className="selos">
              <span className="selo">Envio para todo o Brasil</span>
              <span className="selo">Retirada na cidade</span>
              <span className="selo">Pix ou Shopee</span>
            </div>
          </div>
          <div className="moldura">
            Foto grande da melhor peça
            <br />
            (1200 × 990, fundo escuro ou neutro)
          </div>
        </div>
      </div>
    </section>
  );
}
