import React, { useEffect, useState } from "react";
import GaugeComponent from "react-gauge-component";
import Title from "../muitemplate/Title";

export default function Gauges({ value }) {
  return (
    <React.Fragment>
      {/* <Title>Speedometer</Title> */}
      <GaugeComponent
        value={value}
        minValue={10}
        maxValue={200}
        type={"radial"}
      />
    </React.Fragment>
  );
}
