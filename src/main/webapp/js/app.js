var excelJsLib = excelJsLib || {};

excelJsLib.submit=function () {
    var me=this;
    var reader = new FileReader();
    reader.readAsArrayBuffer(e.target.files[0]);
    var reader = new FileReader();
    var wb;
    reader.readAsArrayBuffer(e.target.files[0]);
    reader.onload = function(e) {
        var data = new Uint8Array(reader.result);
        wb = XLSX.read(data, {type: 'array'});

        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState == XMLHttpRequest.DONE) {
                if (xhr.status == 200) {
                    requestResponse = eval('(' + xhr.wb + ')');

                }
            }
        }
        xhr.open('post', me.dataUrl, false);
        xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhr.send(formData);
    }

}