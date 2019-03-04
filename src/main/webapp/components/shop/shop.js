
/**
 * Api URL
 * */
var apiUrl = 'http://localhost:8080/mystore';

/**
 * Fetch all existing items
 * */
$.get(apiUrl + '/shop/items', function (responseText) {
    $('#itemsResponse').html('');
    $('#itemsResponse').html(responseText);
});

/**
 * Put chosen items into basket
 * */
function putIntoBasket() {
     var items = [];
     /**
      * Test data!!! Uncomment to check how it works.
      * This is what was talking about(No file paths, this data exists just here on frontend to check css marking!!!).
      * Also this is for redirect checking after buy(redirect success or failure html according to the buy result).
      * */
    //items = [{name: "Test!", price: '0$', code: 11111 }];

    $("[class='items-check']").each(function (index, data) {
        if (data.checked) {
            items.push({ name: data.name, price: data.value, code: data.id });
        }
    });

    document.location.href = window.encodeURI(apiUrl + '/shop/basket?items=' + JSON.stringify(items));
}

/**
 * Init basket page with items
 * */
function onBasketLoad() {
    var items = JSON.parse(window.decodeURI(document.location.href).split('?')[1].split('=')[1]);
    var codes = [];
    items.forEach(function (item) {
        codes.push(Number(item.code))
    });

    $.post(apiUrl + '/shop/basket', {'codes': codes}, function (itemGaps) {
        items.forEach(function (item) {
            var wrongCode = itemGaps.includes(item.code);
            $('#basket')
                .append($("<tr>")
                    .append($("<td>").append(item.name).append("</td>").attr({name: 'Name', scope: "row"}))
                    .append($("<td>").append(item.code).append("</td>").attr({name: 'Vendor code'}))
                    .append($("<td>").append(item.price).append("</td>").attr({name: 'Price'}))
                .append("</tr>").attr({
                    style: wrongCode ? "color: red" : "color: blue",
                    name: wrongCode ? "wrong-items" : "items-buy",
                    id: item.code
                }))
        });
    });
}

/**
 * Goes back to shop page
 * */
function goBack(){
    document.location.href = apiUrl + '/shop';
}

/**
 * Sends items on server side
 * */
function buy() {
    var codes = [];
    $("[name='items-buy']").each(function (index, data) {
        codes.push(Number(data.id));
    });
    $.post(apiUrl + '/buyService', {'codes': codes},
        function (data) {
            console.log(data);
            document.location.href = apiUrl + '/shop/success';
        }).fail(function() {
        document.location.href = apiUrl + '/shop/failure';
    });
}
