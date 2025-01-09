package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.*;

public class MessageDAO {
    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            //SQL
            String sql = "INSERT INTO message (message_text, posted_by, time_posted_epoch) VALUES (?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1,message.getMessage_text());
            preparedStatement.setInt(2, message.getPosted_by());
            preparedStatement.setLong(3,message.getTime_posted_epoch());

            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next()) {
                message.setMessage_id((rs.getInt(1)));
                return message;
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            //SQL
            String sql = "SELECT * FROM message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getInt("time_posted_epoch")
                );
                messages.add(message);
            }


        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    public Message getMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;
        
        try {
            //SQL
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1,message_id);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()) {
                message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }

    public Message deleteMessageById(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;

        try {
            //SQL
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1,message_id);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
            }

            if(message != null) {
                String sqlDelete = "DELETE FROM message WHERE message_id = ?;";
                PreparedStatement deletePreparedStatement = connection.prepareStatement(sqlDelete);

                deletePreparedStatement.setInt(1,message_id);
                deletePreparedStatement.executeUpdate();

            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }

    public Message updateMessageById(int message_id, String message_text) {
        Connection connection = ConnectionUtil.getConnection();
        Message newMessage = null;

        // if(message_text == null || message_text.isBlank() || message_text.length() > 255 ) {
        //     return null;
        // }

        try {
            //SQL 
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();
            
            if(rs.next()){
                int posted_by = rs.getInt("posted_by");
                long time_posted_epoch = rs.getLong("time_posted_epoch"); 


                String sqlUpdate = "UPDATE message SET message_text = ? WHERE message_id = ?;";
                PreparedStatement updatePreparedStatement = connection.prepareStatement(sqlUpdate);

                updatePreparedStatement.setString(1, message_text);
                updatePreparedStatement.setInt(2,message_id);

                int rows = updatePreparedStatement.executeUpdate();
                if(rows > 0) {
                    newMessage = new Message(
                        message_id,
                        posted_by,
                        message_text,
                        time_posted_epoch
                    );
                }
            } 


        } catch(SQLException e){
            System.out.println(e.getMessage());
        }


        return newMessage;
    }

    public List<Message> getMessagesByAccountId(int account_id) {
        List<Message> messages = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();

        try {
            //SQL
            String sql = "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, account_id);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
