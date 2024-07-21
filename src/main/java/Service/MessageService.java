package Service;

import Model.Message;

import java.util.List;

import DAO.MessageDAO;


public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message addMessage(Message message) {
        String message_text = message.getMessage_text();

        if ((message_text.length() == 0)||(message_text.length() > 255)) {
            return null;
        }
        else {
            return messageDAO.insertMessage(message);
        }
    }

    public List<Message> getMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessage(int message_id) {
        return messageDAO.getMessageById(message_id);
    }

    public Message deleteMessage(int message_id) {
        return messageDAO.deleteMessageById(message_id);
    }

    public Message updateMessage(int message_id, String message_text) {
        if ((message_text.length() == 0)||(message_text.length() > 255)) {
            return null;
        }
        else {
            return messageDAO.editMessageById(message_id, message_text);
        }
    }

    public List<Message> getMessagesByAccountId(int account_id) {
        return messageDAO.getUserMessages(account_id);
    }
}
