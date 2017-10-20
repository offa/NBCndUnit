/*
 * NBCndUnit - C/C++ unit tests for NetBeans.
 * Copyright (C) 2015-2017  offa
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
package bv.offa.netbeans.cnd.unittest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("util")
public class InstallerTest
{

    @Test
    public void installerHasAllFriends()
    {
        final String requiredFriends[] = new String[]
        {
            "org.netbeans.modules.cnd.testrunner",
            "org.netbeans.modules.gsf.testrunner",
            "org.netbeans.modules.gsf.testrunner.ui",
            "org.netbeans.modules.cnd.modelutil",
            "org.netbeans.modules.cnd.makeproject",
            "org.netbeans.modules.cnd.api.model",
            "org.netbeans.modules.cnd",
            "org.netbeans.modules.cnd.utils"
        };

        Installer installer = new Installer();
        assertThat(installer.getTargetModules(), hasItems(requiredFriends));
    }

}
