package etu1796.framework.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import utilitaires.Utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import etu1796.framework.Mapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import directory.Project;
import annotation.Url;
import annotation.Auth;
import annotation.Scope;
import annotation.Session;
import java.util.Vector;
import modelView.ModelView;
import fileUpload.FileUpload;
import java.io.InputStream;
import javax.servlet.annotation.MultipartConfig;
import com.google.gson.Gson;

@MultipartConfig
public class FrontServlet extends HttpServlet{
    private HashMap<String,Mapping> mappingUrls = new HashMap();
    private String projectName;
    private HashMap<String,Object> list_singleton = new HashMap();

    public boolean checkAnnatationAuth(Method fonction) throws Exception {
        try {
            String methodAnnotation = "annotation.Auth";
            Class AuthAnnotation = Class.forName(methodAnnotation);
            if(fonction.isAnnotationPresent(AuthAnnotation)){
                return true;
            }
            return false;
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }

    public boolean checkAnnotationSession(Method fonction) throws Exception {
        try {
            String methodAnnotation = "annotation.Session";
            Class AuthAnnotation = Class.forName(methodAnnotation);
            if(fonction.isAnnotationPresent(AuthAnnotation)){
                return true;
            }
            return false;
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }

    public void init(){
        try { 
            this.projectName = this.getInitParameter("projectName");
            String methodAnnotation = "annotation.Url"; 
            String classAnnotation = "annotation.Scope";
            Class urlAnnotation = Class.forName(methodAnnotation);
            Class scopeAnnotation = Class.forName(classAnnotation);
            Project proj = new Project();
            Vector<Class> listClasses = proj.getListClassesInPackage(new Vector<Class>(), new String()); 
            for(Class classe : listClasses) {
                
                // maka ny class singleton rehetra
                if(classe.isAnnotationPresent(scopeAnnotation)){
                    Scope scope = (Scope) classe.getAnnotation(Scope.class);
                    if(scope.value().compareToIgnoreCase("singleton") == 0){
                        this.list_singleton.put(classe.getName(),null);
                    }
                }

                Method[] list_Methods =  classe.getDeclaredMethods();
                for(Method fonction : list_Methods){
                    if(fonction.isAnnotationPresent(urlAnnotation)){
                        Url url = (Url) fonction.getAnnotation(Url.class);
                        System.out.println(url.url());
                        Mapping map = new Mapping(classe.getName() , fonction.getName());
                        this.mappingUrls.put(url.url() , map);
                    }
                }
            }
            // System.out.println(this.mappingUrls.get("/emp-all").getMethod());
            // System.out.println(this.mappingUrls.get("/emp-add").getMethod());
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void setAttributes(HttpServletRequest req , HashMap<String, Object> data) throws Exception {
        if(data != null){
            for(Map.Entry<String , Object> entry : data.entrySet()){
                req.setAttribute(entry.getKey() , entry.getValue());
            }
        }
    }

    private String getFileName(Part part){
        String contentDisposition = part.getHeader("content-disposition");
        String[] elements = contentDisposition.split(";");
        for (String element : elements) {
            if(element.trim().startsWith("filename")){
                return element.substring(element.indexOf("=") + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    private boolean checkSingletonClass( String className ){
        if(this.list_singleton.containsKey(className) == true){
            return true;
        }
        return false;
    }

    ///////////////////////////////////////////////////////////// 
    // Fonction qui ajoute la session dans HttpSession
    public void addSession(ModelView mv, HttpServletRequest req){
        HttpSession session = req.getSession();
        HashMap<String, Object> mv_session = mv.getSession();
        if(mv_session != null){
            for(Map.Entry<String , Object> entry : mv_session.entrySet()){
                session.setAttribute(entry.getKey() , entry.getValue());
            }
        }
    }

    
    public boolean authentifier(Method fonction , HttpServletRequest req) throws Exception {
        try {
            String isConnectedName = this.getInitParameter("isConnected");
            String profilName = this.getInitParameter("profil");

            HttpSession session = req.getSession();

            String isConnectedValue = String.valueOf(session.getAttribute(isConnectedName));
            String profilValue = String.valueOf(session.getAttribute(profilName));

            String methodAnnotation = "annotation.Auth";
            Class AuthAnnotation = Class.forName(methodAnnotation);
            Auth authentification = (Auth) fonction.getAnnotation(AuthAnnotation);

            if(isConnectedValue != null && profilValue != null){
                if(profilValue.compareToIgnoreCase(authentification.profil()) == 0){
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }

    public void setobjectSession(Object objet , Field sessionField , HttpServletRequest req) throws Exception {
        try {
            HttpSession session = req.getSession();

            HashMap<String,Object> listSession = new HashMap();
    
            Enumeration<String> attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String attributeName = attributeNames.nextElement();
                Object attributeValue = session.getAttribute(attributeName);
                System.out.println("cle: "+attributeName+" valeur: "+attributeValue);
                listSession.put(attributeName, attributeValue);
            }
            sessionField.setAccessible(true);
            sessionField.set(objet , listSession);
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }

    public void printJson(String json , HttpServletResponse res) throws Exception {
        try {
            res.setContentType("application/json");
            PrintWriter out = res.getWriter();
            out.print(json);
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }

    protected void processRequest(HttpServletRequest req , HttpServletResponse res , String url) throws Exception {
        try {
            String path = Utils.getPath_in_URL(url, this.projectName);

            // instanciation de la classe
            Mapping map = mappingUrls.get(path);
            if(map == null){
                throw new Exception("ressource not found");
            }
            Class<?> classe = Class.forName(map.getClassName());
            
            Object objet = new Object();
            

            // verification si la classe est un singleton
            if(this.checkSingletonClass(classe.getName()) == true){
                System.out.println("--------------------------------");
                System.out.println("--------------------------------");
                if(this.list_singleton.get(classe.getName()) != null){
                    objet = this.list_singleton.get(classe.getName());
                } else {
                    System.out.println("instanciation singleton");
                    objet = classe.newInstance();
                    this.list_singleton.put(classe.getName(), objet);
                }
            } else {
                objet = classe.newInstance();
            }

            

            Field[] listFields = classe.getDeclaredFields();
            for (Field field : listFields) {
                if(req.getParameter(field.getName()) != null ){
                    if(field.getType() == FileUpload.class){
                        if(req.getPart(field.getName()) != null ){
                            Part partFile = req.getPart(field.getName());
                            String fileName = getFileName(partFile);
                            InputStream inputStream = partFile.getInputStream();
                            byte[] bytes = new byte[(int) partFile.getSize()];
                            inputStream.read(bytes);
                            FileUpload fileUpload = new FileUpload(fileName , bytes);
                            Utils.setAttributeFileUpload(classe, objet, fileUpload, field.getName());
                        } 
                    } else {
                        Utils.setAttribute(classe, objet, req.getParameter(field.getName()), field.getName());
                    }
                }
            }

            Method[] listMethods = classe.getDeclaredMethods();
            Vector listParamName = new Vector<String>();
            Method fonction = null;
            Parameter[] parameters = null;
            try {
                fonction = Utils.getMethod(listMethods, map.getMethod());
                System.out.println(fonction.getName());
                parameters = fonction.getParameters();
                // for (Parameter parameter : parameters) {
                //     System.out.println(parameter.getName());
                // }
            } catch (Exception e) {
                // TODO: handle exception
                throw e;
            }

            Vector listParamValues = new Vector();
            for (Parameter param : parameters) {
                // System.out.println("paramName: "+param.getName());
                listParamValues.add(req.getParameter(Utils.getParameterName(param)));
            }

            // verifier raha manana autorisation ilay utilisateur
            if(checkAnnatationAuth(fonction) == true){
                if(authentifier(fonction , req) == false){
                    throw new Exception("vous n'avez pas les droits recquis");
                }
            }

            // verifier raha annote session ilay fonction
            if(checkAnnotationSession(fonction) == true){
                // passage des session dans l'objet
                Field attribut = classe.getDeclaredField("session");
                if(attribut.getType().equals(HashMap.class)){
                    System.out.println("niditraaaaaaaaaaaaaaaaaaaaaaaaaa");
                    setobjectSession(objet , attribut , req);
                }
            }

            ModelView mv = Utils.getModelView(fonction, objet , listParamValues , parameters);
            HashMap<String, Object> data = mv.getData();
            
            // raha misy session
            this.addSession(mv, req);

            // verifier s'il faut transformer data en Json
            if(mv.getIsJson() == true){

                Gson gson = new Gson();
                String json = gson.toJson(data);
                printJson(json , res);

            } else {
                // raha misy data
                this.setAttributes(req, data);
                System.out.println(mv.getView());
                RequestDispatcher dispat = req.getRequestDispatcher(mv.getView());
                dispat.forward(req,res);
            }
            // return page;
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException{
        System.out.println("processus");
        // String projectName = "testFramework";
        try {
            this.processRequest(req , res , req.getRequestURL().toString());
            System.out.println("-----------------------------------------------------------");
            // System.out.println("page: "+page);
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        
        // PrintWriter out = res.getWriter();
        // out.print(path);
        System.out.println("do get");
    }
    
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException{
        // String projectName = "testFramework";
        try {
            this.processRequest(req , res , req.getRequestURL().toString());
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        // PrintWriter out = res.getWriter();
        // out.print(path);
        System.out.println("do post");
    }
}