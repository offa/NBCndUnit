package bv.offa.netbeans.cnd.unittest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.openide.modules.ModuleInfo;
import org.openide.modules.ModuleInstall;
import org.openide.modules.Modules;

public class Installer extends ModuleInstall
{
    private static final long serialVersionUID = 1L;
    private final Set<String> targetModules;
    
    
    public Installer()
    {
        this.targetModules = new HashSet<String>();
        this.targetModules.add("org.netbeans.modules.gsf.testrunner");
        this.targetModules.add("org.netbeans.modules.cnd.testrunner");
    }

    
    
    @Override
    public void validate() throws IllegalStateException
    {
        if( targetModules.isEmpty() == false )
        {
            addFriends();
        }
    }
    
    
    private void addFriends()
    {
        try
        {
            final ModuleInfo moduleInfoOfThis = Modules.getDefault().ownerOf(this.getClass());
            assert( moduleInfoOfThis != null);
            final String codeNameBase = moduleInfoOfThis.getCodeNameBase();
            
            final Method getManagerMethod = moduleInfoOfThis.getClass().getMethod("getManager");
            final Object manager = getManagerMethod.invoke(moduleInfoOfThis);
            
            final Method getMethod = manager.getClass().getMethod("get", String.class);
            
            for( String target : targetModules )
            {
                Object dependency = getMethod.invoke(manager, target);
                assert(dependency != null);
                
                final ModuleInfo moduleInfo = (ModuleInfo) dependency;
                
                final Method dataMethod = Class.forName("org.netbeans.Module", true, moduleInfo.getClass().getClassLoader()).getDeclaredMethod("data");
                dataMethod.setAccessible(true);
                
                final Object dataValue = dataMethod.invoke(moduleInfo);
                
                final Field friendNamesField = Class.forName("org.netbeans.ModuleData", true, dataValue.getClass().getClassLoader()).getDeclaredField("friendNames");
                updateFriendsValue(friendNamesField, dataValue, codeNameBase);
            }
        }
        catch( ReflectiveOperationException ex )
        {
            throw new IllegalStateException(ex);
        }
        catch( SecurityException ex )
        {
            throw new IllegalStateException(ex);
        }
        catch( IllegalArgumentException ex )
        {
            throw new IllegalStateException(ex);
        }
    }
    
    
    private void updateFriendsValue(Field field, Object obj, String friendToAdd) throws IllegalArgumentException, IllegalAccessException
    {
        field.setAccessible(true);
        
        @SuppressWarnings("unchecked")
        Set<String> value = (Set<String>) field.get(obj);
        assert(value != null);
        
        // TODO: Test for null and add then!?
        Set<String> newValue = new HashSet<String>(value);
        newValue.add(friendToAdd);
        field.set(obj, newValue);
    }
}
