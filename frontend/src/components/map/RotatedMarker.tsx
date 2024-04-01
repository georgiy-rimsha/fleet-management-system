import React, { forwardRef, useEffect, useRef, ReactNode } from "react";
import { Marker } from "react-leaflet";

interface RotatedMarkerProps {
  children?: ReactNode;
  rotationAngle: number;
  rotationOrigin: string;
}

const RotatedMarker = forwardRef<Marker, RotatedMarkerProps>(
  ({ children, rotationAngle, rotationOrigin, ...props }, ref) => {
    const markerRef = useRef<Marker | null>(null);

    useEffect(() => {
      const marker = markerRef.current;
      if (marker) {
        marker.setRotationAngle(rotationAngle);
        marker.setRotationOrigin(rotationOrigin);
      }
    }, [rotationAngle, rotationOrigin]);

    return (
      <Marker
        ref={(marker) => {
          markerRef.current = marker;
          if (ref) {
            if (typeof ref === "function") {
              ref(marker);
            } else {
              ref.current = marker;
            }
          }
        }}
        {...props}
      >
        {children}
      </Marker>
    );
  }
);

export default RotatedMarker;
