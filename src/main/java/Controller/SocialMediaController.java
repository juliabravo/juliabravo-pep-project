package Controller;

import java.util.ArrayList;
import java.util.List;

import DAO.MessageDAO;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService(new DAO.AccountDAO());
        this.messageService = new MessageService(new MessageDAO(), accountService);
    }



    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::createAccount);
        app.post("/login", this::login);

        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/accounts/{account_id}/{messages}", this::getMessagesByAccountId);
        app.get("/messages/{message_id}", this::getMessageById);
        app.patch("/messages/{message_id}", this::updateMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageById);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void createAccount(Context context) {
        Account account = context.bodyAsClass(Account.class);
        Account createdAccount = accountService.createAccount(account);

        if(createdAccount != null) {
            context.status(200).json(createdAccount);
        } else {
            context.status(400).json("");
        }    
    }

    private void login(Context context) {
        Account login = context.bodyAsClass(Account.class);
        String username = login.getUsername();
        String password = login.getPassword();
       
        //login
        Account account = accountService.login(username, password);


        if(account != null) {
            context.status(200).json(account);
        } else {
            context.status(401).json("");
        }
    }

    private void createMessage(Context context) {
        Message message = context.bodyAsClass(Message.class);
        Message createdMessage = messageService.createMessage(message);

        if(!accountService.doesUserExist(message.getPosted_by())) {
            context.status(400).json("User does not exist.");
        }
        
        if(createdMessage != null) {
            context.status(200).json(createdMessage);
        } else {
            context.status(400).json("");
        }
    }

    private void getAllMessages(Context context) {
        context.json(messageService.getAllMessages());
    }

    private void getMessagesByAccountId(Context context) {
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountId(account_id);

        if (messages.isEmpty()) {
            context.status(200).json(new ArrayList<>());
        } else {
            context.status(200).json(messages);
        }
    }

    private void getMessageById(Context context) {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(message_id);

        if(message != null){
            context.status(200).json(message);
        } else {
            context.status(200).json("");
        }
    }

    private void updateMessageById(Context context) {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        String message_text = context.bodyAsClass(Message.class).getMessage_text();
        Message message = messageService.updateMessageById(message_id, message_text);

        if(message != null) {
            context.status(200).json(message);
        } else {
            context.status(400).json("");
        }
    }

    private void deleteMessageById(Context context){
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.deleteMessageById(message_id);

        if(message != null) {
            context.status(200).json(message);
        } else {
            context.status(200).json("");
        }
    }
}