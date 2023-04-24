package lab1.afn.preafn;
import java.io.*;
import java.util.*;
 
public class extensionReg {

   /*
    * 
si contiene un ?, esto se interpreta en un |ε que solo afecta al simbolo
si antes de ? hay ) esto se interpreta como que afecta al parentesis (expresion|ε)

sea a, un simbolo de una regex si hay un + este se interpreta como aa*
sea (ab), una expresion entre parentesis, si hay un + luego de ) este afecta a todo el parentesis siendo (abab*)

ref: https://ccc.inaoep.mx/~emorales/Cursos/Automatas/ExpRegulares.pdf

    */

    public static String transform_Sum(String expresion){
        
        List<Character> operadores = Arrays.asList('|','(',')','*','^');
        if(expresion.contains("+")){
            for(int i = 0; i < expresion.length(); i++){
                if(expresion.charAt(i) == '+' && expresion.charAt(i-1) != ')'){
                    for (int reverse = i; reverse >= 0; reverse--){
                        if(operadores.contains(expresion.charAt(reverse)) || reverse == 0){
                            expresion = expresion.substring(0, reverse) + "(" + expresion.substring(reverse, i) + ")" + expresion.substring(i, expresion.length());                     
                            break;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < expresion.length(); i++) {

            Character char1 = expresion.charAt(i);

            if (char1.equals('+')) {

                if (Character.toString(expresion.charAt(i-1)).equals(")")) {

                    int parentesis = 0;

                    for (int x = (i-1); x >= 0; x--) {
                        if ((x != (i-1)) && (Character.toString(expresion.charAt(x)).equals(")"))) {

                            parentesis++;

                        }

                        if ((Character.toString(expresion.charAt(x)).equals("("))) {
                            if (parentesis != 0) {
                                parentesis--;
                            } 
                            else {

                                String repetir = (String) expresion.subSequence(x, i);
                                expresion = expresion.substring(0, x) + repetir + repetir + "*" + expresion.substring(i+1);

                            }
                        }
                    }
                } 
                else {

                    String repetir = Character.toString(expresion.charAt(i-1));
                    expresion = expresion.substring(0, i-1) + repetir + repetir + "*" +expresion.substring(i+1);

                }
            }
        }
        return expresion;
    }
    public static String transform_Question(String expresion){
        
        List<Character> operadores = Arrays.asList('|','(',')','*','^');
        if(expresion.contains("?")){
            for(int i = 0; i < expresion.length(); i++){
                if(expresion.charAt(i) == '?' && expresion.charAt(i-1) != ')'){
                    for (int reverse = i; reverse >= 0; reverse--){
                        if(operadores.contains(expresion.charAt(reverse)) || reverse == 0){
                            expresion = expresion.substring(0, reverse) + "(" + expresion.substring(reverse, i) + ")" + expresion.substring(i, expresion.length());                     
                            break;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < expresion.length(); i++) {

            Character char1 = expresion.charAt(i);
            
            if (char1.equals('?')) {

                if (Character.toString(expresion.charAt(i-1)).equals(")")) {

                    for (int x = (i-1); x >= 0; x--) {

                        if ((Character.toString(expresion.charAt(x)).equals("("))) {
                            if (x != 0) {
                                
                                String cambioExpresion = (String) expresion.subSequence(x, i);
                                expresion = expresion.substring(0, x) + "(" + cambioExpresion + "|ε)" + expresion.substring(i+1);
                                break;

                            }
                        }
                    }
                    
                } 
                else {                    
                    expresion = expresion.substring(0, i-1) + "(" + Character.toString(expresion.charAt(i-1)) + "|ε)" + expresion.substring(i+1);
                }
            }
        }
        return expresion;
    }
    public static String transform_Regex(String regex){
        String sum= transform_Sum(regex);
        String r= transform_Question(sum);
        return r;
    }
    public static void main(String[] args)
    {
       String regex="a+(a+b)+c";
       String t= transform_Regex(regex);
       System.out.println(t);
    }
}