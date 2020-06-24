import { SearchModel, search } from './search';
import { NewsModel, news } from './news';
import { PharmacyModel, pharmacies } from './pharmacies';
import { InsuranceModel, insurances } from './insurances';
export interface StoreModel {
	search: SearchModel;
	news: NewsModel;
	pharmacies: PharmacyModel;
	insurances: InsuranceModel;
}

const model: StoreModel = {
	search,
	news,
	pharmacies,
	insurances,
};

export default model;
