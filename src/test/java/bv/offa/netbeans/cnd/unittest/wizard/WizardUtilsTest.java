/*
 * NBCndUnit - C/C++ unit tests for NetBeans.
 * Copyright (C) 2015-2019  offa
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

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("ui")
public class WizardUtilsTest
{
    @Test
    public void toIdentifierTrims()
    {
        assertThat(WizardUtils.toIdentifier("")).isEqualTo("");
        assertThat(WizardUtils.toIdentifier(" ")).isEqualTo("");
        assertThat(WizardUtils.toIdentifier(" abc")).isEqualTo("abc");
        assertThat(WizardUtils.toIdentifier(" abc  ")).isEqualTo("abc");
    }

    @Test
    public void toIdentifierReplacesInvalidChars()
    {
        assertThat(WizardUtils.toIdentifier("aB c3")).isEqualTo("aB_c3");
        assertThat(WizardUtils.toIdentifier("aB &c3!")).isEqualTo("aB__c3_");
        assertThat(WizardUtils.toIdentifier("aB$c3_")).isEqualTo("aB_c3_");
    }

    @Test
    public void toIdentifierReplacesInvalidFirstChar()
    {
        assertThat(WizardUtils.toIdentifier("3abc")).isEqualTo("_abc");
        assertThat(WizardUtils.toIdentifier(" 3abc")).isEqualTo("_abc");
        assertThat(WizardUtils.toIdentifier("$abc")).isEqualTo("_abc");
    }

    @Test
    public void isValidIdentifierRejectsInnerBlanks()
    {
        assertThat(WizardUtils.isValidIdentifier("a b")).isFalse();
    }

    @Test
    public void isValidIdentifierRejectsLeadingTrailingBlanks()
    {
        assertThat(WizardUtils.isValidIdentifier(" b")).isFalse();
        assertThat(WizardUtils.isValidIdentifier("b ")).isFalse();
    }

    @Test
    public void isValidRejectsEmpty()
    {
        assertThat(WizardUtils.isValidIdentifier("")).isFalse();
    }

    @Test
    public void isValidRejectsInvalidChars()
    {
        assertThat(WizardUtils.isValidIdentifier("3a")).isFalse();
        assertThat(WizardUtils.isValidIdentifier("X$a")).isFalse();
        assertThat(WizardUtils.isValidIdentifier("n-a")).isFalse();
        assertThat(WizardUtils.isValidIdentifier("h\"a")).isFalse();
        assertThat(WizardUtils.isValidIdentifier("{a}")).isFalse();
        assertThat(WizardUtils.isValidIdentifier("a[]")).isFalse();
        assertThat(WizardUtils.isValidIdentifier("a)(")).isFalse();
        assertThat(WizardUtils.isValidIdentifier("a\t")).isFalse();
        assertThat(WizardUtils.isValidIdentifier("a\n")).isFalse();
        assertThat(WizardUtils.isValidIdentifier("a#")).isFalse();
        assertThat(WizardUtils.isValidIdentifier("$a")).isFalse();
    }

    @Test
    public void isValidAcceptsValidNames()
    {
        assertThat(WizardUtils.isValidIdentifier("aB")).isTrue();
        assertThat(WizardUtils.isValidIdentifier("_aB")).isTrue();
        assertThat(WizardUtils.isValidIdentifier("a_B_")).isTrue();
        assertThat(WizardUtils.isValidIdentifier("a3B")).isTrue();
        assertThat(WizardUtils.isValidIdentifier("_3J")).isTrue();
    }

    @Test
    public void isValidAcceptsOneAndTwoChars()
    {
        assertThat(WizardUtils.isValidIdentifier("a")).isTrue();
        assertThat(WizardUtils.isValidIdentifier("ab")).isTrue();
    }

}
