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

package bv.offa.netbeans.cnd.unittest.wizard;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.netbeans.modules.cnd.makeproject.api.configurations.ConfigurationDescriptorProvider;
import org.netbeans.modules.cnd.makeproject.api.configurations.Folder;
import org.netbeans.modules.cnd.makeproject.api.configurations.MakeConfigurationDescriptor;
import org.openide.util.NbBundle;

/**
 * The class {@code WizardUtils} provides helper methods.
 * 
 * @author offa
 */
public final class WizardUtils
{
    private static final Logger LOGGER = Logger.getLogger(WizardUtils.class.getName());
    
    
    private WizardUtils()
    {
        /* Empty */
    }
    
    
    /**
     * Modifies all characters which are not valid in an identifier; leading
     * and trailing blanks are removed.
     * 
     * @param input     Input
     * @return          Modified input
     */
    public static String toIdentifier(String input)
    {
        final String ti = input.trim();
        
        if( ti.isEmpty() == true )
        {
            return ti;
        }
        
        final StringBuilder sb = new StringBuilder(ti);
        char c = sb.charAt(0);
        
        if( Character.isJavaIdentifierStart(c) == false || c == '$' )
        {
            sb.setCharAt(0, '_');
        }
        
        for( int i=1; i<sb.length(); i++ )
        {
            c = sb.charAt(i);
            
            if( Character.isJavaIdentifierPart(c) == false || c == '$' )
            {
                sb.setCharAt(i, '_');
            }
        }
        
        return sb.toString();
    }
    
    
    /**
     * Tests whether the given input is a valid identifier.
     * 
     * @param input     Input
     * @return          Returns {@code true} if {@code input} is a valid
     *                  identifier or {@code false} if not
     */
    public static boolean isValidIdentifier(String input)
    {
        if( input.isEmpty() == true )
        {
            return false;
        }
        
        final char fc = input.charAt(0);
        
        if( Character.isJavaIdentifierStart(fc) == false || fc == '$' )
        {
            return false;
        }
        
        for( int i=1; i<input.length(); i++ )
        {
            final char c = input.charAt(i);
            
            if( Character.isJavaIdentifierPart(c) == false || c == '$' )
            {
                return false;
            }
        }
        
        return true;
    }
    
    
    /**
     * Adds a test files folder to the project if there isn't one yet.
     * 
     * @param project       Project
     * @return              Test Folder
     */
    public static Folder createTestsRootFolder(Project project)
    {
        ConfigurationDescriptorProvider config = project.getLookup().lookup(ConfigurationDescriptorProvider.class);
        MakeConfigurationDescriptor projectDescriptor = config.getConfigurationDescriptor();
        Folder rootFolder = projectDescriptor.getLogicalFolders();
        Folder testFolder = rootFolder.findFolderByName(MakeConfigurationDescriptor.TEST_FILES_FOLDER);
        
        if( testFolder == null )
        {
            LOGGER.log(Level.INFO, "Enable Test Files Folder");
            return rootFolder.addNewFolder(MakeConfigurationDescriptor.TEST_FILES_FOLDER,
                    NbBundle.getMessage(MakeConfigurationDescriptor.class, "TestsFilesTxt"), false, Folder.Kind.TEST_LOGICAL_FOLDER);
        }
        else
        {
            LOGGER.log(Level.INFO, "Test Files Folder already enabled");
        }
        
        return testFolder;
    }
    
}
