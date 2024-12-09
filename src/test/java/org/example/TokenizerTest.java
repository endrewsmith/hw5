package org.example;

import org.example.Tokenizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokenizerTest {

    @Test
    public void test(){
        Tokenizer tokenizer = new Tokenizer("1 + 12 + 2 / 10.1+0");
        assertEquals("1", tokenizer.next());
        assertEquals("+", tokenizer.next());
        assertEquals("12", tokenizer.next());
        assertEquals("+", tokenizer.next());
        assertEquals("2", tokenizer.next());
        assertEquals("/", tokenizer.next());
        assertEquals("10.1", tokenizer.next());
        assertEquals("+", tokenizer.next());
        assertEquals("0", tokenizer.next());
        assertFalse(tokenizer.hasNext());
    }
}
