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

package marvel.scriptTestRunner;

import graph.Graph;
import graph.Node;
import marvel.MarvelPaths;

import java.io.*;
import java.util.*;

/**
 * This class implements a testing driver which reads test scripts from
 * files for testing Graph, the Marvel parser, and your BFS algorithm.
 */
public class MarvelTestDriver {

    private final Map<String, Graph<String,String>> graphs = new HashMap<>();
    private final PrintWriter output;
    private final BufferedReader input;

    // Leave this constructor public
    public MarvelTestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    // Leave this method public
    public void runTests() throws IOException {
        String inputLine;
        while((inputLine = input.readLine()) != null) {
            if((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if(st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<>();
                    while(st.hasMoreTokens()) {
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
            switch(command) {
                case "LoadGraph":
                    loadGraph(arguments);
                    break;
                case "FindPath":
                    findPath(arguments);
                    break;
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch(Exception e) {
            String formattedCommand = command;
            formattedCommand += arguments.stream().reduce("", (a, b) -> a + " " + b);
            output.println("Exception while running command: " + formattedCommand);
            e.printStackTrace(output);
        }
    }

    private void loadGraph(List<String> arguments) throws IOException {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to LoadGraph: " + arguments);
        }

        String graph = arguments.get(0);

        String file = arguments.get(1);

        loadGraph(graph, file);
    }

    private void loadGraph(String graph, String file) throws IOException {
        graphs.put(graph, MarvelPaths.createMarvelGraph(file));
        output.println("loaded graph " + graph);
    }


    private void createGraph(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void findPath(List<String> arguments) {
        if (arguments.size() != 3) {
            throw new CommandException("Bad arguments to FindPath: " + arguments);
        }

        String graph = arguments.get(0);

        String startingCharacter = arguments.get(1);
        String endingCharacter = arguments.get(2);

        startingCharacter = startingCharacter.replaceAll("_", " ");
        endingCharacter = endingCharacter.replaceAll("_", " ");

        findPath(graph, startingCharacter, endingCharacter);
    }

    private void findPath(String graph, String startingCharacter, String endingCharacter) {
        Graph<String,String> MarvelGraph = graphs.get(graph);
        try {
            List<Node<String,String>.Edge> path = MarvelPaths.findPath(MarvelGraph, startingCharacter, endingCharacter);
            output.println("path from " + startingCharacter + " to " + endingCharacter + ":");
            if (path == null) {
                output.println("no path found");
            } else {
                String last = startingCharacter;

                for (Node<String,String>.Edge curr : path) {
                    output.println(last + " to " + curr.getChild().getLabel() + " via " + curr.getEdgeLabel());

                    last = curr.getChild().getLabel();
                }
            }
        } catch (Exception e) {
            if (MarvelGraph.getNode(startingCharacter) == null) {
                output.println("unknown: " + startingCharacter);
            }
            if (MarvelGraph.getNode(endingCharacter) == null) {
                output.println("unknown: " + endingCharacter);
            }
        }
    }

    private void createGraph(String graphName) {
        graphs.put(graphName, new Graph<>());
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        // TODO Insert your code here.

        Graph<String,String> graph = graphs.get(graphName);
        graph.addNode(new Node<>(nodeName));
        output.println("added node " + nodeName + " to " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if(arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        String edgeLabel = arguments.get(3);

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         String edgeLabel) {
        // TODO Insert your code here.

        Graph<String,String> graph = graphs.get(graphName);

        Node<String,String> parent = graph.getNode(parentName);
        Node<String,String> child = graph.getNode(childName);
        graph.addEdge(parent, child, edgeLabel);

        output.println("added edge " + edgeLabel + " from " + parentName + " to " + childName +
                " in " + graphName);
    }

    private void listNodes(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        Graph<String,String> graph = graphs.get(graphName);

        String nodeList = graphName + " contains:";

        List<String> nodeLabels = graph.getNodes();
        Collections.sort(nodeLabels);

        for (String label : nodeLabels) {
            nodeList = nodeList + " " + label;
        }
        output.println(nodeList);
    }

    private void listChildren(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }
    class SortByEdgeLabel implements Comparator<Node<String,String>.Edge> {
        public int compare(Node<String,String>.Edge o1, Node<String,String>.Edge o2) {
            if (o1.getChild().equals(o2.getChild())) {
                return o1.getEdgeLabel().compareTo(o2.getEdgeLabel());
            } else {
                return o1.getChild().getLabel().compareTo(o2.getChild().getLabel());
            }
        }
    }

    private void listChildren(String graphName, String parentName) {
        Graph<String,String> graph = graphs.get(graphName);
        Node<String,String> parent = graph.getNode(parentName);

        List<Node<String,String>.Edge> edgeList = new ArrayList<>();

        for (Node<String,String>.Edge edge : parent.getEdges()) {
            edgeList.add(edge);
        }

        Collections.sort(edgeList, new SortByEdgeLabel());

        String listChildren = "the children of " + parentName + " in " + graphName + " are:";

        for (Node<String,String>.Edge currEdge : edgeList) {
            listChildren = listChildren + " " + currEdge.getChild().getLabel() +
                    "(" + currEdge.getEdgeLabel() + ")";
        }
        output.println(listChildren);
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}
