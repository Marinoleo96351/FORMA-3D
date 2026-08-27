import "./ComoFunciona.css";

const ETAPAS = [
  {
    num: "01",
    titulo: "Você conta a ideia",
    texto: "Descreve o que quer, o tamanho e manda uma foto de referência se tiver.",
  },
  {
    num: "02",
    titulo: "Avaliamos e orçamos",
    texto: "Vemos se dá para imprimir, escolhemos o material e passamos preço e prazo.",
  },
  {
    num: "03",
    titulo: "Você aprova",
    texto: "Nada entra na impressora antes de você confirmar valor, cor e acabamento.",
  },
  {
    num: "04",
    titulo: "Imprimimos e enviamos",
    texto: "A peça é produzida, embalada e segue por correio ou retirada na cidade.",
  },
];

export default function ComoFunciona() {
  return (
    <section id="como">
      <div className="wrap">
        <div className="cabeca">
          <span className="olho">02 — Processo</span>
          <h2>Como funciona um pedido sob medida</h2>
          <p>Você não precisa entender de impressão 3D nem ter arquivo pronto.</p>
        </div>
        <div className="etapas">
          {ETAPAS.map((e) => (
            <div className="etapa" key={e.num}>
              <span className="num">{e.num}</span>
              <h3>{e.titulo}</h3>
              <p>{e.texto}</p>
            </div>
          ))}
        </div>
        <p className="nota">
          O preço depende de quatro coisas: tamanho da peça, quantidade de material,
          complexidade do desenho e material escolhido.
        </p>
      </div>
    </section>
  );
}
