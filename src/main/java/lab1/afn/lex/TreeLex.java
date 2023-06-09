package lab1.afn.lex;
import java.util.*;
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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxStyleUtils;
import lab1.afn.dfa.ToDfa;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Hashtable;
import java.util.Map;
public class TreeLex {
    TreeLeaf root;
    Set<Character>operators;
    HashMap<String,String> ids;
    Character concat;
    public static String la2="";
    public static DefaultDirectedGraph<String, DefaultEdge> graphTree = new DefaultDirectedGraph<>(DefaultEdge.class);

    public TreeLex(String postfix, Character concat,HashMap<String,String>ids) throws InterruptedException, IOException{
        operators= new HashSet<Character>(); 
        operators.add('*');
        operators.add('|');
        operators.add('^');
        operators.add(concat);
        this.ids=ids;
        this.concat=concat;
        createTree(postfix);
        //readRun("C:\\Users\\ravz2\\Escritorio\\afn\\src\\main\\java\\lab1\\afn\\lex\\slr-4.2.yal.run", postfix);
    }

    public void createTree(String postfix){
        Stack<TreeLeaf> stack= new Stack<TreeLeaf>();
        System.out.println("esto"+postfix.trim());

        Set<TreeLeaf> l= new HashSet<TreeLeaf>();
        String currLine;
        Character c;
        for(int i = 0;i<postfix.length();i++){
            c= postfix.charAt(i);
            if(c!= ' '){
                if(operators.contains(c)){
                    if(c!= '*'){
                        TreeLeaf rightNode= stack.pop();
                        
                        TreeLeaf lefNode= stack.pop();
                        
                        TreeLeaf node= new TreeLeaf(Character.toString(c));
                       
                        node.addNode(rightNode);
                        node.addNode(lefNode);
                     

                        stack.push(node);
                    }
                    else{
                        TreeLeaf child= stack.pop();
                        
                        TreeLeaf node = new TreeLeaf(Character.toString(c));
                        
                       
                        node.addNode(child);
                        

                        stack.push(node);
                    }
                    
                }
                else{
                         currLine="";
                     for(int j= i; j<postfix.length();j++){
                        if(!operators.contains(postfix.charAt(j)) && postfix.charAt(j)!=' '){
                            currLine+= postfix.charAt(j);

                        }
                        else if (operators.contains(postfix.charAt(j)) || postfix.charAt(j)==' '){
                            i= (currLine.length()-1)+i;
                            break;
                        }

                     
                    }
                    if( ids.containsKey(currLine)){
                        TreeLeaf node= new TreeLeaf(ids.get(currLine));
                        
                       
                        stack.push(node);
                    }
                    else if( currLine.equals("null")){
                        TreeLeaf node= new TreeLeaf("zero");
                        

                        stack.push(node);
                    }
                    else{
                        TreeLeaf node= new TreeLeaf(currLine);
                        

                        stack.push(node);
                    }
                }
            }
        }
        
        root= stack.pop();
        TreeLeaf currenLeaf;
        ArrayList<String>expr= new ArrayList<String>();
        ArrayList<TreeLeaf>queue= new ArrayList<TreeLeaf>();
        queue.add(root);
        String la="";
        
        int ID= 0;
        int aux=0;
        String prev="";
        System.out.println("ROOT "+root.getLabel());
        graphTree.addVertex(root.getLabel()+": "+ID);
        la= root.getLabel()+": "+aux;
        expr.add(root.getLabel());
        la2+= root.getLabel()+" ";
        while(!queue.isEmpty()){ 
       aux=ID;
       ID++;
       prev=la;
       currenLeaf= queue.get(0);
       
       graphTree.addVertex(prev);
       
       queue.remove(0);
       
       for(int i=0;i<currenLeaf.getNode().size();i++){
        ID++;
           la=currenLeaf.getNode().get(i).getLabel()+": "+ID;
           expr.add(currenLeaf.getNode().get(i).getLabel());
           la2+=currenLeaf.getNode().get(i).getLabel()+" ";
           graphTree.addVertex(la);
           graphTree.addEdge(prev, la);
           queue.add(currenLeaf.getNode().get(i));
       }
    }
        for (DefaultEdge edge : graphTree.edgeSet()) {
            String source = graphTree.getEdgeSource(edge);
            String target = graphTree.getEdgeTarget(edge);
            
        
            System.out.println(source +"--> " + target);
        }


     
    }

    public static String formatTree(String tree, Character concat){
        List<Character> symbols = Arrays.asList('+','*','?','|','^');
        List<Character> allSymbols = Arrays.asList('+','*','?','|','^','(',')');
        String result = new String();
        

        for (int i = 0; i < tree.length();i++) {
            Character sub1 = tree.charAt(i);
            String id = "";
            if(!allSymbols.contains(sub1)){
                id += sub1;
                for(int j = i+1; j < tree.length(); j++){
                    if(!allSymbols.contains(tree.charAt(j))){
                        id += tree.charAt(j);
                    }
                    else{
                        i += (j - i - 1);
                        break;
                    }
                }
            }

            if (i + 1 < tree.length()) {
                Character sub2 = tree.charAt(i+1);

                if(id.equals("")){
                    result += Character.toString(sub1);
                }
                else{
                    result += id;
                }

                if ((!sub1.equals('(') && !sub2.equals(')') && !(sub1 == '|' || sub1 == '^') && !symbols.contains(sub2))) {
                    result += concat;
                }
            }
        }
        result += tree.charAt(tree.length() - 1);
        return result;
    }


    public Map<String, String> MapTheThing(){
        la2= la2.replace(Character.toString(concat), "");
        String[] substrings = la2.split("\\|");
        Map<String, String> map = new HashMap<>();

        for (String substring : substrings) {
            String key = "";
            String value = "";
            String trimmed = substring.trim();
            if (!trimmed.isEmpty()) { // skip empty substrings
                String[] parts = trimmed.split("\\s+");
                if (parts.length >= 2) {
                    for(int i=0; i<parts[1].length();i++){
                        if(!Character.isDigit(parts[1].charAt(i))){
                            key = parts[0];
                            value = parts[1];
                        }
                    }
                    if(key!="" && value!=""){
                        map.put(key, value);

                    }
                }
            }
        }
        for (String value : map.values()) {
            if (value.equals("zero")) {
                // Replace the value with null
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (entry.getValue().equals(value)) {
                        entry.setValue(" ");
                    }
                }
            }
        }
        return map;
    }
    public String readRun(String path, String postfix)throws InterruptedException, IOException{
        String current;
        String answer="";
        BufferedReader reader = new BufferedReader(new FileReader(path));
        while ((current = reader.readLine()) != null) {
            answer+= current+" ";
        }
        Map<String, String> map= MapTheThing();
        ToDfa dfa= new ToDfa(postfix);
        String finall= dfa.accepts(answer, map);
        reader.close();
        return finall;
    }
   
}
