<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <h1>Ajouter employe</h1>
    <form action="/testFramework/emp-save" method="post" enctype="multipart/form-data">
        id:
        <input type="number" name="id">
        nom:
        <input type="text" name="nom">
        salaire:
        <input type="text" name="salaire">
        photo:
        <input type="file" name="photo">
        <input type="submit" value="Ajouter">
    </form>
</body>
</html>