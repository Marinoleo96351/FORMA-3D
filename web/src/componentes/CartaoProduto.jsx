import { CONFIG } from "../dados/config";
import { linkZap } from "../utilitarios/zap";
import { formatarPreco } from "../utilitarios/formato";
import "./CartaoProduto.css";

export default function CartaoProduto({ produto }) {
  const mensagem = `Oi! Vi o ${produto.nome} no site da forma 3d e queria pedir um.`;
  const linkShopee = produto.linkShopee || CONFIG.shopeeLoja;

  return (
    <article className="card">
      <div className="foto">
        {produto.fotoUrl ? (
          <img src={produto.fotoUrl} alt={produto.nome} />
        ) : (
          "foto do produto"
        )}
        <span className="tag">{produto.categoria}</span>
      </div>
      <div className="corpo">
        <h3>{produto.nome}</h3>
        <span className="preco">{formatarPreco(produto.preco)}</span>
        {produto.observacao && <span className="obs">{produto.observacao}</span>}
        <div className="card-acoes">
          <a className="mini mini-shopee" href={linkShopee} target="_blank" rel="noopener noreferrer">
            Comprar na Shopee
          </a>
          <a className="mini mini-zap" href={linkZap(mensagem)} target="_blank" rel="noopener noreferrer">
            Pedir no WhatsApp
          </a>
        </div>
      </div>
    </article>
  );
}
