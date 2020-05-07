import React, { useState } from 'react';

import { getMedicines } from '../service/storageSevice';
import { IonButton } from '@ionic/react';
import { isPlatform } from '@ionic/react';
import { connect, ConnectedProps } from 'react-redux';
import { updateSearchResult } from '../actions/';
import { RootState } from '../reducers';

const mapState = (state: RootState) => ({
	searchResult: state.search,
});
const connector = connect(mapState, { updateSearchResult });

type PropsFromRedux = ConnectedProps<typeof connector>;
const RecentlySearchedList: React.FC<PropsFromRedux> = props => {
	const [medecineList, setMedecineList] = useState<any>(null);

	if (medecineList == null) {
		getMedicines().then(m => {
			setMedecineList(m);
		});
	}
	function renderMedicineList() {
		return medecineList == null || medecineList.length === 0
			? 'Loading...'
			: (medecineList as string[]).map(item => {
					return (
						<div className=" py-3 " key={item}>
							<IonButton
								size={isPlatform('mobile') ? 'default' : 'large'}
								style={{ textAlign: 'center' }}
								onClick={() => props.updateSearchResult({ result: item })}
							>
								{item}
							</IonButton>
						</div>
					);
			  });
	}
	return <>{renderMedicineList()}</>;
};
export default connector(RecentlySearchedList);
