import React, { useEffect, useState } from "react";
import Grid from "@mui/material/Grid";
import Paper from "@mui/material/Paper";
import Chart from "./muitemplate/Chart";
import Deposits from "./muitemplate/Deposits";
import Orders from "./muitemplate/Orders";
import SockJsClient from "react-stomp";
import Title from "./muitemplate/Title";
import FuelConsumptionChart from "./dashboard/FuelConsumptionChart";
import FuelEffeciencyChart from "./dashboard/FuelEffeciencyChart";
import OdometerChart from "./dashboard/OdometerChart";
import GaugeComponent from "react-gauge-component";
import { Statistic } from "antd";
import Container from "@mui/material/Container";
import { ArrowDownOutlined, ArrowUpOutlined } from "@ant-design/icons";
import { ResponsiveContainer } from "recharts";
import Speedometer, {
  Background,
  Arc,
  Needle,
  Progress,
  Marks,
  Indicator,
} from "react-speedometer";

export default function Dashboard() {
  const [telemetry, setTelemetry] = useState({
    averageSpeed: 0,
    // averageOdometer: 0.1,
    // averageFuelGauge: 0.03,
  });

  const SOCKET_URL = "http://localhost:8082/ws-message";
  let onConnected = () => {
    console.log("Connected!!");
  };
  const handleTelemetryMessage = (newMessage) => {
    setTelemetry(newMessage);
  };

  let onMessageReceived = (msg) => {
    console.log(JSON.stringify(msg));
    handleTelemetryMessage(msg);
  };

  const prevValue = 10;

  return (
    <>
      <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
        {/* <div>{telemetry.averageSpeed}</div>
      <div>{telemetry.averageOdometer}</div>
      <div>{telemetry.averageFuelGauge}</div> */}

        <Grid container spacing={3}>
          <Grid item xs={12} md={8} lg={9}>
            <Paper
              sx={{
                p: 2,
                display: "flex",
                flexDirection: "column",
                height: 240,
              }}
            >
              <OdometerChart value={telemetry.averageOdometer} />
            </Paper>
          </Grid>
          <Grid item xs={12} md={4} lg={3}>
            <Paper
              sx={{
                p: 2,
                display: "flex",
                flexDirection: "column",
                height: 240,
                position: "relative",
              }}
            >
              <div
                style={{
                  width: "60%",
                  height: "60%",
                  position: "absolute",
                  top: 0,
                  left: 0,
                }}
              >
                <GaugeComponent
                  value={telemetry.averageSpeed}
                  minValue={10}
                  maxValue={200}
                  type={"radial"}
                />
              </div>
            </Paper>
          </Grid>
          <Grid item xs={12} md={4} lg={3}>
            <Paper
              sx={{
                p: 2,
                display: "flex",
                flexDirection: "column",
                height: 240,
              }}
            >
              {/* <Title>Fuel per 100km</Title> */}
              <Title>Fuel Consumption Efficiency</Title>
              <Statistic
                title=""
                // value={
                //   telemetry.averageFuelGauge * (100 / telemetry.averageOdometer)
                // }
                style={{ marginTop: 16 }}
                value={8.42}
                precision={2}
                valueStyle={{
                  color: 8.42 < prevValue ? "#3f8600" : "#cf1322",
                  fontSize: 27,
                }}
                prefix={
                  8.42 < prevValue ? <ArrowDownOutlined /> : <ArrowUpOutlined />
                }
                suffix="L/100km"
              />
              {/* <h3>
              {telemetry.averageFuelGauge * (100 / telemetry.averageOdometer)}{" "}
              L/100KM
            </h3> */}
            </Paper>
          </Grid>
          <Grid item xs={12} md={8} lg={9}>
            <Paper
              sx={{
                p: 2,
                display: "flex",
                flexDirection: "column",
                height: 240,
              }}
            >
              <FuelConsumptionChart value={telemetry.averageFuelGauge} />
            </Paper>
          </Grid>
        </Grid>
        {/* <Copyright sx={{ pt: 4 }} /> */}
      </Container>

      <SockJsClient
        url={SOCKET_URL}
        topics={["/telematics/telemetry"]}
        onConnect={onConnected}
        onDisconnect={console.log("Disconnected!")}
        onMessage={(msg) => onMessageReceived(msg)}
        debug={false}
      />
    </>
  );
}
