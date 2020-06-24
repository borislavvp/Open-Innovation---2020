import React from 'react';
import { IOpeningTimes } from '../../models/pharmacies';
import { useTranslation } from 'react-i18next';
interface Props {
	weekday: string[];
	times: IOpeningTimes[];
}
const PharmacyTimetable: React.FC<Props> = ({ weekday, times }) => {
	const { t } = useTranslation();
	return (
		<div className="flex">
			<div className="flex flex-col mr-6">
				{weekday.map((day: string) => (
					<span key={day} className="text-gray-700 text-sm mb-2">
						{t(`days.${day}`)}
					</span>
				))}
			</div>
			<div className="flex flex-col">
				{weekday.map((day: string) => {
					const item = times.find((t) => t.dayOfWeek.toLowerCase() === day.toLowerCase());
					if (item !== undefined) {
						return (
							<span className="text-gray-700 text-sm mb-2" key={item.id}>
								{item.startTime.substring(0, 5)} - {item.stopTime.substring(0, 5)}
							</span>
						);
					} else {
						return (
							<span key={day} className="text-red-600 text-sm mb-2">
								{t('Closed')}
							</span>
						);
					}
				})}
			</div>
		</div>
	);
};

export default PharmacyTimetable;
