import React from 'react';
import { useTranslation } from 'react-i18next';
import { Layout } from '../../components/Layout';
import { useStoreState, useStoreActions } from '../../store';
import PharmacyListItem from '../../components/Pharmacy/PharmacyListItem';
import { useIonViewWillEnter } from '@ionic/react';
import FilterAndSortPharmacies from '../../components/FilterAndSortPharmacies';

export const AllPharmacies: React.FC = () => {
	const { t } = useTranslation();
	const getPharmacies = useStoreActions((actions) => actions.pharmacies.getItems);
	useIonViewWillEnter(() => {
		getPharmacies(-1);
	});
	const items = useStoreState((state) => state.pharmacies.displayedItems);
	console.log(items.pharmacies !== undefined);
	return (
		<Layout title={t('navigation.Pharmacies')}>
			<FilterAndSortPharmacies />
			{items.pharmacies !== undefined &&
				items.pharmacies.map((item) => <PharmacyListItem key={item.id} {...item} />)}
		</Layout>
	);
};
