package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserIdHandler);

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
            
            if(account.getUsername() == null || account.getUsername().isBlank() || account.getPassword().length() < 4) {
                context.status(400);
            }
            else {
                Account newAccount = accountService.addAccount(account);
                if(newAccount != null){
                    context.json(mapper.writeValueAsString(newAccount));
                    return;
                }
            }
            
        } catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }
        context.status(400);        
    }

    private void userLoginHandler(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Account account = mapper.readValue(context.body(), Account.class);
            Account result = accountService.loginAccount(account);
            if(result == null){
                context.status(401);
            }
            else{
                context.json(mapper.writeValueAsString(result));                
            }
        } catch(JsonProcessingException e){
            context.status(400);
        }

    }

    private void createMessageHandler(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Message message = mapper.readValue(context.body(), Message.class);
            boolean userExists = accountService.accountExist(message.getPosted_by());
            if(userExists){
                Message insertedMessage = messageService.addMessage(message);
                if(insertedMessage != null){
                    context.json(mapper.writeValueAsString(insertedMessage));
                    return;
                }
            }
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
        context.status(400);
    }
    
    public void retrieveAllMessagesHandler(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Message> messages = messageService.getAllMessages();
            context.json(mapper.writeValueAsString(messages));
            return;
        } catch (JsonProcessingException e){
            System.out.println(e.getMessage());
        }
        context.status(400);
    }

    public void retrieveMessageByIdHandler(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            int message_id = Integer.parseInt(context.pathParam("message_id"));
            Message message = messageService.getMessageById(message_id);
            if(message != null){
                context.json(mapper.writeValueAsString(message));
            }
            else{
                context.status(200);
            }
            return;
        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }
        catch(NumberFormatException e){
            System.out.println(e.getMessage());
        }
        context.status(400);
    }

    private void deleteMessageByIdHandler(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            int message_id = Integer.parseInt(context.pathParam("message_id"));
            Message deleted_message = messageService.deleteMessageById(message_id);
            if(deleted_message != null){
                context.json(mapper.writeValueAsString(deleted_message));
            }
            else{
                context.status(200);
            }
            return;
        } catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }
        catch(NumberFormatException e){
            System.out.println(e.getMessage());
        }
        context.status(400);
    }

    private void updateMessageHandler(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            int message_id = Integer.parseInt(context.pathParam("message_id"));
            Message message = mapper.readValue(context.body(), Message.class);
            Message updated_message = messageService.updateMessageById(message_id, message.getMessage_text());
            if(updated_message != null){
                context.json(mapper.writeValueAsString(updated_message));
            }
            else{
                context.status(400);
            }
            return;
        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }
        catch(NumberFormatException e){
            System.out.println(e.getMessage());
        }
        context.status(400);
    }

    private void getMessagesByUserIdHandler(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            int user_id = Integer.parseInt(context.pathParam("account_id"));
            List<Message> messages = messageService.getMessagesByUser(user_id);
            context.json(mapper.writeValueAsString(messages));
            return;
        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }
        catch(NumberFormatException e){
            System.out.println(e.getMessage());
        }
        context.status(400);
    }

}