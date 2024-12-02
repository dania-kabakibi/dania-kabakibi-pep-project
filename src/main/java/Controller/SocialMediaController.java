package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.AccountDAO;
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
        //app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::userRegistrationHandler);
        app.post("/login", this::userLoginHandler);
        app.post("/messages",this::createMessageHandler);
        app.get("/messages", this::retrieveAllMessagesHandler);
        app.get("/messages/{message_id}", this::retrieveMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    // private void exampleHandler(Context context) {
    //     context.json("sample text");
    // }


    private void userRegistrationHandler(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            Account account = mapper.readValue(context.body(), Account.class);
            Account newAccount = accountService.addAccount(account);

            if(account.getUsername() == null || account.getUsername().isBlank() || account.getPassword().length() < 4) {
                context.status(400);
            }
            else {
                context.json(mapper.writeValueAsString(newAccount));                
            }
            
        } catch(JsonProcessingException e){
            context.status(400);
        }
        
    }

    private void userLoginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account loginRequest = mapper.readValue(context.body(), Account.class);
        if (loginRequest.getUsername() == null || loginRequest.getUsername().isBlank() || 
            loginRequest.getPassword() == null || loginRequest.getPassword().isBlank()) {
            context.status(400);
            return;
        }

        // Account storedAccount = accountDAO.findAccountByUsername(loginRequest.getUsername());
        // if (storedAccount != null && storedAccount.getPassword().equals(loginRequest.getPassword())) {
        //     context.json(storedAccount).status(200);
        // } else {
        //     context.status(401);
        // }

    }

    private void createMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
    
        try {
            Message message = mapper.readValue(context.body(), Message.class);
    
            if (message.getMessage_text() == null || message.getMessage_text().isBlank()) {
                context.status(400).result("Message text cannot be blank.");
                return;
            }
    
            if (message.getMessage_text().length() > 255) {
                context.status(400).result("Message text cannot exceed 255 characters.");
                return;
            }
    
            Message newMessage = messageService.AddMessage(message);
    
            context.json(newMessage);
    
        } catch (JsonProcessingException e) {
            context.status(400).result("Invalid JSON format.");
        } catch (Exception e) {
            context.status(500).result("An unexpected error occurred.");
        }
    }
    
    public void retrieveAllMessagesHandler(Context context) {
        try {
            List<Message> messages = messageService.getAllMessages();
    
            context.json(messages);
    
        } catch (Exception e) {
            context.status(500);
        }
    }

    public void retrieveMessageByIdHandler(Context context) {
        // int messageId = Integer.parseInt(context.pathParam("message_id"));
        // Message message = messageDAO.getMessageById(messageId);

        // if (message != null) {
        //     context.json(message);
        // } else {
        //         context.status(200);
        //         context.result("");
        // }
    }

    private void deleteMessageByIdHandler(Context context) {
        // int messageId = Integer.parseInt(context.pathParam("message_id"));
        // List<Message> messages = messageService.getAllMessages();
        // if (messageDAO.getMessageById(messageId) != null) {
        //     messages.remove(messageId);
        //     context.status(204);
        // } else {
        //     context.status(404);
        // }
    }

    private void updateMessageHandler(Context context) {
    //     int messageId = Integer.parseInt(context.pathParam("message_id"));
    //     String newMessageText = context.body();
    //     if (newMessageText == null || newMessageText.isEmpty() || newMessageText.length() > 255) {
    //         context.status(400);
    //         return;
    //     }
    //     Message message = messageDAO.getMessageById(messageId);
    // if (message == null) {
    //     context.status(400);
    //     return;
    // }
    // messageDAO.updateMessage(message);
    // context.json(message);

    }

    private void getAllMessagesHandler(Context context) {
        // int accountId = Integer.parseInt(context.pathParam("account_id"));
        // List<Message> messages = messageDAO.findByAccountId(accountId);
        // context.json(messages);
    }

}