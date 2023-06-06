<%@ page import = "model.Emp"%>
<%@ page import = "java.util.Vector"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <h1>Liste des employe</h1>
    <% 
        Object listEmp_object = request.getAttribute("listEmp");
        Vector<Emp> listEmp = (Vector)(listEmp_object);
        for(Emp employe : listEmp){
    %>
            <p><a href=<% out.print("/testFramework/detail-emp?id="+employe.getId()); %> > <% out.print(employe.getNom()+" "); %></a></p>
    <%      
        }
    %>
</body>
</html>