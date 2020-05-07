import React from 'react';
import NavBar from '../../components/NavBar';
import NavBarMenu from '../../components/NavBarMenu';
import { IonContent, IonPage, IonItem, IonCard, IonList, IonInput, IonTextarea, IonButton } from '@ionic/react';
import MedicineList from '../../components/Medicine/MedicineList';
import undrawMedicine from '../../assets/undrawMedicine.svg';
import SearchBar from '../../components/SearchBar';
import './Support.css';
import { connect, ConnectedProps } from 'react-redux';
import { RootState } from '../../reducers';
import { Translate } from 'react-redux-i18n';
const mapState = (state: RootState) => ({
	searchResult: state.search,
	enTitlePlaceholder: state.i18n.translations.en.support.title,
	frTitlePlaceholder: state.i18n.translations.fr.support.title,
	enContentPlaceholder: state.i18n.translations.en.support.content,
	frContentPlaceholder: state.i18n.translations.fr.support.content,
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
					<div className="supportPageContent">
						<h2>{<Translate value="support.pagetitle" />}</h2>
						<IonCard>
							<form>
								<IonList>
									<IonItem>
										<IonInput required type="email" placeholder="EMAIL"></IonInput>
									</IonItem>
									<IonItem>
										<IonInput
											required
											type="text"
											placeholder={
												props.locale === 'en'
													? props.enTitlePlaceholder
													: props.frTitlePlaceholder
											}
										></IonInput>
									</IonItem>
									<IonItem>
										<IonTextarea
											rows={10}
											cols={20}
											required
											autoGrow={true}
											placeholder={
												props.locale === 'en'
													? props.enContentPlaceholder
													: props.frContentPlaceholder
											}
										></IonTextarea>
									</IonItem>
								</IonList>
								<div className="ion-padding">
									<IonButton expand="block" type="submit" class="ion-no-margin">
										<Translate value="support.send" />
									</IonButton>
								</div>
							</form>
						</IonCard>
					</div>
					<img className="supportImg" src={undrawMedicine} width="80%" height="80%" alt=""></img>
				</div>
			);
		}
	}
	return (
		<>
			<NavBarMenu />
			<IonPage>
				<NavBar page="support" />
				<IonContent>
					<SearchBar />
					{renderContent()}
				</IonContent>
			</IonPage>
		</>
	);
};
export default connector(Support);
