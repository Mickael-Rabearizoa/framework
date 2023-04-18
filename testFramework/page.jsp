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
<% 
    Object listEmp_object = request.getAttribute("listEmp");
    Vector<Emp> listEmp = (Vector)(listEmp_object);
    for(Emp employe : listEmp){
        out.print(employe.getNom()+" ");
    }
%>
<body>
    <h1>Welcome to this framework</h1>
</body>
</html>