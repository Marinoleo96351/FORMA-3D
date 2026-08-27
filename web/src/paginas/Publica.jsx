import Cabecalho from "../componentes/Cabecalho";
import Topo from "../componentes/Topo";
import Vitrine from "../componentes/Vitrine";
import ComoFunciona from "../componentes/ComoFunciona";
import Personalizado from "../componentes/Personalizado";
import Depoimentos from "../componentes/Depoimentos";
import Rodape from "../componentes/Rodape";
import BotaoFlutuante from "../componentes/BotaoFlutuante";
import { PRODUTOS, DEPOIMENTOS } from "../dados/produtos";

export default function Publica() {
  return (
    <>
      <Cabecalho />
      <main>
        <Topo />
        <div className="divisor" />
        <Vitrine produtos={PRODUTOS} carregando={false} erro={null} />
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
