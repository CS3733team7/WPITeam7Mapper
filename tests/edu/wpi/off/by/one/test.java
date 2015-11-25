package edu.wpi.off.by.one;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import org.junit.rules.ExpectedException;

import edu.wpi.off.by.one.errors.code.*;
import edu.wpi.off.by.one.errors.code.model.Coordinate;
import edu.wpi.off.by.one.errors.code.model.Display;
import edu.wpi.off.by.one.errors.code.model.Edge;
import edu.wpi.off.by.one.errors.code.model.FileIO;
import edu.wpi.off.by.one.errors.code.model.Graph;
import edu.wpi.off.by.one.errors.code.model.Map;
import edu.wpi.off.by.one.errors.code.model.Node;
import edu.wpi.off.by.one.errors.code.model.Path;
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
        /*
        Graph g = new Graph();
        Coordinate c1 = new Coordinate(400);
        Coordinate c2 = new Coordinate(100,0,0);
        Coordinate c3 = new Coordinate(0);
        Coordinate c4 = new Coordinate(600);
        g.addNode(c1);
        g.addNode(c2);
       
        assertEquals( g.addEdgeRint(0, 1), 0);
        
        g.addNode(c3);
        g.addNode(c4);
        
        assertEquals( g.addEdgeRint(2, 3), 1);
        
        Vector<Node> lon = g.getNodes();
        assertEquals(lon.get(0).getId(), 0);
        assertEquals(lon.get(1).getId(), 1);
        
        Vector<Edge> loe = g.getEdges();
        assertEquals(loe.get(0).getId(), 0);
        g.editNode(1, c1);//could have something wrong
        assertEquals(lon.get(1).getCoordinate(), c1);
        */
    }
    
    //test FileIO
    @Test 	
   public void testFile(){
        /*
        Map amap = new Map("Atwater_Kent.png", "Atwater_Kent.png", 0, 0);
        amap.setScale(1);
        Graph g = new Graph();
        Coordinate c1 = new Coordinate(400);
        Coordinate c2 = new Coordinate(100,0,0);
        g.addNode(c1);
        g.addNode(c2);
        assertEquals( g.addEdgeRint(0, 1), 0);
        Display dpy = new Display(amap, g);
        String dir = System.getProperty("user.dir");
        FileIO.save(dir + "testio.txt", dpy);
        
        FileIO.load(dir+"testio.txt", null);
        
        FileIO.load(dir+"testio.txt", dpy);
        */
    }
    
    //test Path
    @Test
        public void testAStar(){
        /*
            	String dir = System.getProperty("user.dir");
            	Display d = FileIO.load(dir + "/src/testmap.txt", null);
            	Graph g = d.getGraph();
        
            	Path traversablePath = new Path(5, 12);
            	ArrayList<Integer> expTPath1 = new ArrayList<Integer>();
            	ArrayList<Integer> expTPath2 = new ArrayList<Integer>();
            	expTPath1.addAll(Arrays.asList(5, 4, 17, 6, 7, 10, 9, 11, 12));
            	expTPath2.addAll(Arrays.asList(5, 4, 17, 7, 10, 9, 11, 12));
            	traversablePath.runAStar(g.getNodes(), g.getEdges());
            	assertNotEquals(traversablePath.getRoute(), expTPath1);
            	assertEquals(traversablePath.getRoute(), expTPath2);
        
            	Path nonTraversablePath = new Path(5, 0);
            	ArrayList<Integer> expNTPath = new ArrayList<Integer>();
            	nonTraversablePath.runAStar(g.getNodes(), g.getEdges());
            	assertEquals(nonTraversablePath.getRoute(), expNTPath);
            	*/
            }
    
}