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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;

/**
 * The class {@code AbstractWizardIterator} is an abstract base class for
 * wizard iterators.
 * 
 * @author offa
 */
public abstract class AbstractWizardIterator implements WizardDescriptor.InstantiatingIterator<WizardDescriptor>
{
    protected WizardDescriptor wizard;
    protected List<WizardDescriptor.Panel<WizardDescriptor>> panels;
    protected final WizardDescriptor.Panel<WizardDescriptor> bottomPanel;
    protected int index;

    
    public AbstractWizardIterator(WizardDescriptor.Panel<WizardDescriptor> bottomPanel)
    {
        this.bottomPanel = bottomPanel;
    }
    
    

    /**
     * Initializes this iterator.
     * 
     * @param wizard    Wizard descriptor
     */
    @Override
    public void initialize(WizardDescriptor wizard)
    {
        this.wizard = wizard;
    }

    
    /**
     * Uninitializes this iterator.
     * 
     * @param wizard    Wizard descriptor
     */
    @Override
    public void uninitialize(WizardDescriptor wizard)
    {
        panels = null;
    }

    
    /**
     * Returns set of instantiated objects. If instantiation fails then wizard
     * remains open to enable correct values.
     * 
     * @return  A set of objects created (the exact type is at the discretion
     *          of the caller)
     * @throws  IOException In case of a failing I/O-operation.
     */
    @Override
    public Set<?> instantiate() throws IOException
    {
        FileObject template = Templates.getTemplate(wizard);
        DataObject templateDO = DataObject.find(template);
        FileObject targetFolder = Templates.getTargetFolder(wizard);
        DataFolder targetFolderDF = DataFolder.findFolder(targetFolder);
        String targetName = WizardUtils.toIdentifier(Templates.getTargetName(wizard));
        
        Map<String, Object> args = getTemplateParameters();
 
        DataObject createdTemplate = templateDO.createFromTemplate(targetFolderDF, targetName, args);
        FileObject createdFile = createdTemplate.getPrimaryFile();
        
        return Collections.singleton(createdFile);
    }
    
    
    /**
     * Returns the current panel.
     * 
     * @return  Panel
     */
    @Override
    public WizardDescriptor.Panel<WizardDescriptor> current()
    {
        return getPanels().get(index);
    }


    /**
     * Returns whether there's a next panel.
     * 
     * @return  Returns {@code true} if there's a next panel,
     *          {@code false} otherwise
     */
    @Override
    public boolean hasNext()
    {
        return index < getPanels().size() - 1;
    }


    /**
     * Returns whether there's a next panel.
     * 
     * @return  Returns {@code true} if there's a previous panel,
     *          {@code false} otherwise
     */
    @Override
    public boolean hasPrevious()
    {
        return index > 0;
    }


    /**
     * Returns the name of the current panel.
     * 
     * @return  Name
     */
    @Override
    public String name()
    {
        return index + 1 + ". from " + getPanels().size();
    }

    
    /**
     * Switches to the next panel by incrementing the index.
     */
    @Override
    public void nextPanel()
    {
        if( !hasNext() )
        {
            throw new NoSuchElementException();
        }
        index++;
    }

    
    /**
     * Switches to the previous panel by decrementing the index.
     */
    @Override
    public void previousPanel()
    {
        if( !hasPrevious() )
        {
            throw new NoSuchElementException();
        }
        index--;
    }

    
    /**
     * Adds the {@link ChangeListener ChangeListener}.
     * 
     * @param l     Listener
     */
    @Override
    public void addChangeListener(ChangeListener l)
    {
        /* Empty */
    }
    
    
    /**
     * Removes the {@link ChangeListener ChangeListener}.
     * 
     * @param l     Listener
     */
    @Override
    public void removeChangeListener(ChangeListener l)
    {
        /* Empty */
    }
    
    
    /**
     * Returns the panels.
     * 
     * <p>If something changes dynamically (besides moving between panels), e.g.
     * the number of panels changes in response to user input, then use
     * ChangeSupport to implement add/removeChangeListener and call fireChange
     * when needed.</p>
     * 
     * @return  Panels
     */
    protected List<WizardDescriptor.Panel<WizardDescriptor>> getPanels()
    {
        if( panels == null )
        {
            panels = new ArrayList<WizardDescriptor.Panel<WizardDescriptor>>();

            Project p = Templates.getProject(wizard);
            SourceGroup[] groups = ProjectUtils.getSources(p).getSourceGroups(Sources.TYPE_GENERIC);
            
            WizardDescriptor.Panel<WizardDescriptor> advNewFilePanel = 
                    Templates.buildSimpleTargetChooser(p, groups).bottomPanel(bottomPanel).create();
            panels.add(advNewFilePanel);
        }
        
        return panels;
    }
    
    
    /**
     * Creates steps.
     * 
     * <p>
     * You could safely ignore this method. Is is here to keep steps which were
     * there before this wizard was instantiated. It should be better handled
     * by NetBeans Wizard API itself rather than needed to be implemented by a
     * client code.
     * </p>
     * 
     * @return  Steps
     */
    protected String[] createSteps()
    {
        String[] beforeSteps = (String[]) wizard.getProperty("WizardPanel_contentData");
        assert beforeSteps != null : "This wizard may only be used embedded in the template wizard";
        String[] res = new String[(beforeSteps.length - 1) + panels.size()];
        for( int i = 0; i < res.length; i++ )
        {
            if( i < (beforeSteps.length - 1) )
            {
                res[i] = beforeSteps[i];
            }
            else
            {
                res[i] = panels.get(i - beforeSteps.length + 1).getComponent().getName();
            }
        }
        return res;
    }

    
    /**
     * Creates a map of template parameters.
     * 
     * @return  Template parameter (parameter name / value)
     */
    protected abstract Map<String, Object> getTemplateParameters();
    
}
