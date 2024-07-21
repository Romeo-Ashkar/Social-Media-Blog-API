package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generated_message_id = (int)pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }
        catch(SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch(SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return messages;
    }

    public Message getMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        Message message = new Message();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                message.setMessage_id(message_id);
                message.setPosted_by(rs.getInt("posted_by")); 
                message.setMessage_text(rs.getString("message_text"));
                message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                return message;
            }
        }
        catch(SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    public Message deleteMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        Message message = getMessageById(message_id);
        try {
            String sql = "DELETE FROM message WHERE message_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message_id);
            preparedStatement.executeUpdate();
        }
        catch(SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return message;
    }

    public Message editMessageById(int message_id, String message_text) {
        Message old_message = getMessageById(message_id);
        if (old_message != null) {
            Connection connection = ConnectionUtil.getConnection();
            try {
                String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
                
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, message_text);
                preparedStatement.setInt(2,message_id);

                preparedStatement.executeUpdate();
                Message message = new Message(message_id, old_message.getPosted_by(), message_text, old_message.getTime_posted_epoch());
                return message;
            }
            catch(SQLException exception) {
                System.out.println(exception.getMessage());
            }
        }
        return null;
    }

    public List<Message> getUserMessages(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<Message>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                Message message = new Message();
                message.setMessage_id(rs.getInt("message_id"));
                message.setPosted_by(account_id); 
                message.setMessage_text(rs.getString("message_text"));
                message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch(SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return messages;
    }
}
