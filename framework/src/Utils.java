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

    // fonction pour caster les attributs de type primaire
    public static Object cast(String a_caster, Class classe) throws IllegalAccessException, InstantiationException {
        try {
            if(classe.getName().compareToIgnoreCase("int") == 0){
                if(a_caster != null){
                    int valeur = Integer.parseInt(a_caster);
                    return valeur;  
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {}

        try {
            if(classe.getName().compareToIgnoreCase("float") == 0){
                
                if(a_caster != null){
                    return Float.parseFloat(a_caster);
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {}

        try {
            if(classe.getName().compareToIgnoreCase("double") == 0){
                
                if(a_caster != null){
                    return Double.parseDouble(a_caster);
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {}

        try {
            if(classe.getName().compareToIgnoreCase("java.sql.Time") == 0){
                
                if(a_caster != null){
                    return Time.valueOf(a_caster);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {}

        try {
            if(classe.getName().compareToIgnoreCase("java.util.Date") == 0){
                
                if(a_caster != null){
                    return new java.util.Date(a_caster);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {}

        try {
            if(classe.getName().compareToIgnoreCase("java.sql.Date") == 0){
                
                if(a_caster != null){
                    return java.sql.Date.valueOf(a_caster);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {}

        try {
            if(classe.getName().compareToIgnoreCase("java.sql.Timestamp") == 0){
                
                if(a_caster != null){
                    return Timestamp.valueOf(a_caster);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {}

        try {
            if(classe.getName().compareToIgnoreCase("long") == 0){
                
                if(a_caster != null){
                    return Long.parseLong(a_caster);
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {}

        try {
            if(classe.getName().compareToIgnoreCase("java.math.BigDecimal") == 0){
                
                if(a_caster != null){
                    return new BigDecimal(a_caster);
                } else {
                    return null;
                }
            }
        } catch (Exception e){}

        try {
            if(classe.getName().compareToIgnoreCase("boolean") == 0){
               
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
            
            if(classe.getName().compareToIgnoreCase("java.lang.String") == 0){
                return a_caster;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return a_caster;
    }    

    // fonction pour prendre l'url associé à la fonction 
    public static String getPath_in_URL(String url , String projectName){
        // spliter l'url par le nom du projet
        String[] firstSplit = url.split(projectName); 
        String path_in_URL = " ";
        // si firstSplit a moins de deux éléments il n'y a aucun Url associé appelé
        if(firstSplit.length < 2){
            path_in_URL = "erreur";
        } else {
            path_in_URL = firstSplit[1];
        }
        return path_in_URL;
    }

    // fonction pour prendre une methode parmis les listes de methode de la classe
    public static Method getMethod(Method[] listMethods , String methodName) throws Exception {
        for (Method method : listMethods) {
            if(methodName.compareToIgnoreCase(method.getName()) == 0){
                return method;
            }
        }
        throw new Exception("method not found");
    }

    // fonction pour prendre le nom de la page pour rediriger le client
    public static String getPage(HashMap<String,Mapping> mappingUrls , String url) throws Exception{
        // prendre le Mapping associé à l'Url dans "mappingUrls"
        Mapping map = mappingUrls.get(url);
        if(map == null){
            throw new Exception("ressource not found");
        } else {
            // instanciation de la classe
            Class classe = Class.forName(map.getClassName());
            Object objet = classe.newInstance();
            Method[] listMethods = classe.getDeclaredMethods();
            try {
                // prendre la fonction à invoker
                Method fonction = Utils.getMethod(listMethods, map.getMethod());
                ModelView mv = (ModelView)(fonction.invoke(objet));
                String view = mv.getView();
                return view;
            } catch (Exception e) {
                // TODO: handle exception
                throw e;
            }
        }
    }

    // caster la liste des parametres de l'objet
    public static Object[] castParams(Vector listParamValues, Parameter[] parameters) throws Exception{
        try {
            Object[] listObjects = new Object[listParamValues.size()];
            for(int i=0;i<listParamValues.size();i++){
                listObjects[i] = Utils.cast(String.valueOf(listParamValues.get(i)) , parameters[i].getType());
            }
            return listObjects;
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
        
    }

    // fonction pour prendre le ModelView retourné par la fonction invoké
    public static ModelView getModelView(Method fonction , Object objet , Vector listParamValues , Parameter[] parameters) throws Exception {
        // caster les parametres de la fonction à invoker
        Object[] args = Utils.castParams(listParamValues, parameters);
        try {
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

    // fonction pour prendre le nom de la liste des parametres d'une fonction
    public static Vector getParametersName(Method fonction){
        Vector listParamName = new Vector<String>();
        Parameter[] params = fonction.getParameters();
        for (Parameter param : params) {
            listParamName.add(param.getName());
        }
        return listParamName;
    }
        
    // fonction pour prendre le type de parametre d'un setter 
    public static Class<?> getParameterType(Method fonction){
        Class<?>[] classes = fonction.getParameterTypes();
        return classes[0];
    }

    // fonction pour setter un attribut d'un objet
    public static void setAttribute(Class classe , Object objet , String attributeValue , String attributeName) throws Exception {
        try {
            Method fonction = Utils.getMethod(classe.getDeclaredMethods() , "set"+attributeName);
            
            fonction.invoke(objet , Utils.cast(attributeValue, Utils.getParameterType(fonction)));
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }

    // fonction pour setter un attribut de type FileUpload d'un objet
    public static void setAttributeFileUpload(Class classe , Object objet , FileUpload fileUpload , String attributeName) throws Exception {
        try {
            Method fonction = Utils.getMethod(classe.getDeclaredMethods() , "set"+attributeName);
            
            fonction.invoke(objet , fileUpload);
            System.out.println("ok");
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }

    // fonction pour prendre le nom d'un parametre grace à l'annotation "ParameterName"
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

    // fonction pour prendre l'objet retourné par une fonction
    public static Object getObject(Method fonction , Object objet , Vector listParamValues , Parameter[] parameters) throws Exception {
        Object[] args = Utils.castParams(listParamValues, parameters);
        try {
            Object obj = fonction.invoke(objet , args);
            return obj;
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }
    
}