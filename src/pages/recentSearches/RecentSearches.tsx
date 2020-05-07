import React from 'react';
import NavBar from '../../components/NavBar';
import NavBarMenu from '../../components/NavBarMenu';
import { IonContent, IonPage, IonRow } from '@ionic/react';
import RecentlySearchedList from '../../components/RecentlySearchedList';
import SearchBar from '../../components/SearchBar';
import './RecentSearches.css';
import MedicineList from '../../components/Medicine/MedicineList';
import { connect, ConnectedProps } from 'react-redux';
import { RootState } from '../../reducers';

const mapState = (state: RootState) => ({
	searchResult: state.search,
});
const connector = connect(mapState);

type PropsFromRedux = ConnectedProps<typeof connector>;
const RecentSearches: React.FC<PropsFromRedux> = props => {
	function renderContent() {
		if (props.searchResult.result !== '') {
			return <MedicineList medicineName={props.searchResult.result} />;
		} else {
			return (
				<div className="container mx-auto px-10">
					<IonRow>
						<RecentlySearchedList />
					</IonRow>
				</div>
			);
		}
	}
	return (
		<>
			<NavBarMenu />
			<IonPage>
				<NavBar page="recent" />
				<IonContent>
					<SearchBar />
					{renderContent()}
				</IonContent>
			</IonPage>
		</>
	);
};
export default connector(RecentSearches);
