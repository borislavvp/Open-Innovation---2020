import { createStore, createTypedHooks } from "easy-peasy";
import model, { StoreModel } from "./models";

const { useStoreActions, useStoreState, useStoreDispatch } = createTypedHooks<
  StoreModel
>();

export { useStoreActions, useStoreState, useStoreDispatch };

const store = createStore(model);

export default store;
