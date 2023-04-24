package lab1.afn.preafn;


    import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class infpos {

   
	public static Map<Character, Integer> precedenceMap =new HashMap<Character, Integer>(); ;
	


	public static Integer getPrecedence(Character c, Character concat) {
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		map.put('(', 1);
		map.put('|', 2);
		map.put(concat, 3); // explicit concatenation operator
		map.put('?', 4);
		map.put('*', 4);
		map.put('+', 4);
		map.put('^', 5);
		precedenceMap = Collections.unmodifiableMap(map);
		Integer precedence = precedenceMap.get(c);
		return precedence == null ? 6 : precedence;
	}

	
	private static String formatRegEx(String regex, Character c) {
		String res = new String();
		List<Character> allOperators = Arrays.asList('|', '?', '+', '*', '^');
		List<Character> binaryOperators = Arrays.asList('^', '|');

		for (int i = 0; i < regex.length(); i++) {
			Character c1 = regex.charAt(i);

			if (i + 1 < regex.length()) {
				Character c2 = regex.charAt(i + 1);

				res += c1;

				if (!c1.equals('(') && !c2.equals(')') && !allOperators.contains(c2) && !binaryOperators.contains(c1)) {
					res += c;
				}
			}
		}
		res += regex.charAt(regex.length() - 1);

		return res;
	}
	public static boolean letterOrDigit(char c)
    {
        if (Character.isLetterOrDigit(c))
            return true;
        else
            return false;
    }
	public static String infixToPostfix2(String regex) {
		StringBuilder postfix = new StringBuilder();
		Stack<Character> operatorStack = new Stack<>();
		Map<Character, Integer> precedence = new HashMap<>();
		precedence.put('(', 1);
		precedence.put('|', 2);
		precedence.put('.', 3); // explicit concatenation operator
		precedence.put('?', 4);
		precedence.put('*', 4);
		precedence.put('+', 4);
		precedence.put('^', 5);
	
		for (int i = 0; i < regex.length(); i++) {
			char c = regex.charAt(i);
	
			if (c == '(') {
				operatorStack.push(c);
			} else if (c == ')') {
				while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
					postfix.append(operatorStack.pop());
				}
				if (!operatorStack.isEmpty() && operatorStack.peek() == '(') {
					operatorStack.pop();
				}
			} else if (isOperator(c)) {
				while (!operatorStack.isEmpty() && precedence.get(c) <= precedence.get(operatorStack.peek())) {
					postfix.append(operatorStack.pop());
				}
				operatorStack.push(c);
			} else {
				postfix.append(c);
			}
		}
	
		while (!operatorStack.isEmpty()) {
			postfix.append(operatorStack.pop());
		}
	
		return postfix.toString();
	}
	
	private static boolean isOperator(char c) {
		return c == '|' || c == '.' || c == '?' || c == '*' || c == '+' || c == '^';
	}
	public static String infixToPostfix(String expression, Character concat) {
		Stack<Character> stack = new Stack<>();
 
        String output = new String("");
        expression = formatRegEx(expression,concat);
        expression = extensionReg.transform_Regex(expression);
        

        for (int i = 0; i < expression.length(); ++i) {
            char c = expression.charAt(i);
 

            if (letterOrDigit(c))
                output += c;

            else if (c == '(')
                stack.push(c);
 
            else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(')
                    output += stack.pop();
 
                stack.pop();
            }
 
            else {
                while (!stack.isEmpty() && getPrecedence(c, concat) <= getPrecedence(stack.peek(), concat)) {
                    output += stack.pop();
                }
                stack.push(c);
            }
        }
 
        while (!stack.isEmpty()) {
            if (stack.peek() == '(')
                return "This expression is invalid";
            output += stack.pop();
        }
        return output;
    }
	public static String special(String regex, Character concat)
    {
        List<Character> operators = Arrays.asList('|','(',')','*','^',concat);
        Stack<Character> stack = new Stack<>();
 
        String result = new String("");
		String r="";
        for (int i = 0; i < regex.length(); ++i) {
            char c = regex.charAt(i);
 

            if (!operators.contains(c)){
                for(int j = i; j < regex.length(); j++){
                    if(!operators.contains(regex.charAt(j))){
						r= ""+regex.charAt(j);
                        result += r;
                    }
                    else{ 
                        result += " ";
                        i += j - i -1;
                        break;
                    }
                }
                
            }
                

            else if (c == '(')
                stack.push(c);
 
            else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(')
				
                    result += stack.pop();
					result+=" ";
 
                stack.pop();
            }
 
            else {
                while (!stack.isEmpty() && getPrecedence(c, concat) <= getPrecedence(stack.peek(), concat)) {
                  
                    result += stack.pop();
					result+=" ";
                }
                stack.push(c);
            }
        }
 
        while (!stack.isEmpty()) {
            if (stack.peek() == '(')
                return "This expression is invalid";
				result+=" ";
            result += stack.pop();
			
        }
        return result;
    }
	/*
	public static String infixToPostfix(String regex) {
		StringBuilder postfix = new StringBuilder();
		Stack<Character> operatorStack = new Stack<>();
		Map<Character, Integer> precedence = new HashMap<>();
		precedence.put('(', 1);
		precedence.put('|', 2);
		precedence.put('.', 3); // explicit concatenation operator
		precedence.put('?', 4);
		precedence.put('*', 4);
		precedence.put('+', 4);
		precedence.put('^', 5);
	
		for (int i = 0; i < regex.length(); i++) {
			char c = regex.charAt(i);
	
			if (c == '(') {
				operatorStack.push(c);
			} else if (c == ')') {
				while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
					postfix.append(operatorStack.pop());
				}
				if (!operatorStack.isEmpty() && operatorStack.peek() == '(') {
					operatorStack.pop();
				}
			} else if (isOperator(c)) {
				while (!operatorStack.isEmpty() && precedence.get(c) <= precedence.get(operatorStack.peek())) {
					postfix.append(operatorStack.pop());
				}
				operatorStack.push(c);
			} else {
				postfix.append(c);
			}
		}
	
		while (!operatorStack.isEmpty()) {
			postfix.append(operatorStack.pop());
		}
	
		return postfix.toString();
	}
	
	private static boolean isOperator(char c) {
		return c == '|' || c == '.' || c == '?' || c == '*' || c == '+' || c == '^';
	} */
	public static void main(String[] args) 
	{
		String a= "(a|b)*.(b|a)*.a.b.b";
		String res= special(a, '.');
		System.out.print(res);
	}
}

