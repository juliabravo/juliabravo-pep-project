package Service;

import java.util.*;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountService accountService;

    public MessageService(MessageDAO messageDAO, AccountService accountService) {
        this.accountService = accountService;
        this.messageDAO = messageDAO;
    }

    public Message createMessage(Message message) {
        if(message == null || message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            return null;
        }

        if(!accountService.doesUserExist(message.getPosted_by())){
            return null;
        }
        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }

    public Message deleteMessageById(int message_id){
        return messageDAO.deleteMessageById(message_id);
    }

    public Message updateMessageById(int message_id, String message_text) {
        if(message_text == null || message_text.isBlank() || message_text.length() > 255) {
            return null;
        }
        return messageDAO.updateMessageById(message_id, message_text);
    }

    public List<Message> getMessagesByAccountId(int account_id) {
        return messageDAO.getMessagesByAccountId(account_id);
    }

}
