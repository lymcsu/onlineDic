LoginDialog = function (opts) {
    var me = this;
    me.opts = $.extend({
        width: 250,
        height: 180
    }, opts);
    me._init();
};

LoginDialog.prototype._init = function () {
    var me = this;

    if (!me.opts.div) {
        me.div = $("<div name='login'>").appendTo($("body"));
    } else {
        me.div = $("#" + me.opts.div);
    }
    me.usernameID = $.uuid();
    me.passwordID = $.uuid();
    var html =
            '   <table style="width:100%;">'
            + '    <tr><td>用户名</td><td><input type="text" id="' + me.usernameID + '" style="width:100%;box-sizing: border-box;"/></td></tr>'
            + '    <tr><td>密码</td><td><input type="password" id="' + me.passwordID + '" style="width:100%;box-sizing: border-box;"/></td></tr>'
            //            + '    <tr><td></td><td><button>Register</button><button>Login</button></td></tr>'
            + ' </table>';
    me.div.html(html);

    me.div.dialog({
        width: me.opts.width,
        height: me.opts.height,
        title: "欢迎登录我的单词本",
        close: function () {
            me.div.remove();
        },
        buttons: [
            {
                text: "注册",
                click: function () {
                    var dlg = new UserDialog({
                        accept: function (val) {
                            var request = {
                                type: "USER_REGISTER",
                                data: val
                            };
                            connect("dicServlet", request, function (json) {
                                dlg.close();
                            });
                        }
                    });
                }
            }, {
                text: "登录",
                click: function () {
                    connect("dicServlet", {
                        type: "USER_LOGIN",
                        data: me.val()
                    }, function (data) {
                        alert("登陆成功");
                        var userhtml = "欢迎您，" + data.user;
                        $("#user").html(userhtml);
                        var username = document.getElementById("user");
                        username.style.display = "inline";
                        var lgin = document.getElementById("lgin");
                        lgin.style.display = "none";
                        var lgout = document.getElementById("lgout");
                        lgout.style.display = "inline";
                        me.close();
                    });
                }
            }
        ]
    });
};



LoginDialog.prototype.close = function () {
    var me = this;
    me.div.dialog("close");
};


LoginDialog.prototype.val = function () {
    var me = this;
    return {
        username: $("#" + me.usernameID).val(),
        password: $("#" + me.passwordID).val()
    }
};