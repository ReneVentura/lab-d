package lab1.afn.lex;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import java.awt.Dimension;
import java.io.IOException;
import lab1.afn.dfa.ToDfa;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxStyleUtils;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Hashtable;
import java.util.Map;
public class TreeVisualization {
    public static void main(String[] args) throws InterruptedException, IOException {
        // Create a directed weighted graph
        Compute.computeTheVoid("slr-2","slr-2.1");
        DefaultDirectedGraph<String, DefaultEdge> graph =TreeLex.graphTree;
        String idk= Compute.idk;
        System.out.println(idk);
        JGraphXAdapter<String, DefaultEdge> jgxAdapter = new JGraphXAdapter<>(graph);

        // Hide edge labels
        jgxAdapter.getModel().beginUpdate();
        jgxAdapter.getEdgeToCellMap().forEach((edge, cell) -> {
            jgxAdapter.getModel().setValue(cell, "");
        });
        jgxAdapter.getModel().endUpdate();
        Map<String, Object> vertexStyle = jgxAdapter.getStylesheet().getDefaultVertexStyle();
        vertexStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
       
       
        mxGraphComponent component = new mxGraphComponent(jgxAdapter);
        Dimension size = component.getSize();
        int x = size.width / 2;
        int y = size.height / 2;
       
        component.setPreferredSize(new Dimension(800, 800)); // Set preferred size to 800 x 600 pixels
        mxHierarchicalLayout layout = new mxHierarchicalLayout(jgxAdapter);
        layout.execute(jgxAdapter.getDefaultParent());
        component.setAlignmentX(x);
        component.setAlignmentY(y);
        JFrame frame = new JFrame("Tree Visualization");
        frame.getContentPane().add(component);  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
