import React, { useEffect, useState } from "react";
import { useTheme } from "@mui/material/styles";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Label,
  ResponsiveContainer,
  CartesianGrid,
  Tooltip,
} from "recharts";
import Title from "../shared/Title";

function createData(time: string, value?: number) {
  return { time, value };
}

let observaionsCounter = 0;
const DATA_POINTS_COUNT = 50;

function FuelConsumptionChart({ value }) {
  const [data, setData] = useState([]);

  useEffect(() => {
    data.push(createData(observaionsCounter++, value));
    setData(data);
    if (data.length > DATA_POINTS_COUNT) {
      data.shift();
    }
  }, [value]);

  const theme = useTheme();

  return (
    <>
      <Title>Average Fuel Consumption</Title>
      <ResponsiveContainer key={`rc_${data.length}`}>
        <LineChart
          data={data}
          margin={{
            top: 16,
            right: 16,
            bottom: 0,
            left: 24,
          }}
        >
          <XAxis
            dataKey="time"
            stroke={theme.palette.text.secondary}
            style={theme.typography.body2}
            tick={false}
          />
          <YAxis
            stroke={theme.palette.text.secondary}
            style={theme.typography.body2}
            tickFormatter={(number) => {
              return number < 100_000
                ? Math.floor(number)
                : Math.floor((number / 1000) * 100) / 100 + "K";
            }}
            type="number"
            domain={["dataMin", "auto"]}
          >
            <Label
              angle={270}
              position="left"
              style={{
                textAnchor: "middle",
                fill: theme.palette.text.primary,
                ...theme.typography.body1,
              }}
            >
              Liters
            </Label>
          </YAxis>
          <Tooltip
            formatter={(number) => Math.ceil(number * 100) / 100}
            labelFormatter={(value) => {
              return "";
            }}
          />
          <CartesianGrid stroke="#eee" strokeDasharray="5 5" />
          <Line
            isAnimationActive={false}
            type="monotone"
            dataKey="value"
            stroke={theme.palette.primary.main}
            dot={false}
          />
        </LineChart>
      </ResponsiveContainer>
    </>
  );
}

export default FuelConsumptionChart;
