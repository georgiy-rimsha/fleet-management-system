import React, { useState } from "react";
import Stack from "@mui/material/Stack";
import { Button } from "@mui/material";
import TextField from "@mui/material/TextField";
import Snackbar from "@mui/material/Snackbar";
import Title from "../shared/Title";
import {
  addVehicle,
  registerDevice,
  getAvailableDevices,
} from "../../services/httpClient.js";
import { generateRandomString } from "../../services/helper.js";

export default function AddVehicleCard({
  landmark,
  fetchVehicles,
  showSnackbar,
}) {
  const [vehicleSpeed, setVehicleSpeed] = useState(400);

  const handleLocateVehicleClick = async () => {
    try {
      let availableDevicesResponse = await getAvailableDevices();

      let availableDevices = availableDevicesResponse.data;

      if (availableDevices.length === 0) {
        await registerDevice({
          serialNumber: generateRandomString(10),
        });

        availableDevicesResponse = await getAvailableDevices();
        availableDevices = availableDevicesResponse.data;
      }

      const randomIndex = Math.floor(Math.random() * availableDevices.length);
      const randomDevice = availableDevices[randomIndex];

      await addVehicle({
        vin: generateRandomString(10),
        make: "Global Automotive",
        model: "Spectra",
        year: 2018,
        groupName: landmark,
        averageSpeed: vehicleSpeed,
        deviceId: randomDevice.id,
      });

      fetchVehicles();
    } catch (error) {
      showSnackbar(error.response?.data?.message || "An error occurred");
      console.error("Error creating vehicle:", error);
    }
  };

  return (
    <Stack spacing={2}>
      <Title>{landmark}</Title>
      <TextField
        id="outlined-basic"
        label="Avg speed, km/h/100"
        variant="outlined"
        value={vehicleSpeed}
        onChange={(e) => {
          setVehicleSpeed(e.target.value);
        }}
      />
      <Button variant="outlined" onClick={handleLocateVehicleClick}>
        Locate vehicle
      </Button>
    </Stack>
  );
}
