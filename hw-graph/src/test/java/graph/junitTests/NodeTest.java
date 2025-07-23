package graph.junitTests;

import graph.Node;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

public class NodeTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    @Test
    public void testAddEdge() {
        Node<String,String> parent = new Node<>("parent");
        Node<String,String> child = new Node<>("child");

        assertFalse("cannot create edge with null label", parent.addEdge(child, null));
        assertFalse("cannot create edge with null child", parent.addEdge(null, "edge1"));
        assertFalse("cannot create edge with empty label", parent.addEdge(child, ""));

        assertTrue("edge should be added", parent.addEdge(child, "edge1"));
        assertTrue("more than 1 edge can be added", parent.addEdge(child, "edge2"));

        assertFalse("edge with same label cannot be added", parent.addEdge(child, "edge1"));
    }
}
