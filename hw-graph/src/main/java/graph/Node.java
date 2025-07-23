package graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Node represents a mutable element in the Graph ADT.
 * Each node as a list of edges that connects with children nodes.
 *
 */
public class Node<V, E> {
    /**
     * Abstraction function:
     * AF(this) = a collection of unordered edges (this.edges) {e1, e2, e3, ..., en}
     * that represents the individual edges leaving this node which is the parent
     * for these edges
     *
     * Representation Invariant:
     * edges != null && !edges.contains(null) && every edge has unique label
     * && no edge label is null && node label is not null
     */

    private static final boolean CHECK_REP = false;
    private V nodeLabel;
    private Set<Edge> edges;

    /**
     * @param nodeLabel = name of this Node
     * @spec.requires nodeLabel != null
     * @spec.effects Creates new Node with given name
     */
    public Node(V nodeLabel) {
        this.nodeLabel = nodeLabel;
        edges = new HashSet<Edge>();
        checkRep();
    }

    /**
     * Creates new edge between this node and child node
     * @param child = child node of this new edge
     * @param edgeLabel = label of this new edge
     * @spec.requires child != null and edgeLabel != null
     * @spec.modifies This node's edges
     * @spec.effects This node contains the new edge created
     * @return true if edge added, false if edge not successfully added (identical edge)
     * or empty label
     */
    public boolean addEdge(Node<V,E> child, E edgeLabel) {
        if (child == null || edgeLabel == null || edgeLabel == "") {
            return false;
        }

        // utilizes HashSet.add return value to see if unique or not
        boolean added = edges.add(new Edge(child, edgeLabel));
        checkRep();

        return added;
    }

    /**
     * @return the label of this node
     */
    public V getLabel() {
        return this.nodeLabel;
    }

    /**
     *
     * @return set of this node's edges
     */
    public Set<Edge> getEdges() {
        return Collections.unmodifiableSet(this.edges);
    }

    /**
     * Standard hashCode function.
     * @return an int that all objects equal to this will also return
     */
    @Override
    public int hashCode() {
        return this.nodeLabel.hashCode();
    }

    /**
     * Standard equality operation.
     *
     * @param obj the object to be compared for equality
     * @return true if and only if 'obj' is an instance of Node and 'this' and 'obj'
     * represent the same graph
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node)) {
            return false;
        }

        boolean equal = this.nodeLabel.equals(((Node<?, ?>) obj).getLabel());

        return equal;
    }

    /**
     * Throws an exception if the representation invariant is violated
     */
    private void checkRep() {
        assert edges != null : "edges must not be null";

        if (CHECK_REP) {
            HashSet<E> allLabels = new HashSet<>();

            for (Edge e : this.edges) {
                assert e != null : "edge must not be null";
                assert e.edgeLabel != null : "edge label must not be null";
                assert !allLabels.contains(e.edgeLabel) : "edge labels must be unique";
                allLabels.add(e.edgeLabel);
            }
        }
    }

    /**
     * Edge class represents a connection between two Node objects and is
     * immutable
     */
    public class Edge {
        /**
         * Abstract Function: this.child represents where the connection ends (child node)
         * and this.edgeLabel represents the edge's label
         *
         * Representation Invariant:
         * this.child != null and this.edgelabel != null
         */

        private Node<V, E> child;
        private E edgeLabel;

        /**
         *
         * @param child Node at end of edge connection
         * @param edgeLabel label for this edge
         * @spec.requires end != null and edgeLabel != null
         * @spec.effects creates new Edge with the child node this.child
         * and label this.edgeLabel
         */
        public Edge(Node<V,E> child, E edgeLabel) {
            this.child = child;
            this.edgeLabel = edgeLabel;
            this.checkRep();
        }

        /**
         *
         * @return the child node of this edge connection
         */
        public Node<V,E> getChild() {
            return this.child;
        }

        /**
         *
         * @return the label of this edge
         */
        public E getEdgeLabel() {
            return this.edgeLabel;
        }

        private void checkRep() {
            assert this.edgeLabel != null : "label cannot be null";
            assert this.child != null : "child node cannot be null";
        }

        /**
         * Standard hashCode function.
         * @return an int that all objects equal to this will also return
         */
        @Override
        public int hashCode() {
            return this.edgeLabel.hashCode() + (31 * this.child.hashCode());
        }

        /**
         * Standard equality operation.
         *
         * @param obj the object to be compared for equality
         * @return true if and only if 'obj' is an instance of Edge and 'this' and 'obj'
         * represent the same graph
         */
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node<?,?>.Edge)) {
                return false;
            }
            Node<?,?>.Edge objEdge = (Node<?,?>.Edge) obj;

            boolean childEqual = this.child.equals(objEdge.child);
            boolean labelEqual = this.edgeLabel.equals(objEdge.edgeLabel);

            // both must be equal
            return childEqual && labelEqual;
        }
    }
}
