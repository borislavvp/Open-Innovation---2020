import React, { useState } from 'react';
import { IonItem, IonLabel, IonCard } from '@ionic/react';
import { IPharmacy, IOpeningTimes, Currency } from '../../models/pharmacies';
import { useTranslation } from 'react-i18next';
import PharmacyTimetable from './PharmacyTimetable';

interface Props {
	pharmacy: IPharmacy;
	price?: number;
	currency?: Currency;
}
const weekday = ['SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'];

const getNextWorkingDay = (from: number, to: number, times: IOpeningTimes[]): IOpeningTimes => {
	var res = times.find((t) => weekday.slice(from, to).find((day) => day === t.dayOfWeek) !== undefined);
	return res !== undefined ? res : { id: -1, dayOfWeek: '', pharmacyId: -1, startTime: '', stopTime: '' };
};
const handleNextWorkingDay = (times: IOpeningTimes[]): IOpeningTimes => {
	const index = new Date().getDay();
	var nextWorkingDay: IOpeningTimes = getNextWorkingDay(index + 1, weekday.length, times);
	if (nextWorkingDay.id !== -1) {
		return nextWorkingDay;
	} else {
		nextWorkingDay = getNextWorkingDay(0, index, times);
		return nextWorkingDay;
	}
};
const PharmacyListItem: React.FC<Props> = ({ pharmacy, price, currency }) => {
	const { name, address, openingTimes } = pharmacy;

	const { t } = useTranslation();
	const [open, setOpen] = useState(false);

	const currentTime: string = new Date().getHours() + ':' + new Date().getMinutes() + ':' + new Date().getSeconds();

	openingTimes.sort(function SortByDay(a, b) {
		let day2 = b.dayOfWeek.toLowerCase();
		let day1 = a.dayOfWeek.toLowerCase();
		return weekday.indexOf(day1) - weekday.indexOf(day2);
	});

	const currentDay: IOpeningTimes | undefined = openingTimes.find(
		(t) => t.dayOfWeek.toLowerCase() === weekday[new Date().getDay()].toLowerCase()
	);
	const isPharmacyOpen =
		currentDay !== undefined && currentTime > currentDay.startTime && currentTime < currentDay.stopTime;

	const nextWorkingDay = handleNextWorkingDay(openingTimes);

	return (
		<IonCard>
			<IonItem lines="none" button={true} detail={false} onClick={() => setOpen(!open)}>
				<IonLabel>
					<h1>{name}</h1>
					<span className="text-gray-700 text-sm mt-1 block">{address}</span>
					{!isPharmacyOpen ? (
						<span className="text-gray-700 text-sm mt-1 block">
							<span className="text-red-600">{t('Closed')} · </span>
							{nextWorkingDay.id !== -1 ? (
								<span>
									{t('Opens at ')} {nextWorkingDay?.startTime.substring(0, 5)}{' '}
									{t(`days.${nextWorkingDay?.dayOfWeek}`)}
								</span>
							) : (
								t('Temporarily Closed')
							)}
						</span>
					) : (
						<span className="text-gray-700 text-sm mt-1 block">
							<span className="text-green-600">{t('Open')} · </span>
						</span>
					)}
					{open && (
						<div className="mt-4 flex">
							<span className="text-gray-700 text-sm font-medium mr-6">{t('Hours')}: </span>
							<PharmacyTimetable weekday={weekday} times={openingTimes} />
						</div>
					)}
				</IonLabel>
				<span className="text-gray-700 text-2xl font-light flex items-center" slot="end">
					{price?.toLocaleString('en-US', { minimumFractionDigits: 2 })} {currency?.toString()}
				</span>
			</IonItem>
		</IonCard>
	);
};
export default PharmacyListItem;
