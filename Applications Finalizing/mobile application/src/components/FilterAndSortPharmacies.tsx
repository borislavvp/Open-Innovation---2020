import React from 'react';
import { IonItem, IonSelect, IonSelectOption, IonLabel } from '@ionic/react';
import { useTranslation } from 'react-i18next';
import { useStoreActions } from '../store';
import { useStoreState } from 'easy-peasy';

export const FilterAndSortPharmacies: React.FC<{ isPharmacy?: boolean }> = ({ isPharmacy = true }) => {
	const setSortType = useStoreActions((actions) => actions.pharmacies.setSorting);
	const setFilterType = useStoreActions((actions) => actions.pharmacies.setFilter);
	const sortAndFilter = useStoreActions((actions) => actions.pharmacies.sortAndFilter);
	const filterType = useStoreState((state) => state.pharmacies.filterType);
	const sortingType = useStoreState((state) => state.pharmacies.sortingType);

	const { t } = useTranslation();
	return (
		<div className="flex w-full pb-3">
			<IonItem className="flex-1">
				<IonLabel>{t('sorting.SORT BY')}</IonLabel>
				<IonSelect
					cancelText={t('Cancel')}
					value={sortingType}
					onIonChange={(e) => {
						setSortType(e.detail.value);
						sortAndFilter();
					}}
				>
					<IonSelectOption value="name">{t('sorting.Alphabetical')}</IonSelectOption>
					{!isPharmacy && <IonSelectOption value="hprice">{t('sorting.Most Expensive')}</IonSelectOption>}
					{!isPharmacy && <IonSelectOption value="lprice">{t('sorting.Least Expensive')}</IonSelectOption>}
				</IonSelect>
			</IonItem>
			<IonItem className="flex-1">
				<IonLabel>{t('filtering.FILTER BY')}</IonLabel>
				<IonSelect
					cancelText={t('Cancel')}
					value={filterType}
					onIonChange={(e) => {
						setFilterType(e.detail.value);
						sortAndFilter();
					}}
				>
					<IonSelectOption value="">{t('filtering.None')}</IonSelectOption>
					<IonSelectOption value="insurance">{t('filtering.Insurance')}</IonSelectOption>
				</IonSelect>
			</IonItem>
		</div>
	);
};

export default FilterAndSortPharmacies;
