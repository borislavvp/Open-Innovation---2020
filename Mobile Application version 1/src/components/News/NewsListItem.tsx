import React from 'react';
import { isPlatform } from '@ionic/react';
import { IonCard, IonCardHeader, IonCardTitle, IonCardContent } from '@ionic/react';

interface NewsListItemProps {
	title: string;
	imgUrl: string;
	content: string;
}

const NewsListItem: React.FC<NewsListItemProps> = ({ title, imgUrl, content }) => {
	const renderNewsContent = (content: String) => {
		var maxChar = 50;
		if (isPlatform('desktop')) {
			maxChar = 200;
		}
		if (content.length > maxChar) {
			content = content.substring(0, maxChar);
			content = content.substring(0, Math.min(content.length, content.lastIndexOf(' '))) + ' . . .';
		}
		return content;
	};
	return (
		<>
			<IonCard class="ion-card">
				<img src={imgUrl} width="120px" height="100%" alt="" />
				<div>
					<IonCardHeader>
						<IonCardTitle class="card-title">{title}</IonCardTitle>
					</IonCardHeader>

					<IonCardContent>{renderNewsContent(content)}</IonCardContent>
				</div>
			</IonCard>
		</>
	);
};
export default NewsListItem;
