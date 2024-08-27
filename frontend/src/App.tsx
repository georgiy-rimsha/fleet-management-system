import * as React from "react";
import { Routes, Route, BrowserRouter as Router } from "react-router-dom";
import Dashboard from "./components/dashboard/Dashboard";
import Frame from "./components/shared/Frame";
import GISMap from "./components/map/GISMap";
import ProtectedRoute from "./components/shared/ProtectedRoute";
import AuthCallback from "./components/auth/AuthCallback";
import Login from "./components/login/Login";

export default function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/openid/callback" element={<AuthCallback />} />
        <Route
          element={
            <ProtectedRoute>
              <Frame />
            </ProtectedRoute>
          }
        >
          <Route path="/map" element={<GISMap />} />
          <Route path="/dashboard" element={<Dashboard />} />
        </Route>
      </Routes>
    </Router>
  );
}
