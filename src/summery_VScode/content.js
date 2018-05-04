console.log("Chrome extension running");

var paragraphs = $('body p');

for(Element of paragraphs){
    if(Element.textContent.trim() !== ""){
        console.log(Element.className += 'summarize_me');
    }
}

$('<button type="button" class="summarize_button">Summarize</button>').insertAfter('.summarize_me');

$('.summarize_button').click(function () {

    $('#summarize_container').remove();

    var text = $(this).prev('p')[0].textContent;
    text = text.replace(/%/g,'percent');

    text = text.replace(/\s+/g,'%20');
    text = text.replace(/\n+/g,'%20');
    text = text.replace(/"/g,'');
    text = text.replace(/'/g,'');
    text = text.replace(/=/g,'%20');
    text = text.replace(/:/g,'%20');
    text = text.replace(/\\/g,'');
    var ignore = "\\";
    text = text.replace(ignore,'');
    text = text.replace(/\[.*?\]/g,'');

    $.ajax({
        url: "http://localhost:6789/test?p=userID&t=" + text,
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