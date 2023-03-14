package directory;

import java.io.File;
import java.util.Vector;

public class Project{

    public Project(){}

    public String[] listObjectIntoListString(Object[] listObject){
        String[] listString = new String[listObject.length];
        for (int i=0; i<listObject.length ; i++) {  
            listString[i] = String.valueOf(listObject[i]);
        }
        return listString;
    }

    public Object[] getObjectClassesNameInPackage(String packageName){
        File directory = new File(packageName);
        File[] listFile = directory.listFiles();
        Vector<String> classesName = new Vector();
        for(int i=0;i<listFile.length;i++){
            String fileName = listFile[i].getName();
            if(fileName.contains(".class")){
                String replacedPoint = fileName.replace(".", "/");
                // System.out.println(replacedPoint);
                String[] splitedName = replacedPoint.split("/");
                String className = splitedName[0];
                classesName.add(className);
            }
        }
        return classesName.toArray();
    }

    public Object[] getObjectDirectoryNameInPackage(String packageName){
        File fichier = new File(packageName);
        File[] listFile = fichier.listFiles();
        Vector<String> directoryName = new Vector();
        for(int i=0; i<listFile.length; i++){
            if(listFile[i].isDirectory()){
                directoryName.add(listFile[i].getName());
            }
        }
        return directoryName.toArray();
    }

    public String[] getDirectoryNameInPackage(String packageName){
        Object[] objectDirectoryName = this.getObjectDirectoryNameInPackage(packageName);
        String[] directoryName = this.listObjectIntoListString(objectDirectoryName);
        return directoryName;
    }

    public boolean test_exist(String packageName){
        File directory = new File(packageName);
        System.out.println( directory.getAbsolutePath());
        return directory.exists();
    }

    public String[] getClassesNameInPackage(String packageName){
        Object[] objectClassesName = this.getObjectClassesNameInPackage(packageName);
        String[] classesName = this.listObjectIntoListString(objectClassesName);
        return classesName;
    }

}