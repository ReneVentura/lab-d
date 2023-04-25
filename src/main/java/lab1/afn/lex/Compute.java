package lab1.afn.lex;
import java.util.*;

import lab1.afn.preafn.extensionReg;
import lab1.afn.preafn.infpos;

import java.io.*;
public class Compute {

    public static String idk;
    static HashMap<String, String> finalIDs;
    static String postFix;






    public static ArrayList<HashMap<String,String>> computeVars(String path) throws InterruptedException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path)); 
            HashMap<String,String> operators = new HashMap<String, String>();
            HashMap<String, String> ids = new HashMap<String, String>();
            HashMap<String, String> aux = new HashMap<String, String>();
            String lower = "a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z";
            String upper = "A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z";
           
            String digits = "0|1|2|3|4|5|6|7|8|9";
            String current;
            String index;
            int indexValue = 100;
            boolean hasIndex = false;
            ArrayList<HashMap<String,String>> lets = new ArrayList<HashMap<String,String>>();
                while ((current = reader.readLine()) != null) {
                    hasIndex = false;
                    if(current.contains("let")){
                        
                        for (int i = 0; i < current.length(); i++){
                            index  = "";
                            if(current.charAt(i) == ' ' && !hasIndex){
                                i += 1;
                                for (int getIndex = i; current.charAt(getIndex) != ' '; getIndex++ ){
                                    index += current.charAt(getIndex);
                                    i += 1;
                                }
                                hasIndex = true;

                                String[] substr = current.split("=");
                                String rightSide = substr[1].replace("' '", "'empty'");
                                rightSide = rightSide.replace(" ", "");
                              ;
                                if(rightSide.charAt(0) == '[' && rightSide.charAt(1) == '\'' && rightSide.charAt(rightSide.length()-1) == ']' && rightSide.charAt(rightSide.length()-2) == '\''){
                                    int begin = rightSide.indexOf('[') + 1;
                                    int end = rightSide.indexOf(']');
                                    String sub = rightSide.substring(begin, end);
                                    sub = sub.replace("''", "|");
                                    sub = sub.replace("'", "");
                                    if(sub.equals("A-Z|a-z")){
                                        operators.put(index, upper+"|"+lower);
                                        ids.put(Integer.toString(indexValue), index);
                                        aux.put(index, Integer.toString(indexValue));
                                        indexValue++;
                                    }
                                    else if(sub.equals("0-9")){
                                        operators.put(index, digits);
                                        ids.put(Integer.toString(indexValue), index);
                                        aux.put(index, Integer.toString(indexValue));
                                        indexValue++;
                                    }
                                    else if(sub.equals("A-Z")){
                                        operators.put(index, upper);
                                        ids.put(Integer.toString(indexValue), index);
                                        aux.put(index, Integer.toString(indexValue));
                                        indexValue++;
                                    }
                                    else if(sub.equals("a-z")){
                                        operators.put(index, lower);
                                        ids.put(Integer.toString(indexValue), index);
                                        aux.put(index, Integer.toString(indexValue));
                                        indexValue++;
                                    }
                                    else{
                                        operators.put(index, sub);
                                        ids.put(Integer.toString(indexValue), index);
                                        aux.put(index, Integer.toString(indexValue));
                                        indexValue++;
                                    }
                                }
                                else if(rightSide.charAt(0) == '[' && rightSide.charAt(1) == '\"' && rightSide.charAt(rightSide.length()-1) == ']' && rightSide.charAt(rightSide.length()-2) == '\"'){
                                    String raw = rightSide.substring(2,rightSide.length()-2);
                                    String newVal = "";
                                    for (int separate = 0; separate < raw.length(); separate++){
                                        if(separate == raw.length()-1){
                                            newVal += raw.charAt(separate);
                                        }
                                        else{
                                            newVal += raw.charAt(separate) + "|";
                                        }
                                    }
                                    operators.put(index, newVal);
                                    ids.put(Integer.toString(indexValue), index);
                                    aux.put(index, Integer.toString(indexValue));
                                    indexValue++;
                                }
                                else{
                                    for (int x = 0; x < rightSide.length(); x++){
                                        if(Character.isLetter(rightSide.charAt(x))){
                                            String symbol = "";
                                            for (int j = x; j < rightSide.length(); j++){
                                                if(Character.isLetter(rightSide.charAt(j))){
                                                    symbol += rightSide.charAt(j);
                                                }
                                                else{
                                                    if(!ids.containsValue(symbol)){
                                                            ids.put(Integer.toString(indexValue), symbol);
                                                            aux.put(symbol, Integer.toString(indexValue));
                                                            rightSide = rightSide.substring(0, x) + Integer.toString(indexValue) + rightSide.substring(j, rightSide.length());
                                                            indexValue++;
                                                            x = j - symbol.length();
                                                            break;     
                                                    }
                                                    else if(rightSide.length() == 2 && (rightSide.charAt(1) == '+' || rightSide.charAt(1) == '?')){
                                                        rightSide = "(" + rightSide.charAt(0) + ")" + rightSide.charAt(1);
                                                    }
                                                    else{
                                                        rightSide = rightSide.substring(0, x) + aux.get(symbol) + rightSide.substring(j, rightSide.length());
                                                        x = j - symbol.length();
                                                        symbol = "";
                                                        break;
                                                    }
                                                }
                                            }
                                            
                                        }
                                    }
                                    if(rightSide.contains("'+'")){
                                        rightSide = rightSide.replace("'+'", "'" + Integer.toString(indexValue) + "'");
                                        ids.put(Integer.toString(indexValue), "+");
                                        indexValue++;
                                    }
                                    if(rightSide.contains("'?'")){
                                        rightSide = rightSide.replace("'?'", "'" + Integer.toString(indexValue) + "'");
                                        ids.put(Integer.toString(indexValue), "?");
                                        indexValue++;
                                    }

                                    
                                    for(int k = 0; k < rightSide.length(); k++){
                                        if(rightSide.charAt(k) == '[' && (rightSide.charAt(k + 1) == '\'')){
                                            String addIns = "(";
                                            int fin = 0;
                                            for(int h = k+1; h < rightSide.length(); h++){
                                                if(rightSide.charAt(h) != ']'){
                                                    addIns += rightSide.charAt(h);
                                                }
                                                else if(rightSide.charAt(h) == ']'){
                                                    addIns += ")";
                                                    fin = h;
                                                    break;
                                                }
                                            }
                                            addIns = addIns.replace("''", "|");
                                            addIns = addIns.replace("\'", "");
                                            rightSide = rightSide.substring(0, k) + addIns + rightSide.substring(fin+1, rightSide.length());
                                            k = 0;
                                        }
                                        else if(rightSide.charAt(k) == '\'' && Character.isLetter(rightSide.charAt(k+1)) && rightSide.charAt(k+2) == '\''){
                                            k = k+2;
                                        }
                                        else if(rightSide.charAt(k) == '\'' && Character.isLetter(rightSide.charAt(k + 3))){
                                            rightSide = rightSide.substring(0, k) + rightSide.charAt(k+1) + "|" + rightSide.substring(k+3, rightSide.length());
                                            k = 0;
                                        }
                                        
                                    }
                                    
                                    rightSide = rightSide.replace("\'", "");
                                    if(rightSide.length() == 2 && (rightSide.charAt(1) == '?' || rightSide.charAt(1) == '+')){
                                        rightSide = "(" + rightSide.charAt(0) + ")" + rightSide.charAt(1);
                                    }
                                   
                                    rightSide = extensionReg.transform_Sum(rightSide);
                                    rightSide = extensionReg.transform_Question(rightSide);
                                    operators.put(index, rightSide);
                                    
                                }
                            }                        
                        }
                    }
                }
                reader.close();
               
                lets.add(operators);
                lets.add(ids);
                return lets;
    }

    public static void computeTokens(String path,ArrayList<HashMap<String,String>> vars) throws InterruptedException, IOException{
        BufferedReader reader = new BufferedReader(new FileReader(path));
            String current;
            HashMap<String, String> operators = vars.get(0);
            HashMap<String, String> ids = vars    .get(1);
            finalIDs = new HashMap<String, String>();
            int finalIndex = 10000000;
            String allRaw = "";
            while ((current = reader.readLine()) != null) {
                if(current.contains("rule tokens")){
                    while ((current = reader.readLine()) != null) {
                        
                        if(!current.isEmpty()){
                            if(current.contains("return")){
                                String symbol = "";
                                for (int nextToken = 0; nextToken < current.length(); nextToken++){
                                    if(current.charAt(nextToken) != ' ' && current.charAt(nextToken) != '{' && current.charAt(nextToken) != '|' && current.charAt(nextToken) != '\'' && current.charAt(nextToken) != '\"'){
                                        symbol += current.charAt(nextToken);
                                    }
                                    else if(current.charAt(nextToken) == '{'){
                                        break;
                                    }
                                }
                                if(operators.containsKey(symbol)){
                                    int start = current.indexOf("{");
                                    int end = current.indexOf("}");
                                    String dummy = current.substring(start+1, end);
                                    dummy = dummy.replace(" ", "");
                                    dummy = dummy.replace("return", "");
                                    allRaw += "((" + operators.get(symbol) + ")" + dummy + ")|";
                                }
                                else{
                                    finalIDs.put(Integer.toString(finalIndex), symbol);
                                    int start = current.indexOf("{");
                                    int end = current.indexOf("}");
                                    String dummy = current.substring(start+1, end);
                                    dummy = dummy.replace(" ", "");
                                    dummy = dummy.replace("return", "");
                                    allRaw += "((" + finalIndex + ")" + dummy + ")|";
                                    finalIndex++;
                                }
                            }
                            else{
                                String allTokens = "";
                            for (int nextToken = 0; nextToken < current.length(); nextToken++){
                                if(current.charAt(nextToken) != ' ' && current.charAt(nextToken) != '{' && current.charAt(nextToken) != '|' && current.charAt(nextToken) != '\'' && current.charAt(nextToken) != '\"'){
                                    allTokens += current.charAt(nextToken);
                                }
                                else if(current.charAt(nextToken) == '{'){
                                    break;
                                }
                            }
                            if(!allTokens.contains("(*")){
                                allRaw += "(" + "(null)" + allTokens + ")|";
                                operators.remove(allTokens);
                                }
                            }
                        }
                    }
                }   
            }
            allRaw = allRaw.substring(0, allRaw.length()-1);
           
            ArrayList<HashMap<String, String>> allMaps = new ArrayList<HashMap<String, String>>();
            allMaps.add(ids);
            allMaps.add(operators);
            allMaps.add(ids);
            allMaps.add(operators);
            List<Character> allSymbols = Arrays.asList('*','|','^', '$','(',')','.');
            for(int indexM = 0; indexM < allMaps.size(); indexM++){
                for (int i = 0; i < allRaw.length(); i++){
                    if(!allSymbols.contains(allRaw.charAt(i)) && allRaw.charAt(i) != ' '){
                        String expression = "";
                        for (int eIndex = i; eIndex < allRaw.length(); eIndex++){
                            if(!allSymbols.contains(allRaw.charAt(eIndex)) && allRaw.charAt(eIndex) != ' '){
                                expression += allRaw.charAt(eIndex);
                            }
                            else if((!expression.equals("")) && (allRaw.charAt(eIndex) == ' ' || allSymbols.contains(allRaw.charAt(eIndex)))){
                                String start = "";
                                if(i == 0){
                                    start = allRaw.substring(0, 0);
                                }
                                else{
                                    start = allRaw.substring(0, i);
                                }
                                String end = allRaw.substring(eIndex, allRaw.length());
                                if(allMaps.get(indexM).containsKey(expression)){
                                    String replace = allMaps.get(indexM).get(expression);
                                    allRaw = start + replace + end;
                                    i = start.length() + replace.length()-1;
                                    break;
                                }
                                else{
                                    allRaw = start + expression + end;
                                    i = start.length() + expression.length()-1;
                                    break;
                                }
                                
                            }
                        }
                    }
                   
                }
            }

            
          
            String fromatted = TreeLex.formatTree(allRaw, '$');
            postFix = infpos.special(fromatted, '$');
            TreeLex tree = new TreeLex(postFix, '$', finalIDs);
            
            
            reader.close();
            
    }
    public static void computeTheVoid(String file,String file2) throws InterruptedException, IOException{
        String dir = "C:\\Users\\ravz2\\Escritorio\\afn\\src\\main\\java\\lab1\\afn\\lex\\"+file+".yal";
        String dir2= "C:\\Users\\ravz2\\Escritorio\\afn\\src\\main\\java\\lab1\\afn\\lex\\"+file2+".yal.run";
        computeTokens(dir,computeVars(dir));
        TreeLex tree = new TreeLex(postFix, '$', finalIDs);
        idk=tree.readRun(dir2, postFix);
    }
    //readRun("C:\\Users\\ravz2\\Escritorio\\afn\\src\\main\\java\\lab1\\afn\\lex\\slr-4.2.yal.run", postfix);
}
