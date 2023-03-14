package etu1796.framework.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import utilitaires.Utils;
import java.util.HashMap;
import etu1796.framework.Mapping;

public class FrontServlet extends HttpServlet{
    HashMap<String,Mapping> mappingUrls = new HashMap();

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