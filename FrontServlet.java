package etu1796.framework.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import utilitaires.Utils;
import java.util.HashMap;
import etu1796.framework.Mapping;
import java.lang.reflect.Method;
import directory.Project;
import annotation.Url;

public class FrontServlet extends HttpServlet{
    HashMap<String,Mapping> mappingUrls = new HashMap();

    public void init(){
    try {
            String directory = "../webapps/framework/WEB-INF/classes";
            String annotation = "annotation.Url"; 
            // String FieldAnnotation = "annotation.Colonne";
            Project proj = new Project();
            // System.out.println(proj.test_exist(directory));
            String[] directoryNames = proj.getDirectoryNameInPackage(directory);
            for(String directoryName : directoryNames){
                // System.out.println(directoryName);

                String[] classNames = proj.getClassesNameInPackage(directory+"/"+directoryName);
                for(String className : classNames){
                    Class classe = Class.forName(directoryName+"."+className);
                    Class annotationClass = Class.forName(annotation);
                    Method[] list_Methods =  classe.getDeclaredMethods();
                    for(Method fonction : list_Methods){
                        if(fonction.isAnnotationPresent(annotationClass)){
                            Url url = (Url) fonction.getAnnotation(Url.class);
                            System.out.println(url.url());
                            Mapping map = new Mapping(className , fonction.getName());
                            this.mappingUrls.put(url.url() , map);
                        }
                    }
                }
            }
            System.out.println(this.mappingUrls.get("/emp-all").getMethod());
            System.out.println(this.mappingUrls.get("/emp-add").getMethod());
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    protected String processRequest(String url , String projectName){
        return Utils.getPath_in_URL(url, projectName);
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException{
        String projectName = "sprint1";
        String path = this.processRequest(req.getRequestURL().toString() , projectName);
        PrintWriter out = res.getWriter();
        out.print(path);
        System.out.println("do get");
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException{
        String projectName = "sprint1";
        String path = this.processRequest(req.getRequestURL().toString() , projectName);
        PrintWriter out = res.getWriter();
        out.print(path);
        System.out.println("do post");
    }
}