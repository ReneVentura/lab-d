package lab1.afn.lex;
import java.util.*;

public class TreeLeaf {
    String label;
    ArrayList<TreeLeaf> node;
    static  int id;
    static  int id2;
 
    public TreeLeaf(String label){
        id++;
        this.label=label;
        this.node= new ArrayList<TreeLeaf>();
       
    }
    public String getLabel(){
        return this.label;
    }
    public ArrayList<TreeLeaf> getNode(){
        return this.node;
    }
    public void setLabel(String label){
        
        this.label=label;
    }
    public void addNode(TreeLeaf node){
        id++;
        id2++;
        this.node.add(node);
      
    }
}
