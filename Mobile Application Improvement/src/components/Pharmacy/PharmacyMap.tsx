/* eslint-disable no-undef */
import React from "react";
import { Plugins, GeolocationPosition } from "@capacitor/core";
import getDistance from "../../utils/getLatLonDistance";
import { IonButton, IonIcon } from "@ionic/react";
import { star } from "ionicons/icons";
import { DirectionsRenderer, Marker, InfoWindow } from "@react-google-maps/api";
const { Geolocation } = Plugins;
//const markerIcon =
//	'https://cdn2.iconfinder.com/data/icons/IconsLandVistaMapMarkersIconsDemo/256/MapMarker_Marker_Outside_Chartreuse.png';

interface PharmacyMapProps {
  pharmacy: {
    title: string;
    lat: number;
    lng: number;
    rating: number;
  };
}
interface PharmacyMapState {
  initialLocation: { latitude: number; longitude: number };
  showingInfoWindow: boolean;
  showDirection: boolean;
  directions?: google.maps.DirectionsResult;
  activeMarker: google.maps.Marker;
}
class PharmacyMap extends React.Component<PharmacyMapProps, PharmacyMapState> {
  constructor(props: PharmacyMapProps, state: PharmacyMapState) {
    super(props, state);

    this.state = {
      initialLocation: { latitude: 0, longitude: 0 },
      showingInfoWindow: false,
      showDirection: false,
      directions: undefined,
      activeMarker: new google.maps.Marker()
    };
  }
  async componentDidMount() {
    const coordinates = await Geolocation.getCurrentPosition();

    this.calculateDirection(coordinates.coords);
  }
  calculateDirection(coordinates: GeolocationPosition["coords"]) {
    const DirectionsService = new google.maps.DirectionsService();
    DirectionsService.route(
      {
        origin: new google.maps.LatLng(
          coordinates.latitude,
          coordinates.longitude
        ),
        destination: new google.maps.LatLng(
          this.props.pharmacy.lat,
          this.props.pharmacy.lng
        ),
        travelMode: google.maps.TravelMode.DRIVING
      },
      (result, status) => {
        if (status === google.maps.DirectionsStatus.OK) {
          this.setState({
            initialLocation: coordinates,
            directions: result
          });
        } else {
          console.error(`error fetching directions ${result}`);
        }
      }
    );
  }
  renderRating(rating: number) {
    var icons = [];
    for (var i = 0; i < rating; i++) {
      icons.push(<IonIcon key={i} icon={star}></IonIcon>);
    }
    return icons;
  }
  watchPosition() {
    Geolocation.watchPosition({}, (position, err) => {
      console.log("POS", position);
    });
  }
  openGoogleMaps = (lat: number, lng: number) => {
    window.open("https://maps.google.com?q=" + lat + "," + lng);
  };
  showDirection = () =>
    this.setState({ showDirection: !this.state.showDirection });
  onMarkerClick = () => {
    this.setState({
      showingInfoWindow: true
    });
  };
  onLoadMarker = (marker: google.maps.Marker) => {
    this.setState({
      activeMarker: marker
    });
  };

  onInfoWindowClose = () => {
    this.setState({
      showingInfoWindow: false
    });
  };

  render() {
    const { title, lat, lng, rating } = this.props.pharmacy;
    const loc = this.state.initialLocation;
    return (
      <div>
        <Marker
          onLoad={this.onLoadMarker}
          onClick={() => this.onMarkerClick()}
          position={{ lat: lat, lng: lng }}
        >
          {this.state.showingInfoWindow && (
            <InfoWindow
              anchor={this.state.activeMarker}
              onCloseClick={() => this.onInfoWindowClose()}
            >
              <div>
                <h3>{title}</h3>
                {this.renderRating(rating)}
                <p>
                  Distance
                  <strong>
                    {getDistance(loc.latitude, loc.longitude, lat, lng).toFixed(
                      1
                    )}
                    km
                  </strong>
                </p>
                {this.state.showDirection && (
                  <IonButton
                    expand="block"
                    onClick={() => this.openGoogleMaps(lat, lng)}
                  >
                    GOOGLE MAPS
                  </IonButton>
                )}
                <IonButton expand="block" onClick={() => this.showDirection()}>
                  {this.state.showDirection ? (
                    "EXIT NAVIGATION"
                  ) : (
                    <span>Navigate</span>
                  )}
                </IonButton>
              </div>
            </InfoWindow>
          )}
        </Marker>

        {this.state.showDirection && (
          <div>
            <Marker
              zIndex={2}
              options={{
                icon: {
                  //url: markerIcon,
                  path: google.maps.SymbolPath.CIRCLE,
                  scale: 12,
                  fillColor: "#0073ff",
                  fillOpacity: 1,
                  strokeColor: "#FFFAFA",
                  strokeWeight: 4,
                  scaledSize: new window.google.maps.Size(50, 50)
                }
              }}
              position={
                new google.maps.LatLng(
                  this.state.initialLocation.latitude,
                  this.state.initialLocation.longitude
                )
              }
            />
            <Marker
              options={{
                icon: {
                  //url: markerIcon,
                  path: google.maps.SymbolPath.CIRCLE,
                  scale: 14,
                  fillOpacity: 1,
                  strokeColor: "#1f39a1",
                  strokeWeight: 1,
                  scaledSize: new window.google.maps.Size(50, 50)
                }
              }}
              position={
                new google.maps.LatLng(
                  this.state.initialLocation.latitude,
                  this.state.initialLocation.longitude
                )
              }
            />
            <DirectionsRenderer
              directions={this.state.directions}
              options={{
                markerOptions: {
                  icon: {
                    path: google.maps.SymbolPath.CIRCLE,
                    scale: 8.5,
                    fillColor: "#FFFAFA",
                    fillOpacity: 1,
                    strokeWeight: 1
                  }
                },
                polylineOptions: {
                  strokeColor: "#0073ff",
                  strokeWeight: 5,
                  strokeOpacity: 1.0
                }
              }}
            />
          </div>
        )}
      </div>
    );
  }
}
export default PharmacyMap;
