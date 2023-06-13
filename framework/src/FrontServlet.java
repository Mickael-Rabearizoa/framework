package etu1796.framework.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import utilitaires.Utils;
import java.util.HashMap;
import java.util.Map;

import etu1796.framework.Mapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import directory.Project;
import annotation.Url;
import annotation.Scope;
import java.util.Vector;
import modelView.ModelView;
import fileUpload.FileUpload;
import java.io.InputStream;
import javax.servlet.annotation.MultipartConfig;

@MultipartConfig
public class FrontServlet extends HttpServlet{
    private HashMap<String,Mapping> mappingUrls = new HashMap();
    private String projectName;
    private HashMap<String,Object> list_singleton = new HashMap();

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

            ModelView mv = Utils.getModelView(fonction, objet , listParamValues , parameters);
            HashMap<String, Object> data = mv.getData();
            
            this.setAttributes(req, data);
            System.out.println(mv.getView());
            RequestDispatcher dispat = req.getRequestDispatcher(mv.getView());
            dispat.forward(req,res);
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