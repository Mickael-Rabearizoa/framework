package utilitaires;

import java.lang.reflect.Method;
import java.util.HashMap;
import etu1796.framework.Mapping;
import modelView.ModelView;

public class Utils{
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
            System.out.println("methodName: "+methodName+" listfonction: "+method.getName());
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
}