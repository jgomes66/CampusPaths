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
import Map from "./Map";

// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";
import EdgeList from "./EdgeList";

interface mapState {val: edge[] | null}

export interface edge {
    mapColor: string;
    X1: number;
    Y1: number;
    X2: number;
    Y2: number;
}

class App extends Component<{}, mapState> {

    constructor(props: any) {
        super(props);
        this.state = {
            val: null
        };
    }

    render() {
        return (
            <div>
                <h1 style={{display: 'flex', justifyContent: 'center'}} id="titletext">Campus Paths</h1>
                <div>
                    <Map val = {this.state.val}/>
                </div>
                <EdgeList onChange={(value) => {
                    this.setState({
                        val: value
                    });
                }}
                />
            </div>
        );
    }

}

export default App;
