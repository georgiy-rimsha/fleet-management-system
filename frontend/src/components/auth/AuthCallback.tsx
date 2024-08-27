import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "./AuthContext";
import CircularProgress from "@mui/material/CircularProgress";

function AuthCallback() {
  const auth = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    auth.loginCallback().then(() => {
      navigate("/map");
    });
  }, []);

  return <CircularProgress />;
}

export default AuthCallback;
