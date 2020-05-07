import React from 'react';
import NavBar from '../../components/NavBar';
import NavBarMenu from '../../components/NavBarMenu';
import { IonContent, IonPage } from '@ionic/react';
import './Pharmacies.css';
import PharmacyList from '../../components/Pharmacy/PharmacyList';
import SearchBar from '../../components/SearchBar';
import MedicineList from '../../components/Medicine/MedicineList';
import { connect, ConnectedProps } from 'react-redux';
import { RootState } from '../../reducers';

const mapState = (state: RootState) => ({
	searchResult: state.search,
});
const connector = connect(mapState);

type PropsFromRedux = ConnectedProps<typeof connector>;
const Pharmacies: React.FC<PropsFromRedux> = props => {
	function renderContent() {
		if (props.searchResult.result !== '') {
			return <MedicineList medicineName={props.searchResult.result} />;
		} else {
			return <PharmacyList />;
		}
	}
	return (
		<>
			<NavBarMenu />
			<IonPage>
				<NavBar page="pharmacies" />
				<IonContent>
					<SearchBar />
					{renderContent()}
				</IonContent>
			</IonPage>
		</>
	);
};
export default connector(Pharmacies);
