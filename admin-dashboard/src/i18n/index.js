// Arquivo: src/i18n/index.js
import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import * as Localization from 'expo-localization';

import pt from '../locales/pt.json';
import en from '../locales/en.json';

// Detecta o idioma do dispositivo/navegador. Se vier algo diferente de
// 'en' (ex: 'pt', 'es', 'fr'), cai no português como padrão — já que
// só temos pt/en traduzidos por enquanto.
const idiomaDetectado = Localization.getLocales()[0]?.languageCode;
const idiomaInicial = idiomaDetectado === 'en' ? 'en' : 'pt';

i18n
  .use(initReactI18next)
  .init({
    resources: {
      pt: { translation: pt },
      en: { translation: en },
    },
    lng: idiomaInicial,
    fallbackLng: 'pt',
    interpolation: {
      escapeValue: false, // React já protege contra XSS, não precisa escapar
    },
  });

export default i18n;