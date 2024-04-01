import * as React from "react";
import { useTheme } from "@mui/material/styles";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Label,
  ResponsiveContainer,
} from "recharts";
import Title from "../muitemplate/Title";

function createData(time: string, amount?: number) {
  return { time, amount };
}

let observaionsCounter = 0;

export default function FuelEffeciencyChart({ value }) {
  const DATA_POINTS_COUNT = 50;
  const [data, setData] = React.useState([]);

  React.useEffect(() => {
    observaionsCounter++;

    data.push(createData(observaionsCounter, value));
    setData(data);
    if (data.length > DATA_POINTS_COUNT) {
      data.shift();
    }
  }, [value]);

  const theme = useTheme();

  return (
    <React.Fragment>
      <Title>Fuel L/100KM history</Title>
      <ResponsiveContainer>
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
          />
          <YAxis
            stroke={theme.palette.text.secondary}
            style={theme.typography.body2}
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
              Sales ($)
            </Label>
          </YAxis>
          <Line
            isAnimationActive={false}
            type="monotone"
            dataKey="amount"
            stroke={theme.palette.primary.main}
            dot={false}
          />
        </LineChart>
      </ResponsiveContainer>
    </React.Fragment>
  );
}
