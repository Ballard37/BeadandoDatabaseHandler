/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ballard
 */
public class DataReaderTest {
    
    public DataReaderTest() {
        DataReader.run();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of munkatarsConverter method, of class DataReader.
     */
    @Test
    public void testMunkatarsConverter() {
        System.out.println("munkatarsConverter");
        int id = 2;
        List<Munkatars> munkatarsak = DataReader.getMunkatarsak();
        Munkatars expResult =  DataReader.getMunkatarsak().get(0); //Lórám Ipse
        Munkatars result = DataReader.munkatarsConverter(id, munkatarsak);
        assertEquals(expResult, result);
        id = -1;
        result = DataReader.munkatarsConverter(id, munkatarsak);
        assertEquals(null, result);
    }
    
}
