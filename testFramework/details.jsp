<%@ page import = "model.Emp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>details</title>
</head>
<%
    Object employe_object = request.getAttribute("employe");
    Emp employe = (Emp)(employe_object);
%>
    <p>Nom: <% out.print(employe.getNom()); %></p>
    <p>salaire: <% out.print(employe.getSalaire()); %></p>
<body>
</body>
</html>