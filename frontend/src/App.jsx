import { lazy, Suspense, useCallback, useEffect, useState } from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import Navbar from './components/Navbar.jsx';
import Hero from './components/Hero.jsx';
import LazySection from './components/LazySection.jsx';
import RequireAuth from './admin/RequireAuth.jsx';
import { obtenerFormConfig } from './data/formConfig.js';

const Carrusel3D = lazy(() => import('./components/Carrusel3D.jsx'));
const FormularioPedido = lazy(() => import('./components/FormularioPedido.jsx'));
const Footer = lazy(() => import('./components/Footer.jsx'));

const AdminLayout = lazy(() => import('./admin/AdminLayout.jsx'));
const ArreglosPage = lazy(() => import('./admin/ArreglosPage.jsx'));
const LoginPage = lazy(() => import('./admin/LoginPage.jsx'));
const MesesPage = lazy(() => import('./admin/MesesPage.jsx'));
const MesDetallePage = lazy(() => import('./admin/MesDetallePage.jsx'));
const ViajesPage = lazy(() => import('./admin/ViajesPage.jsx'));
const ViajeDetallePage = lazy(() => import('./admin/ViajeDetallePage.jsx'));
const InversionPage = lazy(() => import('./admin/InversionPage.jsx'));

function EsperaSeccion({ alto }) {
  return (
    <div
      className="lazy-placeholder"
      style={{ minHeight: alto }}
      aria-hidden="true"
    />
  );
}

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
        <LazySection id="galeria" className="galeria" minHeight={560}>
          <Suspense fallback={<EsperaSeccion alto={560} />}>
            <Carrusel3D />
          </Suspense>
        </LazySection>
        <LazySection id="pedido" className="pedido" minHeight={760}>
          <Suspense fallback={<EsperaSeccion alto={760} />}>
            <FormularioPedido config={config} cargando={cargando} error={error} />
          </Suspense>
        </LazySection>
      </main>
      <LazySection as="footer" id="contacto" className="footer" minHeight={380}>
        <Suspense fallback={<EsperaSeccion alto={380} />}>
          <Footer />
        </Suspense>
      </LazySection>
    </>
  );
}

function App() {
  return (
    <Suspense fallback={<div className="ruta-cargando">Cargando…</div>}>
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
    </Suspense>
  );
}

export default App;
