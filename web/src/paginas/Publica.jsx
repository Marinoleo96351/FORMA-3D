import { useEffect, useState } from "react";
import Cabecalho from "../componentes/Cabecalho";
import Topo from "../componentes/Topo";
import Vitrine from "../componentes/Vitrine";
import ComoFunciona from "../componentes/ComoFunciona";
import Personalizado from "../componentes/Personalizado";
import Depoimentos from "../componentes/Depoimentos";
import Rodape from "../componentes/Rodape";
import BotaoFlutuante from "../componentes/BotaoFlutuante";
import { pedir } from "../admin/api";
import { DEPOIMENTOS } from "../dados/produtos";

export default function Publica() {
  const [produtos, setProdutos] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState(null);
  const [fotoTopoUrl, setFotoTopoUrl] = useState(null);

  useEffect(() => {
    let ativo = true;
    setCarregando(true);
    setErro(null);
    pedir("/api/produtos")
      .then((lista) => {
        if (ativo) setProdutos(lista);
      })
      .catch(() => {
        if (ativo) setErro("Nao foi possivel carregar as pecas agora. Tente de novo em instantes.");
      })
      .finally(() => {
        if (ativo) setCarregando(false);
      });
    return () => {
      ativo = false;
    };
  }, []);

  // O topo e decorativo: se a configuracao demorar ou falhar, o espaco
  // reservado do Topo continua no lugar sem piscar o layout.
  useEffect(() => {
    let ativo = true;
    pedir("/api/configuracao")
      .then((config) => {
        if (ativo && config && config.fotoTopoUrl) setFotoTopoUrl(config.fotoTopoUrl);
      })
      .catch(() => {
        // sem configuracao: mantem o placeholder do Topo
      });
    return () => {
      ativo = false;
    };
  }, []);

  return (
    <>
      <Cabecalho />
      <main>
        <Topo fotoTopoUrl={fotoTopoUrl} />
        <div className="divisor" />
        <Vitrine produtos={produtos} carregando={carregando} erro={erro} />
        <div className="divisor" />
        <ComoFunciona />
        <div className="divisor" />
        <Personalizado />
        <div className="divisor" />
        <Depoimentos depoimentos={DEPOIMENTOS} />
      </main>
      <Rodape />
      <BotaoFlutuante />
    </>
  );
}
