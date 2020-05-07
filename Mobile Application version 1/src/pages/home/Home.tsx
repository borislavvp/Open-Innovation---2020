import React from 'react';
import NavBar from '../../components/NavBar';
import NavBarMenu from '../../components/NavBarMenu';
import { IonContent, IonPage } from '@ionic/react';
import './Home.css';
import NewsList from '../../components/News/NewsList';
import SearchBar from '../../components/SearchBar';
import MedicineList from '../../components/Medicine/MedicineList';
import { connect, ConnectedProps } from 'react-redux';
import { RootState } from '../../reducers';
const mapState = (state: RootState) => ({
	searchResult: state.search,
});
const connector = connect(mapState);

type PropsFromRedux = ConnectedProps<typeof connector>;

const Home: React.FC<PropsFromRedux> = props => {
	function renderContent() {
		if (props.searchResult.result !== '' && props.searchResult !== undefined) {
			return <MedicineList medicineName={props.searchResult.result} />;
		} else {
			return <NewsList />;
		}
	}
	return (
		<>
			<NavBarMenu />
			<IonPage>
				<NavBar page="home" />
				<IonContent>
					<SearchBar />
					{renderContent()}
				</IonContent>
			</IonPage>
		</>
	);
};

export default connector(Home);
