import { useEffect, useRef } from 'react';
import AOS from 'aos';

function useAosRefresh(activo) {
  const previo = useRef(activo);

  useEffect(() => {
    if (previo.current === true && activo === false) {
      AOS.refreshHard();
    }
    previo.current = activo;
  }, [activo]);
}

export default useAosRefresh;