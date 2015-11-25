package edu.wpi.off.by.one.errors.code.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* file format
 * one element per line
 * may have whitespace wherever you want
 * one character at the beginning of the line (may have whitespace before) that indicates the type (p for point, e for edge, etc)
 * each element has a list of args, seperated by whitespace, eg: e 10.5 20.3 18.5 james
 * may change this later, so i can include strings that have spaces in them
 *
 */

//how i load things
//parse file into various buffers (file may be out of order, idk)
//flush node buffer, get node ids
//flush edge buffer, use edge-node indices to look up ids, add edge using those ids (needs node to already be flushed)

public class FileIO {
	static ArrayList<String[]> nodebuf;
	static ArrayList<String[]> edgebuf;

	/**
	 * flush node and edge's buffer
	 * @param dpy
	 */
	static void flush(Display dpy) {
		ArrayList<Id> nodeids = new ArrayList<Id>();
		Graph g = dpy.getGraph();
		int i;
		for (i = 0; i < nodebuf.size(); i++) {
			String[] args = nodebuf.get(i);
			nodeids.add(parsepointline(args, g));
		}
		for (i = 0; i < edgebuf.size(); i++) {
			String[] args = edgebuf.get(i);
			parseedgeline(args, g, nodeids);
		}
		nodeids = null;// best i can do to "free" it
	}
	
	/**
	 * parse input map path
	 * @param args
	 * @param dpy
	 * @return 1 if success
	 */
	static int parsemapline(String[] args, Display dpy){
		//for(String s : args) System.out.println("arg:" + s);
		Coordinate c = new Coordinate(Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]));
		Map m = new Map(args[0], c, Float.parseFloat(args[4]), Float.parseFloat(args[5]));
		if(args.length > 6) m.setName(getTags(args[6])[0]);
		System.out.println(m.getName());
		dpy.setMap(m);
		return 1;
	}
	/**
	 * 
	 * @param args
	 * @return
	 */
	static String[] getTags(String args){
		
		return args.replace("_", " ").split(",");
		
		
	}
	static String toTags(String[] args){
		
		StringBuilder ret = new StringBuilder();
		for(int i= 0; i < args.length; i++){
			String elt = args[i];
			ret.append(elt.replace(" ", "_"));
			if(i < args.length -1) ret.append(",");
		}
		return ret.toString();
	}
	/**
	 * parse 
	 * @param args
	 * @param g
	 * @return id of the node; -1 if wrong input
	 */
	static Id parsepointline(String[] args, Graph g) {
		if (args.length > 5)
			return null;
		Coordinate c = new Coordinate(Float.parseFloat(args[0]), Float.parseFloat(args[1]), Float.parseFloat(args[2]));
		return g.addNodeRint(c);
	}

	/**
	 * parse the input edge
	 * @param args
	 * @param g
	 * @param nodeids
	 * @return -1 if wrong input; edge id if success
	 */
	static Id parseedgeline(String[] args, Graph g, ArrayList<Id> nodeids) {
		if (args.length > 3)
			return null;
		int indice1 = Integer.parseInt(args[0]);
		int indice2 = Integer.parseInt(args[1]);
		if (indice1 < 0 || indice1 > nodeids.size() - 1 || indice2 < 0 || indice2 > nodeids.size() - 1)
			return null;
		Id id1 = nodeids.get(indice1);
		Id id2 = nodeids.get(indice2);
		return g.addEdgeRint(id1, id2); 
	}

	/**
	 * parse input line
	 * @param line
	 * @param dpy
	 */
	static void parseline(String line, Display dpy) {
		// get ready for some obviously dont know how to parse strings in java
		// so im doing it manually stuff
		int i;
		int len = line.length();
		char s = Character.toLowerCase(line.charAt(0));
		for (i = 0; i < len && Character.isWhitespace(s); i++)
			s = Character.toLowerCase(line.charAt(i)); // get rid of whitespace
														// in front
		//System.out.println(line);
		switch (s) {
		case 'p': // point;
			nodebuf.add(line.substring(i + 1).trim().split("\\s"));
			break;
		case 'e': // edge;
			edgebuf.add(line.substring(i + 1).trim().split("\\s"));
			break;
		case 'm': // map;
			parsemapline(line.substring(i + 1).trim().split("\\s"), dpy);
			break;
		default: // some sorta error, or unrecognized element type
			break;
		}
	}

	// when calling load, you should ALWAYS keep track of the return display. It
	// may create a new one.
	/**
	 * load the information about display 
	 * @param inpath: input path for the file
	 * @param indpy: input display class
	 * @return current display
	 */
	public static Display load(String inpath, Display indpy) {
		Display curdpy = indpy;
		if (curdpy == null)
			curdpy = new Display(); // CONTRUCTOOOOOOOOOR needed plz

		// read in all lines
		Path pty = Paths.get(inpath);
		if (!Files.exists(pty)) {
			System.out.printf("File %s does not exist, unable to load\n", inpath);
		}
		edgebuf = new ArrayList<String[]>();
		nodebuf = new ArrayList<String[]>();
		List<String> lines = null;
		// todo should fix this try catch BS
		try {
			lines = Files.readAllLines(pty, Charset.defaultCharset());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int i = 0;
		for (String line : lines) {
			parseline(line, curdpy);
			i++;
		}
		flush(curdpy);
		System.out.printf("Read %d lines\n", i);
		edgebuf = null;
		nodebuf = null; // best i can do to "free" it
		return curdpy;
	}

	/**
	 * save the display information
	 * @param inpath: input path for this file
	 * @param indpy: input display class
	 * @return -1 if fail; otherwise, success
	 */
	public static int save(String inpath, Display indpy) {
		// todo fix this try catch BS
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(inpath, "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(writer == null) return -1;
		Graph g = indpy.getGraph();
		if(g == null) return -1;
		//will change this to ID, Integer
		HashMap<Id, Integer> ids = new HashMap<Id, Integer>();
		int i = 0;
		for( Node n : g.getNodes()){
			if(n == null) continue;
			ids.put(n.getId(), i);
			Coordinate c = n.getCoordinate();
			writer.printf("p %f %f %f\n", c.getX(), c.getY(), c.getZ());
			i++;
		}
		for( Edge e : g.getEdges()){
			if(e == null) continue;
			int indice1 = ids.get(e.getNodeA());
			int indice2 = ids.get(e.getNodeB());
			writer.printf("e %d %d\n", indice1, indice2);
		}
		ids = null;
		//will change this over to iterate over a list later
		Map m = indpy.getMap();
		if(m == null){//continue;
			
		} else {
			Coordinate c = m.center; // should this be a getter?
			//writer.printf("m %s %f %f %f %f %f\n", m.imagePath, c.getX(), c.getY(), c.getZ(), m.rotation, m.scale);
			String[] aaa = null;
			aaa[0] = m.getName();
			writer.println("m " + m.imagePath + " " + c.getX() + " " + c.getY() + " " + c.getZ() + " " + m.rotation + " " + m.scale + " " + toTags(aaa));

		}
		if (writer != null) writer.close();
		return i;
	}

}
