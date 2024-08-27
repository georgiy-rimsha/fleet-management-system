import React, { useEffect, useState } from "react";
import Grid from "@mui/material/Grid";
import Paper from "@mui/material/Paper";
import SockJsClient from "react-stomp";
import Title from "../shared/Title";
import FuelConsumptionChart from "./FuelConsumptionChart";
import OdometerChart from "./OdometerChart";
import GaugeComponent from "react-gauge-component";
import { Statistic } from "antd";
import Container from "@mui/material/Container";
import { useOutletContext } from "react-router-dom";
import { ArrowDownOutlined, ArrowUpOutlined } from "@ant-design/icons";
import Copyright from "../shared/Copyright";

const SOCKET_URL = `${process.env.REACT_APP_WEBSOCKET_URL}/ws-api`;

function Dashboard() {
  const [telemetry, setTelemetry] = useState({
    averageSpeed: 0,
  });
  const [isConnected, setConnected] = useState(false);
  const [firstMessageReceived, setFirstMessageReceived] = useState(false);
  const [setTitle] = useOutletContext();

  useEffect(() => {
    setTitle("Dashboard");
  }, []);

  const [fuelConsumptionEfficiency, setFuelConsumptionEfficiency] = useState(0);
  const prevFuelConsumptionEfficiency = React.useRef<number | null>(null);

  const onConnect = () => {
    if (isConnected === false) {
      console.log("Connected to websocket");
      setConnected(true);
    }
  };

  const onMessageReceived = (message) => {
    if (firstMessageReceived === false) {
      console.log("Message:", message);
      setFirstMessageReceived(true);
    }

    setTelemetry(message);
    const efficiency =
      message.averageFuelGauge * (100 / message.averageOdometer);
    prevFuelConsumptionEfficiency.current = fuelConsumptionEfficiency;
    setFuelConsumptionEfficiency(efficiency);
  };

  return (
    <>
      <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
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
              <GaugeComponent
                value={telemetry.averageSpeed}
                minValue={30}
                maxValue={600}
                type={"radial"}
              />
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
              <Title>Fuel Consumption Efficiency</Title>
              <Statistic
                title=""
                style={{ marginTop: 16 }}
                value={fuelConsumptionEfficiency}
                precision={3}
                valueStyle={{
                  color:
                    fuelConsumptionEfficiency <=
                    prevFuelConsumptionEfficiency.current
                      ? "#3f8600"
                      : "#cf1322",
                  fontSize: 27,
                }}
                prefix={
                  fuelConsumptionEfficiency <=
                  prevFuelConsumptionEfficiency.current ? (
                    <ArrowDownOutlined />
                  ) : (
                    <ArrowUpOutlined />
                  )
                }
                suffix="L/100km"
              />
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
        <Copyright sx={{ pt: 4 }} />
      </Container>

      <SockJsClient
        url={SOCKET_URL}
        topics={["/telematics/telemetry"]}
        onConnect={onConnect}
        onMessage={(message) => onMessageReceived(message)}
        debug={false}
      />
    </>
  );
}

export default Dashboard;
