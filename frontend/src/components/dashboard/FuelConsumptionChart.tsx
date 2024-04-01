import * as React from "react";
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
import Title from "../muitemplate/Title";

function createData(time: string, value?: number) {
  return { time, value };
}

let observaionsCounter = 0;

export default function FuelConsumptionChart({ value }) {
  const DATA_POINTS_COUNT = 50;
  const [data, setData] = React.useState([
    { time: 0, value: 10 },
    { time: 1, value: 13 },
    { time: 2, value: 15 },
    { time: 3, value: 20 },
  ]);

  React.useEffect(() => {
    data.push(createData(observaionsCounter++, value));
    setData(data);
    if (data.length > DATA_POINTS_COUNT) {
      data.shift();
    }
  }, [value]);

  const theme = useTheme();

  return (
    <React.Fragment>
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
    </React.Fragment>
  );
}
