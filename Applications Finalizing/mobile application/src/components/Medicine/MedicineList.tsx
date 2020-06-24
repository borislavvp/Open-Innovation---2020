import React from 'react';
import MedicineListItem from './MedicineListItem';
import { IonList, IonIcon } from '@ionic/react';
import { useTranslation } from 'react-i18next';
import { useStoreState } from '../../store';
import { closeCircleOutline } from 'ionicons/icons';
import FilterAndSortProducts from '../FilterAndSortProducts';
export const MedicineList: React.FC = () => {
	const { t } = useTranslation();

	const medicine = useStoreState((state) => state.search.items);

	return (
		<IonList>
			<FilterAndSortProducts />
			{medicine.length === 0 ? (
				<div className="flex flex-col items-center justify-center h-full text-gray-600">
					<IonIcon icon={closeCircleOutline} className="text-6xl mb-1" />
					<span className="text-xl">{t('No medicine found')}</span>
				</div>
			) : (
				medicine.map((item, index) => <MedicineListItem key={index} medicine={item} updateHistory={true} />)
			)}
		</IonList>
	);
};
