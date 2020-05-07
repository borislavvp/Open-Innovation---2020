import { SearchState, UPDATE_SEARCH_RESULT, SearchActionType } from '../actions/types';

const initialState: SearchState = {
	result: '',
};

export function searchReducer(state = initialState, action: SearchActionType): SearchState {
	switch (action.type) {
		case UPDATE_SEARCH_RESULT:
			console.log(state, action.payload);
			return { result: action.payload.result };
		default:
			return state;
	}
}
