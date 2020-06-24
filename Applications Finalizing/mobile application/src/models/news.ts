import { action, thunk, Action, Thunk } from 'easy-peasy';

export interface INewsItem {
	id: number;
	title: string;
	content: string;
	imgUrl: string;
}

export interface NewsModel {
	items: INewsItem[];
	saveItems: Action<NewsModel, INewsItem[]>;
	getItems: Thunk<NewsModel, void>;
}

export const news: NewsModel = {
	items: [],
	saveItems: action((state, payload) => {
		state.items = payload;
	}),
	getItems: thunk(async (state) => {
		const url = 'https://i416657core.venus.fhict.nl/api/news';

		const req = new Request(url);

		fetch(req).then((response) => {
			response.json().then((r) => {
				console.log(r);
				state.saveItems(
					r.map((item: any, index: number) => ({
						id: index,
						title: item.title,
						content: item.description,
						imgUrl: item.urlToImage,
					}))
				);
			});
		});
	}),
};
