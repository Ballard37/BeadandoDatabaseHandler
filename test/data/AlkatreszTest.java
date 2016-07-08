/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ballard
 */
public class AlkatreszTest {
    
    public AlkatreszTest() {
        DataReader.run();
    }
    
    /**
     * Test of checkCollision method, of class Alkatresz.
     */
    @Test
    public void testCheckCollision() {
        System.out.println("checkCollision");
        int csz = 1234;
        boolean expResult = true;
        boolean result = Alkatresz.checkCollision(csz);
        assertEquals(expResult, result);
        csz = 2000;
        result = Alkatresz.checkCollision(csz);
        assertEquals(false, result);

    }
    
}
