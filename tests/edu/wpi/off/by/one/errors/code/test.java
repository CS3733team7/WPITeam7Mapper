package edu.wpi.off.by.one.errors.code;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.wpi.off.by.one.errors.code.model.Coordinate;
import edu.wpi.off.by.one.errors.code.model.Graph;
import edu.wpi.off.by.one.errors.code.model.Node;
import junit.framework.TestCase;

public class test extends TestCase{
    
    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    
    //test coordinate getters
    @Test
    public void testCo1(){
        Coordinate c = new Coordinate(100,200, -300);
        assertEquals(c.getX(), (float)100.0);
        assertEquals(c.getY(), (float)200.0);
        assertEquals(c.getZ(), (float)-300.0);
    }
    
    @Test
    public void testCo2(){
        Coordinate c = new Coordinate(100,200);
        assertEquals(c.getX(), (float)100.0);
        assertEquals(c.getY(), (float)200.0);
        assertEquals(c.getZ(), (float)0.0);
    }
    @Test
    public void testCo3(){
        Coordinate c = new Coordinate(400);
        assertEquals(c.getX(), (float)400.0);
        assertEquals(c.getY(), (float)400.0);
        assertEquals(c.getZ(), (float)400.0);
    }
    
    //test Graph
    @Test
    public void testGraph(){
        Graph g = new Graph();
        Coordinate c1 = new Coordinate(400);
        Coordinate c2 = new Coordinate(100,0,0);
        Coordinate c3 = new Coordinate(0);
        Coordinate c4 = new Coordinate(600);
        
        Node n1 = g.addNode(c1);
        Node n2 = g.addNode(c2);
        Node n3 = g.addNode(c3);
        Node n4 = g.addNode(c4);
        
        g.addEdgeRint(n1.getId(), n2.getId());
        assertEquals(n1.getEdgelist().get(0) == n2.getEdgelist().get(0), true);
        g.addEdgeRint(n3.getId(), n4.getId());
        assertEquals(n3.getEdgelist().get(0) == n4.getEdgelist().get(0), true);
    }
    
    //test FileIO
    /*@Test
   public void testFile(){
        Map amap = new Map("Atwater_Kent.png", "Atwater_Kent.png", 0, 0);
        amap.setScale(1);
        Graph g = new Graph();
        Coordinate c1 = new Coordinate(400);
        Coordinate c2 = new Coordinate(100,0,0);
        Node n1 = g.addNode(c1);
        Node n2 = g.addNode(c2);
        g.addEdgeRint(n1.getId(), n2.getId());
        Display dpy = new Display(amap, g);
        String dir = System.getProperty("user.dir");
        //FileIO.save(dir + "testio.txt", dpy);//test save file
        //FileIO.load(dir+"testio.txt", null);//test load file without display
        //FileIO.load(dir+"testio.txt", dpy);//test load file with display

    }
    
    //test Path
    @Test
    public void testAStar(){

    	String dir = System.getProperty("user.dir");
    	Display d = FileIO.load(dir + "/src/edu/wpi/off/by/one/errors/code/resources/maps/txtfiles/testmap.txt", null);
    	Graph g = d.getGraph();
    	Path traversablePath = new Path(g.getNodes().get(5).getId(), g.getNodes().get(12).getId());
    	ArrayList<Integer> expTPath1 = new ArrayList<Integer>();
    	ArrayList<Integer> expTPath2 = new ArrayList<Integer>();
    	expTPath1.addAll(Arrays.asList(5, 4, 17, 6, 7, 10, 9, 11, 12));
    	expTPath2.addAll(Arrays.asList(5, 4, 17, 7, 10, 9, 11, 12));
    	traversablePath.runAStar(g);
    	assertNotEquals(traversablePath.getRoute(), expTPath1);
    	assertEquals(traversablePath.getRoute(), expTPath2);
    	
    	Path nonTraversablePath = new Path(g.getNodes().get(5).getId(), g.getNodes().get(0).getId());
    	ArrayList<Integer> expNTPath = new ArrayList<Integer>();
    	nonTraversablePath.runAStar(g);
    	assertEquals(nonTraversablePath.getRoute(), expNTPath);

    }*/
}
