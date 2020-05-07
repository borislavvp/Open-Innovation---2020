import React from 'react';

import { DUMMY_NEWS } from '../../dummy_data';
import NewsListItem from './NewsListItem';

const NewsList: React.FC = () => {
	return (
		<>
			{DUMMY_NEWS.map(item => {
				return <NewsListItem key={item.id} title={item.title} imgUrl={item.imgUrl} content={item.content} />;
			})}
		</>
	);
};
export default NewsList;
