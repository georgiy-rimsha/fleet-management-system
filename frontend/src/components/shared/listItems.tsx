import * as React from "react";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import ListSubheader from "@mui/material/ListSubheader";
import DashboardIcon from "@mui/icons-material/Dashboard";
import RoomIcon from "@mui/icons-material/Room";
import GitHubIcon from "@mui/icons-material/GitHub";
import OpenInNewIcon from "@mui/icons-material/OpenInNew";

export const mainListItems = (
  <React.Fragment>
    <ListItemButton href="map">
      <ListItemIcon>
        <RoomIcon />
      </ListItemIcon>
      <ListItemText primary="Map" />
    </ListItemButton>
    <ListItemButton href="dashboard">
      <ListItemIcon>
        <DashboardIcon />
      </ListItemIcon>
      <ListItemText primary="Dashboard" />
    </ListItemButton>
  </React.Fragment>
);

export const secondaryListItems = (
  <React.Fragment>
    <ListSubheader component="div" inset>
      Useful links
    </ListSubheader>
    <ListItemButton
      href="https://github.com/notenger/fleet-management-system"
      target="_blank"
    >
      <ListItemIcon>
        <GitHubIcon />
      </ListItemIcon>
      <ListItemText primary="GitHub" />
      <OpenInNewIcon />
    </ListItemButton>
  </React.Fragment>
);
