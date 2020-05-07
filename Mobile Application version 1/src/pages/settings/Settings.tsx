import React from 'react';
import NavBar from '../../components/NavBar';
import NavBarMenu from '../../components/NavBarMenu';
import { IonContent, IonPage, IonItem, IonCard, IonList, IonInput, IonButton } from '@ionic/react';
import MedicineList from '../../components/Medicine/MedicineList';
import undrawFeatures from '../../assets/undrawFeatures.svg';
import SearchBar from '../../components/SearchBar';
import './Settings.css';
import { ConnectedProps, connect } from 'react-redux';
import { RootState } from '../../reducers';
import { Translate } from 'react-redux-i18n';
const mapState = (state: RootState) => ({
	searchResult: state.search,
	enAddressPlaceholder: state.i18n.translations.en.settings.address,
	frAddressPlaceholder: state.i18n.translations.fr.settings.address,
	enInsurancePlaceholder: state.i18n.translations.en.settings.insurance,
	frInsurancePlaceholder: state.i18n.translations.fr.settings.insurance,
	locale: state.i18n.locale,
});
const connector = connect(mapState);

type PropsFromRedux = ConnectedProps<typeof connector>;

const Support: React.FC<PropsFromRedux> = props => {
	function renderContent() {
		if (props.searchResult.result !== '') {
			return <MedicineList medicineName={props.searchResult.result} />;
		} else {
			return (
				<div className="container mx-auto p-12">
					<div className="settingsPageContent">
						<h2>
							<Translate value="settings.title" />
						</h2>
						<IonCard>
							<form>
								<IonList>
									<IonItem>
										<IonInput
											required
											type="text"
											placeholder={
												props.locale === 'en'
													? props.enAddressPlaceholder
													: props.frAddressPlaceholder
											}
										></IonInput>
									</IonItem>
									<IonItem>
										<IonInput
											required
											type="text"
											placeholder={
												props.locale === 'en'
													? props.enInsurancePlaceholder
													: props.frInsurancePlaceholder
											}
										></IonInput>
									</IonItem>
								</IonList>
								<div className="ion-padding">
									<IonButton expand="block" type="submit" class="ion-no-margin">
										<Translate value="settings.save" />
									</IonButton>
								</div>
							</form>
						</IonCard>
					</div>
					<img className="supportImg" src={undrawFeatures} width="80%" height="80%" alt=""></img>
				</div>
			);
		}
	}
	return (
		<>
			<NavBarMenu />
			<IonPage>
				<NavBar page="settings" />
				<IonContent>
					<SearchBar />
					{renderContent()}
				</IonContent>
			</IonPage>
		</>
	);
};
export default connector(Support);
