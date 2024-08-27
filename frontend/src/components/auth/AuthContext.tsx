import React, { createContext, useContext, useState, ReactNode } from "react";
import AuthService from "./AuthService";
import { User } from "oidc-client-ts";

const AuthContext = createContext(null!);

const useAuth = () => useContext(AuthContext);

function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null | undefined>(
    JSON.parse(
      sessionStorage.getItem(
        `oidc.user:${process.env.REACT_APP_AUTHORITY}:${process.env.REACT_APP_CLIENT_ID}`
      ) || "null"
    ) || undefined
  );

  const authService = new AuthService();

  const loginCallback = async (): Promise<void> => {
    const authedUser = await authService.loginCallback();
    setUser(authedUser);
  };

  const isAuthenticated = () => {
    if (user === undefined || isTokenExpired()) return false;
    return true;
  };

  const isTokenExpired = () => {
    const expiration = user?.expires_at;

    if (Date.now() > expiration * 1000) {
      return true;
    }
    return false;
  };

  const login = async (): Promise<void> => {
    try {
      await authService.login();
      console.log("Login successful!");
    } catch (error) {
      console.error("Error during login:", error);
      throw error;
    }
  };

  const logout = async (): Promise<void> => {
    try {
      await authService.logout();
      console.log("Logout successful!");
    } catch (error) {
      console.error("Error during logout:", error);
      throw error;
    }
  };

  const value = {
    user,
    login,
    logout,
    loginCallback,
    isAuthenticated,
    isTokenExpired,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export { AuthProvider, useAuth };
