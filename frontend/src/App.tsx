import * as React from "react";
import { Routes, Route, BrowserRouter as Router } from "react-router-dom";
import Dashboard from "./components/Dashboard";
import Frame from "./components/Frame";
import Orders from "./components/muitemplate/Orders";
import MapPage from "./components/MapPage";
import TestAPI from "./components/TestAPI";

export default function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Frame />}>
          <Route path="dashboard" element={<Dashboard />} />
          <Route path="orders" element={<Orders />} />
          <Route path="map" element={<MapPage />} />
          <Route path="api" element={<TestAPI />} />
        </Route>
      </Routes>
    </Router>
  );
}
