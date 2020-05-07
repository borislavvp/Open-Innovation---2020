import React from 'react';
import { IonItem, IonSelect, IonSelectOption, IonLabel } from '@ionic/react';
import { useTranslation } from 'react-i18next';
import { useStoreActions } from '../store';
import { useStoreState } from 'easy-peasy';

export const FilterAndSortPharmacies: React.FC = () => {
	const setSortType = useStoreActions((actions) => actions.pharmacies.setSorting);
	const setFilterType = useStoreActions((actions) => actions.pharmacies.setFilter);
	const sortAndFilter = useStoreActions((actions) => actions.pharmacies.sortAndFilter);
	const filterType = useStoreState((state) => state.pharmacies.filterType);
	const sortingType = useStoreState((state) => state.pharmacies.sortingType);
	const { t } = useTranslation();
	return (
		<div className="flex w-full pb-3">
			<IonItem className="flex-1">
				<IonLabel>{t('SORT BY')}</IonLabel>
				<IonSelect
					value={sortingType}
					onIonChange={(e) => {
						setSortType(e.detail.value);
						sortAndFilter();
					}}
				>
					<IonSelectOption value="name">{t('Alphabetical')}</IonSelectOption>
					<IonSelectOption value="rating">{t('Highest Rated')}</IonSelectOption>
					<IonSelectOption value="distance">{t('Closest')}</IonSelectOption>
				</IonSelect>
			</IonItem>
			<IonItem className="flex-1">
				<IonLabel>{t('FILTER BY')}</IonLabel>
				<IonSelect
					value={filterType}
					onIonChange={(e) => {
						setFilterType(e.detail.value);
						sortAndFilter();
					}}
				>
					<IonSelectOption value="">{t('None')}</IonSelectOption>
					<IonSelectOption>{t('Insurance')}</IonSelectOption>
					<IonSelectOption value="rating">{t('Rating > 3')}</IonSelectOption>
				</IonSelect>
			</IonItem>
		</div>
	);
};

export default FilterAndSortPharmacies;
