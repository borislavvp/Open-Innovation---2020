import { Plugins } from '@capacitor/core';

const { Storage } = Plugins;

export const setMedicine = async (medicine: String) => {
	const formatedMedicine = medicine.toLocaleLowerCase().trim();
	await Storage.set({
		key: formatedMedicine,
		value: 'medecine',
	});
};

export const clearStorage = async () => {
	await Storage.clear();
};
export const getMedicines = async () => {
	const { keys } = await Storage.keys();
	return keys;
};
