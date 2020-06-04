import React, { useEffect } from 'react';
import { IonCard, IonCardHeader, IonCardTitle, IonCardContent } from '@ionic/react';
import { useTranslation } from 'react-i18next';
import { useStoreState, useStoreActions } from '../../store';
import { INewsItem } from '../../models/news';
import { Layout } from '../../components/Layout';
import MedicineListItem from '../../components/Medicine/MedicineListItem';
import { IMedicineItem } from '../../models/search';

const NewsItem: React.FC<INewsItem> = ({ title, content, imgUrl }) => (
	<IonCard className="flex flex-col md:flex-row">
		<img src={imgUrl} className="w-full md:w-64 h-64 md:h-48 object-cover" alt={title} />
		<div>
			<IonCardHeader>
				<IonCardTitle class="card-title">{title}</IonCardTitle>
				<IonCardContent class="px-0 pb-0">{content}</IonCardContent>
			</IonCardHeader>
		</div>
	</IonCard>
);

export const Home: React.FC = () => {
	const { t } = useTranslation();

	const getHistoryItems = useStoreActions((state) => state.search.getHistoryItems);

	const getNewsItems = useStoreActions((state) => state.news.getItems);

	const searchHistory = useStoreState((state) => state.search.history);

	const newsItems = useStoreState((state) => state.news.items);

	useEffect(() => {
		getHistoryItems();
		getNewsItems();
	}, []);

	console.log(newsItems);

	return (
		<Layout title={t('Medicine')} search={true}>
			<div className="flex flex-col">
				{searchHistory.length > 0 && (
					<div className="flex flex-col">
						<span className="px-3 text-2xl text-gray-900 font-light">
							{t('navigation.Recent searches')}
						</span>
						{searchHistory.map((item: IMedicineItem, index: number) => (
							<MedicineListItem key={index} medicine={item} />
						))}
						<div className="h-6" />
					</div>
				)}
				<span className="px-3 text-3xl text-gray-900 font-light">{t('navigation.News')}</span>
				{newsItems.map((item) => (
					<NewsItem key={item.id} {...item} />
				))}
			</div>
		</Layout>
	);
};
