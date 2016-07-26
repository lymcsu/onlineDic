package com.onlinedic;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.PageContext;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class UserServer extends Server {

    @Override
    public void run(HttpServletRequest request) {

        try {
            String type = _request_obj.getString("type");
            if (type.equals("USER_REGISTER")) {
                register();
            } else if (type.equals("USER_LOGIN")) {
                login(request);
            } else if (type.equals("USER_ISLOGIN")) {
                isLogin(request);
            } else if (type.equals("USER_LOGOUT")) {
                Logout(request);
            }
        } catch (JSONException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void register() {
        try {
            _connection.setAutoCommit(false);
            String sql = "insert into user(username,password) values(?,md5(?))";
            PreparedStatement st = _connection.prepareStatement(sql);
            JSONObject data = _request_obj.getJSONObject("data");
            st.setString(1, data.getString("username"));
            st.setString(2, data.getString("password"));

            st.executeUpdate();

            _connection.commit();

            _response_obj.put("success", false);
            _response_obj.put("message", "ok");
        } catch (Exception ex) {
            try {
                _response_obj.put("success", false);
                _response_obj.put("message", ex.getMessage());

            } catch (JSONException ex1) {

            }
        }

    }

    private void login(HttpServletRequest request) {
        try {
            _connection.setAutoCommit(false);
            String sql = "select count(1) from user where username=? and password=md5(?)";
            PreparedStatement st = _connection.prepareStatement(sql);
            JSONObject data = _request_obj.getJSONObject("data");
            st.setString(1, data.getString("username"));
            st.setString(2, data.getString("password"));

            ResultSet rs = st.executeQuery();
            rs.next();
            if (rs.getInt(1) == 1) {
                request.getSession().setAttribute("user", data.getString("username"));

                _response_obj.put("success", true);
                _response_obj.put("user",request.getSession().getAttribute("user"));
                _response_obj.put("message", "ok");

            } else {
                _response_obj.put("success", false);
                _response_obj.put("message", "账号或密码错误");
            }
            _connection.commit();

        } catch (Exception ex) {
            try {
                _response_obj.put("success", false);
                _response_obj.put("message", ex.getMessage());

            } catch (JSONException ex1) {

            }
        }
    }

    private void Logout(HttpServletRequest request) {
        try {
            request.getSession().removeAttribute("user");
            _response_obj.put("success", true);
            _response_obj.put("message", "1");
        } catch (Exception ex) {
            try {
                _response_obj.put("success", false);
                _response_obj.put("message", ex.getMessage());

            } catch (JSONException ex1) {

            }
        }
    }

    private void isLogin(HttpServletRequest request) {
        try {
            if (request.getSession().getAttribute("user") != null) {

                String content = notebook(request);
                _response_obj.put("success", true);
                _response_obj.put("message", "1");
                _response_obj.put("notebook", content);
                _response_obj.put("user", request.getSession().getAttribute("user"));
            } else {
                _response_obj.put("success", true);
                _response_obj.put("message", "0");
            }
        } catch (Exception ex) {
            try {
                _response_obj.put("success", false);
                _response_obj.put("message", ex.getMessage());

            } catch (JSONException ex1) {

            }
        }
    }

    private String notebook(HttpServletRequest request) {
        try {
            _connection.setAutoCommit(false);
            String sql = "select distinct english,meaning from notebook where username=?";
            PreparedStatement st = _connection.prepareStatement(sql);
            st.setString(1, request.getSession().getAttribute("user").toString());
            ResultSet rs = st.executeQuery();
            String content = "";
            while (rs.next()) {
                content += rs.getString("english");
                content += "_";
                content += rs.getString("meaning");
                content += "_";
            }
            _connection.commit();
            return content;
        } catch (SQLException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
            return "读取生词本失败";
        }
    }

}
