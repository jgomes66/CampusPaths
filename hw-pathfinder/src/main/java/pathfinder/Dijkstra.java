package pathfinder;

import graph.Graph;
import graph.Node;
import pathfinder.datastructures.*;
import java.util.*;

/**
 * This class, Dijkstra, implements the Dijkstra
 * search algorithm
 */
public class Dijkstra {

    /**
     * Finds the shortest path, by distance, between the two provided buildings.
     *
     * @param start The start vertex.
     * @param dest The destination vertex.
     * @param graph The graph.
     * @param <P> The type
     * @return A path between start and dest, or null if none exists.
     */
    public static <P> Path<P> dijkstraAlgorithm(P start, P dest, Graph<P, Double> graph) {
        // Dijkstra's algorithm implementation as per pseudocode in spec
        Queue<Path<P>> active = new PriorityQueue<>(new Sort<>());
        HashSet<P> finished = new HashSet<>();
        active.add(new Path<>(start));
        while (!active.isEmpty()) {
            Path<P> minPath = active.remove();
            P minDest = minPath.getEnd();
            if (minDest.equals(dest)) {
                return minPath;
            }
            if (!finished.contains(minDest)) {
                for (Node<P, Double>.Edge curr : graph.getNode(minDest).getEdges()) {
                    P currChild = curr.getChild().getLabel();
                    if (!finished.contains(currChild)) {
                        active.add(minPath.extend(currChild, curr.getEdgeLabel()));
                    }
                }
                finished.add(minDest);
            }
        }
        return null;
    }

    /**
     * Class that sorts the different path costs
     */
    private static class Sort<Point> implements Comparator<Path<Point>> {
        public int compare(Path<Point> p1, Path<Point> p2) {
            return Double.compare(p1.getCost(), p2.getCost());
        }
    }
}
