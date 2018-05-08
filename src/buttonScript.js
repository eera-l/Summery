var HttpClient = function() {
    this.get = function(aUrl, aCallback) {
        var anHttpRequest = new XMLHttpRequest();
        anHttpRequest.onreadystatechange = function() {
            if (anHttpRequest.readyState == 4 && anHttpRequest.status == 200)
                aCallback(anHttpRequest.responseText);
        };
        anHttpRequest.open( "GET", aUrl, true );
        anHttpRequest.send();
    }
};

function button_onClick() {
    var text = document.getElementById("message").value;
    //window.alert(text);

     text = text.replace(/\s+/g,'%20');
     text = text.replace(/\n+/g,'%20');
     text = text.replace(/"/g,'');
     text = text.replace(/'/g,'');
     text = text.replace(/=/g,'%20');
     text = text.replace(/:/g,'%20');
     text = text.replace(/\//g,'');
     text = text.replace(/\\/g,'');

    var client = new HttpClient();
    client.get('http://localhost:6789/test?p=userID&t='+text, function(response) {
        document.getElementById("summarizedText").innerText = response;
        //81.230.35.11:6789
    });
}
