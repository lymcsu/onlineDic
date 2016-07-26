$(document).ready(function () {
    createDiv();
    Outputcase(document.getElementById("Dict"), document.body);
});

var createDiv = function () {
    div = $("<div id='Dict' style = 'border-radius:5px; box-shadow: 5px 5px 4px #888888 ; display:none; position:absolute; cursor:pointer;border:1px solid #ccc;width: 160px;height: 100px;background-color:#eee'></div>").appendTo($("body"));
    var html = $("#Dict").html();
    alert(html);
    $("#Dict").html(html + "<div id = 'engdiv' style = 'width:160px; height:20px;border:1px solid #ccc;'></div><div id = 'chidiv' style = 'width:150px; height:90px;'></div>");
};
var Outputcase = function (content, eleContainer) {
    eleContainer = eleContainer || document;
    var GetSelectTxt = function () {
        var txt = "";
        if (document.selection) {
            txt = document.selection.createRange().text;
        } else {
            txt = document.getSelection();
        }
        return txt.toString();
    };
    var Getinput = function () {
        var request = {
            type: "SEARCH_WORD",
            inputwords: $.trim(GetSelectTxt())
        };

        $.ajax({
            type: "GET",
            async: false,
            url: "http://localhost:8084/onlineDic/dicServlet",
            dataType: "jsonp",
            contentType: "application/jsonp; charset=utf-8",
            jsonp: "callback",
            jsonpCallback: "callBackFun",
            data: {
                "request": JSON.stringify(request)
            },
            success: function (data) {
                callBackFun(data);
            },
            error: function () {
                var html = GetSelectTxt() + "<meta charset='UTF-8'>" + "<button type='button'id='wordsbook' onclick='Login()' style='border:0px solid #fff ; float: right; width: 80px;height: 20px; cursor: pointer;display:block;font-family:黑体'>我的单词本</button>";
                $("#engdiv").html(html);
                var html2 = "对不起，这个词找不到&nbsp;&nbsp; -_-|||，"
                        + "  <a href='http://dict.youdao.com/search?q=" + GetSelectTxt() +
                        "'>有道一下</a>";
                $("#chidiv").html(html2);
            }
        });
    };

    function callBackFun(data) {
        var html = GetSelectTxt() + "<meta charset='UTF-8'>" + "<button type='button'id='wordsbook' onclick='Login()' style='border:0px solid #fff ; float: right; width: 80px;height: 20px; cursor: pointer;display:block;font-family:黑体'>我的单词本</button>";
        alert($('#engdiv').html());
        $("#engdiv").html(html);
        var html2 = data.meaning;
        $("#chidiv").html(html2);
    }
    eleContainer.onmouseup = function (e) {
        e = e || window.event;
        var txt = GetSelectTxt(), sh = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0;
        var left = (e.clientX - 100 < 0) ? e.clientX + 10 : e.clientX + 10, top = (e.clientY - 40 < 0) ? e.clientY + sh + 20 : e.clientY + sh + 10;
        if (txt) {
            content.style.display = "inline";
            content.style.left = left + "px";
            content.style.top = top + "px";
            Getinput();
        } else {
            content.style.display = "none";
        }
    };
    return GetSelectTxt();
};

