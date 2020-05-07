import React from 'react';
import { IonHeader, IonToolbar, IonTitle, IonButtons, IonMenuButton } from '@ionic/react';
import { Translate } from 'react-redux-i18n';
interface NavBarProps {
	page: string;
}
export const NavBar: React.FC<NavBarProps> = ({ page }) => (
	<>
		<IonHeader id="main-content">
			<IonToolbar color="primary">
				<IonButtons slot="start">
					<IonMenuButton></IonMenuButton>
				</IonButtons>
				<IonTitle>
					<Translate value={'navigation.' + page} />
				</IonTitle>
			</IonToolbar>
		</IonHeader>
	</>
);
export default NavBar;
