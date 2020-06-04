import React, { useState } from 'react';
import { IonItem, IonLabel, IonCard, IonIcon } from '@ionic/react';
import { IPharmacy } from '../../models/pharmacies';
import { star } from 'ionicons/icons';
import { useTranslation } from 'react-i18next';

const Stars: React.FC<{ rating: number }> = ({ rating }) => (
	<div className="flex items-center">
		<span className="text-yellow-600 mr-1 text-sm">{rating}.0</span>
		{[...Array(5)].map((_, index) => (
			<IonIcon
				className={`w-3 mr-px ${rating <= index ? 'text-gray-300' : 'text-yellow-600'}`}
				key={index}
				icon={star}
			/>
		))}
	</div>
);

const PharmacyListItem: React.FC<IPharmacy> = ({ name, address, rating }) => {
	const { t } = useTranslation();
	const [open, setOpen] = useState(false);

	return (
		<IonCard>
			<IonItem lines="none" button={true} detail={false} onClick={() => setOpen(!open)}>
				<IonLabel>
					<h1>{name}</h1>
					<Stars rating={rating} />
					<span className="text-gray-700 text-sm mt-2 block">{address}</span>
					<span className="text-gray-700 text-sm mt-1 block">
						<span className="text-red-600">{t('Closed')} · </span>
						{t('Opens at')} 12:30
					</span>
					{open && (
						<div className="mt-4 flex">
							<span className="text-gray-700 text-sm font-medium mr-6">{t('Hours')}: </span>
							<div className="flex">
								<div className="flex flex-col mr-6">
									<span className="text-gray-700 text-sm mb-2">{t('days.Monday')}</span>
									<span className="text-gray-700 text-sm mb-2"> {t('days.Tuesday')}</span>
									<span className="text-gray-700 text-sm mb-2"> {t('days.Wednesday')}</span>
									<span className="text-gray-700 text-sm mb-2"> {t('days.Thursday')}</span>
									<span className="text-gray-700 text-sm mb-2"> {t('days.Friday')}</span>
									<span className="text-gray-700 text-sm mb-2"> {t('days.Saturday')}</span>
									<span className="text-gray-700 text-sm"> {t('days.Sunday')}</span>
								</div>
								<div className="flex flex-col">
									<span className="text-gray-700 text-sm mb-2">11:00 - 21:00</span>
									<span className="text-gray-700 text-sm mb-2">11:00 - 21:00</span>
									<span className="text-gray-700 text-sm mb-2">11:00 - 21:00</span>
									<span className="text-gray-700 text-sm mb-2">12:30 - 21:00</span>
									<span className="text-gray-700 text-sm mb-2">12:30 - 21:00</span>
									<span className="text-red-600 text-sm mb-2">{t('Closed')}</span>
									<span className="text-red-600 text-sm">{t('Closed')}</span>
								</div>
							</div>
						</div>
					)}
				</IonLabel>
			</IonItem>
		</IonCard>
	);
};
export default PharmacyListItem;
