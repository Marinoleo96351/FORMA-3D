import { CONFIG } from "../dados/config";

export function linkZap(texto) {
  return `https://wa.me/${CONFIG.whatsapp}?text=${encodeURIComponent(texto)}`;
}
