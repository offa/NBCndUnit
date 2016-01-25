/*
 * NBCndUnit - C/C++ unit tests for NetBeans.
 * Copyright (C) 2015-2016  offa
 * 
 * This file is part of NBCndUnit.
 *
 * NBCndUnit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NBCndUnit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NBCndUnit.  If not, see <http://www.gnu.org/licenses/>.
 */

package bv.offa.netbeans.cnd.unittest.wizard;

import org.junit.Test;
import static org.junit.Assert.*;

public class WizardUtilsTest
{
    @Test
    public void testToIdentifierTrims()
    {
        assertEquals("", WizardUtils.toIdentifier(""));
        assertEquals("", WizardUtils.toIdentifier(" "));
        assertEquals("abc", WizardUtils.toIdentifier(" abc"));
        assertEquals("abc", WizardUtils.toIdentifier(" abc  "));
    }
    
    @Test
    public void testToIdentifierRelacesInvalidChars()
    {
        assertEquals("aB_c3", WizardUtils.toIdentifier("aB c3"));
        assertEquals("aB__c3_", WizardUtils.toIdentifier("aB &c3!"));
        assertEquals("aB_c3_", WizardUtils.toIdentifier("aB$c3_"));
    }
    
    @Test
    public void testToIdentifierReplacesInvalidFirstChar()
    {
        assertEquals("_abc", WizardUtils.toIdentifier("3abc"));
        assertEquals("_abc", WizardUtils.toIdentifier(" 3abc"));
        assertEquals("_abc", WizardUtils.toIdentifier("$abc"));
    }
    
    @Test
    public void testIsValidIdentifierRejectsInnerBlanks()
    {
        assertFalse(WizardUtils.isValidIdentifier("a b"));
    }
    
    @Test
    public void testIsValidIdentifierRejectsLeadingTrailingBlanks()
    {
        assertFalse(WizardUtils.isValidIdentifier(" b"));
        assertFalse(WizardUtils.isValidIdentifier("b "));
    }
    
    @Test
    public void testIsValidRejectsEmpty()
    {
        assertFalse(WizardUtils.isValidIdentifier(""));
    }
    
    @Test
    public void testIsValidRejectsInvalidChars()
    {
        assertFalse(WizardUtils.isValidIdentifier("3a"));
        assertFalse(WizardUtils.isValidIdentifier("X$a"));
        assertFalse(WizardUtils.isValidIdentifier("n-a"));
        assertFalse(WizardUtils.isValidIdentifier("h\"a"));
        assertFalse(WizardUtils.isValidIdentifier("{a}"));
        assertFalse(WizardUtils.isValidIdentifier("a[]"));
        assertFalse(WizardUtils.isValidIdentifier("a)("));
        assertFalse(WizardUtils.isValidIdentifier("a\t"));
        assertFalse(WizardUtils.isValidIdentifier("a\n"));
        assertFalse(WizardUtils.isValidIdentifier("a#"));
        assertFalse(WizardUtils.isValidIdentifier("$a"));
    }
    
    @Test
    public void testIsValidAcceptsValidNames()
    {
        assertTrue(WizardUtils.isValidIdentifier("aB"));
        assertTrue(WizardUtils.isValidIdentifier("_aB"));
        assertTrue(WizardUtils.isValidIdentifier("a_B_"));
        assertTrue(WizardUtils.isValidIdentifier("a3B"));
        assertTrue(WizardUtils.isValidIdentifier("_3J"));
    }
    
    @Test
    public void testIsValidAcceptsOneAndTwoChars()
    {
        assertTrue(WizardUtils.isValidIdentifier("a"));
        assertTrue(WizardUtils.isValidIdentifier("ab"));
    }
    
}
