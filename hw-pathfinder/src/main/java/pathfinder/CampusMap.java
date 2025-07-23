/*
 * Copyright (C) 2023 Hal Perkins.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Winter Quarter 2023 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package pathfinder;

import graph.Graph;
import graph.Node;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampusMap implements ModelAPI {

    private static final boolean CHECK_REP = false;

    private Graph<Point, Double> campusGraph;

    private Map<String, String> buildingNames;

    private Map<String, Point> mapPoints;

    public CampusMap() {
        this.campusGraph = new Graph<>();
        this.buildingNames = new HashMap<>();
        this.mapPoints = new HashMap<>();
        List<CampusBuilding> campusBuildings = CampusPathsParser.parseCampusBuildings("campus_buildings.csv");
        for (CampusBuilding campusBuilding : campusBuildings) {
            campusGraph.addNode(new Node<>(new Point(campusBuilding.getX(), campusBuilding.getY())));
            buildingNames.put(campusBuilding.getShortName(), campusBuilding.getLongName());
            mapPoints.put(campusBuilding.getShortName(), new Point(campusBuilding.getX(), campusBuilding.getY()));
        }
        List<CampusPath> campusPaths = CampusPathsParser.parseCampusPaths("campus_paths.csv");
        for (CampusPath campusPath : campusPaths) {
            Node<Point, Double> startVertex = campusGraph.getNode(new Point(campusPath.getX1(), campusPath.getY1()));
            Node<Point, Double> endVertex = campusGraph.getNode(new Point(campusPath.getX2(), campusPath.getY2()));
            if (startVertex == null) {
                startVertex = new Node<>(new Point(campusPath.getX1(), campusPath.getY1()));
                campusGraph.addNode(startVertex);
            }
            if (endVertex == null) {
                endVertex = new Node<>(new Point(campusPath.getX2(), campusPath.getY2()));
                campusGraph.addNode(endVertex);
            }
            campusGraph.addEdge(startVertex, endVertex, campusPath.getDistance());
            campusGraph.addEdge(endVertex, startVertex, campusPath.getDistance());
        }
        checkRep();
    }

    @Override
    public boolean shortNameExists(String shortName) {
        return buildingNames.containsKey(shortName);
    }

    @Override
    public String longNameForShort(String shortName) {
        return buildingNames.get(shortName);
    }

    @Override
    public Map<String, String> buildingNames() {
        return buildingNames;
    }

    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
        return Dijkstra.dijkstraAlgorithm(mapPoints.get(startShortName), mapPoints.get(endShortName), campusGraph);
    }

    private void checkRep() {
        if (CHECK_REP) {
            assert this.campusGraph != null : "campusGraph can't be null";
            assert this.buildingNames != null : "buildingNames map can't be null";
            assert !this.buildingNames.containsKey(null) : "buildingNames can't contain null map key";
            assert !this.buildingNames.containsValue(null) : "buildingNames can't contain null map value";
            assert this.mapPoints != null : "mapPoints map can't be null";
            assert !this.mapPoints.containsKey(null) : "mapPoints can't contain null map key";
            assert !this.mapPoints.containsValue(null) : "mapPoints can't contain null map value";
        }
    }
}
