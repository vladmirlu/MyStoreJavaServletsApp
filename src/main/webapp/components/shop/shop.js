
/**
 * Api URL
 * */
var apiUrl = 'http://localhost:8080/mystore';

/**
 * Fetch all existing items
 * */
$.get(apiUrl + '/shop/items', function (items) {
    for (var i = 0; i < items.length; i++) {
        $('#itemsResponse')
            .append("<tr>")
            .append($("<td>").append(items[i].name).append("</td>").attr({name: 'Name', scope: "row"}))
            .append($("<td>").append(items[i].code).append("</td>").attr({name: 'Vendor code'}))
            .append($("<td>").append(items[i].price).append("</td>").attr({name: 'Price'}))
            .append($("<td>")
                .append($('<input>').attr({
                    type: 'checkbox',
                    class: 'items-check',
                    id: items[i].code,
                    name: items[i].name,
                    value: items[i].price
                })).append("</td>").attr({name: 'Choose'}))
            .append("</tr>");
    }
});

/**
 * Put chosen items into basket
 * */
function putIntoBasket() {
    // items = [];
    var items = [{name: "Test!", price: '0$', code: 11111 }];
    $("[class='items-check']").each(function (index, data) {
        if (data.checked) {
            items.push({ name: data.name, price: data.value, code: data.id });
        }
    });

    document.location.href = apiUrl + '/shop/basket?items=' + JSON.stringify(items);
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
        document.location.href = apiUrl + "/shop/failure";
    });
}
