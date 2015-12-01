package old.controller;

import edu.wpi.off.by.one.errors.code.application.NodeDisplay;
import edu.wpi.off.by.one.errors.code.model.Display;
import edu.wpi.off.by.one.errors.code.model.Node;

public class NodeEditorController {

	Display currDisp;
	NodeDisplay currentND;
	
	public void updateNodeInfo(NodeDisplay nd){
		
	}
	
	public void onAddTag(String newTag){
		Node n = currDisp.getGraph().returnNodeById(currentND.getNode());
		n.addTag(newTag);
	}
}
