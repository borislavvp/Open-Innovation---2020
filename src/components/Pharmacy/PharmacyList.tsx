import React, { useState } from 'react';
import { DUMMY_PHARMACIES } from '../../dummy_data';
import PharmacyListItem from './PharmacyListItem';
import PharmacyMap from './PharmacyMap';
//import { withScriptjs, withGoogleMap, GoogleMap } from 'react-google-maps';
import { GoogleMap, LoadScript } from '@react-google-maps/api';
interface Pharmacy {
	title: string;
	lat: number;
	lng: number;
	rating: number;
}

const PharmacyList: React.FC = () => {
	const [pharmacyDetails, setPharmacyDetails] = useState<Pharmacy>({
		title: '',
		lat: 1,
		lng: 1,
		rating: -1,
	});
	const handleMap = (pharmacy: Pharmacy) => {
		setPharmacyDetails(pharmacy);
	};
	const mapContainerStyle = {
		height: '100vh',
		width: '100%',
	};

	const center = {
		lat: pharmacyDetails.lat,
		lng: pharmacyDetails.lng,
	};
	function renderContent() {
		if (pharmacyDetails.title !== '') {
			const map = document.getElementById('map');
			if (map !== null) map.style.display = ' block';

			return (
				<LoadScript googleMapsApiKey="API_KEY">
					<GoogleMap mapContainerStyle={mapContainerStyle} zoom={14} center={center}>
						<PharmacyMap pharmacy={pharmacyDetails} />
					</GoogleMap>
				</LoadScript>
			);
		} else {
			return DUMMY_PHARMACIES.map(p => {
				return (
					<div className="container mx-auto px-5" key={p.id}>
						<PharmacyListItem
							title={p.title}
							lat={p.lat}
							lng={p.lng}
							rating={p.rating}
							handleMap={handleMap}
						/>
					</div>
				);
			});
		}
	}
	return <>{renderContent()}</>;
};
export default PharmacyList;
