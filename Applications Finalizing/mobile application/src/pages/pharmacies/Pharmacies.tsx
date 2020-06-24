import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Layout } from '../../components/Layout';
import { useStoreState, useStoreActions } from '../../store';
import PharmacyListItem from '../../components/Pharmacy/PharmacyListItem';
import { Swipeable } from 'react-swipeable';
import FilterAndSortPharmacies from '../../components/FilterAndSortPharmacies';
import { useIonViewWillEnter, IonIcon } from '@ionic/react';
import { IInventoryItem } from '../../models/pharmacies';
import { Map, TileLayer, Marker, Popup } from 'react-leaflet';

import './map.css';
import { closeCircleOutline } from 'ionicons/icons';

const MapLayer: React.FC<{ inventories: IInventoryItem[] }> = ({ inventories }) => {
	return (
		<div className="w-full h-full absolute top-0 left-0">
			<Map center={[-1.968, 30.065]} zoom={14}>
				<TileLayer
					url="https://{s}.api.tomtom.com/map/1/tile/basic/{style}/{z}/{x}/{y}.{ext}?key={apikey}"
					apikey="nR93GrhNvl88LEz1xVYZJ1PkvjXYAoYk"
					style="main"
					ext="png"
				/>
				{inventories.map(
					(item, index) =>
						item.pharmacy.latitude !== null &&
						item.pharmacy.longitude != null && (
							<Marker key={index} position={[item.pharmacy.latitude, item.pharmacy.longitude]}>
								<Popup>{item.pharmacy.name}</Popup>
							</Marker>
						)
				)}
			</Map>
		</div>
	);
};

const getDisplayHeight = (display: 'full' | 'half' | 'none') => {
	switch (display) {
		case 'full':
			return 'calc(100vh - 105px)';
		case 'half':
			return '45vh';
		case 'none':
			return '30px';
	}
};
export const Pharmacies: React.FC = () => {
	const { t } = useTranslation();

	const [display, setDisplay] = useState<'full' | 'half' | 'none'>('half');

	const getInventoryItems = useStoreActions((actions) => actions.pharmacies.getItems);

	useIonViewWillEnter(() => {
		const productId = Number.parseInt(window.location.search.substring(3));
		!isNaN(productId) && getInventoryItems(productId);
	});
	const items = useStoreState((state) => state.pharmacies.displayedItems);

	console.log(items.inventoryItems);

	return (
		<Layout title={t('navigation.Pharmacies')} back="/" search={false}>
			<FilterAndSortPharmacies isPharmacy={false} />
			<div className="relative -mt-3" style={{ height: 'calc(100vh - 105px)' }}>
				{items.inventoryItems !== undefined && <MapLayer inventories={items.inventoryItems} />}
				<div
					className={`fixed bottom-0 bg-white overflow-y-auto w-full${
						display === 'half' ? ' rounded-t-lg' : ''
					}`}
					style={{ height: getDisplayHeight(display), zIndex: 99999 }}
				>
					<Swipeable
						onSwipedUp={() => (display === 'none' ? setDisplay('half') : setDisplay('full'))}
						onSwipedDown={() => (display === 'full' ? setDisplay('half') : setDisplay('none'))}
					>
						<div className="w-full pt-3 pb-1">
							<div className="rounded-full w-6 bg-gray-300 h-1 mx-auto"></div>
						</div>
					</Swipeable>
					{display !== 'none' && items.inventoryItems !== undefined && items.inventoryItems.length > 0 ? (
						items.inventoryItems.map((item) => (
							<PharmacyListItem
								key={item.pharmacy.id}
								pharmacy={item.pharmacy}
								price={item.price}
								currency={item.currency}
							/>
						))
					) : (
						<div className="flex flex-col items-center justify-center h-full text-gray-600">
							<IonIcon icon={closeCircleOutline} className="text-6xl mb-1" />
							<span className="text-xl">{t('No medicine found')}</span>
						</div>
					)}
				</div>
			</div>
		</Layout>
	);
};
export default Pharmacies;
