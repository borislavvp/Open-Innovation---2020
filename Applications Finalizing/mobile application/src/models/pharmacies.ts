import { action, thunk, Action, Thunk } from 'easy-peasy';
import { StorageService } from '../service/StorageService';
import { IMedicineItem } from './search';

export interface IItems {
	pharmacies?: IPharmacy[];
	inventoryItems?: IInventoryItem[];
}
export interface IOpeningTimes {
	id: number;
	dayOfWeek: string;
	pharmacyId: number;
	startTime: string;
	stopTime: string;
}
export enum Currency {
	EUR,
	USD,
	RWF,
}
export interface IInventoryItem {
	currency: Currency;
	id: number;
	quantity: number;
	price: number;
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
	insurances: {
		id: number;
		insurance: string;
	}[];
	openingTimes: IOpeningTimes[];
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
	if (option === 'insurance') {
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
	}
	return 0;
}
const sortInventoryItemsByPrice = async (productId: Number) => {
	const url =
		'https://7rfbtov049.execute-api.us-east-1.amazonaws.com/dev/product/' +
		productId +
		'/inventory-items?sortPrice=true';
	return await (await fetch(url))
		.json()
		.then((invetoryItems: IInventoryItem[]) => invetoryItems)
		.catch(() => []);
};
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
				.then((pharmacies: IPharmacy[]) => pharmacies)
				.catch(() => []);
			data = { pharmacies };
		} else {
			const url =
				'https://7rfbtov049.execute-api.us-east-1.amazonaws.com/dev/product/' + payload + '/inventory-items';

			const inventoryItems: IInventoryItem[] = await (await fetch(url))
				.json()
				.then((invetoryItems: IInventoryItem[]) => invetoryItems)
				.catch(() => []);
			data = { inventoryItems };
		}
		console.log(data);
		actions.setFilter('');
		actions.setSorting('');
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
					data.pharmacies = filterPharmacies(data.pharmacies, filterType, settings?.insurance);
					data.pharmacies.sort((a, b) => sortPharmacies(a, b, sortType));
				} else {
					data.pharmacies = filterPharmacies(data.pharmacies, filterType, '');
					data.pharmacies.sort((a, b) => sortPharmacies(a, b, sortType));
				}
			}
		} else if (data.inventoryItems !== undefined) {
			if (sortType === 'hprice') {
				await sortInventoryItemsByPrice(data.inventoryItems[0].product.id).then(
					(r) => (data.inventoryItems = r.reverse())
				);
			} else if (sortType === 'lprice') {
				await sortInventoryItemsByPrice(data.inventoryItems[0].product.id).then(
					(r) => (data.inventoryItems = r)
				);
			} else if (sortType !== '') {
				data.inventoryItems.sort((a, b) => sortPharmacies(a.pharmacy, b.pharmacy, sortType));
			}

			if (filterType === 'insurance') {
				const settings = await StorageService.getItem('SETTINGS');
				data.inventoryItems = filterInventoryItems(data.inventoryItems, filterType, settings?.insurance);
			} else if (filterType !== '') {
				data.inventoryItems = filterInventoryItems(data.inventoryItems, filterType, '');
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
