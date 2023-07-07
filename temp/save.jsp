<%@ page import = "model.Emp"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <h1>Saved emp</h1>
    <% 
        Emp employe = (Emp)request.getAttribute("employe");
    %>
    <p>Id : <%= employe.getId() %></p>
    <p>Nom: <%= employe.getNom() %></p>
    <p>Salaire <%= employe.getSalaire() %></p>
    <p>Nom photo : <%= employe.getPhoto().getName() %></p>
    <p>bytes : <%= employe.getPhoto().getBytes() %></p>
</body>
</html>