import { Navigate, useLocation } from "react-router-dom";
import { useAutenticacao } from "./autenticacao";

// Envolve as telas de /admin. Sem token valido, manda para o login e guarda
// de onde a pessoa veio para voltar depois de entrar.
export default function RotaProtegida({ children }) {
  const { autenticado } = useAutenticacao();
  const local = useLocation();

  if (!autenticado) {
    return <Navigate to="/admin/login" replace state={{ de: local.pathname }} />;
  }
  return children;
}
