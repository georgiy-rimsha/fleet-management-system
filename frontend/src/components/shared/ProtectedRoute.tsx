import { useAuth } from "../auth/AuthContext";
import React, { useEffect } from "react";
import CircularProgress from "@mui/material/CircularProgress";

export default function ProtectedRoute({ children }) {
  const auth = useAuth();

  useEffect(() => {
    if (!auth.isAuthenticated()) auth.login();
  });

  return !auth.isAuthenticated() ? <CircularProgress /> : children;
}
