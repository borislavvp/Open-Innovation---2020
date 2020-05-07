//import axios from 'axios';
import { SearchState, UPDATE_SEARCH_RESULT, SearchActionType } from './types';

export function updateSearchResult(result: SearchState): SearchActionType {
	return {
		type: UPDATE_SEARCH_RESULT,
		payload: result,
	};
}
