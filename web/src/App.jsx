import { Routes, Route } from "react-router-dom";
import Publica from "./paginas/Publica";
import RotaProtegida from "./admin/RotaProtegida";
import Login from "./admin/paginas/Login";
import ListaProdutos from "./admin/paginas/ListaProdutos";
import FormularioProduto from "./admin/paginas/FormularioProduto";
import AparenciaSite from "./admin/paginas/AparenciaSite";

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Publica />} />
      <Route path="/admin/login" element={<Login />} />
      <Route
        path="/admin"
        element={
          <RotaProtegida>
            <ListaProdutos />
          </RotaProtegida>
        }
      />
      <Route
        path="/admin/aparencia"
        element={
          <RotaProtegida>
            <AparenciaSite />
          </RotaProtegida>
        }
      />
      <Route
        path="/admin/produtos/novo"
        element={
          <RotaProtegida>
            <FormularioProduto />
          </RotaProtegida>
        }
      />
      <Route
        path="/admin/produtos/:id"
        element={
          <RotaProtegida>
            <FormularioProduto />
          </RotaProtegida>
        }
      />
    </Routes>
  );
}
