//@ts-nocheck
import React, { useEffect, useState } from "react";
import SockJsClient from "react-stomp";
import Stack from "@mui/material/Stack";
import Button from "@mui/material/Button";
import { saveVehicle } from "../services/client.js";

export default function TestAPI() {
  const warehousesData = [
    {
      id: 4,
      latitude: 62.113239,
      longitude: 15.086749,
    },
    {
      id: 5,
      latitude: 55.396002,
      longitude: 10.388319,
    },
    {
      id: 6,
      latitude: 52.480652,
      longitude: -1.897603,
    },
  ];

  const vehiclesData = [
    {
      id: 1,
      warehouseId: 1,
    },
    {
      id: 2,
      warehouseId: 2,
    },
  ];

  const postVehicle = (vehicle) => {
    saveVehicle(vehicle)
      .then((res) => {
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      })
      .finally(() => {});
  };

  let onConnected = () => {
    console.log("Connected!!");
  };

  let onMessageReceived = (msg) => {
    console.log(JSON.stringify(msg));
    setModelTime(msg.time);
    setSpeed(msg.speed);
    setLocation(msg.location);
  };

  const [location, setLocation] = useState("-");
  const [speed, setSpeed] = useState("-");
  const [modelTime, setModelTime] = useState("Placeholder for the model time");

  let vehicleIndex = 0;

  const SOCKET_URL = "http://localhost:8080/ws-message";

  return (
    <>
      <Stack spacing={2} direction="column" style={{ width: "20%" }}>
        <Button
          variant="outlined"
          onClick={() => postVehicle(vehiclesData[vehicleIndex])}
        >
          New Vehicle
        </Button>
        <p>Current Location:</p>
        <div>{JSON.stringify(location)}</div>
        <p>Current Speed:</p>
        <div>{speed}</div>
        <p>Model Time:</p>
        <div>{modelTime}</div>
      </Stack>

      <SockJsClient
        url={SOCKET_URL}
        topics={["/topic/message"]}
        onConnect={onConnected}
        onDisconnect={console.log("Disconnected!")}
        onMessage={(msg) => onMessageReceived(msg)}
        debug={false}
      />
    </>
  );
}
