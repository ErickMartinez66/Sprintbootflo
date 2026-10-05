import { useState } from 'react';
import Navbar from './components/Navbar.jsx';
import Hero from './components/Hero.jsx';
import Catalogo from './components/Catalogo.jsx';
import ModalPedido from './components/ModalPedido.jsx';
import Footer from './components/Footer.jsx';

function App() {
  const [arregloSeleccionado, setArregloSeleccionado] = useState(null);

  const abrirPedido = (arreglo) => setArregloSeleccionado(arreglo);
  const cerrarPedido = () => setArregloSeleccionado(null);

  return (
    <>
      <Navbar />
      <main>
        <Hero />
        <Catalogo onSeleccionar={abrirPedido} />
      </main>
      <Footer />
      {arregloSeleccionado && (
        <ModalPedido
          arreglo={arregloSeleccionado}
          onCerrar={cerrarPedido}
        />
      )}
    </>
  );
}

export default App;