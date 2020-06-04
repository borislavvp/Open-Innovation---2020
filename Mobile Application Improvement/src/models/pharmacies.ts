import { action, thunk, Action, Thunk } from 'easy-peasy';
import { StorageService } from '../service/StorageService';
import { IMedicineItem } from './search';

export interface IItems {
	pharmacies?: IPharmacy[];
	inventoryItems?: IInventoryItem[];
}

export interface IInventoryItem {
	id: number;
	quantity: number;
	price: bigint;
	product: IMedicineItem;
	pharmacy: IPharmacy;
}
export interface IPharmacy {
	id: number;
	name: string;
	address: string;
	approved: boolean;
	latitude: string;
	longitude: string;
	rating: number;
	insurances: {
		id: number;
		insurance: string;
	}[];
}

export interface PharmacyModel {
	items: IItems;
	loading: boolean;
	filterType: String;
	sortingType: String;
	displayedItems: IItems;
	getItems: Thunk<PharmacyModel, Number>;
	setFilter: Action<PharmacyModel, String>;
	setSorting: Action<PharmacyModel, String>;
	sortAndFilter: Thunk<PharmacyModel>;
	initiateLoading: Action<PharmacyModel>;
	finalizeLoading: Action<PharmacyModel, IItems>;
	finalizeFilteringOrSorting: Action<PharmacyModel, IItems>;
}
function filterPharmacies(data: IPharmacy[], option: String, insurance: string): IPharmacy[] {
	if (option === 'rating') {
		return [...data.filter((p) => p.rating > 3)];
	} else if (option === 'insurance') {
		return [
			...data.filter((p) => {
				return p.insurances.some((i) => {
					return i.insurance.includes(insurance);
				});
			}),
		];
	}
	return data;
}
function sortPharmacies(a: IPharmacy, b: IPharmacy, option: String): number {
	if (option === 'name') {
		return a.name.localeCompare(b.name);
	} else if (option === 'rating') {
		return (a.rating - b.rating) * -1;
	} else if (option === 'distance') {
		return (a.rating - b.rating) * -1;
	}
	return 0;
}
function sortInventoryItems(a: IInventoryItem, b: IInventoryItem, option: String): number {
	if (option === 'hprice') {
		return a.price < b.price ? 1 : a.price > b.price ? -1 : 0;
	} else if (option === 'lprice') {
		return a.price < b.price ? -1 : a.price > b.price ? 1 : 0;
	} else {
		return sortPharmacies(a.pharmacy, b.pharmacy, option);
	}
}
function filterInventoryItems(data: IInventoryItem[], option: String, insurance: string): IInventoryItem[] {
	let pharmacies = filterPharmacies(
		data.map((d) => d.pharmacy),
		option,
		insurance
	);
	return data.filter((d) => pharmacies.includes(d.pharmacy));
}
export const pharmacies: PharmacyModel = {
	loading: false,
	items: {},
	filterType: '',
	sortingType: '',
	displayedItems: {},
	getItems: thunk(async (actions, payload) => {
		actions.initiateLoading();
		let data: IItems = {};
		if (payload === -1) {
			const url = 'https://7rfbtov049.execute-api.us-east-1.amazonaws.com/dev/pharmacy';

			const pharmacies: IPharmacy[] = await (await fetch(url))
				.json()
				.then((pharmacies: IPharmacy[]) => pharmacies);
			//todo fetch pharmacies
			pharmacies.forEach((p) => (p.rating = 5));
			data = { pharmacies };
		} else {
			const url =
				'https://7rfbtov049.execute-api.us-east-1.amazonaws.com/dev/product/' + payload + '/inventory-items';

			const inventoryItems: IInventoryItem[] = await (await fetch(url))
				.json()
				.then((invetoryItems: IInventoryItem[]) => invetoryItems);
			inventoryItems.forEach((i) => (i.pharmacy.rating = 5));
			data = { inventoryItems };
		}
		actions.finalizeLoading(data);
	}),
	sortAndFilter: thunk(async (actions, payload, state) => {
		const filterType = state.getState().filterType;
		const sortType = state.getState().sortingType;

		actions.initiateLoading();
		let data: IItems = { ...state.getState().items };
		if (data.pharmacies !== undefined) {
			if (filterType === '') {
				data.pharmacies.sort((a, b) => sortPharmacies(a, b, sortType));
			} else {
				if (filterType === 'insurance') {
					const settings = await StorageService.getItem('SETTINGS');
					data.pharmacies = filterPharmacies(data.pharmacies, filterType, settings.insurance);
					data.pharmacies.sort((a, b) => sortPharmacies(a, b, sortType));
				} else {
					data.pharmacies = filterPharmacies(data.pharmacies, filterType, '');
					data.pharmacies.sort((a, b) => sortPharmacies(a, b, sortType));
				}
			}
		} else if (data.inventoryItems !== undefined) {
			if (filterType === '') {
				data.inventoryItems.sort((a, b) => sortInventoryItems(a, b, sortType));
			} else {
				if (filterType === 'insurance') {
					const settings = await StorageService.getItem('SETTINGS');
					data.inventoryItems = filterInventoryItems(data.inventoryItems, filterType, settings.insurance);
					data.inventoryItems.sort((a, b) => sortInventoryItems(a, b, sortType));
				} else {
					data.inventoryItems = filterInventoryItems(data.inventoryItems, filterType, '');
					data.inventoryItems.sort((a, b) => sortInventoryItems(a, b, sortType));
				}
			}
		}

		actions.finalizeFilteringOrSorting(data);
	}),
	setSorting: action((state, payload) => {
		state.sortingType = payload;
	}),
	setFilter: action((state, payload) => {
		state.filterType = payload;
	}),
	initiateLoading: action((state) => {
		state.loading = true;
	}),
	finalizeLoading: action((state, payload) => {
		state.items = payload;
		state.displayedItems = payload;
		state.loading = false;
	}),
	finalizeFilteringOrSorting: action((state, payload) => {
		state.displayedItems = payload;
		state.loading = false;
	}),
};
