import axios from "axios";

const getAuthConfig = () => ({
  headers: {
    Authorization: `Bearer ${
      JSON.parse(
        sessionStorage.getItem(
          `oidc.user:${process.env.REACT_APP_AUTHORITY}:${process.env.REACT_APP_CLIENT_ID}`
        )
      ).access_token
    }`,
  },
});

export const getPlaces = async () => {
  try {
    return await axios.get(
      `${process.env.REACT_APP_PLACE_BASE_URL}/api/v1/places`,
      getAuthConfig()
    );
  } catch (e) {
    throw e;
  }
};

export const getVehicles = async () => {
  try {
    return await axios.get(
      `${process.env.REACT_APP_VEHICLE_BASE_URL}/api/v1/vehicles`,
      getAuthConfig()
    );
  } catch (e) {
    throw e;
  }
};

export const getAvailableDevices = async () => {
  try {
    return await axios.get(
      `${process.env.REACT_APP_DEVICE_BASE_URL}/api/v1/devices/available`,
      getAuthConfig()
    );
  } catch (e) {
    throw e;
  }
};

export const getDevice = async (id) => {
  try {
    return await axios.get(
      `${process.env.REACT_APP_DEVICE_BASE_URL}/api/v1/devices/${id}`,
      getAuthConfig()
    );
  } catch (e) {
    throw e;
  }
};

export const addVehicle = async (vehicle) => {
  try {
    return await axios.post(
      `${process.env.REACT_APP_VEHICLE_BASE_URL}/api/v1/vehicles`,
      vehicle,
      getAuthConfig()
    );
  } catch (e) {
    throw e;
  }
};

export const registerDevice = async (device) => {
  try {
    return await axios.post(
      `${process.env.REACT_APP_DEVICE_BASE_URL}/api/v1/devices`,
      device,
      getAuthConfig()
    );
  } catch (e) {
    throw e;
  }
};

export const updateVehicle = async (id, update) => {
  try {
    return await axios.put(
      `${process.env.REACT_APP_VEHICLE_BASE_URL}/api/v1/vehicles/${id}`,
      update,
      getAuthConfig()
    );
  } catch (e) {
    throw e;
  }
};

export const updateDevice = async (id, update) => {
  try {
    return await axios.put(
      `${process.env.REACT_APP_DEVICE_BASE_URL}/api/v1/devices/${id}`,
      update,
      getAuthConfig()
    );
  } catch (e) {
    throw e;
  }
};

export const deleteVehicle = async (id) => {
  try {
    return await axios.delete(
      `${process.env.REACT_APP_VEHICLE_BASE_URL}/api/v1/vehicles/${id}`,
      getAuthConfig()
    );
  } catch (e) {
    throw e;
  }
};

export const deleteDevice = async (id) => {
  try {
    return await axios.delete(
      `${process.env.REACT_APP_DEVICE_BASE_URL}/api/v1/devices/${id}`,
      getAuthConfig()
    );
  } catch (e) {
    throw e;
  }
};