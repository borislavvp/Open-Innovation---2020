import React, { useState } from 'react';
import { IonIcon, IonSearchbar, IonButton, IonButtons } from '@ionic/react';
import { barcodeSharp } from 'ionicons/icons';
import { BarcodeScanner } from '@ionic-native/barcode-scanner';
import { isPlatform } from '@ionic/react';
import { useStoreActions } from '../store';
import { useTranslation } from 'react-i18next';
export const SearchBar: React.FC = () => {
	const { t } = useTranslation();
	const [BarText, setBarText] = useState('');
	const setSearchText = useStoreActions((actions) => actions.search.updateSearchText);

	const handleSearchChange = (value: string): void => {
		setBarText(value);
		setSearchText(value);
	};

	const openBarCodeScanner = async () => {
		await BarcodeScanner.scan({
			orientation: 'portrait',
			showFlipCameraButton: true,
			resultDisplayDuration: 2000,
		});
	};
	const searchBarClearIcon: HTMLElement = document.getElementsByClassName('searchbar-clear-button')[0] as HTMLElement;

	const searchBox = (
		<IonSearchbar
			debounce={0}
			value={BarText}
			placeholder={t('navigation.Search medicine')}
			onIonChange={(event) => {
				handleSearchChange(event.detail.value!);
				if (searchBarClearIcon!) {
					event.detail.value !== ''
						? (searchBarClearIcon.style.display = 'block')
						: (searchBarClearIcon.style.display = 'none');
				}
			}}
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
