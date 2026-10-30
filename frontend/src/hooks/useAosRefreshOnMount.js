import { useEffect } from 'react';
import AOS from 'aos';

function useAosRefreshOnMount() {
  useEffect(() => {
    const id = window.requestAnimationFrame(() => AOS.refreshHard());
    return () => window.cancelAnimationFrame(id);
  }, []);
}

export default useAosRefreshOnMount;
