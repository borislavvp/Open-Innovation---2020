import React from 'react';
import { IonItem, IonSelect, IonSelectOption, IonLabel } from '@ionic/react';
import { useTranslation } from 'react-i18next';
import { useStoreActions, useStoreState } from '../store';

export const FilterAndSortProducts: React.FC = () => {
	const setSortType = useStoreActions((actions) => actions.search.setSorting);
	const sortItems = useStoreActions((actions) => actions.search.sortItems);
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
						sortItems();
					}}
				>
					<IonSelectOption value="name">{t('sorting.Alphabetical')}</IonSelectOption>
				</IonSelect>
			</IonItem>
		</div>
	);
};

export default FilterAndSortProducts;
