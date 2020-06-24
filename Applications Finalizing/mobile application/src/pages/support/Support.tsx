import React from 'react';
import { IonItem, IonLabel, IonList, IonInput, IonTextarea, IonButton, IonSelect, IonSelectOption } from '@ionic/react';
import { useTranslation } from 'react-i18next';
import { Layout } from '../../components/Layout';

export const Support: React.FC = () => {
	const { t } = useTranslation();

	return (
		<Layout title={t('navigation.Support')}>
			<div>
				<IonList>
					<IonItem>
						<IonLabel>{t('Category')}</IonLabel>
						<IonSelect cancelText={t('Cancel')} placeholder={t('Select One')}>
							<IonSelectOption value="general">{t('General question')}</IonSelectOption>
							<IonSelectOption value="pharmacy">{t('navigation.Pharmacies')}</IonSelectOption>
							<IonSelectOption value="insurance">{t('filtering.Insurance')}</IonSelectOption>
							<IonSelectOption value="technical">{t('Technical issues')}</IonSelectOption>
						</IonSelect>
					</IonItem>
					<IonItem>
						<IonInput required type="text" placeholder={t('Title')} />
					</IonItem>
					<IonItem>
						<IonTextarea rows={10} cols={20} required autoGrow={true} placeholder={t('Content')} />
					</IonItem>
				</IonList>
			</div>
			<IonButton expand="block" type="submit" class="px-2">
				{t('Send question')}
			</IonButton>
		</Layout>
	);
};
