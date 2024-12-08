package org.example;

import org.example.hm5.Tokenizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HomeWorkTest {

    // Проверим вспомогательные методы size
    @Test
    void isDoubleTest() {
        Tokenizer tokenizer = new Tokenizer("1 + 1");
        // Проверим выброс исключения из-за лишнего пробела
        assertThrows(RuntimeException.class, () -> {
            tokenizer.isDouble(" 10");
        });

        assertTrue(tokenizer.isDouble("10"));
        assertTrue(tokenizer.isDouble("-10"));
        assertTrue(tokenizer.isDouble("10.3"));
        assertFalse(tokenizer.isDouble("10a"));
        assertFalse(tokenizer.isDouble(""));
    }

    @Test
    void getTokensTest() {
        Tokenizer tokenizer = new Tokenizer("1 + 1 + ( 1 + 2 )");
        assertEquals("1", tokenizer.getTokens("1 + 1 + ( 1 + 2 )")[0]);
        assertEquals("+", tokenizer.getTokens("1 + 1 + ( 1 + 2 )")[1]);
        assertEquals("(", tokenizer.getTokens("1 + 1 + ( 1 + 2 )")[4]);
    }

    @Test
    void checkFnTest() {

        Tokenizer tokenizer = new Tokenizer("1 + 1");
        // Проверим выброс исключения из-за лишнего пробела
        assertThrows(RuntimeException.class, () -> {
            tokenizer.checkFn("sin( 1)");
        });
        assertFalse(tokenizer.checkFn("sin()"));
        assertTrue(tokenizer.checkFn("sin(1)"));
        assertTrue(tokenizer.checkFn("sin(-1)"));
        assertTrue(tokenizer.checkFn("cos(-1)"));
        assertTrue(tokenizer.checkFn("sqr(2)"));
        // Недописанная функция
        assertFalse(tokenizer.checkFn("sqr(2"));

    }

    @Test
    void checkPowTest() {

        Tokenizer tokenizer = new Tokenizer("1 + 1");
        // Проверим выброс исключения из-за лишнего пробела
        assertThrows(RuntimeException.class, () -> {
            tokenizer.checkPow("pow( 1,2)");
        });

        assertFalse(tokenizer.checkPow("pow(,2)"));
        assertFalse(tokenizer.checkPow("pow(1)"));
        assertFalse(tokenizer.checkPow("pow(1,)"));
        assertTrue(tokenizer.checkPow("pow(1,0)"));
        assertTrue(tokenizer.checkPow("pow(-1,2)"));
        assertTrue(tokenizer.checkPow("pow(1,2)"));
        // Недописанная функция
        assertFalse(tokenizer.checkPow("pow(1,2"));
        assertFalse(tokenizer.checkPow("pow1,2)"));

    }

    @Test
    void checkTokensTest() {
        Tokenizer tokenizer = new Tokenizer("1 + 1");

        assertTrue(tokenizer.checkTokens(tokenizer.getTokens("-1 + 1 + ( 1 + 3 ) * sin(1)")));
        assertTrue(tokenizer.checkTokens(tokenizer.getTokens("1 + 1")));
        // Нет пробела
        assertFalse(tokenizer.checkTokens(tokenizer.getTokens("1+ 1")));
        // Не закрыта скобка
        assertFalse(tokenizer.checkTokens(tokenizer.getTokens("1 + 1 + ( 1 + 3")));
        // Не открыта скобка
        assertFalse(tokenizer.checkTokens(tokenizer.getTokens("1 + 1 + 1 + 3 )")));
        assertTrue(tokenizer.checkTokens(tokenizer.getTokens("1 + 1 + ( 1 + 3 )")));
        assertFalse(tokenizer.checkTokens(tokenizer.getTokens("a + 1 + ( 1 + 3 )")));
        assertTrue(tokenizer.checkTokens(tokenizer.getTokens("-1 + 1 + ( 1 + 3 ) * 5 + pow(1,2) + cos(90)")));

    }

    @Test
    void openBracketsTest() {
        ExpressionCalculator calculator = new ExpressionCalculator();
        // Проверяем скобки
        assertEquals("1 + 5.0 + 4", calculator.openBrackets("1 + ( 2 + 3 ) + 4"));
        // Снятие вторых скобок
        assertEquals("1 + 5.0 + 4", calculator.openBrackets("1 + ( ( 2 + 3 ) ) + 4"));
        assertEquals("1 + 23.0 + 4", calculator.openBrackets("1 + ( 1 + ( 1 + ( 2 + 3 ) * 2 ) * 2 ) + 4"));
        // Последовательные скобки
        assertEquals("1 + 5.0 + 4 + 7.0", calculator.openBrackets("1 + ( 2 + 3 ) + 4 + ( 2 + 5 )"));
    }

    @Test
    void calcTest() {
        ExpressionCalculator calculator = new ExpressionCalculator();

        assertEquals(2, calculator.calc("1 + 1"));
        assertEquals(8, calculator.calc("2 * 4"));
        // Проверим выброс исключения при делении на ноль
        assertThrows(IllegalStateException.class, () -> {
            calculator.calc("2 / 0");
        });
        // Проверим выброс исключения
        assertThrows(RuntimeException.class, () -> {
            assertEquals(10.0, calculator.calc("2 / -2 + ( 1 + sqr(3) ) + sin(90"));
        });
        assertEquals(2, calculator.calc("2 / 1"));
        assertEquals(4, calculator.calc("sqr(2)"));
        assertEquals(0, calculator.calc("sin(0)"));
        assertEquals(1, calculator.calc("sin(90)"));
        assertEquals(1, calculator.calc("cos(0)"));
        assertEquals(-1, calculator.calc("cos(180)"));
        assertEquals(16, calculator.calc("pow(2,4)"));
        assertEquals(1, calculator.calc("pow(2,0)"));
        assertEquals(8, calculator.calc("pow(2,3)"));
        assertEquals(17, calculator.calc("1 + pow(2,3) * 2"));
        assertEquals(7.0, calculator.calc("1 + 2 * ( 1 + 2 )"));
        assertEquals(4.0, calculator.calc("1 + 2 * ( 1 + 2 ) / 2"));
        assertEquals(-1.0, calculator.calc("2 / -2"));
        assertEquals(10.0, calculator.calc("2 / -2 + ( 1 + sqr(3) ) + sin(90)"));
    }

}
