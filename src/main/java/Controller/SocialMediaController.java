package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import kotlin._Assertions;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerUser);
        app.post("/login", this::verifyLogin);
        app.post("/messages", this::createMessage);

        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.get("/accounts/{account_id}/messages", this::getUserMessages);
        
        app.delete("/messages/{message_id}", this::deleteMessageById);

        app.patch("/messages/{message_id}", this::editMessageById);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException 
     * @throws JsonMappingException 
     */
    private void registerUser(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if (addedAccount != null) {
            ctx.json(mapper.writeValueAsString(addedAccount));
        }
        else {
            ctx.status(400);
        }
    }

    private void verifyLogin(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account checkedAccount = accountService.checkAccount(account);
        if (checkedAccount != null) {
            ctx.json(mapper.writeValueAsString(checkedAccount));
        }
        else {
            ctx.status(401);
        }
    }

    private void createMessage(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if (addedMessage != null) {
            ctx.json(mapper.writeValueAsString(addedMessage));
        }
        else {
            ctx.status(400);
        }
    }

    private void getAllMessages(Context ctx) {
        List<Message> messages = messageService.getMessages();
        ctx.json(messages);
    }

    private void getMessageById(Context ctx) {
        int message_id = Integer.valueOf(ctx.pathParam("message_id"));
        Message message = messageService.getMessage(message_id);
        if (message != null) {
            ctx.json(message);
        }
        else {
            ctx.json("");
        }
    }

    private void deleteMessageById(Context ctx) {
        int message_id = Integer.valueOf(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessage(message_id);
        if (message != null) {
            ctx.json(message);
        }
        else {
            ctx.json("");
        }
    }

    private void editMessageById(Context ctx) throws JsonMappingException, JsonProcessingException {
        int message_id = Integer.valueOf(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message received_message = mapper.readValue(ctx.body(), Message.class);
        Message message = messageService.updateMessage(message_id, received_message.getMessage_text());
        if (message != null) {
            ctx.json(message);
        }
        else {
            ctx.status(400);
        }
    }

    private void getUserMessages(Context ctx) {
        int account_id = Integer.valueOf(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountId(account_id);
        ctx.json(messages);
    }

}