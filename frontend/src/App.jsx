import { useCallback, useEffect, useState } from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import Navbar from './components/Navbar.jsx';
import Hero from './components/Hero.jsx';
import Carrusel3D from './components/Carrusel3D.jsx';
import FormularioPedido from './components/FormularioPedido.jsx';
import Footer from './components/Footer.jsx';
import AdminLayout from './admin/AdminLayout.jsx';
import ArreglosPage from './admin/ArreglosPage.jsx';
import MesesPage from './admin/MesesPage.jsx';
import MesDetallePage from './admin/MesDetallePage.jsx';
import ViajesPage from './admin/ViajesPage.jsx';
import ViajeDetallePage from './admin/ViajeDetallePage.jsx';
import AccesoriosPage from './admin/AccesoriosPage.jsx';
import InversionPage from './admin/InversionPage.jsx';
import { listarArreglosActivos } from './api.js';

function Tienda() {
  const [config, setConfig] = useState(null);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');

  const cargarConfig = useCallback(async () => {
    try {
      setCargando(true);
      setError('');
      const activos = await listarArreglosActivos();
      setConfig(activos[0] ?? null);
    } catch (err) {
      setError(err.message);
    } finally {
      setCargando(false);
    }
  }, []);

  useEffect(() => {
    cargarConfig();
  }, [cargarConfig]);

  return (
    <>
      <Navbar />
      <main>
        <Hero />
        <Carrusel3D />
        <FormularioPedido config={config} cargando={cargando} error={error} />
      </main>
      <Footer />
    </>
  );
}

function App() {
  return (
    <Routes>
      <Route path="/" element={<Tienda />} />
      <Route path="/admin" element={<AdminLayout />}>
        <Route index element={<Navigate to="arreglos" replace />} />
        <Route path="arreglos" element={<ArreglosPage />} />
        <Route path="meses" element={<MesesPage />} />
        <Route path="meses/:id" element={<MesDetallePage />} />
        <Route path="viajes" element={<ViajesPage />} />
        <Route path="viajes/:id" element={<ViajeDetallePage />} />
        <Route path="accesorios" element={<AccesoriosPage />} />
        <Route path="inversion" element={<InversionPage />} />
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default App;