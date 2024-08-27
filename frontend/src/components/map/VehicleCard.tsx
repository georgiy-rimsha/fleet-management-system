import React, { useEffect, useState } from "react";
import Stack from "@mui/material/Stack";
import Slider from "@mui/material/Slider";
import { Button } from "@mui/material";
import Title from "../shared/Title";
import DeleteIcon from "@mui/icons-material/Delete";
import {
  deleteVehicle,
  deleteDevice,
  updateDevice,
  getDevice,
  getAvailableDevices,
} from "../../services/httpClient.js";

function VehicleCard({ vehicle, fetchVehicles }) {
  const [vehicleSpeed, setVehicleSpeed] = useState();

  useEffect(() => {
    getDevice(vehicle.deviceId)
      .then((res) => {
        setVehicleSpeed(res.data.averageSpeed);
      })
      .catch((err) => {
        console.error(err);
      })
      .finally(() => {});
  }, []);

  const handleDeleteVehicleClick = async () => {
    try {
      await deleteVehicle(vehicle.id);

      const availableDevicesResponse = await getAvailableDevices();
      const availableDevices = availableDevicesResponse.data;

      if (availableDevices.length > 2) {
        const randomIndex = Math.floor(Math.random() * availableDevices.length);
        const randomDevice = availableDevices[randomIndex];
        await deleteDevice(randomDevice.id);
      }
      await fetchVehicles();
    } catch (error) {
      console.error("Error deleting vehicle:", error);
    }
  };

  return (
    <Stack spacing={2}>
      <Title>{"ID " + vehicle.id}</Title>
      <Title>{vehicleSpeed + " km/h/100"}</Title>
      <Slider
        aria-label="Volume"
        value={vehicleSpeed}
        defaultValue={400}
        min={200}
        max={600}
        onChange={(id, value: number | number[]) => {
          setVehicleSpeed(value);
          updateDevice(vehicle.deviceId, { averageSpeed: value })
            .then((res) => {
              console.log("Successfully updated device");
            })
            .catch((err) => {
              console.error(err);
            })
            .finally(() => {});
        }}
      />
      <Button
        variant="outlined"
        startIcon={<DeleteIcon />}
        onClick={handleDeleteVehicleClick}
      >
        Delete
      </Button>
    </Stack>
  );
}

export default VehicleCard;