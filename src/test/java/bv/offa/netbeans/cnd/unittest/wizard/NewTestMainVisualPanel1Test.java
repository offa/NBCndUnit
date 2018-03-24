/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bv.offa.netbeans.cnd.unittest.wizard;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class NewTestMainVisualPanel1Test
{
    private final NewTestMainVisualPanel1 panel = new NewTestMainVisualPanel1();

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }


    @org.junit.jupiter.api.Test
    public void defaultSettings()
    {
        assertTrue(panel.getEnableVerbose());
        assertTrue(panel.getEnableColor());
        assertTrue(panel.getEnableModernCpp());
        assertFalse(panel.getConfigureCustomProject());
    }

}
