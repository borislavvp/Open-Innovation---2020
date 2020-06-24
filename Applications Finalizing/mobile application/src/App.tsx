import React, { useEffect } from 'react';
import { Route, Router } from 'react-router-dom';
import { IonApp, IonRouterOutlet } from '@ionic/react';
import { IonReactRouter } from '@ionic/react-router';
import { Home } from './pages/home/Home';
import { Pharmacies } from './pages/pharmacies/Pharmacies';
import { Support } from './pages/support/Support';
import { Settings } from './pages/settings/Settings';
import { AllPharmacies } from './pages/allPharmacies/AllPharmacies';
import history from './history';
import i18n from './i18n';

/* Core CSS required for Ionic components to work properly */
import '@ionic/react/css/core.css';

/* Basic CSS for apps built with Ionic */
import '@ionic/react/css/normalize.css';
import '@ionic/react/css/structure.css';
import '@ionic/react/css/typography.css';

/* Theme variables */
import './theme/variables.css';
import { StorageService } from './service/StorageService';

const App: React.FC = () => {
	useEffect(() => {
		(async () => {
			const settings = await StorageService.getItem('SETTINGS');

			const language: string = settings?.language;

			i18n.changeLanguage(language ? language.toLowerCase() : 'en');
		})();
	}, []);

	return (
		<IonApp>
			<IonReactRouter>
				<IonRouterOutlet animated={false}>
					<Router history={history}>
						<Route path="/" component={Home} exact={true} />
						<Route path="/pharmacies" component={AllPharmacies} />
						<Route path="/product" component={Pharmacies} />
						<Route path="/support" component={Support} />
						<Route path="/settings" component={Settings} />
					</Router>
				</IonRouterOutlet>
			</IonReactRouter>
		</IonApp>
	);
};

export default App;
