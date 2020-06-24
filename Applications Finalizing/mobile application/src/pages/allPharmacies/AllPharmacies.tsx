import React from 'react';
import { useTranslation } from 'react-i18next';
import { Layout } from '../../components/Layout';
import { useStoreState, useStoreActions } from '../../store';
import PharmacyListItem from '../../components/Pharmacy/PharmacyListItem';
import { useIonViewWillEnter, IonIcon } from '@ionic/react';
import FilterAndSortPharmacies from '../../components/FilterAndSortPharmacies';
import { closeCircleOutline } from 'ionicons/icons';

export const AllPharmacies: React.FC = () => {
	const { t } = useTranslation();
	const getPharmacies = useStoreActions((actions) => actions.pharmacies.getItems);
	useIonViewWillEnter(() => {
		getPharmacies(-1);
	});
	const items = useStoreState((state) => state.pharmacies.displayedItems);
	return (
		<Layout title={t('navigation.Pharmacies')}>
			<FilterAndSortPharmacies />
			{items.pharmacies !== undefined && items.pharmacies.length > 0 ? (
				items.pharmacies.map((item) => <PharmacyListItem key={item.id} pharmacy={item} />)
			) : (
				<div className="flex flex-col items-center justify-center h-full text-gray-600">
					<IonIcon icon={closeCircleOutline} className="text-6xl mb-1" />
					<span className="text-xl">{t('No medicine found')}</span>
				</div>
			)}
		</Layout>
	);
};
