    package lab1.afn.dfa;
    import java.util.*;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
    import java.io.IOException;
    import lab1.afn.thompson.State;
    public class ToDfa {
  // Class variables
  private String postfix; // the postfix regular expression
  private List<Node> nodes; // a list of nodes in the syntax tree
  private Stack<Node> stackOfNodes; // a stack used to build the syntax tree
  private int posCount; // the number of positions in the syntax tree
  private HashMap<List<Integer>, Integer> prevStateMap; // a map from a set of positions to a state ID
  private HashMap<Integer, HashMap<String, Integer>> transtitions; // a map from a state ID to a map of symbols to state IDs
  private List<Transition> transitionsResult; // a list of transitions in the DFA
  private List<Integer> initialState; // the initial state of the DFA
  private HashMap<Integer, List<Integer>> followposTable; // a table of followpos values
  private HashMap<Integer, String> symbolStates; // a map from a position to the symbol that produced it
  private HashMap<Integer, List<Integer>> stateMap; // a map from a state ID to a set of positions
  private int finall; // the position of the final node in the syntax tree
  private int stateIDs; // a counter for state IDs
  private List<String> symbols; // the symbols in the regular expression
  private List<Integer> finalStates; // the final states of the DFA

  public ToDfa(String postfixRegexp) {
      this.postfix = postfixRegexp;
      this.nodes = new LinkedList<>();
      this.stackOfNodes = new Stack<>();
      this.symbols = new LinkedList<>();
      this.finalStates = new LinkedList<>();
      this.initialState = new LinkedList<>();
      this.followposTable = new HashMap<>();
      this.symbolStates = new HashMap<>();
      this.stateMap = new HashMap<>();
      this.prevStateMap = new HashMap<>();
      this.transtitions = new HashMap<>();
      this.transitionsResult = new LinkedList<>();
      stateIDs = 0;
      // Add a "#" to the end of the postfix regular expression
      addHashtag();
              
      // Build the syntax tree
      getTree();
      
      // Compute followpos values
      followPos();
      
      // Transform the syntax tree to a DFA
      transformToDfa();
      System.out.println("postfixRegexp");
  }

   // Add a "#" to the end of the postfix regular expression
  private void addHashtag() {
      this.postfix = this.postfix + " # $";
  }

  private void getTree() {
    String[] a= postfix.split(" ");
       
    for(int i=0;i<a.length;i++){
     a[i]=a[i].replace("*", "").replace("|", "").replace("$", "").replace("^", "");
     
     }
    Set<String> set = new LinkedHashSet<>(Arrays.asList(a));
 // Convert the Set back to an array
    String[] uniqueArr = set.toArray(new String[set.size()]);
    System.out.println(Arrays.toString(uniqueArr));
    for(int i=0; i<uniqueArr.length;i++){
        uniqueArr[i]=uniqueArr[i].replace("#", "");
        if(uniqueArr[i]!="")
        symbols.add(uniqueArr[i]);
      
    }  

      int pos = 1;
      String[] li= postfix.split(" ");
      List<String> list = new ArrayList<>();
      for (String s : li) {
        if (!s.isEmpty()) {
            list.add(s);
        }
    }

    // Convert the ArrayList back to a String array
    li = list.toArray(new String[0]);
      for (int i = 0; i < li.length; i++) {
          String currSymbol = li[i];
          if (li[i].contains("$")||li[i].contains("*")||li[i].contains("|")){
            if(li[i].length()>1){
                int splitIndex= i;
                if (splitIndex != -1) {
                    List<String> list2 = new ArrayList<>(Arrays.asList(li));
                
                    String str = list2.get(splitIndex);
                    String[] strArr = str.split("");
                
                    // Convert the String array to a List
                    List<String> strList = new ArrayList<>(Arrays.asList(strArr));
                
                    // Replace the original element with the new elements
                    list2.remove(splitIndex);
                    list2.addAll(splitIndex, strList);
                    li=list2.toArray(new String[0]);
                
            }
            System.out.println(Arrays.toString(li));
            currSymbol = li[i];
            }
        }
          if ((!currSymbol.equals("*")) && (!currSymbol.equals("$")) && (!currSymbol.equals("|"))) {

              if (!currSymbol.equals("ε")) {
                  Node currNode = new Node(currSymbol);
                  currNode.setIsPosition(true); 
                  currNode.setPosition(pos); 
                  List<Integer> firstLastPos = new LinkedList<>();
                  firstLastPos.add(pos);
                  currNode.setFirstPos(firstLastPos);
                  currNode.setLastPos(firstLastPos); 
                  currNode.setNullable(false);

                  symbolStates.put(pos, currSymbol); 

                  

                  if (currSymbol.equals("#")) { currNode.setLast(true); }
                  pos++; 
              
                  nodes.add(currNode);
                  stackOfNodes.push(currNode);
                  posCount = pos;
              } else {
                  Node currNode = new Node(currSymbol);
                  currNode.setIsPosition(false);
                  List<Integer> emptySet = new LinkedList<>();
                  currNode.setFirstPos(emptySet);
                  currNode.setLastPos(emptySet);
                  currNode.setNullable(true);
                  nodes.add(currNode);
                  stackOfNodes.push(currNode);
              }
          } 
          
          else if (currSymbol.equals("|")) {
              if (!stackOfNodes.isEmpty()) {
                  Node rightChild = stackOfNodes.pop();
                  Node leftChild = stackOfNodes.pop();
                  Node currNode = new Node(currSymbol, leftChild, rightChild);

              
                  boolean isNullable = (rightChild.getNullable()) || (leftChild.getNullable());
                  currNode.setNullable(isNullable);

                  List<Integer> currFirstPos = new LinkedList<>();
                  currFirstPos.addAll(leftChild.getFirstPos());
                  currFirstPos.addAll(rightChild.getFirstPos());
                  currNode.setFirstPos(currFirstPos);

                  List<Integer> currLastPos = new LinkedList<>();
                  currLastPos.addAll(leftChild.getLastPos());
                  currLastPos.addAll(rightChild.getLastPos());
                  currNode.setLastPos(currLastPos);

                  nodes.add(currNode);
                  stackOfNodes.push(currNode);
              }
          }
          else if (currSymbol.equals("$")) {
              if (!stackOfNodes.isEmpty()) {
                  Node rightChild = stackOfNodes.pop();
                  Node leftChild = stackOfNodes.pop();
                  Node currNode = new Node(currSymbol, leftChild, rightChild);

                  
                  boolean isNullable = (rightChild.getNullable()) && (leftChild.getNullable());
                  currNode.setNullable(isNullable);

              
                  List<Integer> currFirstPos = new LinkedList<>();
                  if (leftChild.getNullable()) {
                      currFirstPos.addAll(leftChild.getFirstPos());
                      currFirstPos.addAll(rightChild.getFirstPos());
                  } else {
                      currFirstPos.addAll(leftChild.getFirstPos());
                  }
                  currNode.setFirstPos(currFirstPos);

                  
                  List<Integer> currLastPos = new LinkedList<>();
                  if (rightChild.getNullable()) {
                      currLastPos.addAll(leftChild.getLastPos());
                      currLastPos.addAll(rightChild.getLastPos());
                  } else {
                      currLastPos.addAll(rightChild.getLastPos());
                  }
                  currNode.setLastPos(currLastPos);

                  nodes.add(currNode);
                  stackOfNodes.push(currNode);
              }
          }
          else if (currSymbol.equals("*")) { 
              if (!stackOfNodes.isEmpty()) {
                  Node prevNode = stackOfNodes.pop(); 
                  Node currNode = new Node(currSymbol, prevNode);
                  currNode.setNullable(true); 
              
                  currNode.setFirstPos(prevNode.getFirstPos());
                  currNode.setLastPos(prevNode.getLastPos());
                  
                  nodes.add(currNode);
                  stackOfNodes.push(currNode);
              }
          }  
      }
  }

  private void followPos() {
  
      for (int i = 0; i < posCount; i++) {
          this.followposTable.put(i, null);
      }

      
      for (int i = 0; i < nodes.size(); i++) {
          if (nodes.get(i).isLast()) {
              finall = nodes.get(i).getPosition();
          }
      }

      boolean flag = false;

  
      for (int i = 0; i < nodes.size(); i++) {
          Node currentNode = nodes.get(i);
          String symbol = currentNode.getSymbol();

      
          if (symbol.equals("*")) {
              List<Integer> lastpos = currentNode.getLastPos();

              
              for (int l = 0; l < lastpos.size(); l++) {
                  List<Integer> tmpFollowPos = new LinkedList<>();
                  int currentPos = lastpos.get(l);
                  if (followposTable.get(currentPos) != null) {
                      tmpFollowPos = currentNode.getFirstPos();
                      tmpFollowPos.addAll(followposTable.get(currentPos));
                      followposTable.put(currentPos, tmpFollowPos);

                      if (currentNode.getFirstPos().contains(finall)) {
                          flag = true;
                      }

                  } else {
                      tmpFollowPos = currentNode.getFirstPos();
                      followposTable.put(currentPos, tmpFollowPos);

                      if (currentNode.getFirstPos().contains(finall)) {
                          flag = true;
                      }

                  }
              }
          }

      
          if (symbol.equals("$")) {
              List<Integer> lastpos = currentNode.getLeftChild().getLastPos();

              for (int l = 0; l < lastpos.size(); l++) {
                  List<Integer> currFollowPos = new LinkedList<>();
                  int currentPos = lastpos.get(l);

                  if (followposTable.get(currentPos) != null) {
                      currFollowPos = currentNode.getRightChild().getFirstPos();
                      currFollowPos.addAll(followposTable.get(currentPos));
                      followposTable.put(currentPos, currFollowPos);

                      if (currentNode.getRightChild().getFirstPos().contains(finall)) {
                          flag = true;
                      }

                  } else {
                      currFollowPos = currentNode.getRightChild().getFirstPos();
                      followposTable.put(currentPos, currFollowPos);

                      if (currentNode.getRightChild().getFirstPos().contains(finall)) {
                          flag = true;
                      }
                  }
              }
          }

          if (flag) {
               i = nodes.size();
               } 

      }


      for (int i = 0; i < followposTable.size(); i++) {
          Set<Integer> fill = new HashSet<>();
          if (followposTable.get(i) != null) {
              fill.addAll(followposTable.get(i));
              followposTable.put(i, new LinkedList<Integer>(fill));
          }
      }

  }

  private void transformToDfa() {
      
      for (int i = 0; i < followposTable.size(); i++) {
          if (!followposTable.isEmpty()) {
              if (followposTable.get(i) != null) {
                  if (!stateMap.containsValue(followposTable.get(i))){
                      stateMap.put(stateIDs, followposTable.get(i));
                      prevStateMap.put(followposTable.get(i), stateIDs);
                      stateIDs++;
                  }
              }
          }
      }

  
      for (int j = 1; j < posCount; j++) {
          boolean flag = false;
          for (int i = 0; i < stateMap.size(); i++) {
              if ((stateMap.get(i) != null) && stateMap.get(i).contains(j)) {
                  flag = true;
              }
          }

          if (!flag) {
              List<Integer> state = new LinkedList<>();
              state.add(j);
              stateMap.put(stateIDs, state);
              prevStateMap.put(state, stateIDs);
              stateIDs++;
              break;
          }

      }

  
      for (int i = 0; i < stateMap.size(); i++) {
          if (stateMap.get(i).contains(finall)) {
              finalStates.add(prevStateMap.get(stateMap.get(i)));
          }

          if (stateMap.get(i).contains(1)) {
              initialState.add(prevStateMap.get(stateMap.get(i)));
          }
      }

      for (int i = 0; i < stateMap.size(); i++) {
          List<Integer> currentState = stateMap.get(i);

          HashMap<String, Integer> columns = new HashMap<>();
          for (int j = 0; j < currentState.size(); j++) {
              int currentPos = currentState.get(j);
              String currPosSymbol = symbolStates.get(currentPos);


          
              for (int k = 0; k < symbols.size(); k++) {
                  if (currPosSymbol.equals(symbols.get(k))) { 
                      List<Integer> posFollowPos = followposTable.get(currentPos);
                      int posFollowPosStateID = prevStateMap.get(posFollowPos);
                      columns.put(symbols.get(k), posFollowPosStateID);
                  }
              }

          }



          transtitions.put(prevStateMap.get(currentState), columns);

      }

      for (int i = 0; i < transtitions.size(); i++) {
          HashMap<String, Integer> info = transtitions.get(i);

          for (int j = 0; j < symbols.size(); j++) {
              String currSymbol = symbols.get(j);
              if (info.get(currSymbol) != null) {
                  Transition tmp = new Transition(currSymbol, new State2(i), new State2(info.get(currSymbol)));
                  transitionsResult.add(tmp);
              }
          }
      }   
  }

  public List<String> getSymbols() { 
      return this.symbols; 
  }
  
  public List<Integer> getFinalStates() {
       return this.finalStates;
       }
  
  public List<Integer> getInitialState() {
       return this.initialState;
       }
 
  public List<Transition> getTransitionsResult() { 
      return this.transitionsResult; 
  }

  public String accepts(String input, Map<String, String> map) {
    String answer="";
    String newS = input;
    for(int i=0; i<input.length();i++){
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue().equals(Character.toString(input.charAt(i)))) {
                answer+=input.charAt(i)+" "+ entry.getKey()+"\n";
                newS = newS.replace(Character.toString(input.charAt(i)), " ");
            }
            else if(i+1<input.length()){
                if(entry.getValue().equals(Character.toString(input.charAt(i))+Character.toString(input.charAt(i+1)))){
                    answer+=Character.toString(input.charAt(i))+Character.toString(input.charAt(i+1))+" "+ entry.getKey()+"\n";
                    newS = newS.replace(Character.toString(input.charAt(i))+Character.toString(input.charAt(i+1)), " ");
                }
            }
             
        }
    }
    String[]l= newS.split(" ");
    String t="";
    for(int i =0;i< l.length;i++){
        for (HashMap<String, Integer> innerMap : transtitions.values()) {
            int matchingValues = 0;
            for (String key : innerMap.keySet()) {
             for(int j=0;j<l[i].length();j++){
                if (l[i].charAt(j)==(key.charAt(0))&&key.length()==1) {
                    
                    matchingValues++;
                    if (matchingValues >= l[i].length()) {
                        if(!Character.isDigit(innerMap.keySet().iterator().next().charAt(0)))
                       {
                        t+=l[i]+" "+ innerMap.keySet().iterator().next()+"\n";
                       }
                        
                        //return innerMap.getKey();
                    }
                    
                }
             }
                
            }
        }
    }
   
    // The input string didn't match any IDs, so we return null to indicate an error
        return answer+"\n"+t;
  }
    }
