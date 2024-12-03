package org.example.hm5;

import java.util.*;

public class ExpressionCalculator {

    private final Map<String, Integer> PRIORITIES = new HashMap<>();

    {
        PRIORITIES.put("+", 1);
        PRIORITIES.put("-", 1);
        PRIORITIES.put("*", 2);
        PRIORITIES.put("/", 2);
        PRIORITIES.put("sqr", 2);
        PRIORITIES.put("pow", 2);
        PRIORITIES.put("sin", 3);
        PRIORITIES.put("cos", 3);
    }

    public double calc(String expr) {

        Queue<String> rpn = getRpn(openBrackets(expr));
        double result = eval(rpn);
        return result;
    }

    // Метод раскрывающий скобки
    public String openBrackets(String expr) {
        String result = expr;
        while (result.lastIndexOf(" ( ") >= 0) {
            int left = result.lastIndexOf("( ");
            String tmp = result.substring(left + 2);
            int right = tmp.indexOf(" )");
            String str = tmp.substring(0, right);

            if (!str.isEmpty()) {
                result = result.substring(0, left)
                        + calc(str)
                        + result.substring(right + 4 + left);
            }
        }

        return result;
    }

    private double eval(Queue<String> rpn) {

        Stack<Double> stack = new Stack<>();

        while (!rpn.isEmpty()) {
            String token = rpn.remove();
            if (isDigit(token)) {
                stack.push(Double.valueOf(token));
                continue;
            }
            double curResult;
            switch (token) {
                case "+": {
                    double rhs = stack.pop();
                    double lhs = stack.pop();
                    curResult = lhs + rhs;
                    break;
                }
                case "-": {
                    double rhs = stack.pop();
                    double lhs = stack.pop();
                    curResult = lhs - rhs;
                    break;
                }
                case "*": {
                    double rhs = stack.pop();
                    double lhs = stack.pop();
                    curResult = lhs * rhs;
                    break;
                }
                case "/": {
                    double rhs = stack.pop();
                    double lhs = stack.pop();
                    if (rhs == 0) {
                        throw new IllegalStateException("Деление на ноль");
                    }
                    curResult = lhs / rhs;
                    break;
                }
                case "sqr": {
                    double rhs = stack.pop();
                    curResult = rhs * rhs;
                    break;
                }
                case "sin": {
                    double rhs = stack.pop();
                    curResult = Math.sin(Math.toRadians(rhs));
                    break;
                }
                case "cos": {
                    double rhs = stack.pop();
                    curResult = Math.cos(Math.toRadians(rhs));
                    break;
                }
                case "pow": {
                    double rhs = stack.pop();
                    double lhs = stack.pop();
                    curResult = Math.pow(rhs, lhs);
                    break;
                }
                default:
                    throw new IllegalStateException("Неизвестная операция: " + token);
            }
            stack.push(curResult);
        }
        double result = stack.pop();
        if (!stack.isEmpty()) {
            throw new IllegalStateException("Стек не пустой");
        }
        return result;
    }

    public Queue<String> getRpn(String expr) {
        Tokenizer tokenizer = new Tokenizer(expr);
        Queue<String> result = new LinkedList<>();
        Stack<String> stack = new Stack<>();
        Queue<String> resultFn;
        while (tokenizer.hasNext()) {
            String token = tokenizer.next();
            if (isDigit(token)) {
                result.add(token);
                continue;
            } else if (token.length() > 1 && isFunctionOneArg(token)) {
                resultFn = rpnFunctionOneArg(token);
                token = resultFn.poll();
                result.add(resultFn.poll());
            } else if (token.length() > 1 && isFunctionTwoArg(token)) {
                resultFn = rpnFunctionTwoArg(token);
                token = resultFn.poll();
                result.add(resultFn.poll());
                result.add(resultFn.poll());
            }
            removeByPriorities(result, stack, token);
            stack.push(token);
        }
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }

        return result;
    }

    // Удаление согласно приоритету
    private void removeByPriorities(Queue<String> result, Stack<String> stack, String curToken) {

        int curTokenPriority = PRIORITIES.get(curToken);
        while (!stack.isEmpty()) {
            String tmp = stack.pop();
            int tmpPriority = PRIORITIES.get(tmp);
            if (curTokenPriority > tmpPriority) {
                stack.push(tmp);
                break;
            }
            result.add(tmp);
        }
    }

    // Проверка на число
    private boolean isDigit(String token) {
        if (token.charAt(0) == '-') {
            return Character.isDigit(token.charAt(1));
        }
        return Character.isDigit(token.charAt(0));
    }

    // Проверка на функцию с одним аргументом
    private boolean isFunctionOneArg(String token) {
        String fn = token.substring(0, 4);
        if (!fn.equals("sin(") && !fn.equals("cos(") && !fn.equals("sqr(")) {
            return false;
        }
        return true;
    }

    // Проверка на функцию с двумя аругментами
    private boolean isFunctionTwoArg(String token) {
        String fn = token.substring(0, 4);
        if (!fn.equals("pow(")) {
            return false;
        }
        return true;
    }

    // Получение польской нотации от функции с одним аругментом
    private Queue<String> rpnFunctionOneArg(String token) {
        Queue<String> rpnTmp = new LinkedList<>();
        // Кладем в очередь токен первым номером
        rpnTmp.add(token.substring(0, 3));
        // Вторым номером кладем аргумент
        rpnTmp.add(token.substring(4, token.length() - 1));
        return rpnTmp;
    }

    // Получение польской нотации от функции с двумя аругментами
    private Queue<String> rpnFunctionTwoArg(String token) {
        Queue<String> rpnTmp = new LinkedList<>();
        // Кладем в очередь токен первым номером
        rpnTmp.add(token.substring(0, 3));

        String argsStr = token.substring(4, token.length() - 1);
        String[] args = argsStr.split(",");

        // Вторым и третьим номерами кладем аргументы 1 и 0
        rpnTmp.add(args[1]);
        rpnTmp.add(args[0]);

        return rpnTmp;
    }
}
