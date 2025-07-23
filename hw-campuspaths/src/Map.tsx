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
import {edge} from "./EdgeList";

// This defines the location of the map. These are the coordinates of the UW Seattle campus
const position: LatLngExpression = [UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER];

// NOTE: This component is a suggestion for you to use, if you would like to. If
// you don't want to use this component, you're free to delete it or replace it
// with your hw-lines Map

interface MapProps {
    val: edge[] | null;
}

interface MapState {}

class Map extends Component<MapProps, MapState> {
    constructor(props: MapProps) {
        super(props);
    }
    drawLine(): JSX.Element[]{
        let val = this.props.val;
        let result: JSX.Element[] = [];
        if (val === null){
            return result;
        }
        for(let i = 1; i <= val.length; i++) {
            let temp = val[i-1];
            result.push(<MapLine key={i-1} color={temp.mapColor} x1={temp.X1} y1={temp.Y1} x2={temp.X2} y2={temp.Y2}/>);
        }
        return result;
    }
  render() {
    return (
      <div id="map">
        <MapContainer
          center={position}
          zoom={15}
          scrollWheelZoom={false}
          style={{ width: '90vw', height: '70vh', maxWidth: '1400px', maxHeight: '900px', minWidth: '400px', minHeight: '400px' }}
        >
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />
          {
            this.drawLine()
          }
        </MapContainer>
      </div>
    );
  }
}

export default Map;
