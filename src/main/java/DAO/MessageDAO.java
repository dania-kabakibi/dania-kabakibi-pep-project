package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                return message;
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getGeneratedKeys(); // to get the id of the inserted message
            if(resultSet.next()){
                int messageId = resultSet.getInt(1);   
                message.setMessage_id(messageId);
                return message;
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean deleteMessageById(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            
            int deleted_rows = preparedStatement.executeUpdate();
            return deleted_rows != 0;
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Message updateMessageById(Message message, String new_text){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, new_text);
            preparedStatement.setInt(2, message.getMessage_id());
            
            preparedStatement.executeUpdate();
            
            message.setMessage_text(new_text);
            return message;
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getMessagesbyPostedBy(int posted_by){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, posted_by);
            
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Message> messages = new ArrayList<>();

            while(resultSet.next()){
                int message_id = resultSet.getInt("message_id");
                String message_text = resultSet.getString("message_text");
                Long time_posted_epoch = resultSet.getLong("time_posted_epoch");
                messages.add(new Message(message_id, posted_by, message_text, time_posted_epoch));
            }
            return messages;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
