<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>excel file uploader</title>
    <link rel="stylesheet" type="text/css" href="./css/style.css">

    <script src="./js/app.js"></script>
</head>

<body>
<div id=center">
    <h2 id="header">excel data upload to server using servlet</h2>
    <form id="form" action = "FileHandler" method = "post" enctype = "multipart/form-data">
        <label for="file">Select an excel file:</label>
        <input type="file" id="file" name="myfile" onchange="return this.fileValidation()"><br><br>
        <input type="submit" id="button">
    </form>
    <div id="excel-content">

    </div>
</div>
</body>
</html>
