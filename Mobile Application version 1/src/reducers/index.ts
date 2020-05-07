import { searchReducer } from './searchReducer';
import { i18nReducer } from 'react-redux-i18n';
import { combineReducers } from 'redux';

export const rootReducer = combineReducers({
	search: searchReducer,
	i18n: i18nReducer,
});

export type RootState = ReturnType<any>;
