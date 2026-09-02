import { useEffect, useMemo, useRef, useState } from "react";
import { useAutenticacao } from "../autenticacao";
import LayoutAdmin from "../LayoutAdmin";
import { comprimirImagem } from "../utilitarios/comprimirImagem";
import "./AparenciaSite.css";

export default function AparenciaSite() {
  const { requisitar } = useAutenticacao();

  const [fotoUrl, setFotoUrl] = useState("");
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState("");
  const [aviso, setAviso] = useState("");
  const [fotoArquivo, setFotoArquivo] = useState(null);
  const [preparando, setPreparando] = useState(false);
  const [salvando, setSalvando] = useState(false);

  const refFoto = useRef(null);

  useEffect(() => {
    let ativo = true;
    (async () => {
      try {
        const config = await requisitar("/api/configuracao");
        if (ativo) setFotoUrl(config?.fotoTopoUrl || "");
      } catch (falha) {
        if (ativo) setErro(falha.message);
      } finally {
        if (ativo) setCarregando(false);
      }
    })();
    return () => {
      ativo = false;
    };
  }, [requisitar]);

  const previa = useMemo(
    () => (fotoArquivo ? URL.createObjectURL(fotoArquivo) : fotoUrl),
    [fotoArquivo, fotoUrl]
  );
  useEffect(() => {
    return () => {
      if (fotoArquivo && previa) URL.revokeObjectURL(previa);
    };
  }, [fotoArquivo, previa]);

  async function escolherFoto(evento) {
    const arquivo = evento.target.files?.[0];
    evento.target.value = ""; // deixa reescolher o mesmo arquivo depois
    if (!arquivo) return;
    setErro("");
    setAviso("");
    setPreparando(true);
    try {
      setFotoArquivo(await comprimirImagem(arquivo));
    } catch (falha) {
      setErro(falha.message);
    } finally {
      setPreparando(false);
    }
  }

  // Mesmo caminho da foto de produto: comprime no navegador, sobe pelo
  // POST /api/admin/upload e grava a URL na configuracao.
  async function gravar(novaUrl, mensagemOk) {
    setErro("");
    setAviso("");
    setSalvando(true);
    try {
      const config = await requisitar("/api/admin/configuracao", {
        metodo: "PUT",
        corpo: { fotoTopoUrl: novaUrl },
      });
      setFotoUrl(config?.fotoTopoUrl || "");
      setFotoArquivo(null);
      setAviso(mensagemOk);
    } catch (falha) {
      setErro(falha.message);
    } finally {
      setSalvando(false);
    }
  }

  async function salvar() {
    if (!fotoArquivo) return;
    setErro("");
    setSalvando(true);
    try {
      const dados = new FormData();
      dados.append("arquivo", fotoArquivo);
      const enviado = await requisitar("/api/admin/upload", { metodo: "POST", formData: dados });
      await gravar(enviado.url, "Foto do topo atualizada. Recarregue a pagina inicial para ver.");
    } catch (falha) {
      setErro(falha.message);
      setSalvando(false);
    }
  }

  function remover() {
    gravar("", "Foto do topo removida. O espaco reservado volta ao lugar.");
  }

  const titulo = "Aparencia do site";

  if (carregando) {
    return (
      <LayoutAdmin titulo={titulo}>
        <p className="admin-aviso">Carregando...</p>
      </LayoutAdmin>
    );
  }

  return (
    <LayoutAdmin titulo={titulo}>
      <section className="aparencia">
        <h2 className="aparencia-subtitulo">Foto do topo da pagina inicial</h2>
        <p className="aparencia-texto">
          E a imagem grande ao lado do titulo, no topo do site. A area tem proporcao de
          aproximadamente 4:3,3 e o corte e automatico (a foto preenche sem distorcer). Sem foto,
          o espaco reservado atual continua no lugar.
        </p>

        {erro && <p className="aparencia-erro" role="alert">{erro}</p>}
        {aviso && <p className="aparencia-ok" role="status">{aviso}</p>}

        <div className="aparencia-previa">
          {previa ? (
            <img src={previa} alt="" />
          ) : (
            <span>Sem foto — usando o espaco reservado</span>
          )}
        </div>

        <input ref={refFoto} type="file" accept="image/*" hidden onChange={escolherFoto} />

        <div className="aparencia-acoes">
          <button
            type="button"
            className="btn btn-cheio"
            disabled={preparando || salvando}
            onClick={() => refFoto.current?.click()}
          >
            {preparando ? "Preparando..." : previa ? "Escolher outra" : "Escolher foto"}
          </button>

          {fotoArquivo && (
            <>
              <button
                type="button"
                className="btn btn-cheio"
                disabled={salvando || preparando}
                onClick={salvar}
              >
                {salvando ? "Salvando..." : "Salvar"}
              </button>
              <button
                type="button"
                className="btn btn-vazio"
                disabled={salvando || preparando}
                onClick={() => setFotoArquivo(null)}
              >
                Cancelar
              </button>
            </>
          )}

          {fotoUrl && !fotoArquivo && (
            <button
              type="button"
              className="btn btn-vazio"
              disabled={salvando}
              onClick={remover}
            >
              Remover foto
            </button>
          )}
        </div>

        <p className="aparencia-dica">
          A imagem e reduzida para 1000 px de largura antes de enviar.
        </p>
      </section>
    </LayoutAdmin>
  );
}
