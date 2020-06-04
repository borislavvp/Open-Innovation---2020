import { SearchModel, search } from './search';
import { NewsModel, news } from './news';
import { PharmacyModel, pharmacies } from './pharmacies';

export interface StoreModel {
	search: SearchModel;
	news: NewsModel;
	pharmacies: PharmacyModel;
}

const model: StoreModel = {
	search,
	news,
	pharmacies,
};

export default model;
