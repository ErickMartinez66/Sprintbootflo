import {
  Flower,
  Flower2,
  Rose,
  Sun,
  Leaf,
  Sprout,
  Trees,
  Heart,
  Sparkles,
  Star,
  Gem,
  Crown,
  Feather,
} from 'lucide-react';

const MAPA = {
  '💐': Flower,
  '🌸': Flower,
  '🌺': Flower,
  '🌼': Flower2,
  '🌷': Flower2,
  '🌹': Rose,
  '🥀': Rose,
  '🌻': Sun,
  '🌿': Leaf,
  '🍃': Leaf,
  '🌱': Sprout,
  '🌳': Trees,
  '🌲': Trees,
  '💝': Heart,
  '💗': Heart,
  '❤️': Heart,
  '✨': Sparkles,
  '⭐': Star,
  '💎': Gem,
  '👑': Crown,
  '🪶': Feather,
};

function IconoArreglo({ emoji, size = 26, className }) {
  if (emoji && MAPA[emoji]) {
    const Icono = MAPA[emoji];
    return <Icono size={size} className={className} aria-hidden="true" />;
  }
  return <Flower size={size} className={className} aria-hidden="true" />;
}

export default IconoArreglo;