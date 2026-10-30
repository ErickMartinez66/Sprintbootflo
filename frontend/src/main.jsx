import React, { useEffect } from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import AOS from 'aos';
import 'aos/dist/aos.css';
import App from './App.jsx';
import './index.css';

let aosIniciado = false;

function Raiz() {
  useEffect(() => {
    if (aosIniciado) return;
    aosIniciado = true;
    AOS.init({
      once: true,
      duration: window.matchMedia('(prefers-reduced-motion: reduce)').matches ? 0 : 650,
      easing: 'ease-out-cubic',
      offset: 40,
      disable: () => window.matchMedia('(prefers-reduced-motion: reduce)').matches,
    });
  }, []);

  return <App />;
}

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <BrowserRouter>
      <Raiz />
    </BrowserRouter>
  </React.StrictMode>
);