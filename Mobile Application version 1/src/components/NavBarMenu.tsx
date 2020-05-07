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
import { bookOutline, medkitOutline, headsetOutline, settingsOutline, newspaperOutline } from 'ionicons/icons';
import { Translate } from 'react-redux-i18n';
import LanguageToogle from './LanguageToggle/LanguageToggle';
import { setLocale } from 'react-redux-i18n';
import PropTypes from 'prop-types';
import { RootState } from '../reducers';
import { connect, ConnectedProps } from 'react-redux';
import { Link } from 'react-router-dom';
const mapState = (state: RootState) => ({
	locale: state.i18n.locale,
});
const mapDispatchToProps = { setLocale };
const connector = connect(mapState, mapDispatchToProps);

type PropsFromRedux = ConnectedProps<typeof connector>;

const routes = [
	{ title: 'navigation.home', path: '/home', icon: newspaperOutline },
	{ title: 'navigation.recent', path: '/recent', icon: bookOutline },
	{ title: 'navigation.pharmacies', path: '/pharmacies', icon: medkitOutline },
	{ title: 'navigation.support', path: '/support', icon: headsetOutline },
	{ title: 'navigation.settings', path: '/settings', icon: settingsOutline },
];
interface Pages {
	title: string;
	path: string;
	icon: string;
}
export const NavBarMenu: React.FC<PropsFromRedux> = props => {
	function renderMenuItems(pages: Pages[]) {
		return pages.map(p => (
			<Link to={p.path} key={p.icon} style={{ textDecoration: 'none' }}>
				<IonItem lines="none" href="#">
					<IonIcon slot="start" icon={p.icon} />
					<IonLabel>
						<Translate value={p.title} />
					</IonLabel>
				</IonItem>
			</Link>
		));
	}
	return (
		<>
			<IonMenu side="start" contentId="main-content">
				<IonHeader>
					<IonToolbar color="primary">
						<IonTitle>Menu</IonTitle>
						<LanguageToogle locale={props.locale} setLocale={props.setLocale} />
					</IonToolbar>
				</IonHeader>
				<IonContent>
					<IonList class="menu-item">
						<IonItem lines="none"></IonItem>
						{renderMenuItems(routes)}
					</IonList>
				</IonContent>
			</IonMenu>
		</>
	);
};
NavBarMenu.propTypes = {
	locale: PropTypes.string.isRequired,
};
export default connector(NavBarMenu);
