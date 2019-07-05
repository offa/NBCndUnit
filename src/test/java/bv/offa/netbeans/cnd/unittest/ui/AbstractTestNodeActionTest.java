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

package bv.offa.netbeans.cnd.unittest.ui;

import static com.google.common.truth.Truth.assertThat;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.netbeans.api.project.Project;

@Tag("ui")
public class AbstractTestNodeActionTest
{

    @Test
    public void getValueReturnsActionName()
    {
        AbstractTestNodeAction nodeAction = new AbstractTestNodeActionMock("actionName", null);
        assertThat(nodeAction.getValue(Action.NAME)).isEqualTo("actionName");
    }


    private static class AbstractTestNodeActionMock extends AbstractTestNodeAction
    {
        private static final long serialVersionUID = 1L;

        public AbstractTestNodeActionMock(String actionName, Project project)
        {
            super(actionName, project);
        }

        @Override
        protected void doActionPerformed(ActionEvent ae)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
}
