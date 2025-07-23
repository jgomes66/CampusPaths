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
    onChange(edges: edge[]): void;  // called when a new edge list is ready
                                 // change the type of edges so it isn't `any`
}

export interface edge {
    mapColor: string;
    X1: number;
    Y1: number;
    X2: number;
    Y2: number;
}
interface edgeState {
    val: string;
}

/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class EdgeList extends Component<EdgeListProps, edgeState> {

    constructor(props: EdgeListProps) {
        super(props);
        this.state = {
            val: "",
        }
    }

    TextClear() {
        this.setState({
            val: "",
        })
        this.props.onChange([]);
    }

    TextChange() {
        let edges = this.checkValidEdge(this.state.val);
        this.props.onChange(edges);
    }

    checkValidEdge(str: string) {
        let result: edge[] = [];
        if ( str === "") alert("Null string");
        let temp = str.split("\n");

        for( let i =1; i<=temp.length; i++) {
            let value = temp[i-1].trim().split(" ");
            if(4 >= value.length) alert("Invalid argument");
            let numCheck : boolean = true;
            for (let j = 1; j <= value.length ; j++) {
                if (parseInt(value[j-1]) > 4000 || parseInt(value[j-1]) < 0){
                    alert("Invalid number");
                    numCheck = false;
                }
            }
            if(numCheck) {
                let edge: edge = {
                    X1:parseInt(value[0]),
                    Y1:parseInt(value[1]),
                    X2:parseInt(value[2]),
                    Y2:parseInt(value[3]),
                    mapColor:value[4],
                }
                result.push(edge);
            }
        }
        return result;
    }

    render() {
        return (
            <div id="edge-list">
                Edges <br/>
                <textarea
                    rows={5}
                    cols={30}
                    onChange={(event) => {this.setState({val: event.target.value,})}}
                    value={this.state.val}
                /> <br/>
                <button onClick={() => {this.TextChange()}}>Draw</button>
                <button onClick={() => {this.TextClear()}}>Clear</button>
            </div>
        );
    }
}

export default EdgeList;
