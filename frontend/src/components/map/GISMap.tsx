import React, { useEffect, useState, useRef, forwardRef } from "react";
import {
  MapContainer,
  TileLayer,
  Marker,
  Popup,
  Polyline,
} from "react-leaflet";
import Stack from "@mui/material/Stack";
import TextField from "@mui/material/TextField";
import Slider from "@mui/material/Slider";
import MarkerClusterGroup from "react-leaflet-cluster";
import LocalShippingTwoToneIcon from "@mui/icons-material/LocalShippingTwoTone";
import VehicleCard from "./VehicleCard";
import RotatedMarker from "./RotatedMarker";
import marker from "../../assets/truck3.png";
import { Icon } from "leaflet";
import {
  saveVehicle,
  updateVehicle,
  getVehicles,
  getPlaces,
  deleteVehicle,
} from "../../services/client.js";
import SockJsClient from "react-stomp";
import L from "leaflet";
import "leaflet-rotatedmarker";
import "leaflet/dist/leaflet.css";
import "./GISMap.css";
import { Button } from "@mui/material";
import AddVehicleCard from "./AddVehicleCard";

const vehicleIcon = new Icon({
  // iconUrl:
  // "https://t4.ftcdn.net/jpg/01/90/06/69/360_F_190066933_cyOkhH7qs7gWc8wy08LP4FsiFNhhpSZH.jpg",
  // iconUrl:
  // "https://www.shutterstock.com/image-vector/truck-top-view-icon-lorry-260nw-525177583.jpg",
  // iconUrl:
  //   "https://thumbs.dreamstime.com/b/truck-top-view-cargo-transport-color-icon-isolated-white-background-263423715.jpg",
  iconUrl: marker,
  iconSize: [30, 90],
});
const landmarkIcon = new Icon({
  iconUrl: "https://cdn-icons-png.flaticon.com/512/3721/3721984.png",
  iconSize: [38, 38],
});

const SOCKET_URL = "http://localhost:8082/ws-message";
let onConnected = () => {
  console.log("Connected!!");
};

export default function GISMap() {
  const [landmarks, setLandmarks] = useState([]);
  const [vehicles, setVehicles] = useState([]);

  const fetchLocations = () => {
    getPlaces()
      .then((res) => {
        setLandmarks(res.data);
      })
      .catch((err) => {
        console.log("Error trying feth locations: ", JSON.stringify(err));
      })
      .finally(() => {});
  };

  const fetchVehicles = () => {
    getVehicles()
      .then((res) => {
        setVehicles(res.data);
        console.log(res.data);
      })
      .catch((err) => {
        console.log("Error trying fetch vehicles", JSON.stringify(err));
      })
      .finally(() => {});
  };

  useEffect(() => {
    fetchLocations();
    fetchVehicles();
    console.log(vehicles);
  }, []);

  const handleGeolocationMessage = (message) => {
    const vehicle = vehicles.find((veh) => veh.id === message.vehicleId);

    if (vehicle) {
      const updatedVehicles = vehicles.map((veh) =>
        veh.id === message.vehicleId
          ? {
              ...veh,
              latitude: message.latitude,
              longitude: message.longitude,
              heading: message.heading,
              pathData: message.pathData,
            }
          : veh
      );

      setVehicles(updatedVehicles);
    }
  };

  let onMessageReceived = (msg) => {
    console.log(JSON.stringify(msg));
    console.log(JSON.stringify(vehicles));
    handleGeolocationMessage(msg);
  };

  const polyline = [
    [57.913534, 59.990375],
    [57.911389, 59.987534],
    [57.911612, 59.98737],
  ];

  const fillBlueOptions = { color: "red", dashArray: [10, 10] };
  const blackOptions = { color: "black" };
  const limeOptions = { color: "lime" };
  const purpleOptions = { color: "purple" };
  const redOptions = { color: "red" };

  const [selectedVehicleId, setSelectedVehicleId] = useState();
  const selectedVehicle = vehicles.find((v) => v.id === selectedVehicleId);

  return (
    <>
      <MapContainer center={[50.080345, 14.428973]} zoom={5}>
        {/* <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          // url="https://api.maptiler.com/maps/cadastre-satellite/{z}/{x}/{y}.png?key=UcLvBWmWMFXRRhkhe5st"
        /> */}
        {/* WATERCOLOR CUSTOM TILES */}
        {/* <TileLayer
          attribution='Map tiles by <a href="http://stamen.com">Stamen Design</a>, <a href="http://creativecommons.org/licenses/by/3.0">CC BY 3.0</a> &mdash; Map data &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://stamen-tiles-{s}.a.ssl.fastly.net/watercolor/{z}/{x}/{y}.jpg"
        /> */}
        {/* GOOGLE MAPS TILES */}
        <TileLayer
          attribution="Google Maps"
          url="http://{s}.google.com/vt/lyrs=m&x={x}&y={y}&z={z}" // regular
          // url="http://{s}.google.com/vt/lyrs=s,h&x={x}&y={y}&z={z}" // satellite
          // url="http://{s}.google.com/vt/lyrs=p&x={x}&y={y}&z={z}" // terrain
          maxZoom={20}
          subdomains={["mt0", "mt1", "mt2", "mt3"]}
        />
        {selectedVehicle && selectedVehicle.pathData && (
          <Polyline
            positions={selectedVehicle.pathData}
            pathOptions={fillBlueOptions}
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
              />
            </Popup>
          </Marker>
        ))}
        {vehicles.map((vehicle) => {
          if (
            vehicle &&
            vehicle.latitude &&
            vehicle.longitude &&
            vehicle.heading
          ) {
            return (
              <RotatedMarker
                key={vehicle.id}
                position={[vehicle.latitude, vehicle.longitude]}
                icon={vehicleIcon}
                rotationOrigin="center"
                rotationAngle={vehicle.heading * (180 / Math.PI)}
                data={vehicle.id}
                eventHandlers={{
                  click: (e) => {
                    console.log("marker clicked", e);
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
            console.log("no fields...");
            return null;
          }
        })}
      </MapContainer>
      <SockJsClient
        url={SOCKET_URL}
        topics={["/telematics/geolocation"]}
        onConnect={onConnected}
        onDisconnect={console.log("Disconnected!")}
        onMessage={(msg) => onMessageReceived(msg)}
        debug={false}
      />
    </>
  );
}
