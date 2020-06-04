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
		const url =
			'https://newsapi.org/v2/everything?' + 'q=kigali+medicine&' + 'apiKey=67cdb54db7fb4e50896c92751b644f78';

		const req = new Request(url);

		fetch(req).then((response) => {
			response.json().then(({ articles }) => {
				state.saveItems(
					articles.map((item: any, index: number) => ({
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
