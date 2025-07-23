/*
 * Copyright (C) 2022 Kevin Zatloukal and James Wilcox.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Autumn Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

import { LatLngExpression } from "leaflet";
import React, { Component } from "react";
import { MapContainer, TileLayer } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import MapLine from "./MapLine";
import { UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER } from "./Constants";

// This defines the location of the map. These are the coordinates of the UW Seattle campus
const position: LatLngExpression = [UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER];

interface MapProps {
  value: edge[];
}

export interface edge {
    mapColor: string;
    X1: number;
    Y1: number;
    X2: number;
    Y2: number;
}

interface MapState {}

class Map extends Component<MapProps, MapState> {
    constructor(props: MapProps) {super(props);}
  render() {
      let result : JSX.Element[] = [];
      for ( let i = 1; i<= this.props.value.length; i++) {
          let element = this.props.value[i-1];
          result.push(<MapLine color={element.mapColor} x1={element.X1} y1={element.Y1} x2={element.X2} y2={element.Y2}/>);
      }

    return (
      <div id="map">
        <MapContainer
          center={position}
          zoom={15}
          scrollWheelZoom={false}
        >
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />
          {
              <div>{result}</div>
          }
        </MapContainer>
      </div>
    );
  }
}

export default Map;
