import React from 'react';
import {
	IonPage,
	IonContent,
	IonHeader,
	IonToolbar,
	IonButtons,
	IonMenuButton,
	IonTitle,
	IonIcon,
	IonButton,
	IonSpinner,
} from '@ionic/react';
import { SearchBar } from './SearchBar';
import { useStoreState } from '../store';
import { MedicineList } from './Medicine/MedicineList';
import { NavBarMenu } from './NavBarMenu';
import { arrowBack } from 'ionicons/icons';
import { useHistory } from 'react-router';

export const Layout: React.FC<{
	title: string;
	search?: boolean;
	back?: string;
}> = ({ title, search = false, back, children }) => {
	const history = useHistory();

	const searchText = useStoreState((state) => state.search.text);
	const loadingState = useStoreState((state) => state.search.loading || state.pharmacies.loading);
	return (
		<>
			<NavBarMenu />
			<IonPage>
				<IonHeader id="main-content">
					<IonToolbar color="primary">
						<IonButtons slot="start">
							{back ? (
								<IonButton onClick={() => history.push(back)}>
									<IonIcon icon={arrowBack} size="large" className="w-6" />
								</IonButton>
							) : (
								<IonMenuButton />
							)}
						</IonButtons>
						<IonTitle>{title}</IonTitle>
					</IonToolbar>
				</IonHeader>
				{search && <SearchBar />}
				<IonContent>
					{loadingState ? (
						<div className="flex flex-col items-center justify-center h-full text-gray-600">
							<IonSpinner style={{ width: '60px', height: '60px' }} color="primary" name="lines" />
						</div>
					) : !search || searchText.trim() === '' ? (
						children
					) : (
						<MedicineList />
					)}
				</IonContent>
			</IonPage>
		</>
	);
};
