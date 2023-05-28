package etu1796.framework.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import utilitaires.Utils;
import java.util.HashMap;
import java.util.Map;

import etu1796.framework.Mapping;
import java.lang.reflect.Method;
import directory.Project;
import annotation.Url;
import java.util.Vector;
import modelView.ModelView;

public class FrontServlet extends HttpServlet{
    private HashMap<String,Mapping> mappingUrls = new HashMap();

    public void init(){
        try {
            String annotation = "annotation.Url"; 
            Project proj = new Project();
            Vector<Class> listClasses = proj.getListClassesInPackage(new Vector<Class>(), new String()); 
            for(Class classe : listClasses) {
                Class annotationClass = Class.forName(annotation);
                    Method[] list_Methods =  classe.getDeclaredMethods();
                    for(Method fonction : list_Methods){
                        if(fonction.isAnnotationPresent(annotationClass)){
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

    protected void processRequest(HttpServletRequest req , HttpServletResponse res , String url , String projectName) throws Exception {
        try {
            String path = Utils.getPath_in_URL(url, projectName);
            ModelView mv = Utils.getModelView(this.mappingUrls , path);
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
        String projectName = "testFramework";
        try {
            this.processRequest(req , res , req.getRequestURL().toString() , projectName);
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
        String projectName = "testFramework";
        try {
            this.processRequest(req , res , req.getRequestURL().toString() , projectName);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        // PrintWriter out = res.getWriter();
        // out.print(path);
        System.out.println("do post");
    }
}