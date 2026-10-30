import { useEffect, useRef, useState } from 'react';
import GALERIA from '../data/galeria.js';
import useAosRefreshOnMount from '../hooks/useAosRefreshOnMount.js';

const TOTAL = GALERIA.length;
const ANGULO = 42;

function Carrusel3D() {
  useAosRefreshOnMount();
  const escenaRef = useRef(null);
  const arrastre = useRef({ activo: false, inicioX: 0, inicioPos: 0, movimiento: 0 });
  const hoverRef = useRef(false);
  const reduceMotion = useRef(
    typeof window !== 'undefined' &&
      window.matchMedia('(prefers-reduced-motion: reduce)').matches
  );

  const [posicion, setPosicion] = useState(0);
  const [arrastrando, setArrastrando] = useState(false);
  const [medidas, setMedidas] = useState({
    paso: 200,
    profundidad: 120,
    ancho: 250,
    alto: 330,
  });

  useEffect(() => {
    const calcular = () => {
      const ancho = escenaRef.current?.clientWidth || window.innerWidth;
      const itemAncho = Math.max(150, Math.min(260, ancho * 0.3));
      const itemAlto = itemAncho * 1.32;
      setMedidas({
        ancho: itemAncho,
        alto: itemAlto,
        paso: itemAncho * 0.82,
        profundidad: Math.max(70, itemAncho * 0.55),
      });
    };
    calcular();
    window.addEventListener('resize', calcular);
    return () => window.removeEventListener('resize', calcular);
  }, []);

  // Autoplay: avanza una imagen y se pausa al arrastrar o pasar el mouse.
  useEffect(() => {
    if (reduceMotion.current) return undefined;
    const id = window.setInterval(() => {
      if (!arrastre.current.activo && !hoverRef.current) {
        setPosicion((p) => Math.round(p) + 1);
      }
    }, 3500);
    return () => window.clearInterval(id);
  }, []);

  // Arrastre con puntero.
  useEffect(() => {
    if (!arrastrando) return undefined;

    const mover = (e) => {
      if (!arrastre.current.activo) return;
      const dx = e.clientX - arrastre.current.inicioX;
      arrastre.current.movimiento = Math.abs(dx);
      setPosicion(arrastre.current.inicioPos - dx / medidas.paso);
    };

    const soltar = () => {
      if (!arrastre.current.activo) return;
      arrastre.current.activo = false;
      setArrastrando(false);
      setPosicion((p) => Math.round(p));
    };

    window.addEventListener('pointermove', mover);
    window.addEventListener('pointerup', soltar);
    window.addEventListener('pointercancel', soltar);
    return () => {
      window.removeEventListener('pointermove', mover);
      window.removeEventListener('pointerup', soltar);
      window.removeEventListener('pointercancel', soltar);
    };
  }, [arrastrando, medidas.paso]);

  const desfase = (i) => {
    let d = i - posicion;
    d = ((d % TOTAL) + TOTAL) % TOTAL;
    if (d > TOTAL / 2) d -= TOTAL;
    return d;
  };

  const alPresionar = (e) => {
    arrastre.current = {
      activo: true,
      inicioX: e.clientX,
      inicioPos: posicion,
      movimiento: 0,
    };
    setArrastrando(true);
  };

  const centrar = (i) => {
    if (arrastre.current.movimiento > 8) {
      arrastre.current.movimiento = 0;
      return;
    }
    arrastre.current.movimiento = 0;
    setPosicion((p) => {
      let d = i - p;
      d = ((d % TOTAL) + TOTAL) % TOTAL;
      if (d > TOTAL / 2) d -= TOTAL;
      return p + d;
    });
  };

  return (
    <div className="container">
        <span className="eyebrow" data-aos="fade-up">
          Galería
        </span>
        <h2 className="titulo-seccion" data-aos="fade-up">
          Nuestros arreglos
        </h2>
        <p className="subtitulo-seccion" data-aos="fade-up" data-aos-delay="60">
          Arrastra para girar o toca una imagen para verla en el centro.
        </p>

        <div
          ref={escenaRef}
          className={`car-escena${arrastrando ? ' arrastrando' : ''}`}
          onPointerDown={alPresionar}
          onDragStart={(e) => e.preventDefault()}
          onMouseEnter={() => {
            hoverRef.current = true;
          }}
          onMouseLeave={() => {
            hoverRef.current = false;
          }}
        >
          {GALERIA.map((src, i) => {
            const d = desfase(i);
            const abs = Math.abs(d);
            const transform =
              `translate(-50%, -50%) translateX(${d * medidas.paso}px) ` +
              `translateZ(${-abs * medidas.profundidad}px) ` +
              `rotateY(${-d * ANGULO}deg) scale(${Math.max(0.55, 1 - abs * 0.13)})`;

            return (
              <figure
                key={src}
                className={`car-item${abs < 0.5 ? ' centro' : ''}`}
                style={{
                  width: medidas.ancho,
                  height: medidas.alto,
                  transform,
                  zIndex: Math.round(100 - abs * 10),
                  opacity: abs > 4.5 ? 0 : Math.max(0.35, 1 - abs * 0.18),
                  pointerEvents: abs > 4.5 ? 'none' : 'auto',
                }}
                onClick={() => centrar(i)}
              >
                <img
                  src={src}
                  alt={`Arreglo floral ${i + 1}`}
                  draggable={false}
                  loading="lazy"
                  decoding="async"
                />
              </figure>
            );
          })}
        </div>
    </div>
  );
}

export default Carrusel3D;
