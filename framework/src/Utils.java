package utilitaires;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
// import java.util.Date;
import java.sql.Date;
import java.util.HashMap;
import java.util.Vector;

import etu1796.framework.Mapping;
import modelView.ModelView;
import fileUpload.FileUpload;
import annotation.ParameterName;

public class Utils{

    public static Object cast(String a_caster, Class classe) throws IllegalAccessException, InstantiationException {
        // System.out.println("niantso cast");
        
        try {
            System.out.println("niantso cast");
            System.out.println("class: "+classe.getName()+"  Compare: int");
            if(classe.getName().compareToIgnoreCase("int") == 0){
                System.out.println("Type: int");
                if(a_caster != null){
                    System.out.println("not null");
                    System.out.println("a_caster: "+a_caster);
                    int huhu = Integer.parseInt(a_caster);
                    System.out.println("tona eto ");
                    return huhu;  
                } else {
                    System.out.println("null");
                    return 0;
                }
            }
        } catch (Exception e) {}

        try {
            if(classe.getName().compareToIgnoreCase("float") == 0){
                System.out.println("Type: float");
                if(a_caster != null){
                    return Float.parseFloat(a_caster);
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {}

        try {
            if(classe.getName().compareToIgnoreCase("double") == 0){
                System.out.println("Type: double");
                if(a_caster != null){
                    return Double.parseDouble(a_caster);
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {}

        try {
            if(classe.getName().compareToIgnoreCase("java.sql.Time") == 0){
                System.out.println("Type: Time");
                if(a_caster != null){
                    return Time.valueOf(a_caster);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {}

        try {
            if(classe.getName().compareToIgnoreCase("java.util.Date") == 0){
                System.out.println("Type: util.Date");
                if(a_caster != null){
                    return new java.util.Date(a_caster);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {}

        try {
            if(classe.getName().compareToIgnoreCase("java.sql.Date") == 0){
                System.out.println("Type: sql.Date");
                if(a_caster != null){
                    return java.sql.Date.valueOf(a_caster);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {}

        try {
            if(classe.getName().compareToIgnoreCase("java.sql.Timestamp") == 0){
                System.out.println("Type: Timestamp");
                if(a_caster != null){
                    return Timestamp.valueOf(a_caster);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {}

        try {
            if(classe.getName().compareToIgnoreCase("long") == 0){
                System.out.println("Type: long");
                if(a_caster != null){
                    return Long.parseLong(a_caster);
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {}

        try {
            if(classe.getName().compareToIgnoreCase("java.math.BigDecimal") == 0){
                System.out.println("Type: BigDecimal");
                if(a_caster != null){
                    return new BigDecimal(a_caster);
                } else {
                    return null;
                }
            }
        } catch (Exception e){}

        try {
            if(classe.getName().compareToIgnoreCase("boolean") == 0){
                System.out.println("Type: boolean");
                if(a_caster != null){
                    return new BigDecimal(a_caster);
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        try {
            System.out.println("class: "+classe.getName()+"  Compare: java.lang.String");
            if(classe.getName().compareToIgnoreCase("java.lang.String") == 0){
                System.out.println("Type: String");
                    return a_caster;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        System.out.println("return");
        return a_caster;
    }    

    public static String getPath_in_URL(String url , String projectName){
        String[] firstSplit = url.split(projectName); 
        String path_in_URL = " ";
        if(firstSplit.length < 2){
            path_in_URL = "erreur";
        } else {
            path_in_URL = firstSplit[1];
        }
        // String path_in_URL = firstSplit.split("?")[0];
        return path_in_URL;
    }

    public static Method getMethod(Method[] listMethods , String methodName) throws Exception {
        for (Method method : listMethods) {
            // System.out.println("methodName: "+methodName+" listfonction: "+method.getName());
            if(methodName.compareToIgnoreCase(method.getName()) == 0){
                return method;
            }
        }
        throw new Exception("method not found");
    }

    public static String getPage(HashMap<String,Mapping> mappingUrls , String url) throws Exception{
        // System.out.println("getPage");
        System.out.println("URL: "+url);
        Mapping map = mappingUrls.get(url);
        if(map == null){
            throw new Exception("ressource not found");
        } else {
            Class classe = Class.forName(map.getClassName());
            Object objet = classe.newInstance();
            Method[] listMethods = classe.getDeclaredMethods();
            try {
                Method fonction = Utils.getMethod(listMethods, map.getMethod());
                ModelView mv = (ModelView)(fonction.invoke(objet));
                String view = mv.getView();
                // System.out.println("return: "+view);
                return view;
            } catch (Exception e) {
                // TODO: handle exception
                throw e;
            }
        }
    }

    public static Object[] castParams(Vector listParamValues, Parameter[] parameters) throws Exception{
        try {
            Object[] listObjects = new Object[listParamValues.size()];
            System.out.println("size: "+listParamValues.size());
            for(int i=0;i<listParamValues.size();i++){
                System.out.println("typeCast: "+parameters[i].getType());
                listObjects[i] = Utils.cast(String.valueOf(listParamValues.get(i)) , parameters[i].getType());
            }
            return listObjects;
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
        
    }

    //  public static ModelView getModelView(Class<?> classe , Mapping map , Object objet , String url) throws Exception {
    public static ModelView getModelView(Method fonction , Object objet , Vector listParamValues , Parameter[] parameters) throws Exception {
        // System.out.println("URL: "+url);
        // Mapping map = mappingUrls.get(url);
        // Class classe = Class.forName(map.getClassName());
        // objet = classe.newInstance();
        // Method[] listMethods = classe.getDeclaredMethods();
        Object[] args = Utils.castParams(listParamValues, parameters);
        try {
            // fonction = Utils.getMethod(listMethods, map.getMethod());
            ModelView mv = (ModelView)(fonction.invoke(objet , args));
            if(mv == null){
                throw new Exception("No view called");
            }
            return mv;
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }

    // public static getMethod(Class classe, String methodName){
    //     Method[] listMethods = classe.getDeclaredMethods();
    //     for (Method method : listMethods) {
    //         if(methodName.compareToIgnoreCase(method.getName())){
    //             return method;
    //         }
    //     } 
    //     return null;
    // }

    public static Vector getParametersName(Method fonction){
        Vector listParamName = new Vector<String>();
        Parameter[] params = fonction.getParameters();
        for (Parameter param : params) {
            listParamName.add(param.getName());
        }
        return listParamName;
    }
        
    

    public static Class<?> getParameterType(Method fonction){
        Class<?>[] classes = fonction.getParameterTypes();
        return classes[0];
    }

    public static void setAttribute(Class classe , Object objet , String attributeValue , String attributeName) throws Exception {
        try {
            Method fonction = Utils.getMethod(classe.getDeclaredMethods() , "set"+attributeName);
            // String[] args = new String[1];
            // args[0] = attributeValue;
            fonction.invoke(objet , Utils.cast(attributeValue, Utils.getParameterType(fonction)));
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }

    public static void setAttributeFileUpload(Class classe , Object objet , FileUpload fileUpload , String attributeName) throws Exception {
        try {
            Method fonction = Utils.getMethod(classe.getDeclaredMethods() , "set"+attributeName);
            
            fonction.invoke(objet , fileUpload);
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }

    public static String getParameterName(Parameter param) throws Exception {
        String annotation = "annotation.ParameterName";
        Class annotationClass = Class.forName(annotation);
        if(param.isAnnotationPresent(annotationClass)){
            ParameterName paramName = (ParameterName) param.getAnnotation(annotationClass);
            return paramName.parameterName();
        } else {
            throw new Exception("les parametres de la fonction invoque doivent etre annote par: @ParameterName");
        }
    }

    public static Object getObject(Method fonction , Object objet , Vector listParamValues , Parameter[] parameters) throws Exception {
        Object[] args = Utils.castParams(listParamValues, parameters);
        try {
            // fonction = Utils.getMethod(listMethods, map.getMethod());
            Object obj = fonction.invoke(objet , args);
            return obj;
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }

    // public static void setAttribute(Class classe , Object objet , String attributeValue , String attributeName) throws Exception {
    //     try {
    //         Method fonction = Utils.getMethod(classe.getDeclaredMethods() , "set"+attributeName);
    //         Object[] args = new Object[1];
    //         args[0] = attributeValue;
    //         fonction.invoke(objet , args);
    //     } catch (Exception e) {
    //         // TODO: handle exception
    //         throw e;
    //     }    
    // }
    
}