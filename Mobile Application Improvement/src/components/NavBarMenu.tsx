import React from 'react';
import {
	IonMenu,
	IonHeader,
	IonToolbar,
	IonTitle,
	IonContent,
	IonList,
	IonItem,
	IonIcon,
	IonLabel,
} from '@ionic/react';
import { medkit, headset, settings, newspaper } from 'ionicons/icons';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

interface Page {
	title: string;
	path: string;
	icon: string;
}

const LinkItem: React.FC<Page> = ({ title, path, icon }) => (
	<Link to={path} key={icon} style={{ textDecoration: 'none' }}>
		<IonItem lines="none" href="#">
			<IonIcon slot="start" icon={icon} />
			<IonLabel>{title}</IonLabel>
		</IonItem>
	</Link>
);

export const NavBarMenu: React.FC = () => {
	const { t } = useTranslation();

	return (
		<IonMenu side="start" contentId="main-content">
			<IonHeader>
				<IonToolbar color="primary">
					<IonTitle>{t('navigation.Menu')}</IonTitle>
				</IonToolbar>
			</IonHeader>
			<IonContent>
				<IonList class="menu-item">
					<LinkItem title={t('navigation.News')} path="/" icon={newspaper} />
					<LinkItem title={t('navigation.Pharmacies')} path="/pharmacies" icon={medkit} />
					<LinkItem title={t('navigation.Support')} path="/support" icon={headset} />
					<LinkItem title={t('navigation.Settings')} path="/settings" icon={settings} />
				</IonList>
			</IonContent>
		</IonMenu>
	);
};
