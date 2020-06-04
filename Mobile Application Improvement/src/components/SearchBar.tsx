import React from 'react';
import { IonIcon, IonSearchbar, IonButton, IonButtons } from '@ionic/react';
import { barcodeSharp } from 'ionicons/icons';
import { BarcodeScanner } from '@ionic-native/barcode-scanner';
import { isPlatform } from '@ionic/react';
import { useStoreState, useStoreActions } from '../store';
import { useTranslation } from 'react-i18next';

export const SearchBar: React.FC = () => {
	const { t } = useTranslation();

	const searchText = useStoreState((state) => state.search.text);

	const setSearchText = useStoreActions((actions) => actions.search.updateSearchText);

	const openBarCodeScanner = async () => {
		await BarcodeScanner.scan({
			orientation: 'portrait',
			showFlipCameraButton: true,
			resultDisplayDuration: 2000,
		});
	};

	const searchBox = (
		<IonSearchbar
			value={searchText}
			placeholder={t('navigation.Search medicine')}
			onIonChange={(event) => setSearchText(event.detail.value!)}
		/>
	);

	if (isPlatform('cordova')) {
		return (
			<div className="flex items-center">
				{searchBox}
				<IonButtons className="pr-2">
					<IonButton onClick={openBarCodeScanner} color="medium">
						<IonIcon size="large" icon={barcodeSharp} />
					</IonButton>
				</IonButtons>
			</div>
		);
	}

	return searchBox;
};
