package graph;

import java.util.*;

/**
 * This Graph class represents a directed and labeled Graph ADT
 * built up from Nodes
 */

public class Graph<V, E> {

    /**
     * Abstraction function:
     * AF(this) = collection of Nodes and Edges where Nodes are represented
     * as (node1, node2, ..., noden) and Edges are represented as
     * (node1.edges, node2.edges, node3.edges, ..., noden.edges)
     *
     * Representation invariant:
     * nodes != null && !nodes.containsValue(null) && nodes.keySet() does
     * not contain any duplicates
     */

    private static final boolean CHECK_REP = false;

    // map so nodes can be searched for by label
    private Map<V, Node<V,E>> nodes;

    /**
     * @spec.effects Constructs empty graph
     */
    public Graph() {
        this.nodes = new HashMap<>();
        checkRep();
    }

    /**
     * @param nodes = a list of nodes to be added to the Graph when first
     *              created
     * @spec.requires no node in list is null
     * @spec.effects Constructs a Graph with given nodes in the graph
     */
    public Graph(List<Node<V,E>> nodes) {
        if (nodes == null || nodes.contains(null)) {
            throw new IllegalArgumentException();
        }

        this.nodes = new HashMap<>();

        // add all nodes in list to this graph
        for (Node<V,E> currNode : nodes) {
            this.nodes.put(currNode.getLabel(), currNode);
        }

        checkRep();
    }

    /**
     * Add the node into the graph if new, i.e it has a unique label
     *
     * @param node = Node that is added to the graph
     * @spec.requires node != null
     * @spec.modifies the current graph
     * @spec.effects the current graph's nodes list now contains new node
     * @return true if added, false if it is not added (not unique name)
     */
    public boolean addNode(Node<V,E> node) {
        if (nodes.containsKey(node.getLabel())) {
            return false;
        }

        nodes.put(node.getLabel(), node);
        checkRep();
        return true;
    }

    /**
     *  Create an edge connecting two nodes already in the graph if new,
     *  i.e it has different parent and child nodes or it has the same
     *  parent and child nodes as a current edge in the graph but the
     *  label is different
     *
     *  The parent is the starting point of the edge and the child
     *  is the ending point of the that edge
     *
     * @param parent = designated parent node
     * @param child = designed child node
     * @param label = the label of the new edge
     * @spec.requires parent != null AND end != null AND label != null
     * @spec.modifies parent, new edge
     * @spec.effects creates and adds the new edge to the set of edges for
     * this graph
     * @return true if edge is successfully added, false if edge is already in
     * graph (identical parent and child nodes and same label name as well)
     * or edge label is empty
     */
    public boolean addEdge(Node<V,E> parent, Node<V,E> child, E label) {
        if (!nodes.containsKey(parent.getLabel()) || !nodes.containsKey(child.getLabel())) {
            return false;
        }

        boolean addedEdge = parent.addEdge(child, label);
        checkRep();

        return addedEdge;
    }

    /**
     *
     * @param nodeLabel label of node we want to get
     * @return node with given label or null if node
     * not in graph
     */
    public Node<V,E> getNode(V nodeLabel) {
        Node<V, E> node = nodes.get(nodeLabel);

        return node;
    }

    /**
     *
     * @return list of all the labels of nodes in this graph
     */
    public List<V> getNodes() {
        Collection<V> allNodes = nodes.keySet();
        return new ArrayList<>(allNodes);

    }

    /**
     * Throws an exception if the representation invariant is violated
     */
    private void checkRep() {
        assert this.nodes != null : "nodes cannot be null";

        if (CHECK_REP) {
            Set<V> nodeLabels = new HashSet<>();
            for (V currNode : nodes.keySet()) {
                assert currNode != null : "nodes cannot contain null value";

                assert !nodeLabels.contains(currNode) : "all node labels must be unique";
                nodeLabels.add(currNode);
            }
        }
    }
}
