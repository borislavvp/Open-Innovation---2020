import React, { FormEvent } from 'react';
import { IonIcon, IonSearchbar, IonButton, IonButtons } from '@ionic/react';
import { barcodeSharp } from 'ionicons/icons';
import { BarcodeScanner } from '@ionic-native/barcode-scanner';
import { isPlatform } from '@ionic/react';
import { setMedicine } from '../service/storageSevice';
import { connect, ConnectedProps } from 'react-redux';
import { updateSearchResult } from '../actions/';
import { RootState } from '../reducers';
const mapState = (state: RootState) => ({
	searchResult: state.search,
	enPlaceholder: state.i18n.translations.en.searchbar.title,
	frPlaceholder: state.i18n.translations.fr.searchbar.title,
	locale: state.i18n.locale,
});
const connector = connect(mapState, { updateSearchResult });
interface SearchBarState {
	barcodeIconState: string;
	searchState: string;
	searchStateUpdate?: string;
	isChanging: boolean;
}
type PropsFromRedux = ConnectedProps<typeof connector>;

class SearchBar extends React.Component<PropsFromRedux, SearchBarState> {
	constructor(props: PropsFromRedux, state: SearchBarState) {
		super(props);
		this.state = {
			barcodeIconState: 'block',
			searchState: props.searchResult.result,
			isChanging: false,
		};
	}

	openScanner = async () => {
		if (isPlatform('cordova')) {
			const data = await BarcodeScanner.scan({
				orientation: 'portrait',
				showFlipCameraButton: true,
				resultDisplayDuration: 2000,
			});
			console.log('Barcode data:', data.text);
			this.setState({
				barcodeIconState: 'none',
			});
			//this.props.updateSearchResult({ result: data.text });
		}
	};
	onSubmit = (e: FormEvent) => {
		e.preventDefault();
		if (this.state.searchState !== '') {
			setMedicine(this.state.searchState).then(() => {
				this.props.updateSearchResult({ result: this.state.searchState });
			});
		}
	};

	render() {
		const placeholder = this.props.locale === 'en' ? this.props.enPlaceholder : this.props.frPlaceholder;
		return (
			<>
				<form onSubmit={this.onSubmit}>
					<IonSearchbar
						value={this.state.isChanging ? this.state.searchState : this.props.searchResult.result}
						placeholder={placeholder}
						onIonClear={() => this.props.updateSearchResult({ result: '' })}
						onIonFocus={() =>
							this.setState({
								isChanging: true,
								barcodeIconState: 'none',
							})
						}
						onIonBlur={() => {
							this.setState({
								isChanging: false,
								barcodeIconState: 'block',
							});
						}}
						onIonChange={e => {
							const value = (e.target as HTMLTextAreaElement).value;
							this.setState({
								searchState: value,
							});
							if (value === '') this.props.updateSearchResult({ result: '' });
						}}
					/>
				</form>
				<IonButtons class="barcode-scan-button">
					<IonButton
						style={{ display: this.state.barcodeIconState }}
						fill="clear"
						onClick={() => this.openScanner()}
					>
						<IonIcon size="large" icon={barcodeSharp} color="primary" />
					</IonButton>
				</IonButtons>
			</>
		);
	}
}
export default connector(SearchBar);
