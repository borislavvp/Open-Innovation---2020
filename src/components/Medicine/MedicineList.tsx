import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import { DUMMY_PHARMACIES } from '../../dummy_data';
import MedicineListItem from './MedicineListItem';
import { IonList, IonAlert } from '@ionic/react';
import MedicineMap from './MedicineMap';

interface MedicineListProps {
	medicineName: string;
}
const MedicineList: React.FC<MedicineListProps> = ({ medicineName }) => {
	const [isMapVisible, setMapVisible] = useState(false);
	const [coordinates, setMapCoordinates] = useState<any>({});

	let history = useHistory();

	function renderContent() {
		var itemPrice = 0;
		const availablePharmacies = DUMMY_PHARMACIES.filter(p => {
			return p.medecines.every(m => {
				if (m.title.trim().toLowerCase() === medicineName.trim().toLowerCase()) {
					itemPrice += m.price;
					return true;
				} else {
					return false;
				}
			});
		});

		if (isMapVisible) {
			return <MedicineMap pharmacies={availablePharmacies} />;
		} else {
			if (availablePharmacies.length > 0) {
				return (
					<div className="container mx-auto px-5">
						<IonList>
							<MedicineListItem
								setMapCoordinates={setMapCoordinates}
								setMapVisible={setMapVisible}
								title={medicineName}
								pharmacies={availablePharmacies}
								price={itemPrice}
							/>
						</IonList>
					</div>
				);
			} else {
				return (
					<IonAlert
						isOpen
						message="Sorry, there is no pharmacy that has this medicine.
						Please ask for help if it is urgent !"
						buttons={[
							{
								text: 'SUPPORT',
								role: 'cancel',
								handler: _ => history.push('/support'),
							},
						]}
					></IonAlert>
				);
			}
		}
	}
	return <>{renderContent()}</>;
};
export default MedicineList;
