
package com.onlinedic;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class Server {

    protected JSONObject _request_obj = null;
    protected JSONObject _response_obj = null;
    protected Connection _connection = null;

    public Server() {
        try {
            _response_obj = new JSONObject();
            _response_obj.put("success", false);
            _response_obj.put("message", "Unknown error!");
        } catch (JSONException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run(HttpServletRequest request) {

    }

    public JSONObject getResponse() {
        return _response_obj;
    }

    void openDB(String host, String port, String dbname, String user, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + host + "/" + dbname;
            Properties props = new Properties();
            props.setProperty("user", user);
            props.setProperty("password", password);
            _connection = DriverManager.getConnection(url, props);
        } catch (Exception ex) {
            try {
                _response_obj.put("success", false);
                _response_obj.put("message", ex.getMessage());

            } catch (JSONException ex1) {

            }
        } 
    }

    void closeDB() {
        try {
            _connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setRequest(JSONObject reqobj) {
        _request_obj = reqobj;
    }
}
