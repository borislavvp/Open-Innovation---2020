import React from 'react';
import { Redirect, Route, Router } from 'react-router-dom';
import { IonApp, IonRouterOutlet } from '@ionic/react';
import { IonReactRouter } from '@ionic/react-router';

/* Core CSS required for Ionic components to work properly */
import '@ionic/react/css/core.css';

/* Basic CSS for apps built with Ionic */
import '@ionic/react/css/normalize.css';
import '@ionic/react/css/structure.css';
import '@ionic/react/css/typography.css';

/* Optional CSS utils that can be commented out */
import '@ionic/react/css/padding.css';
import '@ionic/react/css/float-elements.css';
import '@ionic/react/css/text-alignment.css';
import '@ionic/react/css/text-transformation.css';
import '@ionic/react/css/flex-utils.css';
import '@ionic/react/css/display.css';

/* Theme variables */
import './theme/variables.css';
import Home from './pages/home/Home';
import RecentSearches from './pages/recentSearches/RecentSearches';
import Pharmacies from './pages/pharmacies/Pharmacies';
import Support from './pages/support/Support';
import Settings from './pages/settings/Settings';
import history from './history';
const App: React.FC = () => (
	<IonApp>
		<IonReactRouter>
			<IonRouterOutlet>
				<Router history={history}>
					<Route path="/home" component={Home} />
					<Route path="/recent" component={RecentSearches} />
					<Route path="/pharmacies" component={Pharmacies} />
					<Route path="/support" component={Support} />
					<Route path="/settings" component={Settings} />
					<Route path="/" render={() => <Redirect to="/home" />} exact={true} />
				</Router>
			</IonRouterOutlet>
		</IonReactRouter>
	</IonApp>
);

export default App;
