import { configureStore } from '@reduxjs/toolkit';
import { rootReducer } from './reducers';
import { setLocale, loadTranslations, syncTranslationWithStore } from 'react-redux-i18n';
import translations from './translations/translations';
const store = configureStore({
	reducer: rootReducer,
});

syncTranslationWithStore(store);
store.dispatch(loadTranslations(translations));
store.dispatch(setLocale('en'));

export default store;
