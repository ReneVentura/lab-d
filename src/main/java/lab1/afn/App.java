package lab1.afn;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;


import lab1.afn.dfa.ToDfa;
import lab1.afn.preafn.extensionReg;
import lab1.afn.preafn.infpos;
//import lab1.afn.thompson.DfaMinimizer;
import lab1.afn.thompson.Edges;
import lab1.afn.thompson.NfaToDfaConverter;
import lab1.afn.thompson.Thompson;
import lab1.afn.preafn.infpos;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import lab1.afn.thompson.State;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/*
 * 
 * copiar en terminal   1 para interpretar epsilon
    (a|b)*(b|a)*abb
    ((ε|a)b*)*
    (.|;)*-/.(.|;)*
    (x|t)+((a|m)?)+
    ("(.(;(.;(.|;)+)*)*)*

 */
public class App {
    
  

    public static void main(String[] args) throws IOException, InterruptedException {
      //  System.setProperty("org.graphstream.ui", "javafx");
        //Platform.startup(() -> {}); 
      
        /*List<Edges> edges = thompson.getTransitionList();
        
        Graph graph = new SingleGraph("Thompson NFA");
        
        // Add nodes
        for (int i = 0; i <= thompson.stateCount; i++) {
            Node node = graph.addNode(Integer.toString(i));
            if (i == thompson.stateCount) {
                node.setAttribute("ui.class", "final");
            }
            if (thompson.initialState.states.contains(i)) {
                node.setAttribute("ui.class", "initial");
            }
            node.setAttribute("ui.label", Integer.toString(i));
        }
        
        // Add edges
        int c=15;
        for (Edges edge : edges) {
            c++;
            graph.addEdge(Integer.toString(c) , Integer.toString(edge.getSource()), Integer.toString(edge.getTarget())).setAttribute("ui.label", edge.getLabel());
        }
        
        // Set style
        graph.setAttribute("ui.stylesheet", "graph { fill-color: white; } node { shape: circle; size: 30px, 30px; fill-color: white; stroke-mode: plain; stroke-color: black; text-size: 20px; text-color: black; text-style: bold; } node.final { fill-color: #D9E5FF; stroke-color: blue; } node.initial { fill-color: #D9E5FF; stroke-mode: double; stroke-color: green; } edge { fill-color: black; size: 2px; text-size: 20px; text-color: black; text-style: bold; }");
        
        // Display graph
        Viewer viewer = graph.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);*/
        // Create an instance of Thompson representing the NFA
         // create a new Thompson NFA
         // ejercicio 1
        System.out.print("*************** EJERCICIO 1 (a|b)*.(b|a)*.a.b.b *************************\n");
        
        String regex = "(a|b)*$(b|a)*$a$b$b";
        String input="bbabb" ;
        String input2="babb" ;
        String input3="aaaaaaaaaabbbbbbabababababababababababababbb" ;
        String input4="abb" ;
  System.out.println(infpos.infixToPostfix2(regex));
       System.out.println(infpos.special(regex,'.'));
        ToDfa dfa= new ToDfa(infpos.special(regex,'$'));
        System.out.println(dfa.getSymbols());
System.out.println("Acepta "+input+" ? "+dfa.accepts(input)+"\n");
        System.out.println("Acepta "+input2+" ? "+dfa.accepts(input2)+"\n");
        System.out.println("Acepta "+input3+" ? "+dfa.accepts(input3)+"\n");
        System.out.println("Acepta "+input4+" ? "+dfa.accepts(input4)+"\n");
        System.out.println(dfa.getTransitionsResult().toString());

       String pos= infpos.special(regex,'.');
       Set<Character>operators;
       operators= new HashSet<Character>(); 
       operators.add('*');
       operators.add('|');
       operators.add('^');
       operators.add('.');

        }
       }
    




  

    

