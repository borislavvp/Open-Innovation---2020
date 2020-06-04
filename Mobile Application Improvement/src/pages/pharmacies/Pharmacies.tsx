import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Layout } from '../../components/Layout';
import { useStoreState, useStoreActions } from '../../store';
import PharmacyListItem from '../../components/Pharmacy/PharmacyListItem';
import GoogleMapReact from 'google-map-react';
import { Swipeable } from 'react-swipeable';
import FilterAndSortPharmacies from '../../components/FilterAndSortPharmacies';
import { useIonViewWillEnter } from '@ionic/react';
import { IInventoryItem } from '../../models/pharmacies';
import { withRouter, RouteComponentProps } from 'react-router';
import { pure } from 'recompose';

const Marker: React.FC<any> = ({ children }) => (
	<div className="rounded-full bg-red-700 flex justify-center items-center text-white font-medium w-8 h-8 text-lg">
		{children}
	</div>
);
class MapLayer extends React.PureComponent<{ inventories: IInventoryItem[] }> {
	render() {
		return (
			<div className="w-full h-screen absolute top-0 left-0">
				<GoogleMapReact
					defaultCenter={{
						lat: -1.968,
						lng: 30.065,
					}}
					defaultZoom={14}
					options={() => ({
						zoomControl: false,
						fullscreenControl: false,
					})}
				>
					{this.props.inventories.map((item, index) => (
						<Marker key={index} lat={item.pharmacy.latitude} lng={item.pharmacy.longitude}>
							{index + 1}
						</Marker>
					))}
				</GoogleMapReact>
			</div>
		);
	}
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
export const Pharmacies: React.FC<RouteComponentProps> = () => {
	const { t } = useTranslation();

	const [display, setDisplay] = useState<'full' | 'half' | 'none'>('half');

	const getInventoryItems = useStoreActions((actions) => actions.pharmacies.getItems);

	useIonViewWillEnter(() => {
		const productId = Number.parseInt(window.location.search.substring(3));
		!isNaN(productId) && getInventoryItems(productId);
	});

	const items = useStoreState((state) => state.pharmacies.displayedItems);

	return (
		<Layout title={t('navigation.Pharmacies')} back="/" search={false}>
			<FilterAndSortPharmacies isPharmacy={false} />
			<div className="relative -mt-3">
				{items.inventoryItems !== undefined && <MapLayer inventories={items.inventoryItems} />}
				<div
					className={`fixed bottom-0 bg-white overflow-y-auto w-full${
						display === 'half' ? ' rounded-t-lg' : ''
					}`}
					style={{ height: getDisplayHeight(display) }}
				>
					<Swipeable
						onSwipedUp={() => (display === 'none' ? setDisplay('half') : setDisplay('full'))}
						onSwipedDown={() => (display === 'full' ? setDisplay('half') : setDisplay('none'))}
					>
						<div className="w-full pt-3 pb-1">
							<div className="rounded-full w-6 bg-gray-300 h-1 mx-auto"></div>
						</div>
					</Swipeable>
					{display !== 'none' &&
						items.inventoryItems !== undefined &&
						items.inventoryItems.map((item) => (
							<PharmacyListItem key={item.pharmacy.id} {...item.pharmacy} />
						))}
				</div>
			</div>
		</Layout>
	);
};
export default withRouter(Pharmacies);
