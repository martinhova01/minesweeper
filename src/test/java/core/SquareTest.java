package core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import core.Square;

public class SquareTest {

    private Square s;

    @BeforeEach
    public void setup(){
        s = new Square();
    }

    @Test
    public void testRevealNewSquare(){
        s.reveal();
        assertTrue(s.isRevealed());
    }

    @Test
    public void testRevealFlaggedSquare(){
        s.flag();
        s.reveal();
        assertFalse(s.isRevealed());
    }

    @Test
    public void testRevealRevealedSquare(){
        s.reveal();
        s.reveal();
        assertTrue(s.isRevealed());
    }

    @Test
    public void testFlaggNewSquare(){
        s.flag();
        assertTrue(s.isFlagged());
    }

    @Test
    public void testFlaggRevealedSquare(){
        s.reveal();
        s.flag();
        assertFalse(s.isFlagged());
    }

    @Test
    public void testFlagFlaggedSquare(){
        s.flag();
        s.flag();
        assertFalse(s.isFlagged());
    }

}
