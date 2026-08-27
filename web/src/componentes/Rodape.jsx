import { CONFIG } from "../dados/config";
import { linkZap } from "../utilitarios/zap";
import "./Rodape.css";

export default function Rodape() {
  return (
    <footer id="contato">
      <div className="wrap rodape">
        <div>
          <span className="logo">forma 3d</span>
          <p className="olho" style={{ marginTop: "14px" }}>
            A ideia ganha <b>forma.</b>
          </p>
        </div>
        <div className="links-rodape">
          <a href={linkZap("Oi! Vim pelo site.")} target="_blank" rel="noopener noreferrer">
            WhatsApp
          </a>
          <a href={CONFIG.instagram} target="_blank" rel="noopener noreferrer">
            Instagram
          </a>
          <a href={CONFIG.shopeeLoja} target="_blank" rel="noopener noreferrer">
            Loja na Shopee
          </a>
        </div>
      </div>
      <div className="wrap">
        <p className="assinatura">forma 3d · Cornélio Procópio, PR</p>
      </div>
    </footer>
  );
}
