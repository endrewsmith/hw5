package org.example.hm5;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Tokenizer implements Iterator<String> {
    private List<String> tokens;
    private int index;

    public Tokenizer(String str) {

        this.index = 0;
        String[] tmp = getTokens(str);
        if (checkTokens(tmp)) {
            tokens = Arrays.asList(tmp);
        } else {
            throw new RuntimeException("Неверно введена строка");
        }
    }

    @Override
    public boolean hasNext() {
        return index < tokens.size();
    }

    @Override
    public String next() {

        String token = tokens.get(index);
        index++;
        return token;
    }

    private boolean isDelimiter(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '(' || ch == ')';
    }

    public String[] getTokens(String str) {
        // Разбиваем строку на массив из цифр, операций, скобок
        if (str != null && !str.isEmpty()) {
            return str.split(" ");
        } else {
            throw new RuntimeException("Неверно введена строка");
        }
    }

    // Проверяем все токены
    public boolean checkTokens(String[] tokens) {
        int left = 0;
        int right = 0;
        for (String token : tokens) {
            char ch = token.charAt(0);
            // если токен число, то пробуем получить его, если не вышло, то ошибка в токене
            if (Character.isDigit(ch)) {
                try {
                    Double.parseDouble(token);
                } catch (Exception e) {
                    return false;
                }
                continue;
            } else if (isDelimiter(ch)) {
                // Подсчет скобок
                if (ch == '(') {
                    left++;
                } else if (ch == ')') {
                    right++;
                }
                continue;
            }
            // Если не цифра и не разделитель, то проверяем на функцию
            else {
                if (checkFn(token)) {
                    continue;
                } else if (checkPow(token)) {
                    continue;
                }
                return false;
            }
        }
        // Сразу проверим количество открывающихся
        // и закрывающихся скобок
        if (left != right) {
            return false;
        }

        return true;
    }

    // Проверка на функцию с одним аргументом
    public boolean checkFn(String token) {
        if (token.length() < 6) {
            return false;
        }
        String fn = token.substring(0, 4);
        // Проверим начало функции
        if (!fn.equals("sin(") && !fn.equals("cos(") && !fn.equals("sqr(")) {
            return false;
        }
        // Проверим, что заканчивается на скобку
        if (token.charAt(token.length() - 1) != ')') {
            return false;
        }
        String arg = token.substring(4, token.length() - 1);
        return isDouble(arg);
    }

    // Проверка на функцию с двумя аругментами (в нашем случаем только pow())
    public boolean checkPow(String token) {
        if (token.length() < 8) {
            return false;
        }
        String pow = token.substring(0, 4);
        if (!pow.equals("pow(")) {
            return false;
        }
        // Првоерим, что заканчивается на скобку
        if (token.charAt(token.length() - 1) != ')') {
            return false;
        }
        String argsStr = token.substring(4, token.length() - 1);
        String[] args = argsStr.split(",");
        if (args.length != 2) {
            return false;
        }
        if (!isDouble(args[0])) {
            return false;
        }
        return isDouble(args[1]);
    }

    // Проверяем, что у нас число, которое можно распарсить в double
    public boolean isDouble(String str) {
        if (str.contains(" ")) {
            throw new RuntimeException("Неверная запись, лишний пробел в записи токена");
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
