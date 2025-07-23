package marvel;
import graph.Graph;
import graph.Node;
import java.io.IOException;
import java.util.*;

/**
 * This class, MarvelPaths, creates a graph of the MCU
 * from the given dataset and finds the shortest path
 * between two characters
 */
public class MarvelPaths {

    /**
     * This class, Graph, is not an ADT.
     */

    /**
     * The main method
     *
     * @param args the arguments that are passed in when the class runs
     * @throws IOException the arguments that are passed in when the class runs
     */
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        boolean stop = false;
        Graph<String,String> MarvelGraph = createMarvelGraph("marvel.csv");
        while (!stop) {
            System.out.println("Character name 1: ");
            String startingCharacter = input.nextLine();
            System.out.println("Character name 2: ");
            String endingCharacter = input.nextLine();
            try {
                List<Node<String,String>.Edge> bestPath = findPath(MarvelGraph, startingCharacter, endingCharacter);
                if (bestPath != null) {
                    String last = startingCharacter;
                    for (Node<java.lang.String, java.lang.String>.Edge curr : bestPath) {
                        System.out.println("There is a path between " + last + " and " + curr.getChild().getLabel() + " through " + curr.getEdgeLabel() + ".");
                        last = (String) curr.getChild().getLabel();
                    }
                } else {
                    System.out.println("There is no path between " + startingCharacter + " and " + endingCharacter + ".");
                }
            } catch (IllegalArgumentException e) {
                if (MarvelGraph.getNode(startingCharacter) == null) {
                    System.out.println(startingCharacter + " isn't in the provided data.");
                }
                if (MarvelGraph.getNode(endingCharacter) == null) {
                    System.out.println(endingCharacter + " isn't in the provided data.");
                }
            }
            System.out.println();
            System.out.println("Stop? (Y/N)");
            String result = input.nextLine();
            while (!(result.equals("Y") || result.equals("N"))) {
                result = input.nextLine();
                System.out.println("Format response as 'Y' or 'N'.");
                System.out.println("Stop? (Y/N)");
            }
            if (result.equals("Y")) {
                stop = true;
            }
        }
    }

    /**
     * Creates a graph from the given data
     *
     * @param file = file name given to create graph
     * @throws IOException the arguments that are passed in when the class runs
     * @return returns a graph built from file data of the MCU
     */
    public static Graph<String,String> createMarvelGraph(String file) throws IOException {
        List<List<String>> data = MarvelParser.parseData(file);
        Graph<String,String> MarvelGraph = new Graph<>();
        HashMap<String, HashSet<Node<String,String>>> charactersBooks = new HashMap<>();
        for (List<String> line : data) {
            String character = line.get(0);
            Node<String,String> characterVertex = new Node<>(character);
            boolean vertexAdded = MarvelGraph.addNode(characterVertex);
            if (!vertexAdded) {
                characterVertex = MarvelGraph.getNode(character);
            }
            String book = line.get(1);
            if (!charactersBooks.containsKey(book)) {
                charactersBooks.put(book, new HashSet<>());
            }
            HashSet<Node<String,String>> currCharacterBook = charactersBooks.get(book);
            for (Node<String, String> currCharacter : currCharacterBook) {
                MarvelGraph.addEdge(characterVertex, currCharacter, book);
                MarvelGraph.addEdge(currCharacter, characterVertex, book);
            }
            currCharacterBook.add(characterVertex);
        }
        return MarvelGraph;
    }

    /**
     * Finds the shortest paths between two characters in the graph
     *
     * @param MarvelGraph = graph in which we are finding the path
     * @param startingCharacter = vertex value of the starting character
     * @param endingCharacter = vertex value of the ending character
     * @return list containing the shortest and lexicographically
     * least path
     */
    public static List<Node<String,String>.Edge> findPath(Graph<String,String> MarvelGraph, String startingCharacter, String endingCharacter) {
        if (MarvelGraph.getNode(startingCharacter) == null || MarvelGraph.getNode(endingCharacter) == null) {
            throw new IllegalArgumentException();
        }
        Node<String,String> start = MarvelGraph.getNode(startingCharacter);
        Node<String,String> end = MarvelGraph.getNode(endingCharacter);
        if (start.equals(end)) {
            return new ArrayList<>();
        } else {
            Queue<Node<String,String>> queue = new LinkedList<>();
            queue.add(start);
            Map<Node<String,String>, List<Node<String,String>.Edge>> seen = new HashMap<>();
            seen.put(start, new ArrayList<>());
            while (!queue.isEmpty()) {
                Node<String,String> curr = queue.remove();
                if (curr.equals(end)) {
                    return seen.get(curr);
                }
                List<Node<String,String>.Edge> currPath = new ArrayList<>(curr.getEdges());
                currPath.sort(new compareChildren());
                for (Node<String,String>.Edge edge : currPath) {
                    if (!seen.containsKey(edge.getChild())) {
                        List<Node<String, String>.Edge> newPath = new ArrayList<Node<java.lang.String, java.lang.String>.Edge>(seen.get(curr));
                        newPath.add(edge);
                        seen.put(edge.getChild(), newPath);
                        queue.add(edge.getChild());
                    }
                }
            }
        }
        return null;
    }

    /**
     * This class, compareChildren, is a comparator for the children vertices
     */
    private static class compareChildren implements Comparator<Node<String,String>.Edge> {
        public int compare(Node<String,String>.Edge e1, Node<String,String>.Edge e2) {
            if (e1.getChild().equals(e2.getChild())) {
                return e1.getEdgeLabel().compareTo(e2.getEdgeLabel());
            } else {
                return e1.getChild().getLabel().compareTo(e2.getChild().getLabel());
            }
        }
    }
}
