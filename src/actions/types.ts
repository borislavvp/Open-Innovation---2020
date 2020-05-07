export const UPDATE_SEARCH_RESULT = 'UPDATE_SEARCH_RESULT';

export interface SearchState {
	result: string;
}
interface UpdateSearchResultAction {
	type: typeof UPDATE_SEARCH_RESULT;
	payload: SearchState;
}

export type SearchActionType = UpdateSearchResultAction;
