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

import React, {Component} from 'react';

interface EdgeListProps {
    onChange(edges: edge[] | null): void;
}

export type edge = {
    mapColor: string;
    X1: number;
    Y1: number;
    X2: number;
    Y2: number;
}
interface edgeState {
    campusBuildings: string[];  // List of campus buildings
    startingBuilding: string; // Starting campus building
    endingBuilding: string;
    buildingExists: boolean;

}

/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class EdgeList extends Component<EdgeListProps, edgeState> {

    constructor(props: EdgeListProps) {
        super(props);
        this.state = {
            campusBuildings: [],  // List of campus buildings
            startingBuilding: "", // Starting campus building
            endingBuilding: "",
            buildingExists: false,
        }
        this.getBuildings();
    }

    changeStart = (start : string) => {
        this.setState({
            startingBuilding: start
        })
    }

    // Changes ending building
    changeEnd = (end : string) => {
        this.setState({
            endingBuilding: end
        })
    }

    ClearBut = () => {
        this.setState({startingBuilding: "", endingBuilding: "",})
        this.props.onChange(null);
    }

    checkEdge(): boolean {
        return !(this.state.startingBuilding === "" || this.state.endingBuilding === "");
    }

    getBuildings(): void {
        if (!this.state.buildingExists){
            fetch('http://localhost:4567/campusBuildings').then(response => response.json().then(data => {
                this.setState({
                    buildingExists: true,
                    campusBuildings: data
                });
            })).catch(() => {
                alert("An issue with the server has occurred");
            })
        }
    }

    parsePath(data: string) : edge[] {
        let result: edge[] = [];
        let obj = JSON.parse(data);
        let color: string;
        color = "red";


        obj["path"].forEach((e: any) => {
            let edge: edge = {
                mapColor: color,
                X1: e['start']['x'],
                Y1: e['start']['y'],
                X2: e['end']['x'],
                Y2: e['end']['y'],
            }
            result.push(edge);
        })
        return result;
    }

    DropDownMenu = (buildingName: string, val: string, run: Function) : JSX.Element => {
        return(
            <div style={{display: 'flex', justifyContent: 'center'}}>
                <label>
                    {buildingName}
                    <select value={val} onChange={(e) => run(e.target.value)}>
                        <option value="" key="">Choose Building</option>
                        {this.state.campusBuildings.map((building) => (
                            <option value={building} key={building}>{building}</option>
                        ))}
                    </select>
                </label>
            </div>
        )
    }
    FindRoute() : void{
        let boolean = this.checkEdge();
        if (!boolean){
            alert("Building not selected");
            return;
        }

        let url = new URL('http://localhost:4567/FindRoute');
        url.searchParams.append("s", this.state.startingBuilding);
        url.searchParams.append("e", this.state.endingBuilding);
        fetch(url.toString())
            .then(response => response.json()
                .then(data => {
                    let edge = this.parsePath(JSON.stringify(data));
                    this.props.onChange(edge);
                }))
    }

    render() {
        return (
            <div style={{display: 'flex', justifyContent: 'center'}} id="edge-list"> <br/>
                <div>
                    {this.DropDownMenu("Start", this.state.startingBuilding, this.changeStart)}
                    {this.DropDownMenu("Destination", this.state.endingBuilding, this.changeEnd)}
                </div>
                <button onClick={() => {this.FindRoute();}}>Draw</button>
                <button onClick={() => { this.ClearBut();}}>Reset</button>
            </div>
        );
    }
}

export default EdgeList;
