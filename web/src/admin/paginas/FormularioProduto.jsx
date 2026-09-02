import { useEffect, useMemo, useRef, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { useAutenticacao } from "../autenticacao";
import LayoutAdmin from "../LayoutAdmin";
import { comprimirImagem } from "../utilitarios/comprimirImagem";
import "./FormularioProduto.css";

const CATEGORIAS_PADRAO = ["Decoração", "Presentes", "Utilidades", "Miniaturas"];

const CAMPOS_VAZIOS = {
  nome: "",
  categoria: "",
  preco: "",
  observacao: "",
  linkShopee: "",
  fotoUrl: "",
  ordem: 0,
  ativo: true,
};

function paraNumero(preco) {
  return Number(String(preco).replace(",", ".").trim());
}

export default function FormularioProduto() {
  const { id } = useParams();
  const editando = Boolean(id);
  const { requisitar } = useAutenticacao();
  const navegar = useNavigate();

  const [campos, setCampos] = useState(CAMPOS_VAZIOS);
  const [carregando, setCarregando] = useState(editando);
  const [salvando, setSalvando] = useState(false);
  const [erro, setErro] = useState("");
  const [problemas, setProblemas] = useState({});
  const [fotoArquivo, setFotoArquivo] = useState(null);
  const [preparandoFoto, setPreparandoFoto] = useState(false);
  const [categoriaOutros, setCategoriaOutros] = useState(false);

  const refNome = useRef(null);
  const refCategoria = useRef(null);
  const refCategoriaOutros = useRef(null);
  const refPreco = useRef(null);
  const refFoto = useRef(null);

  useEffect(() => {
    if (!editando) return undefined;
    let ativo = true;
    (async () => {
      try {
        const p = await requisitar(`/api/admin/produtos/${id}`);
        if (!ativo) return;
        setCategoriaOutros(
          Boolean(p.categoria) && !CATEGORIAS_PADRAO.includes(p.categoria)
        );
        setCampos({
          nome: p.nome,
          categoria: p.categoria,
          preco: String(p.preco).replace(".", ","),
          observacao: p.observacao || "",
          linkShopee: p.linkShopee || "",
          fotoUrl: p.fotoUrl || "",
          ordem: p.ordem,
          ativo: p.ativo,
        });
      } catch (falha) {
        if (ativo) setErro(falha.message);
      } finally {
        if (ativo) setCarregando(false);
      }
    })();
    return () => {
      ativo = false;
    };
  }, [editando, id, requisitar]);

  const previa = useMemo(
    () => (fotoArquivo ? URL.createObjectURL(fotoArquivo) : campos.fotoUrl),
    [fotoArquivo, campos.fotoUrl]
  );
  useEffect(() => {
    return () => {
      if (fotoArquivo && previa) URL.revokeObjectURL(previa);
    };
  }, [fotoArquivo, previa]);

  function editar(nome, valor) {
    setCampos((atual) => ({ ...atual, [nome]: valor }));
    if (problemas[nome]) setProblemas((atual) => ({ ...atual, [nome]: undefined }));
  }

  function escolherCategoria(valor) {
    if (valor === "__outros__") {
      setCategoriaOutros(true);
      editar("categoria", "");
    } else {
      setCategoriaOutros(false);
      editar("categoria", valor);
    }
  }

  async function escolherFoto(evento) {
    const arquivo = evento.target.files?.[0];
    evento.target.value = ""; // deixa reescolher o mesmo arquivo depois
    if (!arquivo) return;
    setErro("");
    setPreparandoFoto(true);
    try {
      setFotoArquivo(await comprimirImagem(arquivo));
    } catch (falha) {
      setErro(falha.message);
    } finally {
      setPreparandoFoto(false);
    }
  }

  function removerFoto() {
    setFotoArquivo(null);
    setCampos((atual) => ({ ...atual, fotoUrl: "" }));
  }

  function validar() {
    const achado = {};
    const nome = campos.nome.trim();
    if (!nome) achado.nome = "Informe o nome da peca.";
    else if (nome.length > 80) achado.nome = "O nome deve ter no maximo 80 caracteres.";

    if (!campos.categoria.trim()) {
      achado.categoria = categoriaOutros
        ? "Digite o nome da categoria."
        : "Escolha a categoria.";
    }

    const preco = paraNumero(campos.preco);
    if (!campos.preco.trim() || Number.isNaN(preco) || preco <= 0) {
      achado.preco = "Informe um preco maior que zero.";
    }

    if (campos.observacao.trim().length > 120) {
      achado.observacao = "A observacao deve ter no maximo 120 caracteres.";
    }

    setProblemas(achado);
    const foco = {
      nome: refNome,
      categoria: categoriaOutros ? refCategoriaOutros : refCategoria,
      preco: refPreco,
    };
    const primeiro = ["nome", "categoria", "preco"].find((chave) => achado[chave]);
    if (primeiro) foco[primeiro].current?.focus();
    return Object.keys(achado).length === 0;
  }

  async function salvar(evento) {
    evento.preventDefault();
    setErro("");
    if (!validar()) return;

    setSalvando(true);
    try {
      let fotoUrl = campos.fotoUrl;
      if (fotoArquivo) {
        const dados = new FormData();
        dados.append("arquivo", fotoArquivo);
        const enviado = await requisitar("/api/admin/upload", { metodo: "POST", formData: dados });
        fotoUrl = enviado.url;
      }

      const corpo = {
        nome: campos.nome.trim(),
        categoria: campos.categoria.trim(),
        preco: paraNumero(campos.preco),
        observacao: campos.observacao.trim(),
        linkShopee: campos.linkShopee.trim(),
        fotoUrl,
        ordem: campos.ordem,
        ativo: campos.ativo,
      };

      await requisitar(editando ? `/api/admin/produtos/${id}` : "/api/admin/produtos", {
        metodo: editando ? "PUT" : "POST",
        corpo,
      });
      navegar("/admin");
    } catch (falha) {
      setErro(falha.message);
      setSalvando(false);
    }
  }

  const titulo = editando ? "Editar peca" : "Nova peca";

  if (carregando) {
    return (
      <LayoutAdmin titulo={titulo}>
        <p className="admin-aviso">Carregando...</p>
      </LayoutAdmin>
    );
  }

  return (
    <LayoutAdmin titulo={titulo}>
      <form className="form-produto" onSubmit={salvar} noValidate>
        {erro && <p className="form-erro-geral" role="alert">{erro}</p>}

        <div className="foto-bloco">
          <div className="foto-previa">
            {previa ? <img src={previa} alt="" /> : <span>sem foto</span>}
          </div>
          <div className="foto-acoes">
            <input
              ref={refFoto}
              type="file"
              accept="image/*"
              hidden
              onChange={escolherFoto}
            />
            <button
              type="button"
              className="botao-linha"
              disabled={preparandoFoto}
              onClick={() => refFoto.current?.click()}
            >
              {preparandoFoto ? "Preparando..." : previa ? "Trocar foto" : "Escolher foto"}
            </button>
            {previa && (
              <button type="button" className="botao-linha perigo" onClick={removerFoto}>
                Remover
              </button>
            )}
            <p className="foto-dica">
              A imagem e reduzida para 1000 px de largura antes de enviar.
            </p>
          </div>
        </div>

        <div className={`campo${problemas.nome ? " ruim" : ""}`}>
          <label htmlFor="nome">Nome</label>
          <input
            id="nome"
            ref={refNome}
            maxLength={80}
            value={campos.nome}
            onChange={(e) => editar("nome", e.target.value)}
          />
          {problemas.nome && <span className="campo-erro">{problemas.nome}</span>}
        </div>

        <div className={`campo${problemas.categoria ? " ruim" : ""}`}>
          <label htmlFor="categoria">Categoria</label>
          <select
            id="categoria"
            ref={refCategoria}
            value={categoriaOutros ? "__outros__" : campos.categoria}
            onChange={(e) => escolherCategoria(e.target.value)}
          >
            <option value="">Selecione...</option>
            {CATEGORIAS_PADRAO.map((c) => (
              <option key={c} value={c}>{c}</option>
            ))}
            <option value="__outros__">Outros</option>
          </select>
          {categoriaOutros && (
            <input
              id="categoria-outros"
              ref={refCategoriaOutros}
              maxLength={40}
              placeholder="Nome da nova categoria"
              value={campos.categoria}
              onChange={(e) => editar("categoria", e.target.value)}
            />
          )}
          {problemas.categoria && <span className="campo-erro">{problemas.categoria}</span>}
        </div>

        <div className={`campo${problemas.preco ? " ruim" : ""}`}>
          <label htmlFor="preco">Preco (R$)</label>
          <input
            id="preco"
            ref={refPreco}
            inputMode="decimal"
            placeholder="Ex.: 45,90"
            value={campos.preco}
            onChange={(e) => editar("preco", e.target.value)}
          />
          {problemas.preco && <span className="campo-erro">{problemas.preco}</span>}
        </div>

        <div className={`campo${problemas.observacao ? " ruim" : ""}`}>
          <label htmlFor="observacao">Observacao (opcional)</label>
          <input
            id="observacao"
            maxLength={120}
            placeholder="Ex.: 12 cm, varias cores"
            value={campos.observacao}
            onChange={(e) => editar("observacao", e.target.value)}
          />
          {problemas.observacao && <span className="campo-erro">{problemas.observacao}</span>}
        </div>

        <div className="campo">
          <label htmlFor="linkShopee">Link da Shopee (opcional)</label>
          <input
            id="linkShopee"
            type="url"
            placeholder="Deixe vazio para apontar para a loja geral"
            value={campos.linkShopee}
            onChange={(e) => editar("linkShopee", e.target.value)}
          />
        </div>

        <label className="campo-ativo">
          <input
            type="checkbox"
            checked={campos.ativo}
            onChange={(e) => editar("ativo", e.target.checked)}
          />
          <span>Aparece na vitrine do site</span>
        </label>

        <div className="form-rodape">
          <button type="submit" className="btn btn-cheio" disabled={salvando || preparandoFoto}>
            {salvando ? "Salvando..." : "Salvar peca"}
          </button>
          <Link to="/admin" className="btn btn-vazio">Cancelar</Link>
        </div>
      </form>
    </LayoutAdmin>
  );
}
