package directory;
import java.net.URL;
import java.io.File;
import java.util.Vector;

public class Project{

    public Project(){}

    public Vector<Class> getListClassesInPackage(Vector<Class> listClass , String packageName) throws ClassNotFoundException {
        File directory = null;
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }
            String path = packageName.replace('.', '/');
            URL resource = classLoader.getResource(path);
            if (resource == null) {
                throw new ClassNotFoundException("No resource for " + path);
            }
            directory = new File(resource.getFile());
        } catch (NullPointerException x) {
            x.printStackTrace();
            throw new ClassNotFoundException(packageName + " (" + directory + ") does not appear to be a valid package");
        }
        if (directory.exists()) {
            
            File[] listFile = directory.listFiles();
            // Vector<String> directoryName = new Vector();
            for(int i=0; i<listFile.length; i++){
                if(listFile[i].isDirectory()){
                    // directoryName.add(listFile[i].getName());
                    if(packageName.isEmpty()){
                        getListClassesInPackage(listClass , listFile[i].getName());
                    } else {
                        getListClassesInPackage(listClass , packageName + "." + listFile[i].getName());
                    }
                } else {
                    if (listFile[i].getName().endsWith(".class")) {
                        System.out.println( "files : "+listFile[i] );
                        listClass.add(Class.forName(packageName + '.' + listFile[i].getName().substring(0, listFile[i].getName().length() - 6)));
                    }
                }
            }
            
        }
        return listClass;
    }

}