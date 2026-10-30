import { useEffect, useRef, useState } from 'react';

function LazySection({
  as: Etiqueta = 'section',
  id,
  className,
  minHeight = 320,
  children,
}) {
  const ref = useRef(null);
  const [visible, setVisible] = useState(false);

  useEffect(() => {
    if (visible) return undefined;

    const elemento = ref.current;
    if (!elemento) return undefined;

    if (typeof IntersectionObserver === 'undefined') {
      setVisible(true);
      return undefined;
    }

    const observer = new IntersectionObserver(
      (entradas) => {
        if (entradas.some((entrada) => entrada.isIntersecting)) {
          setVisible(true);
          observer.disconnect();
        }
      },
      { rootMargin: '300px 0px', threshold: 0 }
    );

    observer.observe(elemento);
    return () => observer.disconnect();
  }, [visible]);

  return (
    <Etiqueta
      ref={ref}
      id={id}
      className={className}
      aria-busy={!visible}
    >
      {visible ? (
        children
      ) : (
        <div
          className="lazy-placeholder"
          style={{ minHeight }}
          aria-hidden="true"
        />
      )}
    </Etiqueta>
  );
}

export default LazySection;
