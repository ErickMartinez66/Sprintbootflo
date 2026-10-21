import { useEffect } from 'react';

function FormModal({ titulo, onCerrar, children }) {
  useEffect(() => {
    const alPresionar = (e) => {
      if (e.key === 'Escape') onCerrar();
    };
    window.addEventListener('keydown', alPresionar);
    return () => window.removeEventListener('keydown', alPresionar);
  }, [onCerrar]);

  return (
    <div className="admin-modal-overlay" onClick={onCerrar}>
      <div
        className="admin-modal"
        role="dialog"
        aria-modal="true"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="admin-modal-cabecera">
          <h3 className="admin-modal-titulo">{titulo}</h3>
          <button className="admin-modal-cerrar" onClick={onCerrar} aria-label="Cerrar">
            ✕
          </button>
        </div>
        <div className="admin-modal-cuerpo">{children}</div>
      </div>
    </div>
  );
}

export default FormModal;