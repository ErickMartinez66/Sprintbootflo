# Plan para usar el logo de la carpeta /logo como logo circular en la página principal

## Objetivo
Tomar la imagen de rontend/logo/photo_2026-10-20_14-25-32.jpg y ubicarla como el logo circular en la página principal, al principio donde hay un círculo de emojis/flor.

## Ubicaciones identificadas
- src/components/Hero.jsx - Círculo decorativo con íconos de flor (.hero-flor-circulo)

## Imagen
Copiada a rontend/public/logo.jpg para acceso público.

## Cambios a realizar

### 1. Hero.jsx
Reemplazar el contenido de .hero-flor-circulo para mostrar la imagen:
`jsx
<div className="hero-flor" data-aos="fade-up" data-aos-delay="120" aria-hidden="true">
  <div className="hero-flor-circulo">
    <img src="/logo.jpg" alt="Ady Bloom" className="hero-flor-logo" />
  </div>
</div>
`

### 2. index.css
Agregar estilos para la imagen del logo para que quede circular, centrada y ajuste bien:
`css
.hero-flor-logo {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
  display: block;
}
`

## Verificación
- La imagen aparece en el círculo hero al cargar la página principal
- Se ve circular y centrada
- Funciona en responsive
