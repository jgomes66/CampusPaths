package graph.junitTests;

import graph.Graph;
import graph.Node;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GraphTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    private Graph<String,String> emptyGraph;
    private Graph<String,String> singleNodeGraph;
    private Graph<String,String> multipleNodesGraph;
    private Graph<String,String> nodesWithEdgeGraph;
    private Graph<String,String> circularConnectionGraph;

    private Node<String,String> parent;
    private Node<String,String> child;
    private Node<String,String> circular;

    @Before
    public void setUp() {
        // set up empty graph
        emptyGraph = new Graph<>();

        // set up graph with single isolated node
        ArrayList<Node<String,String>> singleNode = new ArrayList<>();
        singleNode.add(new Node<>("node"));
        singleNodeGraph = new Graph<>(singleNode);

        // set up graph with multiple nodes
        ArrayList<Node<String,String>> multipleNodes = new ArrayList<>();
        multipleNodes.add(new Node<>("node1"));
        multipleNodes.add(new Node<>("node2"));
        multipleNodes.add(new Node<>("node3"));
        multipleNodesGraph = new Graph<>(multipleNodes);

        // set up graph with 2 nodes connected by an edge
        ArrayList<Node<String,String>> nodesToConnect = new ArrayList<>();
        parent = new Node<>("parent");
        child = new Node<>("child");
        nodesToConnect.add(parent);
        nodesToConnect.add(child);
        nodesWithEdgeGraph = new Graph<>(nodesToConnect);
        nodesWithEdgeGraph.addEdge(parent, child, "edge");

        // set up graph with a node with an edge to itself
        ArrayList<Node<String,String>> nodeToItself = new ArrayList<>();
        circular = new Node<>("node");
        nodeToItself.add(circular);
        circularConnectionGraph = new Graph<>(nodeToItself);
        circularConnectionGraph.addEdge(circular, circular, "circularEdge");
    }

    @Test
    public void testAddNode() {
        // test with adding just one node to empty graph
        Graph<String,String> singleNodeTest = new Graph<>();
        assertTrue(singleNodeTest.addNode(new Node<>("node")));

        // test with adding multiple nodes
        Graph<String,String> multipleNodesTest = new Graph<>();
        assertTrue(multipleNodesTest.addNode(new Node<>("node1")));
        assertTrue(multipleNodesTest.addNode(new Node<>("node2")));
        assertTrue(multipleNodesTest.addNode(new Node<>("node3")));

        // test to see if we return false if node already exists in graph
        assertFalse(singleNodeTest.addNode(new Node<>("node")));
        assertFalse(multipleNodesTest.addNode(new Node<>("node1")));
        assertFalse(multipleNodesTest.addNode(new Node<>("node2")));
        assertFalse(multipleNodesTest.addNode(new Node<>("node3")));
    }

    @Test
    public void testAddEdge() {
        Graph<String,String> addEdgeTestGraph = new Graph<>();

        Node<String,String> newParent = new Node<>("parent");
        Node<String,String> newChild = new Node<>("child");

        // check to see if edgeAdd fails correctly when a node is not in the graph
        assertFalse("nodes not in graph", addEdgeTestGraph.addEdge(newParent, newChild, "e1"));

        addEdgeTestGraph.addNode(newParent);
        addEdgeTestGraph.addNode(newChild);

        // adding edge should now work because both nodes are in graph
        assertTrue("edge should be added", addEdgeTestGraph.addEdge(newParent, newChild, "e1"));

        // adding edge to same parent and child with different name should also work
        assertTrue("edge should be added", addEdgeTestGraph.addEdge(newParent, newChild, "e2"));

        // adding identical edge already located in graph should not work and return false
        assertFalse("edge shouldn't be add", addEdgeTestGraph.addEdge(newParent, newChild, "e1"));
    }

    @Test
    public void testGetNode() {
        Graph<String,String> getNodeTest = new Graph<>();

        Node<String,String> toFind = new Node<>("node");

        getNodeTest.addNode(toFind);

        assertTrue(getNodeTest.getNode("node") == toFind);

        assertTrue(getNodeTest.getNode("otherNode") == null);
    }
}
