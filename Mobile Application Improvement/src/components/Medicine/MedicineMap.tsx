import React, { Component } from 'react';
import PharmacyMap from '../Pharmacy/PharmacyMap';
const { LoadScript, GoogleMap } = require('@react-google-maps/api');

interface MedicineMapProps {
	pharmacies: {
		id: number;
		title: string;
		lat: number;
		lng: number;
		rating: number;
		medecines: { id: number; title: string; price: number }[];
	}[];
}

class MedicineMap extends Component<MedicineMapProps> {
	renderContent() {
		return this.props.pharmacies.map(pharmacy => {
			return <PharmacyMap key={pharmacy.title} pharmacy={pharmacy} />;
		});
	}
	mapContainerStyle = {
		height: '100vh',
		width: '100%',
	};

	center = {
		lat: -1.953173,
		lng: 30.062697,
	};

	render() {
		return (
			<LoadScript googleMapsApiKey="AIzaSyC_J4204nO4CwprAMK5dj9y2hs86LpcWjw">
				<GoogleMap mapContainerStyle={this.mapContainerStyle} zoom={14} center={this.center}>
					{this.renderContent()}
				</GoogleMap>
			</LoadScript>
		);
	}
}

export default MedicineMap;
