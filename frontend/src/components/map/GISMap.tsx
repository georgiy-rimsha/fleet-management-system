import React, { useEffect, useState } from "react";
import {
  MapContainer,
  TileLayer,
  Marker,
  Popup,
  Polyline,
} from "react-leaflet";
import { useOutletContext } from "react-router-dom";
import VehicleCard from "./VehicleCard";
import RotatedMarker from "./RotatedMarker";
import pinkTruck from "../../assets/pink-truck.png";
import whiteTruck from "../../assets/white-truck.png";
import brownTruck from "../../assets/brown-truck.png";
import locationIcon from "../../assets/geomarker.png";
import { Icon } from "leaflet";
import Snackbar from "@mui/material/Snackbar";
import { getVehicles, getPlaces } from "../../services/httpClient.js";
import SockJsClient from "react-stomp";
import "leaflet-rotatedmarker";
import "leaflet/dist/leaflet.css";
import "./GISMap.css";
import AddVehicleCard from "./AddVehicleCard";

const vehicleIcons = [
  new Icon({
    iconUrl: pinkTruck,
    iconSize: [40, 90],
  }),
  new Icon({
    iconUrl: whiteTruck,
    iconSize: [75, 95],
  }),
  new Icon({
    iconUrl: brownTruck,
    iconSize: [30, 90],
  }),
];

const landmarkIcon = new Icon({
  iconUrl: locationIcon,
  iconSize: [38, 38],
});

const SOCKET_URL = `${process.env.REACT_APP_WEBSOCKET_URL}/ws-api`;

function GISMap() {
  const [landmarks, setLandmarks] = useState([]);
  const [vehicles, setVehicles] = useState([]);
  const [isConnected, setConnected] = useState(false);
  const [firstMessageReceived, setFirstMessageReceived] = useState(false);
  const [setTitle] = useOutletContext();
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");

  const handleSnackbarClose = () => {
    setSnackbarOpen(false);
  };

  const handleShowSnackbar = (message) => {
    setSnackbarMessage(message);
    setSnackbarOpen(true);
  };

  const fetchLocations = () => {
    getPlaces()
      .then((res) => {
        setLandmarks(res.data);
        console.log("Successfully fetched locations");
      })
      .catch((err) => {
        console.error("Error trying fetch locations:", err);
      })
      .finally(() => {});
  };

  const fetchVehicles = () => {
    getVehicles()
      .then((res) => {
        setVehicles(res.data);
        console.log("Successfully fetched vehicles");
      })
      .catch((err) => {
        console.log("Error trying fetch vehicles:", err);
      })
      .finally(() => {});
  };

  useEffect(() => {
    setTitle("Map");
    fetchLocations();
    fetchVehicles();
  }, []);

  const onConnect = () => {
    if (isConnected === false) {
      console.log("Connected to websocket");
      setConnected(true);
    }
  };

  const onMessageReceived = (message: any) => {
    if (firstMessageReceived === false) {
      console.log("Message:", message);
      setFirstMessageReceived(true);
    }
    setVehicles((prevVehicles) =>
      prevVehicles.map((vehicle) => {
        if (vehicle.deviceId === message.deviceId) {
          return {
            ...vehicle,
            telemetry: message,
          };
        }
        return vehicle;
      })
    );
  };

  const pickVehicleIcon = (vehicleId) => {
    return vehicleIcons[vehicleId % vehicleIcons.length];
  };

  const [selectedVehicleId, setSelectedVehicleId] = useState();
  const selectedVehicle = vehicles.find((v) => v.id === selectedVehicleId);

  return (
    <>
      <MapContainer center={[50.080345, 14.428973]} zoom={5}>
        <TileLayer
          attribution="Google Maps"
          url="http://{s}.google.com/vt/lyrs=m&x={x}&y={y}&z={z}"
          maxZoom={20}
          subdomains={["mt0", "mt1", "mt2", "mt3"]}
        />
        {selectedVehicle && selectedVehicle.telemetry && (
          <Polyline
            positions={selectedVehicle.telemetry.pathData}
            pathOptions={{ color: "red", dashArray: [10, 10] }}
          />
        )}
        {landmarks.map((landmark, index) => (
          <Marker
            icon={landmarkIcon}
            position={[landmark.latitude, landmark.longitude]}
            key={index}
            data={landmark.name}
          >
            <Popup>
              <AddVehicleCard
                landmark={landmark.name}
                fetchVehicles={fetchVehicles}
                showSnackbar={handleShowSnackbar}
              />
            </Popup>
          </Marker>
        ))}
        {vehicles.map((vehicle) => {
          if (vehicle && vehicle.telemetry) {
            return (
              <RotatedMarker
                key={vehicle.id}
                position={[
                  vehicle.telemetry.latitude,
                  vehicle.telemetry.longitude,
                ]}
                icon={pickVehicleIcon(vehicle.id)}
                rotationOrigin="center"
                rotationAngle={vehicle.telemetry.heading * (180 / Math.PI)}
                data={vehicle.id}
                eventHandlers={{
                  click: (e) => {
                    setSelectedVehicleId(e.target.options.data);
                  },
                }}
              >
                <Popup>
                  <VehicleCard
                    vehicle={vehicle}
                    fetchVehicles={fetchVehicles}
                  />
                </Popup>
              </RotatedMarker>
            );
          } else {
            return null;
          }
        })}
      </MapContainer>
      <Snackbar
        open={snackbarOpen}
        onClose={handleSnackbarClose}
        message={snackbarMessage}
        key={"bottom" + "right"}
        anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
      />
      <SockJsClient
        url={SOCKET_URL}
        topics={["/telematics/geolocation"]}
        onConnect={onConnect}
        onMessage={(message) => onMessageReceived(message)}
        debug={false}
      />
    </>
  );
}

export default GISMap;
