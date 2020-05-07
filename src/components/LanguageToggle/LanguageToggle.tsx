import React, { useState } from 'react';
import FranceFlag from '../../assets/france.png';
import UKFlag from '../../assets/uk.png';
import './LanguageToggle.css';
interface LanguageToggleProps {
	setLocale: Function;
	locale: string;
}
const LanguageToggle: React.FC<LanguageToggleProps> = props => {
	const [langActive, setlangActive] = useState(props.locale);
	return (
		<div className="language">
			<ul>
				<li>
					<img
						className={langActive === 'fr' ? 'active' : ''}
						onClick={() => {
							props.setLocale('fr', false);
							setlangActive('fr');
						}}
						src={FranceFlag}
						alt="French"
					></img>
				</li>
				<li>
					<img
						className={langActive === 'en' ? 'active' : ''}
						onClick={() => {
							props.setLocale('en', false);
							setlangActive('en');
						}}
						src={UKFlag}
						alt="Engish"
					></img>
				</li>
			</ul>
		</div>
	);
};
export default LanguageToggle;
