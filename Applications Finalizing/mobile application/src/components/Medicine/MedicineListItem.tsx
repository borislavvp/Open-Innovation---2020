import React from 'react';
import { IonItem, IonLabel, IonCard } from '@ionic/react';
import { IMedicineItem } from '../../models/search';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router';
import { useStoreActions } from '../../store';

const MedicineListItem: React.FC<{
	medicine: IMedicineItem;
	updateHistory?: boolean;
}> = ({ medicine, updateHistory }) => {
	const { t } = useTranslation();

	const history = useHistory();

	const addHistoryItem = useStoreActions((actions) => actions.search.addHistoryItem);

	const openMedicine = () => {
		if (updateHistory) {
			addHistoryItem(medicine);
		}

		history.push({
			pathname: '/product',
			search: `q=${medicine.id}`,
		});
	};

	return (
		<IonCard>
			<IonItem lines="none" button={true} detail={true} onClick={openMedicine}>
				<IonLabel>
					<h1>{medicine.name}</h1>
					<p>
						{t('Available at')} {medicine.amountOfPharmacies} {t('pharmacies')}
					</p>
				</IonLabel>
			</IonItem>
		</IonCard>
	);
};
export default MedicineListItem;
