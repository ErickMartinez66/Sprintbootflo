import{c as l,r as c,j as a}from"./index-DusVtxAl.js";/**
 * @license lucide-react v1.55.0 - ISC
 *
 * This source code is licensed under the ISC license.
 * See the LICENSE file in the root directory of this source tree.
 */const o={name:"plus",size:24,node:[["path",{d:"M5 12h14",key:"1ays0h"}],["path",{d:"M12 5v14",key:"s699le"}]]};o.node;const t=l(o);function m({titulo:d,onCerrar:e,children:i}){return c.useEffect(()=>{const s=n=>{n.key==="Escape"&&e()};return window.addEventListener("keydown",s),()=>window.removeEventListener("keydown",s)},[e]),a.jsx("div",{className:"admin-modal-overlay",onClick:e,children:a.jsxs("div",{className:"admin-modal",role:"dialog","aria-modal":"true",onClick:s=>s.stopPropagation(),children:[a.jsxs("div",{className:"admin-modal-cabecera",children:[a.jsx("h3",{className:"admin-modal-titulo",children:d}),a.jsx("button",{className:"admin-modal-cerrar",onClick:e,"aria-label":"Cerrar",children:"✕"})]}),a.jsx("div",{className:"admin-modal-cuerpo",children:i})]})})}export{m as F,t as P};
