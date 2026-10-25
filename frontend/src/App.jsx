import { useCallback, useEffect, useState } from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import Navbar from './components/Navbar.jsx';
import Hero from './components/Hero.jsx';
import Carrusel3D from './components/Carrusel3D.jsx';
import FormularioPedido from './components/FormularioPedido.jsx';
import Footer from './components/Footer.jsx';
import AdminLayout from './admin/AdminLayout.jsx';
import ArreglosPage from './admin/ArreglosPage.jsx';
import LoginPage from './admin/LoginPage.jsx';
import MesesPage from './admin/MesesPage.jsx';
import MesDetallePage from './admin/MesDetallePage.jsx';
import RequireAuth from './admin/RequireAuth.jsx';
import ViajesPage from './admin/ViajesPage.jsx';
import ViajeDetallePage from './admin/ViajeDetallePage.jsx';
import InversionPage from './admin/InversionPage.jsx';
import { obtenerFormConfig } from './data/formConfig.js';

function Tienda() {
  const [config, setConfig] = useState(null);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');

  const cargarConfig = useCallback(() => {
    try {
      setCargando(true);
      setError('');
      const cfg = obtenerFormConfig();
      setConfig(cfg);
    } catch (err) {
      setError(err.message);
    } finally {
      setCargando(false);
    }
  }, []);

  useEffect(() => {
    cargarConfig();
  }, [cargarConfig]);

  useEffect(() => {
    if (typeof window === 'undefined') return;

    const recargar = () => {
      cargarConfig();
    };

    window.addEventListener('focus', recargar);
    window.addEventListener('adybloom:config-actualizada', recargar);
    document.addEventListener('visibilitychange', recargar);
    window.addEventListener('pageshow', recargar);
    window.addEventListener('storage', recargar);

    return () => {
      window.removeEventListener('focus', recargar);
      window.removeEventListener('adybloom:config-actualizada', recargar);
      document.removeEventListener('visibilitychange', recargar);
      window.removeEventListener('pageshow', recargar);
      window.removeEventListener('storage', recargar);
    };
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
      <Route path="/admin/login" element={<LoginPage />} />
      <Route
        path="/admin"
        element={
          <RequireAuth>
            <AdminLayout />
          </RequireAuth>
        }
      >
        <Route index element={<Navigate to="arreglos" replace />} />
        <Route path="arreglos" element={<ArreglosPage />} />
        <Route path="meses" element={<MesesPage />} />
        <Route path="meses/:id" element={<MesDetallePage />} />
        <Route path="viajes" element={<ViajesPage />} />
        <Route path="viajes/:id" element={<ViajeDetallePage />} />
        <Route path="inversion" element={<InversionPage />} />
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default App;