import { Plugins } from "@capacitor/core";

const { Storage } = Plugins;

export const StorageService = {
  setItem: async (key: string, value: any) => {
    await Storage.set({
      key,
      value: JSON.stringify(value),
    });
  },
  getItem: async (key: string): Promise<any> => {
    const { value } = await Storage.get({ key });

    return value === null ? null : JSON.parse(value);
  },
};
