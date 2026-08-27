import "./Depoimentos.css";

export default function Depoimentos({ depoimentos }) {
  if (!depoimentos || depoimentos.length === 0) return null;

  return (
    <section>
      <div className="wrap">
        <div className="cabeca">
          <span className="olho">04 — Clientes</span>
          <h2>Quem já recebeu</h2>
        </div>
        <div className="depos">
          {depoimentos.map((d, i) => (
            <div className="depo" key={i}>
              <p>{d.texto}</p>
              <p className="quem">{d.quem}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
