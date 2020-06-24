import { action, thunk, Action, Thunk } from 'easy-peasy';
//import { DefaultApi } from 'kigali_api';

export interface IInsurance {
	id: number;
	insurance: String;
}

export interface InsuranceModel {
	loading: boolean;
	items: IInsurance[];
	getItems: Thunk<InsuranceModel>;
	initiateLoading: Action<InsuranceModel>;
	finalizeLoading: Action<InsuranceModel, IInsurance[]>;
}

export const insurances: InsuranceModel = {
	items: [],
	loading: false,
	getItems: thunk(async (actions) => {
		actions.initiateLoading();

		const url = 'https://7rfbtov049.execute-api.us-east-1.amazonaws.com/dev/insurance';

		const data: IInsurance[] = await (await fetch(url))
			.json()
			.then((insurances: IInsurance[]) => insurances)
			.catch(() => []);
		console.log(data);
		actions.finalizeLoading(data);
	}),
	initiateLoading: action((state, payload) => {
		state.loading = true;
	}),
	finalizeLoading: action((state, payload) => {
		state.items = [...payload];
		state.loading = false;
	}),
};
