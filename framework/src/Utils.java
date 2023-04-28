package utilitaires;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
// import java.util.Date;
import java.sql.Date;
import java.util.HashMap;
import etu1796.framework.Mapping;
import modelView.ModelView;

public class Utils{

    public static Object cast(String a_caster, Class classe) throws IllegalAccessException, InstantiationException {
        // System
        try {
            return Integer.parseInt(a_caster);    
        } catch (Exception e) {}

        // try {
        //     return Float.parseFloat(a_caster);
        // } catch (Exception e) {}

        try {
            return Double.parseDouble(a_caster);
        } catch (Exception e) {}

        try {
            Time.valueOf(a_caster);
        } catch (Exception e) {}

        try {
            if(classe.getName().compareToIgnoreCase("java.util.Date") == 0){
                return new java.util.Date(a_caster);
            }
        } catch (Exception e) {}

        try {
            return java.sql.Date.valueOf(a_caster);
        } catch (Exception e) {}

        try {
            return Timestamp.valueOf(a_caster);
        } catch (Exception e) {}

        try {
            return Long.parseLong(a_caster);
        } catch (Exception e) {}

        try {
            return new BigDecimal(a_caster);
        } catch (Exception e){}

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

    public static ModelView getModelView(Class<?> classe , Mapping map , Object objet , String url) throws Exception {
        System.out.println("URL: "+url);
        // Mapping map = mappingUrls.get(url);
        // Class classe = Class.forName(map.getClassName());
        // objet = classe.newInstance();
        Method[] listMethods = classe.getDeclaredMethods();
        try {
            Method fonction = Utils.getMethod(listMethods, map.getMethod());
            ModelView mv = (ModelView)(fonction.invoke(objet));
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