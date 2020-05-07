import { action, thunk, Action, Thunk } from 'easy-peasy';

export interface IPharmacy {
	id: number;
	name: string;
	address: string;
	approved: boolean;
	latitude: string;
	longitude: string;
	rating: number;
}

export interface PharmacyModel {
	items: IPharmacy[];
	loading: boolean;
	filterType: String;
	sortingType: String;
	displayedItems: IPharmacy[];
	getPharmacies: Thunk<PharmacyModel>;
	setFilter: Action<PharmacyModel, String>;
	setSorting: Action<PharmacyModel, String>;
	sortAndFilter: Thunk<PharmacyModel>;
	initiateLoading: Action<PharmacyModel>;
	finalizeLoading: Action<PharmacyModel, IPharmacy[]>;
	finalizeFilteringOrSorting: Action<PharmacyModel, IPharmacy[]>;
}
function sortPharmacies(data: IPharmacy[], option: String): IPharmacy[] {
	if (option === 'name') {
		return [...data.sort((a, b) => a.name.localeCompare(b.name))];
	} else if (option === 'rating') {
		return [...data.sort((a, b) => (a.rating - b.rating) * -1)];
	} else if (option === 'distance') {
		return [...data.sort((a, b) => (a.rating - b.rating) * -1)];
	}
	return data;
}
function filterPharmacies(data: IPharmacy[], option: String): IPharmacy[] {
	if (option === 'rating') {
		return [...data.filter((p) => p.rating > 3)];
	}
	return data;
}
export const pharmacies: PharmacyModel = {
	loading: false,
	items: [],
	filterType: '',
	sortingType: '',
	displayedItems: [],
	getPharmacies: thunk(async (actions) => {
		actions.initiateLoading();
		//todo fetch pharmacies
		const data = [
			{
				id: 1,
				name: 'Kipharma Pharmacy',
				address: 'KN 74 Street, Kigali, Rwanda',
				approved: true,
				latitude: '-1.9522057',
				longitude: '30.0756311',
				rating: 4,
			},
			{
				id: 2,
				name: 'Pharmacie Conseil',
				address: 'KG 521 St, Kigali, Rwanda',
				approved: true,
				latitude: '-1.9503045',
				longitude: '30.0571625',
				rating: 2,
			},
		];
		setTimeout(() => {
			actions.finalizeLoading(data);
		}, 500);
	}),
	sortAndFilter: thunk(async (actions, payload, state) => {
		const filterType = state.getState().filterType;
		const sortType = state.getState().sortingType;

		actions.initiateLoading();
		var data: IPharmacy[] = state.getState().items;

		if (filterType === '') {
			data = sortPharmacies(data, sortType);
		} else {
			data = sortPharmacies(filterPharmacies(data, filterType), sortType);
		}

		setTimeout(() => {
			actions.finalizeFilteringOrSorting(data);
		}, 500);
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
		state.items = [...payload];
		state.displayedItems = [...payload];
		state.loading = false;
	}),
	finalizeFilteringOrSorting: action((state, payload) => {
		state.displayedItems = [...payload];
		state.loading = false;
	}),
};
