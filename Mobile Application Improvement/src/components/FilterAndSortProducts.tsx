import React from 'react';
import { IonItem, IonSelect, IonSelectOption, IonLabel } from '@ionic/react';
import { useTranslation } from 'react-i18next';
import { useStoreActions, useStoreState } from '../store';

export const FilterAndSortPharmacies: React.FC = () => {
	const setSortType = useStoreActions((actions) => actions.search.setSorting);
	const setFilterType = useStoreActions((actions) => actions.search.setFilter);
	const sortAndFilter = useStoreActions((actions) => actions.search.sortAndFilter);
	const filterType = useStoreState((state) => state.search.filterType);
	const sortingType = useStoreState((state) => state.search.sortingType);
	const { t } = useTranslation();
	return (
		<div className="flex w-full pb-3 -mt-3">
			<IonItem className="flex-1">
				<IonLabel>{t('sorting.SORT BY')}</IonLabel>
				<IonSelect
					value={sortingType}
					onIonChange={(e) => {
						setSortType(e.detail.value);
						sortAndFilter();
					}}
				>
					<IonSelectOption value="name">{t('sorting.Alphabetical')}</IonSelectOption>
					<IonSelectOption value="hprice">{t('sorting.Most Expensive')}</IonSelectOption>
					<IonSelectOption value="lprice">{t('sorting.Least Expensive')}</IonSelectOption>
				</IonSelect>
			</IonItem>
			<IonItem className="flex-1">
				<IonLabel>{t('filtering.FILTER BY')}</IonLabel>
				<IonSelect
					value={filterType}
					onIonChange={(e) => {
						setFilterType(e.detail.value);
						sortAndFilter();
					}}
				>
					<IonSelectOption value="">{t('filtering.None')}</IonSelectOption>
					<IonSelectOption value="pcs">{t('filtering.Pcs')}</IonSelectOption>
					<IonSelectOption value="mg">{t('filtering.Mg')}</IonSelectOption>
				</IonSelect>
			</IonItem>
		</div>
	);
};

export default FilterAndSortPharmacies;
