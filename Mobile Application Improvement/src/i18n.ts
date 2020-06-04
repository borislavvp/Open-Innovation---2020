import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

import translationEN from './translations/en.json';

import translationFR from './translations/fr.json';

import translationKN from './translations/kn.json';

const resources = {
	en: {
		translation: translationEN,
	},
	fr: {
		translation: translationFR,
	},
	kn: {
		translation: translationKN,
	},
};

i18n.use(initReactI18next).init({
	resources,
	lng: document.documentElement.lang,
	fallbackLng: 'en',
	//keySeparator: false,
	interpolation: {
		escapeValue: false,
	},
});

export default i18n;
