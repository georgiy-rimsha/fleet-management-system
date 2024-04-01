import React, { useEffect, useState } from "react";
import Stack from "@mui/material/Stack";
import Slider from "@mui/material/Slider";
import { Button } from "@mui/material";
import Title from "../muitemplate/Title";
import DeleteIcon from "@mui/icons-material/Delete";
import {
  deleteVehicle,
  deleteDevice,
  updateDevice,
  getDevice,
} from "../../services/client.js";

export default function VehicleCard({ vehicle, fetchVehicles }) {
  const [vehicleSpeed, setVehicleSpeed] = useState();

  useEffect(() => {
    getDevice(vehicle.deviceId)
      .then((res) => {
        console.log("getDevice response: " + res);
        setVehicleSpeed(res.data.averageSpeed);
      })
      .catch((err) => {
        console.log(err);
      })
      .finally(() => {});
  }, []);

  return (
    <Stack spacing={2}>
      <Title>{"ID " + vehicle.id}</Title>
      <Title>{vehicleSpeed + " km/h"}</Title>
      <Slider
        aria-label="Volume"
        value={vehicleSpeed}
        min={70}
        max={300}
        onChange={(id, value: number | number[]) => {
          setVehicleSpeed(value);
          updateDevice(vehicle.deviceId, { averageSpeed: value })
            .then((res) => {
              console.log(res);
            })
            .catch((err) => {
              console.log(err);
            })
            .finally(() => {});
        }}
      />

      <Button
        variant="outlined"
        startIcon={<DeleteIcon />}
        onClick={() => {
          deleteVehicle(vehicle.id)
            .then((res) => {
              console.log(res);
              fetchVehicles();
            })
            .catch((err) => {
              console.log(err);
            })
            .finally(() => {});

          deleteDevice(vehicle.deviceId)
            .then((res) => {
              console.log(res);
            })
            .catch((err) => {
              console.log(err);
            })
            .finally(() => {});
        }}
      >
        Delete
      </Button>
    </Stack>
  );
}
