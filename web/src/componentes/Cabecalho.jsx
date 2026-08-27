import "./Cabecalho.css";

export default function Cabecalho() {
  return (
    <header className="cabecalho">
      <div className="wrap barra">
        <a href="#" className="logo">
          forma <span style={{ letterSpacing: ".02em" }}>3d</span>
        </a>
        <nav className="menu">
          <a href="#produtos">Vitrine</a>
          <a href="#como">Como funciona</a>
          <a href="#personalizado">Personalizado</a>
          <a href="#contato">Contato</a>
        </nav>
      </div>
    </header>
  );
}
