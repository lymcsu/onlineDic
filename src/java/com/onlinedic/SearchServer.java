package com.onlinedic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class SearchServer extends Server {

    @Override
    public void run(HttpServletRequest request) {

        try {
            String type = _request_obj.getString("type");
            if (type.equals("SEARCH_WORD")) {
                searchWord(request);
            } else if (type.equals("SEARCH_BOOK")) {
            }
        } catch (JSONException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void searchWord(HttpServletRequest request) {
        try {
            _connection.setAutoCommit(false);
            String sql = "select meaning,lx,Word from words where Word=? ";
            PreparedStatement st = _connection.prepareStatement(sql);
            String data = _request_obj.getString("inputwords");
            st.setString(1, data);

            
            ResultSet rs = st.executeQuery();
            boolean isexist = false;
            JSONObject content = new JSONObject();
            if (rs.next()) {
                content.put("meaning", rs.getString(1));
                content.put("lx", rs.getString(2));
                isexist = true;
                saveIntoNotebook(rs.getString(1),rs.getString(3), request);
            }
            _response_obj.put("success", isexist);
            _response_obj.put("message", content);
            _connection.commit();

        } catch (Exception ex) {
            try {
                _response_obj.put("success", false);
                _response_obj.put("message", ex.getMessage());

            } catch (JSONException ex1) {

            }
        }
    }



    private void saveIntoNotebook(String meaning,String str, HttpServletRequest request) {
        try {
            String sql = "INSERT INTO notebook(username,english,meaning) VALUES (?,?,?)";
            PreparedStatement sta = _connection.prepareStatement(sql);
            if (request.getSession().getAttribute("user") != null) {
                sta.setString(1, request.getSession().getAttribute("user").toString());
                sta.setString(2, str);
                sta.setString(3, meaning);
                sta.executeUpdate();
                _connection.commit();
            }
        } catch (SQLException ex) {

        }
    }

}
