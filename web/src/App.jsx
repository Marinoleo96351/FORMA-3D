import { Routes, Route } from "react-router-dom";
import Publica from "./paginas/Publica";

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Publica />} />
    </Routes>
  );
}
