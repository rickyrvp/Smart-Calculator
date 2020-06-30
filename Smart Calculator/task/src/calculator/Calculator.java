package calculator;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Calculator {
    private static final Pattern expPattern = Pattern.compile("^\\(*(([+-]?\\d+)|([+-]?\\d+|[a-zA-Z]+)((\\s*([+-]+|[\\*\\/])\\s*)[()]*([+-]?\\d+|[a-zA-Z]+)\\s*[()]*)+)$");
    private static final Pattern commPattern = Pattern.compile("\\/\\w+\\s*");
    private static final Pattern varPattern = Pattern.compile("^\\s*\\w+\\s*(=\\s*\\w+\\s*)*$");
    private Map<String, BigInteger> varMap = new HashMap<>();

    private Scanner scanner = new Scanner(System.in);

    // run the application
    void turnOn() {
        menu();
        turnOff();
    }

    void menu() {
        while (true) {
            String input = scanner.nextLine().trim();
            Matcher expMatcher = expPattern.matcher(input);
            Matcher commMatcher = commPattern.matcher(input);
            Matcher varMatcher = varPattern.matcher(input);

            if (input.isBlank()) {
                continue;
            }

            if (commMatcher.matches()) {
                if ("/exit".equals(input)) {
                    break;
                }
                else if ("/help".equals(input)) {
                    System.out.println("The program calculates the sum of numbers");
                    continue;
                } else {
                    System.out.println("Unknown command");
                    continue;
                }
            }

            if (varMatcher.matches()) {
                // ^\s*[a-zA-Z]+\s*(=\s*(\d+|[a-zA-z]+)\s*)*$
                boolean validVarInput = checkVariable(input);

                if (validVarInput) {
                    assignVar(input);
                }

                continue;

            }

            if (expMatcher.matches()) {
                calculate(input);
            } else {
                System.out.println("Invalid expression");
            }
        }

    }

    boolean checkVariable(String input) {
        // ^\s*[a-zA-Z]+\s*(=\s*(\d+|[a-zA-z]+)\s*)*$
        String invalidIdentifier = "^\\s*([a-zA-Z]+(\\d)\\w*|(\\d)+[a-zA-Z]+\\w*)\\s*(=\\s*(\\d+|[a-zA-z]+)\\s*)*$";
        String invalidAssignment = "^\\s*[a-zA-Z]+\\s*=\\s*([a-zA-Z]+(\\d)\\w*|(\\d)+[a-zA-Z]+\\w*)\\s*$";
        String invalidAssignmentX = "^\\s*[a-zA-Z]+\\s*(\\s*=\\s*\\w+){2,}\\s*$";
        if (input.matches(invalidIdentifier)) {
            System.out.println("Invalid identifier");
            return false;
        }

        if (input.matches(invalidAssignment)) {
            System.out.println("Invalid assignment");
            return false;
        }

        if (input.matches(invalidAssignmentX)) {
            System.out.println("Invalid assignment");
            return false;
        }

        return true; //identifier and assignment done correctly
    }

    void assignVar(String input) {
        // var = var
        if (input.matches("^\\s*[a-zA-Z]+\\s*=\\s*[a-zA-z]+\\s*$")) {
            String[] vars = input.split("=");
            // invalid assignment variable
            if (!varMap.containsKey(vars[1].trim())) {
                System.out.println("Unknown variable");
            } else {
                varMap.put(vars[0].trim(), varMap.get(vars[1].trim()));
            }
        } else if (input.matches("^\\s*[a-zA-Z]+\\s*$")) { //var
            if (varMap.containsKey(input.trim())) {
                System.out.println(varMap.get(input.trim()));
            } else {
                System.out.println("Unknown variable");
            }
        } else { //var = integer
            String[] vars = input.split("=");
            varMap.put(vars[0].trim(), new BigInteger(vars[1].trim()));
        }
    }

    void calculate(String input) {
        String[] expression = simplifyExpression(input);

        String postfix = convertInfixToPostfix(expression);

        if (postfix.equals("invalid")) {
            System.out.println("Invalid expression");
        } else {
            String[] postfixArray = postfix.split(" ");
            Deque<BigInteger> resultStack = new ArrayDeque<>();

            for (int i = 0; i < postfixArray.length; i++) {
                // incoming is digit
                if (postfixArray[i].matches("[+-]?\\d+")) {
                    resultStack.offerLast(BigInteger.valueOf(Long.parseLong(postfixArray[i])));
                }
                // incoming is variable
                else if (postfixArray[i].matches("[a-zA-Z]+")) {
                    resultStack.offerLast(varMap.get(postfixArray[i]));
                }
                // incoming is operator
                else {
                    BigInteger b = resultStack.pollLast();
                    BigInteger a = resultStack.pollLast();
                    BigInteger ans = BigInteger.ZERO;

                    switch (postfixArray[i]) {
                        case "+":
                            ans = a.add(b);
                            break;
                        case "-":
                            ans = a.subtract(b);
                            break;
                        case "*":
                            ans = a.multiply(b);
                            break;
                        case "/":
                            ans = a.divide(b);
                            break;
                    }

                    resultStack.offerLast(ans);
                }
            }

            System.out.println(resultStack.peekLast());
        }

    }

    String[] simplifyExpression(String input) {
        return input.
                replaceAll("\\s+", "").
                replaceAll("--", "+").
                replaceAll("\\+\\++", "+").
                replaceAll("\\+-", "-").
                replaceAll("-", " - ").
                replaceAll("\\+", " + ").
                replaceAll("\\*", " * ").
                replaceAll("\\/", " / ").
                split("((?<=\\()|(?=\\))|(\\s+))");
    }

    String convertInfixToPostfix(String[] expression) {
        String postfix  = "";
        Deque<String> stack = new ArrayDeque<>();

        for (int i = 0; i < expression.length; i++) {

            String str = expression[i];

            // if operand, then push to stack
            if (str.matches("([+-]?\\d+|[a-zA-Z]+)")) {
                postfix += str + " ";
            }
            // push '(' to stack
            else if (str.matches("\\(")) {
                stack.offerLast(str);
            }
            //if ')', pop and output from stack until '(' is reached
            else if (str.matches("\\)")) {
                while (!stack.isEmpty() && !stack.peekLast().matches("\\(")) {
                    postfix += stack.pollLast() + " ";
                }

                /*********/
                if (stack.isEmpty() || !stack.peekLast().matches("\\(")) {
                    return "invalid";
                } else {
                    stack.pollLast();
                }
            }
            // operator encountered
            else {
                while (!stack.isEmpty() && precedence(str) <= precedence(stack.peekLast())) {
                    if (stack.peekLast().matches("\\(")) {
                        return "invalid";
                    }
                    postfix += stack.pollLast() + " ";
                }
                stack.offerLast(str);
            }
        }

        while (!stack.isEmpty()) {
            if (stack.peekLast().matches("\\(")) {
                return "invalid";
            }

            postfix += stack.pollLast() + " ";
        }

        return postfix;
    }

    // Ranks the precedence of operators
    static int precedence(String op) {
        switch (op)
        {
            case "+":
            case "-":
                return 1;

            case "*":
            case "/":
                return 2;

            case "^":
                return 3;
        }
        return -1;
    }

    void turnOff() {
        System.out.println("Bye!");
        scanner.close();
    }
}
