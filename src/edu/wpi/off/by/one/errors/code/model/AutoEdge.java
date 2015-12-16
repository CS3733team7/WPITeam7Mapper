package edu.wpi.off.by.one.errors.code.model;

/**
 * Created by roboman2444 on 12/8/15.
 */
public class AutoEdge {
    /*
    public static void doAuto(Display dpy, Id nodeid){
        Node s = dpy.getGraph().returnNodeById(nodeid);
        Graph ge = dpy.getGraph();
        Coordinate nc = s.getCoordinate();
        if(s == null) return;
        for(Node n : ge.getNodes()){
            if(n == null) continue;
            if(n.getId() == nodeid) continue;
            Coordinate ec = n.getCoordinate();
            if(ec.getZ()- 0.1 < nc.getZ() || ec.getZ() +0.1 > nc.getZ()) continue;
            //check if it makes the maps
            int i;
            for(i = 0;i < dpy.getMaps().size(); i++){
                Map m = dpy.getMaps().get(i);
                if(m == null) continue;
                Coordinate mc = m.getCenter();
                if(mc.getZ()- 0.1 < nc.getZ() || mc.getZ() +0.1 > nc.getZ()) continue;
                if(!m.checkLines(nc, ec)) break;
            }
            if(i < dpy.getMaps().size()) continue;
            //check if edge already exists
            for(i = 0; i < n.getEdgelist().size(); i++){
                Id e = n.getEdgelist().get(i);
                Edge eg = ge.returnEdgeById(e);
                if(eg == null) continue;
                if(eg.getNodeA() == nodeid || eg.getNodeB() == nodeid) break;
            }
            if(i < n.getEdgelist().size()) continue;
            //got to the end!
            //gotta add here
            ge.addEdge(nodeid, n.getId());
            System.out.println("added!");
        }
    }
    */
}
