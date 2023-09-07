package core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import core.Minesweeper;

public class MinesweeperTest {

    private Minesweeper m;
    

    @Test
    public void testConstructor(){
        m = new Minesweeper(10, 10, 10, null);

        //count bombs
        int bombs = 0;
        for(int i = 0; i <  10; i++){
            for(int j = 0; j < 10; j++){
                if(m.get(i, j).isBomb()){
                    bombs++;
                }
            }
        }
       assertEquals(10, bombs);
    }

    @Test
    public void testBombsLeft(){
        m = new Minesweeper(10, 10, 10, null);
        assertEquals(10, m.getBombsLeft());

        m.flag(0, 0);
        assertEquals(9, m.getBombsLeft());

        m.flag(0, 0);
        assertEquals(10, m.getBombsLeft());

        for(int i = 0; i < 10; i++){
            m.flag(i, 0);
        }
        assertEquals(0, m.getBombsLeft());

        m.flag(0, 1);
        assertEquals(-1, m.getBombsLeft());
    }
}
