import { action, thunk, Action, Thunk, ActionCreator } from 'easy-peasy';
import { StorageService } from '../service/StorageService';
import debounce from 'lodash/debounce';
//import { DefaultApi } from 'kigali_api';

export interface IMedicineItem {
	id: number;
	name: string;
	unit: string;
	amountOfPharmacies: number;
}

export interface SearchModel {
	text: string;
	loading: boolean;
	items: IMedicineItem[];
	history: IMedicineItem[];
	sortingType: String;
	setSorting: Action<SearchModel, string>;
	addHistoryItem: Thunk<SearchModel, IMedicineItem>;
	getHistoryItems: Thunk<SearchModel, void>;
	saveHistoryItems: Action<SearchModel, IMedicineItem[]>;
	sortItems: Thunk<SearchModel>;
	updateSearchText: Thunk<SearchModel, string>;
	initiateLoading: Action<SearchModel, string>;
	finalizeLoading: Action<SearchModel, IMedicineItem[]>;
}

function sortProducts(data: IMedicineItem[], option: String): IMedicineItem[] {
	if (option === 'name') {
		return [...data.sort((a, b) => a.name.localeCompare(b.name))];
	}
	return data;
}
const debouncedSearchProductsCall = debounce(async (finalizeLoading: ActionCreator<IMedicineItem[]>, query) => {
	const url =
		'https://7rfbtov049.execute-api.us-east-1.amazonaws.com/dev/product?search=' + query.trim().toLowerCase();

	const data: IMedicineItem[] = await (await fetch(url))
		.json()
		.then((products: IMedicineItem[]) => products)
		.catch(() => []);
	finalizeLoading(data);
}, 500);

export const search: SearchModel = {
	text: '',
	items: [],
	history: [],
	sortingType: '',
	loading: false,
	addHistoryItem: thunk(async (state, payload) => {
		const value = await StorageService.getItem('SEARCH_HISTORY');

		if (value !== null && value.find((item: IMedicineItem) => item.name === payload.name)) {
			return;
		}

		const newHistory = (value !== null ? [payload, ...value] : [payload]).slice(0, 4);

		StorageService.setItem('SEARCH_HISTORY', newHistory);

		state.saveHistoryItems(newHistory);
	}),
	getHistoryItems: thunk(async (state, payload) => {
		const value = await StorageService.getItem('SEARCH_HISTORY');

		const newHistory = value !== null ? [...value] : [];

		state.saveHistoryItems(newHistory);
	}),
	saveHistoryItems: action((state, payload) => {
		state.history = payload;
	}),
	sortItems: thunk(async (actions, payload, state) => {
		const sortType = state.getState().sortingType;

		actions.initiateLoading(state.getState().text);
		var data: IMedicineItem[] = state.getState().items;

		data = sortProducts(data, sortType);

		actions.finalizeLoading(data);
	}),
	updateSearchText: thunk(async (actions, payload) => {
		actions.initiateLoading(payload);

		if (payload.trim() === '') {
			actions.finalizeLoading([]);
			return;
		}

		// const api = new DefaultApi();
		// const data: IMedicineItem[] = await api.productGet({
		//   search: payload.trim(),
		// });
		debouncedSearchProductsCall(actions.finalizeLoading, payload);
	}),
	setSorting: action((state, payload) => {
		state.sortingType = payload;
	}),
	initiateLoading: action((state, payload) => {
		state.text = payload;
		state.loading = true;
	}),
	finalizeLoading: action((state, payload) => {
		state.items = [...payload];
		state.loading = false;
	}),
};
