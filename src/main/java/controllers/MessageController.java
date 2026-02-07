package controllers;

import app.App;
import jexer.*;
import jexer.backend.Screen;
import models.Chat;
import models.Screens;
import models.User;
import utils.Logging;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;

public class MessageController extends AbstractController {
    private final User user;
    
    public MessageController(TWindow root, User user) {
        super(root, Screens.MESSAGES);
        this.user = user;
    }
    
    @Override
    protected void show(TWindow root) {
        int menuX = 4, menuY = 5;
        Screen screen = root.getScreen();
        int width = screen.getWidth(), height = screen.getHeight();
        
        new TButton(
                root,
                "< Back",
                menuX, menuY,
                new TAction() {
                    @Override
                    public void DO() {
                        try {
                            App.changeScreen(MessageController.this, Screens.HOME);
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
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
        
        TPanel content = new TPanel(
                scrollView,
                0, 0,
                scrollView.getWidth() - 1,
                1  // will grow with content addition
        );
        
        ArrayList<Chat> chats = new ArrayList<>(user.getChats());
        int panelWidth = content.getWidth(), panelHeight = 3, y = 0;
        for (int i = 0; i < chats.size(); i++) {
            TPanel panel = new TPanel(
                    content,
                    menuX, y,
                    panelWidth, panelHeight
            );
            
            StringBuilder members = new StringBuilder();
            ArrayList<User> users = new ArrayList<>(chats.get(i).getUsers());
            for (int j = 0; j < users.size(); j++) {
                members.append(users.get(j).getUsername());
                
                if (j != users.size())
                    members.append(',');
            }
            
            new TLabel(panel, "Members: " + members, 2, 1);
            new TLabel(panel, "Last Message Preview: " + chats.get(i).getMessages().getLast(), 2, 2);
            
            int finalI = i;
            new TButton(
                    panel,
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
        TPanel newChatPanel = new TPanel(
                root,
                (int) (width * .75f),
                menuY + 2,
                (int) (width * .25f) - 2,
                (int) ((height - menuY - 2) * .8f)
        );
        
        new TLabel(
                newChatPanel,
                "Send Message:",
                1, 1
        );
        
        TField ips = new TField(
                newChatPanel,
                1, 2,
                newChatPanel.getWidth() - 1,
                false,
                "IP..."
        );
        
        TField port = new TField(
                newChatPanel,
                1, 3,
                newChatPanel.getWidth() - 1,
                false,
                "Port..."
        );
        
        TText messageBox = new TText(
                newChatPanel,
                "Message...",
                1, 4,
                newChatPanel.getWidth() - 1,
                newChatPanel.getHeight() - 5
        );
        
        new TButton(
                newChatPanel,
                "Send Message",
                1, newChatPanel.getHeight() - 1,
                new TAction() {
                    @Override
                    public void DO() {
                        ArrayList<User> members = new ArrayList<>();
                        for (String ip : ips.getText().split(","))
                            members.add(getUserFromServer(ip)); // ???
                        members.add(user);
                        
                        Chat newChat = new Chat(
                                chats.size(),
                                members,
                                messageBox.getText()
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
