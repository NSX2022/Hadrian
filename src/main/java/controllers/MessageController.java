package controllers;

import app.App;
import jexer.*;
import jexer.backend.Screen;
import models.Chat;
import models.Screens;
import models.User;
import utils.Logging;

import java.util.ArrayList;
import java.util.logging.Level;

public class MessageController extends AbstractController {
    private final User user;
    
    public MessageController(TWindow root, User user) {
        super(root, Screens.MESSAGES);
        this.user = user;
    }
    
    @Override
    protected void show() {
        Screen screen = root.getScreen();
        int menuX = 4, menuY = 5, width = screen.getWidth(), height = screen.getHeight();
        
        root.addButton(
                "< Back",
                menuX, menuY,
                new TAction() {
                    @Override
                    public void DO() {
                        App.changeScreen(MessageController.this, Screens.HOME);
                        Logging.log("Returning To Home Page", Level.INFO);
                    }
                }
        );
        
        // region Chats
        TVScroller scrollView = new TVScroller(
                root,
                1, menuY + 2,
                (int) ((height - menuY - 2) * .8f)
        );
        scrollView.setActive(true);
        
        TPanel content = scrollView.addPanel(
                0, 0,
                scrollView.getWidth() - 1,
                1  // will grow with content addition
        );
        
        ArrayList<Chat> chats = new ArrayList<>(user.getChats());
        int panelWidth = content.getWidth(), panelHeight = 3, y = 0;
        for (int i = 0; i < chats.size(); i++) {
            Chat chat = chats.get(i);
            
            TPanel panel = content.addPanel(
                    menuX, y,
                    panelWidth, panelHeight
            );
            
            StringBuilder members = new StringBuilder();
            ArrayList<User> users = new ArrayList<>(chat.getUsers());
            for (int j = 0; j < users.size(); j++) {
                members.append(users.get(j).getUsername());
                
                if (j != users.size())
                    members.append(',');
            }
            
            panel.addLabel("Members: " + members, 2, 1);
            panel.addLabel("Last Message Preview: " + chat.getMessages().getLast(), 2, 2);
            
            int finalI = i;
            panel.addButton(
                    "> Open",
                    2, 3,
                    new TAction() {
                        @Override
                        public void DO() {
                            toChat(finalI);
                        }
                    }
            );
            
            y += panelHeight + 1;
        }
        
        content.setHeight(y);  // TODO get chats to scroll
        // endregion
        
        
        // region New Chat Panel
        TPanel newChatPanel = root.addPanel(
                (int) (width * .75f),
                menuY + 2,
                (int) (width * .25f) - 2,
                (int) ((height - menuY - 2) * .8f)
        );
        int fieldWidth = newChatPanel.getWidth() - 1, newChatPanelHeight = newChatPanel.getHeight();
        
        newChatPanel.addLabel(
                "Send Message:",
                1, 1
        );
        
        TField ips = newChatPanel.addField(
                1, 2,
                fieldWidth,
                false,
                "IP..."
        );
        
        TField port = newChatPanel.addField(
                1, 3,
                fieldWidth,
                false,
                "Port..."
        );
        
        TText messageBox = newChatPanel.addText(
                "Message...",
                1, 4,
                fieldWidth,
                newChatPanelHeight - 5
        );
        
        newChatPanel.addButton(
                "Send Message",
                1, newChatPanelHeight - 1,
                new TAction() {
                    @Override
                    public void DO() {
                        String[] receivers = ips.getText().strip().split(", ");
                        if (receivers.length == 0) {
                            displayNotif("At Least 1 Receiver Is Required For New Chat");
                            Logging.log("Tried To Create New Chat With No Receivers", Level.WARNING);
                            return;
                        } if (port.getText().isBlank() || messageBox.getText().isBlank()) {
                            displayNotif("All Text Boxes Must Be Filled In");
                            Logging.log("Tried To Create New Chat Without Filling In All Boxes", Level.WARNING);
                            return;
                        }
                        
                        ArrayList<User> members = new ArrayList<>();
                        for (String ip : ips.getText().split(","))
                            members.add(getUserFromServer(ip)); // ???
                        members.add(user);
                        
                        Chat newChat = new Chat(
                                chats.size(),
                                members,
                                messageBox.getText().strip()
                        );
                        // TODO chat creation and message send action
                    }
                }
        );
        // endregion
    }
    
    private void toChat(int chatId) {
        // TODO switch to chat ???
//        try {
//            App.changeScreen(MessageController.this, Screens.CHAT);
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
    }
}
