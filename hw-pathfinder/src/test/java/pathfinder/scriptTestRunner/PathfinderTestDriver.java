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

package pathfinder.scriptTestRunner;

import graph.Graph;
import graph.Node;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;

import java.io.*;
import java.util.*;

public class PathfinderTestDriver {
    private final Map<String, Graph<String, Double>> graphs = new HashMap<>();
    private final PrintWriter output;
    private final BufferedReader input;

    public PathfinderTestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    public void runTests() throws IOException {
        String inputLine;
        while ((inputLine = input.readLine()) != null) {
            if ((inputLine.trim().length() == 0) || (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                StringTokenizer st = new StringTokenizer(inputLine);
                if (st.hasMoreTokens()) {
                    String command = st.nextToken();
                    List<String> arguments = new ArrayList<>();
                    while (st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }
                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch (command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "FindPath":
                    findPath(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch (Exception e) {
            String formattedCommand = command;
            formattedCommand += arguments.stream().reduce("", (a, b) -> a + " " + b);
            output.println("Exception while running command: " + formattedCommand);
            e.printStackTrace(output);
        }
    }

    private void createGraph(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }
        String graphName = arguments.get(0);
        graphs.put(graphName, new Graph<>());
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }
        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);
        Graph<String, Double> graph = graphs.get(graphName);
        graph.addNode(new Node<>(nodeName));
        output.println("added node " + nodeName + " to " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if (arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }
        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        double weight = Double.parseDouble(arguments.get(3));
        Graph<String, Double> graph = graphs.get(graphName);
        Node<String, Double> parent = graph.getNode(parentName);
        Node<String, Double> child = graph.getNode(childName);
        graph.addEdge(parent, child, weight);
        output.printf("added edge %.3f from %s to %s in %s\n", weight, parentName, childName, graphName);
    }

    private void findPath(List<String> arguments) {
        if (arguments.size() != 3) {
            throw new CommandException("Bad arguments to FindPath: " + arguments);
        }
        String graphName = arguments.get(0);
        String start = arguments.get(1);
        String end = arguments.get(2);
        Graph<String, Double> graph = graphs.get(graphName);
        if (graph.getNode(start) == null) {
            output.println("unknown: " + start);
        }
        if (graph.getNode(end) == null) {
            if (!start.equals(end)) {
                output.println("unknown: " + end);
            }
        }
        if (graph.getNode(start) == null || graph.getNode(end) == null) {
            return;
        }
        if (start.equals(end)) {
            output.println("path from " + start + " to " + end + ":");
            output.println("total cost: 0.000");
            return;
        }
        Path<String> path = dijkstra(start, end, graph);
        output.println("path from " + start + " to " + end + ":");
        if (path == null) {
            output.println("no path found");
        } else {
            for (Path<String>.Segment segment : path) {
                output.printf("%s to %s with weight %.3f\n", segment.getStart(), segment.getEnd(), segment.getCost());
            }
            output.printf("total cost: %.3f\n", path.getCost());
        }
    }

    // Dijkstra's algorithm for String nodes
    private Path<String> dijkstra(String start, String dest, Graph<String, Double> graph) {
        Queue<Path<String>> active = new PriorityQueue<>(Comparator.comparingDouble(Path::getCost));
        Set<String> finished = new HashSet<>();
        active.add(new Path<>(start));
        while (!active.isEmpty()) {
            Path<String> minPath = active.remove();
            String minDest = minPath.getEnd();
            if (minDest.equals(dest)) {
                return minPath;
            }
            if (!finished.contains(minDest)) {
                for (Node<String, Double>.Edge curr : graph.getNode(minDest).getEdges()) {
                    String currChild = curr.getChild().getLabel();
                    if (!finished.contains(currChild)) {
                        active.add(minPath.extend(currChild, curr.getEdgeLabel()));
                    }
                }
                finished.add(minDest);
            }
        }
        return null;
    }

    static class CommandException extends RuntimeException {
        public CommandException() { super(); }
        public CommandException(String s) { super(s); }
        public static final long serialVersionUID = 3495;
    }
}
