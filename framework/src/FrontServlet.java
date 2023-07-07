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
import annotation.RestAPI;
import annotation.Scope;
import annotation.Session;
import java.util.Vector;
import modelView.ModelView;
import fileUpload.FileUpload;
import java.io.InputStream;
import javax.servlet.annotation.MultipartConfig;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.google.gson.Gson;

@MultipartConfig
public class FrontServlet extends HttpServlet{
    private HashMap<String,Mapping> mappingUrls = new HashMap();
    private String projectName;
    private HashMap<String,Object> list_singleton = new HashMap();

    // fonction verifier annotation d'une fonction
    public boolean checkMethodAnnatation(Method fonction , String methodAnnotation) throws Exception {
        try {
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
            // prendre le nom du projet
            this.projectName = this.getInitParameter("projectName");

            // declaration des noms de classs d'annotation
            String methodAnnotation = "annotation.Url"; 
            String classAnnotation = "annotation.Scope";
            // instanciation des class d'annotation
            Class urlAnnotation = Class.forName(methodAnnotation);
            Class scopeAnnotation = Class.forName(classAnnotation);

            // Prendre la list des class du projet
            Project proj = new Project();
            Vector<Class> listClasses = proj.getListClassesInPackage(new Vector<Class>(), new String()); 

            for(Class classe : listClasses) {
                // maka ny class singleton rehetra atao anaty "list_singleton" an'ny FrontServlet
                if(classe.isAnnotationPresent(scopeAnnotation)){
                    Scope scope = (Scope) classe.getAnnotation(Scope.class);
                    if(scope.value().compareToIgnoreCase("singleton") == 0){
                        this.list_singleton.put(classe.getName(),null);
                    }
                }

                // prendre la liste des fonctions de la classe
                Method[] list_Methods =  classe.getDeclaredMethods();
                for(Method fonction : list_Methods){
                    // verifie si la fonction presente l'annotation Url
                    if(fonction.isAnnotationPresent(urlAnnotation)){
                        Url url = (Url) fonction.getAnnotation(Url.class);
                        
                        // creation d'une instance de Mapping avec le nom de classe et le nom de fonction 
                        Mapping map = new Mapping(classe.getName() , fonction.getName());
                        // ajout de l'instance de Mapping et de l'Url associé dans l'attribut "mappingUrls"
                        this.mappingUrls.put(url.url() , map);
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    // ajout du data de ModelView dans des attributs de HttpRequest
    public void setAttributes(HttpServletRequest req , HashMap<String, Object> data) throws Exception {
        if(data != null){
            for(Map.Entry<String , Object> entry : data.entrySet()){
                req.setAttribute(entry.getKey() , entry.getValue());
            }
        }
    }

    // prendre le nom d'un fichier uploadé
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

    // Verifier rah anaty anaty "list_singleton" ny class iray
    private boolean checkSingletonClass( String className ){
        if(this.list_singleton.containsKey(className) == true){
            return true;
        }
        return false;
    }

    // Fonction qui ajoute la session de ModelView dans HttpSession
    public void addSession(ModelView mv, HttpServletRequest req){
        HttpSession session = req.getSession();
        HashMap<String, Object> mv_session = mv.getSession();
        if(mv_session != null){
            for(Map.Entry<String , Object> entry : mv_session.entrySet()){
                session.setAttribute(entry.getKey() , entry.getValue());
            }
        }
    }

    // fonction mamafa session anaty HttpSession
    public void removeSession(ModelView mv , HttpServletRequest req){
        HttpSession session = req.getSession();
        Vector<String> removeSession = mv.getRemoveSession();
        for (String key : removeSession) {
            session.removeAttribute(key);
        }
    }
    
    // fonction verifier si l'utilisateur a le droit d'appeler une fonction
    public boolean authentifier(Method fonction , HttpServletRequest req) throws Exception {
        try {
            // prendre les valeurs de connection dans le fichier "web.xml"
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

    // ajout des attributs de HttpSession dans un objet pour les utiliser dans un "Model/Controller"
    public void setobjectSession(Object objet , Field sessionField , HttpServletRequest req) throws Exception {
        try {
            HttpSession session = req.getSession();

            // instanciation d'un HashMap 
            HashMap<String,Object> listSession = new HashMap();
    
            // prendre les clés de la session
            Enumeration<String> attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String attributeName = attributeNames.nextElement();
                Object attributeValue = session.getAttribute(attributeName);

                // Ajout de l'attribut de session dans l'instance HashMap
                listSession.put(attributeName, attributeValue);
            }
            // Ajout de l'instance dans l'attribut de session de l'objet 
            sessionField.setAccessible(true);
            sessionField.set(objet , listSession);
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }

    // fonction pour afficher un Json
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
            // prendre le package de la classe 
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
                // si la classe singleton a déja été instancé
                if(this.list_singleton.get(classe.getName()) != null){
                    // prendre l'instance déja existante
                    objet = this.list_singleton.get(classe.getName());
                } else {
                    // instanciation d'un objet singleton
                    objet = classe.newInstance();
                    this.list_singleton.put(classe.getName(), objet);
                }
            } else {
                // si la classe n'est pas un singleton 
                objet = classe.newInstance();
            }

            // prendre la liste des attributs de la classe
            Field[] listFields = classe.getDeclaredFields();
            for (Field field : listFields) {
                // si les parametres de HttpRequest correspondent aux attributs de la classe
                if(req.getParameter(field.getName()) != null ){    
                    // si le parametre de HttpRequest n'est pas un FileUpload
                    Utils.setAttribute(classe, objet, req.getParameter(field.getName()), field.getName());
                    
                } else {
                    if(field.getType() == FileUpload.class){
                        if(ServletFileUpload.isMultipartContent(req)){
                            // si le parametre FileUpload de HttpRequest correspond à un attribut de la classe
                            if(req.getPart(field.getName()) != null ){
                                Part partFile = req.getPart(field.getName());

                                // prendre le nom du fichier FileUpload
                                String fileName = getFileName(partFile);
                                InputStream inputStream = partFile.getInputStream();
                                // prendre la liste de byte du fichier FileUpload
                                byte[] bytes = new byte[(int) partFile.getSize()];
                                inputStream.read(bytes);
                                // instanciation d'un FileUpload
                                FileUpload fileUpload = new FileUpload(fileName , bytes);
                                // set des attributs de l'objet
                                System.out.println("setFileUpload");
                                Utils.setAttributeFileUpload(classe, objet, fileUpload, field.getName());
                            }
                        } 
                    }
                }
            }

            // prendre la liste des fonctions de la classe pour etre utilisé dans "listMethods"
            Method[] listMethods = classe.getDeclaredMethods();
            Vector listParamName = new Vector<String>();
            Method fonction = null;
            Parameter[] parameters = null;
            try {
                // prendre la fonction à invoker
                fonction = Utils.getMethod(listMethods, map.getMethod());
                
                parameters = fonction.getParameters();
            } catch (Exception e) {
                // TODO: handle exception
                throw e;
            }

            Vector listParamValues = new Vector();
            for (Parameter param : parameters) {
                // prendre le nom du parametre de la fonction grace à l'annotation "ParameterName"
                listParamValues.add(req.getParameter(Utils.getParameterName(param)));
            }

            // verifier raha manana autorisation ilay utilisateur
            if(checkMethodAnnatation(fonction , "annotation.Auth") == true){
                if(authentifier(fonction , req) == false){
                    throw new Exception("vous n'avez pas les droits recquis");
                }
            }

            // verifier raha annoté session ilay fonction
            if(checkMethodAnnatation(fonction , "annotation.Session") == true){
                // passage des attributs de HttpSession dans l'objet
                Field attribut = classe.getDeclaredField("session");
                if(attribut.getType().equals(HashMap.class)){
                    setobjectSession(objet , attribut , req);
                }
            }

            // verification si la fonction est annoté RestAPI
            if(checkMethodAnnatation(fonction , "annotation.RestAPI")){
                // prendre l'objet retourné par la fonction à invoker
                Object obj = Utils.getObject(fonction, objet , listParamValues , parameters);

                Gson gson = new Gson();
                String json = gson.toJson(obj);
                printJson(json , res);

            } else {
                // prendre le ModelView retourné par la fonction à invoker
                ModelView mv = Utils.getModelView(fonction, objet , listParamValues , parameters);
                HashMap<String, Object> data = mv.getData();
                
                // raha tokony fafana daoly ny session rehetra
                if(mv.getInvalidateSession() == true){
                    HttpSession session = req.getSession();
                    session.invalidate();
                }

                // rah misy session fafana anaty HttpSession
                this.removeSession(mv, req);

                // raha misy session ampidirina anaty HttpSession
                this.addSession(mv, req);
    
                // verifier s'il faut transformer data en Json
                if(mv.getIsJson() == true){
    
                    Gson gson = new Gson();
                    String json = gson.toJson(data);
                    printJson(json , res);
    
                } else {
                    // raha misy data de ampidirina anaty attribut an'ny HttpRequest
                    this.setAttributes(req, data);
                    
                    RequestDispatcher dispat = req.getRequestDispatcher(mv.getView());
                    dispat.forward(req,res);
                }
            }
            // return page;
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException{
        System.out.println("processus");
        try {
            this.processRequest(req , res , req.getRequestURL().toString());
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        
    }
    
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException{
        try {
            this.processRequest(req , res , req.getRequestURL().toString());
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        
    }
}