import { action, thunk, Action, Thunk } from 'easy-peasy';
import { StorageService } from '../service/StorageService';
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
	filterType: String;
	sortingType: String;
	displayedItems: IMedicineItem[];
	setFilter: Action<SearchModel, string>;
	setSorting: Action<SearchModel, string>;
	addHistoryItem: Thunk<SearchModel, IMedicineItem>;
	getHistoryItems: Thunk<SearchModel, void>;
	saveHistoryItems: Action<SearchModel, IMedicineItem[]>;
	sortAndFilter: Thunk<SearchModel>;
	updateSearchText: Thunk<SearchModel, string>;
	initiateLoading: Action<SearchModel, string>;
	finalizeLoading: Action<SearchModel, IMedicineItem[]>;
	finalizeFilteringOrSorting: Action<SearchModel, IMedicineItem[]>;
}

function sortProducts(data: IMedicineItem[], option: String): IMedicineItem[] {
	if (option === 'name') {
		return [...data.sort((a, b) => a.name.localeCompare(b.name))];
	} else if (option === 'highestprice') {
		//todo
	} else if (option === 'lowestprice') {
		//todo
	}
	return data;
}

function filterProducts(data: IMedicineItem[], option: String): IMedicineItem[] {
	if (option === 'pcs') {
		data = [...data.filter((p) => p.unit === 'pcs')];
	} else if (option === 'mg') {
		data = [...data.filter((p) => p.unit === 'mg')];
	}
	return data;
}

export const search: SearchModel = {
	text: '',
	items: [],
	history: [],
	filterType: '',
	sortingType: '',
	displayedItems: [],
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
	sortAndFilter: thunk(async (actions, payload, state) => {
		const filterType = state.getState().filterType;
		const sortType = state.getState().sortingType;

		actions.initiateLoading(state.getState().text);
		var data: IMedicineItem[] = state.getState().items;

		if (filterType === '') {
			data = sortProducts(data, sortType);
		} else {
			data = sortProducts(filterProducts(data, filterType), sortType);
		}
		actions.finalizeFilteringOrSorting(data);
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
		const url =
			'https://7rfbtov049.execute-api.us-east-1.amazonaws.com/dev/product?search=' + payload.trim().toLowerCase();

		const data: IMedicineItem[] = await (await fetch(url)).json().then((products: IMedicineItem[]) => products);

		// const data: IMedicineItem[] = [
		// 	{
		// 		name: 'Paracetamol 1000mg',
		// 		unit: 'mg',
		// 		amountOfPharmacies: 2,
		// 	},
		// 	{
		// 		name: 'Paracetamol 500mg',
		// 		unit: 'mg',
		// 		amountOfPharmacies: 2,
		// 	},
		// ].filter((item) => item.name.toLowerCase().indexOf(payload.trim().toLowerCase()) > -1);

		actions.finalizeLoading(data);
	}),
	setSorting: action((state, payload) => {
		state.sortingType = payload;
	}),
	setFilter: action((state, payload) => {
		state.filterType = payload;
	}),
	initiateLoading: action((state, payload) => {
		state.text = payload;
		state.loading = true;
	}),
	finalizeLoading: action((state, payload) => {
		state.items = [...payload];
		state.displayedItems = [...payload];
		state.loading = false;
	}),
	finalizeFilteringOrSorting: action((state, payload) => {
		state.displayedItems = [...payload];
		state.loading = false;
	}),
};
