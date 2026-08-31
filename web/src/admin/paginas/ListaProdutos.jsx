import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useAutenticacao } from "../autenticacao";
import LayoutAdmin from "../LayoutAdmin";
import { formatarPreco } from "../../utilitarios/formato";
import "./ListaProdutos.css";

// Monta o corpo que o PUT /api/admin/produtos/{id} espera a partir de um produto ja carregado.
function corpoDe(produto) {
  return {
    nome: produto.nome,
    categoria: produto.categoria,
    preco: produto.preco,
    observacao: produto.observacao,
    fotoUrl: produto.fotoUrl,
    linkShopee: produto.linkShopee,
    ordem: produto.ordem,
    ativo: produto.ativo,
  };
}

export default function ListaProdutos() {
  const { requisitar } = useAutenticacao();
  const [produtos, setProdutos] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState("");
  const [ocupado, setOcupado] = useState(false);
  const [confirmando, setConfirmando] = useState(null);
  const [versao, setVersao] = useState(0);

  useEffect(() => {
    let ativo = true;
    (async () => {
      try {
        const lista = await requisitar("/api/admin/produtos");
        if (ativo) {
          setProdutos(lista);
          setErro("");
        }
      } catch (falha) {
        if (ativo) setErro(falha.message);
      } finally {
        if (ativo) setCarregando(false);
      }
    })();
    return () => {
      ativo = false;
    };
  }, [requisitar, versao]);

  function recarregar() {
    setCarregando(true);
    setErro("");
    setVersao((v) => v + 1);
  }

  async function alternarAtivo(produto) {
    setOcupado(true);
    setErro("");
    try {
      const atualizado = await requisitar(`/api/admin/produtos/${produto.id}`, {
        metodo: "PUT",
        corpo: { ...corpoDe(produto), ativo: !produto.ativo },
      });
      setProdutos((lista) => lista.map((p) => (p.id === atualizado.id ? atualizado : p)));
    } catch (falha) {
      setErro(falha.message);
    } finally {
      setOcupado(false);
    }
  }

  async function mover(indice, direcao) {
    const alvo = indice + direcao;
    if (alvo < 0 || alvo >= produtos.length) return;

    const nova = [...produtos];
    [nova[indice], nova[alvo]] = [nova[alvo], nova[indice]];

    setOcupado(true);
    setErro("");
    try {
      const mudaram = nova
        .map((p, i) => ({ p, i }))
        .filter(({ p, i }) => p.ordem !== i);
      await Promise.all(
        mudaram.map(({ p, i }) =>
          requisitar(`/api/admin/produtos/${p.id}/ordem`, {
            metodo: "PATCH",
            corpo: { ordem: i },
          })
        )
      );
      setProdutos(nova.map((p, i) => ({ ...p, ordem: i })));
    } catch (falha) {
      setErro(falha.message);
    } finally {
      setOcupado(false);
    }
  }

  async function excluir(produto) {
    setOcupado(true);
    setErro("");
    try {
      await requisitar(`/api/admin/produtos/${produto.id}`, { metodo: "DELETE" });
      setProdutos((lista) => lista.filter((p) => p.id !== produto.id));
      setConfirmando(null);
    } catch (falha) {
      setErro(falha.message);
    } finally {
      setOcupado(false);
    }
  }

  const acaoNova = (
    <Link to="/admin/produtos/novo" className="btn btn-cheio">Nova peca</Link>
  );

  return (
    <LayoutAdmin titulo="Pecas do catalogo" acao={acaoNova}>
      {carregando && <p className="admin-aviso">Carregando pecas...</p>}

      {!carregando && erro && produtos.length === 0 && (
        <div className="admin-aviso admin-aviso-erro">
          <p>{erro}</p>
          <button type="button" className="btn btn-vazio" onClick={recarregar}>
            Tentar de novo
          </button>
        </div>
      )}

      {!carregando && !erro && produtos.length === 0 && (
        <p className="admin-aviso">Nenhuma peca cadastrada ainda.</p>
      )}

      {!carregando && produtos.length > 0 && (
        <>
          {erro && <p className="lista-erro" role="alert">{erro}</p>}
          <ul className="lista">
            {produtos.map((produto, indice) => (
              <li key={produto.id} className={`linha${produto.ativo ? "" : " inativa"}`}>
                <div className="mini">
                  {produto.fotoUrl ? (
                    <img src={produto.fotoUrl} alt="" />
                  ) : (
                    <span className="mini-vazia" aria-hidden="true">sem foto</span>
                  )}
                </div>

                <div className="dados">
                  <span className="nome">{produto.nome}</span>
                  <span className="meta">
                    {produto.categoria} &middot; {formatarPreco(Number(produto.preco))}
                  </span>
                </div>

                <div className="ordem">
                  <button
                    type="button"
                    aria-label="Subir na ordem"
                    disabled={ocupado || indice === 0}
                    onClick={() => mover(indice, -1)}
                  >
                    &uarr;
                  </button>
                  <button
                    type="button"
                    aria-label="Descer na ordem"
                    disabled={ocupado || indice === produtos.length - 1}
                    onClick={() => mover(indice, 1)}
                  >
                    &darr;
                  </button>
                </div>

                <label className="chave">
                  <input
                    type="checkbox"
                    checked={produto.ativo}
                    disabled={ocupado}
                    onChange={() => alternarAtivo(produto)}
                  />
                  <span>{produto.ativo ? "Ativo" : "Inativo"}</span>
                </label>

                <div className="acoes">
                  <Link to={`/admin/produtos/${produto.id}`} className="botao-linha">
                    Editar
                  </Link>
                  {confirmando === produto.id ? (
                    <span className="confirma">
                      Excluir mesmo?
                      <button
                        type="button"
                        className="botao-linha perigo"
                        disabled={ocupado}
                        onClick={() => excluir(produto)}
                      >
                        Sim
                      </button>
                      <button
                        type="button"
                        className="botao-linha"
                        disabled={ocupado}
                        onClick={() => setConfirmando(null)}
                      >
                        Nao
                      </button>
                    </span>
                  ) : (
                    <button
                      type="button"
                      className="botao-linha perigo"
                      onClick={() => setConfirmando(produto.id)}
                    >
                      Excluir
                    </button>
                  )}
                </div>
              </li>
            ))}
          </ul>
        </>
      )}
    </LayoutAdmin>
  );
}
