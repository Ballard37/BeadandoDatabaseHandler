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
public class Felh_AlkatreszTest {
    
    public Felh_AlkatreszTest() {
        DataReader.run();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of getAr method, of class Felh_Alkatresz.
     */
    @Test
    public void testGetAr() {
        System.out.println("getAr");
        Felh_Alkatresz instance = DataReader.getFelh_alkatreszek().get(0); //1234 - ára 8000
        int expResult = 8000;
        int result = instance.getAr();
        assertEquals(expResult, result);

    }
    
}
