import React, { useState, useEffect } from 'react';
import { IonItem, IonList, IonInput, IonLabel, IonSelect, IonSelectOption } from '@ionic/react';
import { useTranslation } from 'react-i18next';
import { Layout } from '../../components/Layout';
import { StorageService } from '../../service/StorageService';
import i18n from '../../i18n';
import { useStoreActions, useStoreState } from '../../store';

const Header: React.FC<{ top?: boolean }> = ({ top, children }) => (
	<span className={`px-4 py-2 bg-gray-200 font-medium text-sm text-gray-800 tracking-wide flex${top ? ' mt-2' : ''}`}>
		{children}
	</span>
);

export const Settings: React.FC = () => {
	const { t } = useTranslation();

	const getInsurances = useStoreActions((actions) => actions.insurances.getItems);
	const insurances = useStoreState((state) => state.insurances.items);
	const [settings, setSettings] = useState<any>({
		address: '',
		city: '',
		country: '',
		insurance: '',
		language: 'EN',
	});

	useEffect(() => {
		(async () => {
			const oldSettings = await StorageService.getItem('SETTINGS');

			setSettings({ ...settings, ...oldSettings });
			await getInsurances();
		})();
	}, []);

	useEffect(() => {
		StorageService.setItem('SETTINGS', settings);

		const language: string = settings?.language;

		i18n.changeLanguage(language ? language.toLowerCase() : 'en');
	}, [settings]);

	return (
		<Layout title={t('navigation.Settings')}>
			<IonList>
				<Header>{t('Location')}</Header>
				<IonItem>
					<IonLabel position="stacked">{t('Address')}</IonLabel>
					<IonInput
						required
						type="text"
						placeholder={t('Address')}
						onIonChange={(event) =>
							setSettings({
								...settings,
								address: event.detail.value,
							})
						}
						value={settings.address}
					/>
				</IonItem>
				<IonItem>
					<IonLabel position="stacked">{t('City')}</IonLabel>
					<IonInput
						required
						type="text"
						placeholder={t('City')}
						onIonChange={(event) =>
							setSettings({
								...settings,
								city: event.detail.value,
							})
						}
						value={settings.city}
					/>
				</IonItem>
				<IonItem>
					<IonLabel position="stacked">{t('Country')}</IonLabel>
					<IonInput
						required
						type="text"
						placeholder={t('Country')}
						onIonChange={(event) =>
							setSettings({
								...settings,
								country: event.detail.value,
							})
						}
						value={settings.country}
					/>
				</IonItem>
				<Header top>{t('filtering.Insurance')}</Header>
				<IonItem>
					<IonLabel>{t('Insurance provider')}</IonLabel>
					<IonSelect
						cancelText={t('Cancel')}
						value={settings.insurance}
						onIonChange={(event) =>
							setSettings({
								...settings,
								insurance: event.detail.value,
							})
						}
					>
						<IonSelectOption value="none">{t('None')}</IonSelectOption>
						{insurances.map((i) => (
							<IonSelectOption key={i.id} value={i.insurance}>
								{i.insurance}
							</IonSelectOption>
						))}
					</IonSelect>
				</IonItem>
				<Header top>{t('Application')}</Header>
				<IonItem>
					<IonLabel>{t('Language')}</IonLabel>
					<IonSelect
						cancelText={t('Cancel')}
						value={settings.language}
						onIonChange={(event) =>
							setSettings({
								...settings,
								language: event.detail.value,
							})
						}
					>
						<IonSelectOption value="EN">{t('English')}</IonSelectOption>
						<IonSelectOption value="FR">{t('French')}</IonSelectOption>
						<IonSelectOption value="KN">{t('Kinyarwanda')}</IonSelectOption>
					</IonSelect>
				</IonItem>
			</IonList>
		</Layout>
	);
};
