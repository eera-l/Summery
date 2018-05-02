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
    });
// http://localhost:6789/test?p=userID&t=Artificial%20intelligence%20is%20a%20powerful%20tool%20and%20it%20raises%20a%20lot%20of%20ethical%20questions,%20but%20we%20cannot%20abandon%20the%20entire%20field%20just%20because%20it%20is%20too%20dangerous.
// "Artificial%20intelligence%20is%20a%20powerful%20tool%20and%20it%20raises%20a%20lot%20of%20ethical%20questions,%20but%20we%20cannot%20abandon%20the%20entire%20field%20just%20because%20it%20is%20too%20dangerous.%20"
}
