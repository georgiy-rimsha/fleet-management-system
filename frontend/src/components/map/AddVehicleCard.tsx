import React, { useEffect, useState } from "react";
import Stack from "@mui/material/Stack";
import Slider from "@mui/material/Slider";
import { Button } from "@mui/material";
import TextField from "@mui/material/TextField";
import Title from "../muitemplate/Title";
import DeleteIcon from "@mui/icons-material/Delete";
import { saveVehicle, bindVehicle, saveDevice } from "../../services/client.js";

export default function AddVehicleCard({ landmark, fetchVehicles }) {
  const [vehicleSpeed, setVehicleSpeed] = useState(160);

  return (
    <Stack spacing={2}>
      <Title>{landmark}</Title>
      <TextField
        id="outlined-basic"
        label="Average speed, km/h"
        variant="outlined"
        value={vehicleSpeed}
        onChange={(e) => {
          setVehicleSpeed(e.target.value);
        }}
      />
      <Button
        variant="outlined"
        onClick={async () => {
          await saveDevice({
            deviceSerialNumber: "0055111999",
            placeName: landmark,
            averageSpeed: vehicleSpeed,
          });
          const vehicleResponse = await saveVehicle({
            vinNumber: "5887509223",
            make: "AAA",
            model: "cc",
            year: 2016,
          });
          await bindVehicle(vehicleResponse.data.vehicleId);
          fetchVehicles();
        }}
      >
        Locate vehicle
      </Button>
    </Stack>
  );
}
