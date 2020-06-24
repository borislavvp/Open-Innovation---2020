/* eslint-disable no-undef */
import React, { Component } from "react";
import { Plugins } from "@capacitor/core";
import { IonIcon } from "@ionic/react";
import { carSharp } from "ionicons/icons";
import getDistance from "../../utils/getLatLonDistance";
const { Geolocation } = Plugins;

interface PharamcyTravelDistanceProps {
  lat: number;
  lng: number;
}
interface PharamcyTravelDistanceState {
  distance: number;
}

class PharmacyTravelDistance extends Component<
  PharamcyTravelDistanceProps,
  PharamcyTravelDistanceState
> {
  constructor(
    props: PharamcyTravelDistanceProps,
    state: PharamcyTravelDistanceState
  ) {
    super(props, state);
    this.state = {
      distance: 0
    };
  }
  async componentDidMount() {
    const coordinates = await Geolocation.getCurrentPosition();

    const distanceKm = getDistance(
      coordinates.coords.latitude,
      coordinates.coords.longitude,
      this.props.lat,
      this.props.lng
    );

    this.setState({
      distance: parseFloat(distanceKm.toFixed(1))
    });
  }

  render() {
    return (
      <nav className="travelDistance">
        <ul style={{ listStyleType: "none", overflow: "hidden" }}>
          <li style={{ float: "left" }}>
            <p>
              Distance
              <IonIcon
                style={{ paddingRight: "5px", paddingLeft: "10px" }}
                slot="start"
                icon={carSharp}
              ></IonIcon>
              {this.state.distance}km
            </p>
          </li>
        </ul>
      </nav>
    );
  }
}

export default PharmacyTravelDistance;
