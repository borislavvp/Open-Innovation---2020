import React from 'react';
import { IonItem, IonAvatar, IonLabel, IonCard, IonIcon } from '@ionic/react';
import { star } from 'ionicons/icons';
import PharmacyTravelDistance from './PharmacyTravelDistance';

interface MedecineItemProps {
	title: string;
	lat: any;
	lng: any;
	rating: number;
	handleMap: Function;
	//setMapCoordinates: React.Dispatch<React.SetStateAction<Object>>;
}
const PharmacyListItem: React.FC<MedecineItemProps> = ({ title, lat, lng, rating, handleMap }) => {
	function renderRating() {
		var icons = [];
		for (var i = 0; i < rating; i++) {
			icons.push(<IonIcon key={i} icon={star}></IonIcon>);
		}
		return icons;
	}

	return (
		<>
			<IonCard>
				<IonItem
					onClick={() => handleMap({ title, lat, lng, rating })}
					lines="none"
					button={true}
					detail={true}
				>
					<IonAvatar slot="start"></IonAvatar>
					<IonLabel>
						<h1>{title.toUpperCase()}</h1>
						<h3>{renderRating()}</h3>
						<PharmacyTravelDistance lat={lat} lng={lng} />
					</IonLabel>
				</IonItem>
			</IonCard>
		</>
	);
};
export default PharmacyListItem;
