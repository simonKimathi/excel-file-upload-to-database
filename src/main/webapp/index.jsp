<html>
<head>
    <script src="jquery-2.1.4.js"></script>
    <script lang="javascript" src="xlsx.full.min.js"></script>
    <link href="css/style.css" rel="stylesheet">

    <script src="./js/app.js"></script>
</head>

<body>
<h2>excel data upload to server using servlet</h2>
<form id="form">
    <label for="myfile">Select an excel file:</label>
    <input type="file" id="myfile" name="myfile"><br><br>
    <input type="submit">
</form>
<div id="excel-content">

</div>
<script>
    $('#myfile').change(function(e){
        var reader = new FileReader();
        reader.readAsArrayBuffer(e.target.files[0]);
        reader.onload = function(e) {
            var data = new Uint8Array(reader.result);
            var wb = XLSX.read(data,{type:'array'});
            var htmlstr = XLSX.write(wb,{sheet:"sheet no1", type:'binary',bookType:'html'});
            $('#excel-content')[0].innerHTML += htmlstr;
        }
    });
</script>

</body>
</html>
