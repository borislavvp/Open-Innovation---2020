import React from 'react';
import { IonItem, IonAvatar, IonLabel, IonCard } from '@ionic/react';
import { Plugins } from '@capacitor/core';
import { Translate } from 'react-redux-i18n';

const { Geolocation } = Plugins;
interface MedecineItemProps {
	title: string;
	price: number;
	pharmacies: {
		id: number;
		title: string;
		lat: any;
		lng: any;
		rating: number;
		medecines: { id: number; title: string; price: number }[];
	}[];
	setMapVisible: React.Dispatch<React.SetStateAction<boolean>>;
	setMapCoordinates: React.Dispatch<React.SetStateAction<Object>>;
}
const MedicineListItem: React.FC<MedecineItemProps> = ({
	title,
	pharmacies,
	price,
	setMapVisible,
	setMapCoordinates,
}) => {
	const getCurrentPosition = async () => {
		const cordinates = await Geolocation.getCurrentPosition();
		setMapCoordinates(cordinates.coords);
	};
	return (
		<>
			<IonCard>
				<IonItem
					lines="none"
					button={true}
					onClick={() => {
						getCurrentPosition();
						setMapVisible(true);
					}}
					detail={true}
				>
					<IonAvatar slot="start"></IonAvatar>
					<IonLabel>
						<h1>{title.toUpperCase()}</h1>
						<h3>
							{' '}
							<Translate value="product.AvgPrice" /> {price / pharmacies.length} RWF
						</h3>
						<p>
							<Translate value="product.AvailableAt" /> {pharmacies.length} pharmacies
						</p>
					</IonLabel>
				</IonItem>
			</IonCard>
		</>
	);
};
export default MedicineListItem;
