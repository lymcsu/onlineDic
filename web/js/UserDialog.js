UserDialog = function (opts) {
    var me = this;
    me.opts = $.extend({
        width: 250,
        height:190
    }, opts);
    me._init();
};

UserDialog.prototype._init = function () {
    var me = this;

    if (!me.opts.div) {
        me.div = $("<div name='register'>").appendTo($("body"));
    } else {
        me.div = $("#" + me.opts.div);
    }

    me.usernameID = $.uuid();
    me.passwordID = $.uuid();
    me.password2ID = $.uuid();

    var html =
            '   <table style="width:100%;">'
            + '    <tr><td>用户名</td><td><input type="text" id="' + me.usernameID + '" style="width:100%;box-sizing: border-box;"/></td></tr>'
            + '    <tr><td>密码</td><td><input type="password" id="' + me.passwordID + '" style="width:100%;box-sizing: border-box;"/></td></tr>'
            + '    <tr><td>确认密码</td><td><input type="password" id="' + me.password2ID + '" style="width:100%;box-sizing: border-box;"/></td></tr>'
//            + '    <tr><td></td><td><button>Register</button><button>Login</button></td></tr>'
            + ' </table>';
    me.div.html(html);

    me.div.dialog({
        width: me.opts.width,
        height: me.opts.height,
        title: "欢迎注册",
        close: function () {
            me.div.remove();
        },
        buttons: [
            {
                text: "取消",
                click: function () {
                    me.div.dialog("close");
                }
            }, {
                text: "确定",
                click: function () {
                    var user = me.val();
                    if ($.trim(user.username) === "" || $.trim(user.password) === "") {
                        return;
                    }
                    if (user.password !== user.password2) {
                        alert("两次输入的密码不一致，请重新输入");
                        return;
                    }

                    if (me.opts.accept) {
                        me.opts.accept(user);
                    }
                }
            }
        ]
    });
};

UserDialog.prototype.val = function () {
    var me = this;
    return {
        username: $("#" + me.usernameID).val(),
        password: $("#" + me.passwordID).val(),
        password2: $("#" + me.password2ID).val()
    }
};


UserDialog.prototype.close = function () {
    var me = this;
    me.div.dialog("close");
};



