import { createI18n } from 'vue-i18n';
import en from './locales/en.js';
import es from './locales/es.js';
import gl from './locales/gl.js';

// Definir los mensajes disponibles
const messages = { en, es, gl };

// Recuperar el idioma guardado o usar el valor por defecto
const savedLocale = localStorage.getItem('idioma') || 'es';  // Idioma por defecto es 'es'

// Configuración de i18n
const i18n = createI18n({
  legacy: false,  // Usar la API moderna de vue-i18n
  locale: savedLocale,  // Idioma a usar (el recuperado de localStorage)
  fallbackLocale: 'es',  // Idioma por defecto si no se encuentra el idioma solicitado
  messages,  // Los mensajes de cada idioma
});

// Método para actualizar el idioma
i18n.global.setLocaleMessage = function (lang) {
  localStorage.setItem('idioma', lang);  // Guardar el idioma en localStorage
};

export default i18n;
