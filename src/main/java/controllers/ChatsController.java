package controllers;

import app.App;
import models.Chat;
import models.ChatPanel;
import models.Screens;
import models.User;

import javax.swing.*;

public class ChatsController extends AbstractController implements Loadable {
    private JFrame appFrame;
    private JPanel contentPanel;
    private User user;
    private JButton backButton;
    private JPanel chatsPanel;
    
    public ChatsController(JFrame appFrame) {
        super(appFrame, Screens.CHATS);
        this.appFrame = appFrame;
        
        backButton.addActionListener(e -> App.changeScreen(Screens.HOME));
    }

    @Override
    public void load(User user) {
        this.user = user;
        
        for (int i = 0; i < user.getChats().size(); i++) {
            Chat chat = user.getChat(i);
            ChatPanel panel = new ChatPanel(appFrame, i, user, chat.getUsers(), chat.getLastMessage().text());
            chatsPanel.add(panel, i);
        }
    }
    
    @Override
    protected JPanel getContentPanel() {
        return contentPanel;
    }
    
//    @Override
//    protected void show() {
//        for (int i = 0; i < chats.size(); i++) {
//            Chat chat = chats.get(i);
//
//            TPanel panel = content.addPanel(
//                    menuX, y,
//                    panelWidth, panelHeight
//            );
//
//            StringBuilder memberIPs = new StringBuilder();
//            ArrayList<String> userIPs = new ArrayList<>(chat.getUsers());
//            for (int j = 0; j < userIPs.size(); j++) {
//                memberIPs.append(userIPs.get(j));
//
//                if (j != userIPs.size() - 1)
//                    memberIPs.append(',');
//            }
//
//            panel.addLabel("Members: " + memberIPs, 2, 1);
//            panel.addLabel("Last Message: " + chat.getMessages().getLast().text(), 2, 2);
//
//            int finalI = i;
//            panel.addButton(
//                    "> Open",
//                    2, 3,
//                    new TAction() {
//                        @Override
//                        public void DO() {
//                            toChat(finalI);
//                        }
//                    }
//            );
//
//            y += panelHeight + 1;
//        }
//
//        content.setHeight(y);  // TODO get chats to scroll
//        // endregion
//
//
//        // region New Chat Panel
//        TPanel newChatPanel = root.addPanel(
//                (int) (width * .75f),
//                menuY + 2,
//                (int) (width * .25f) - 2,
//                (int) ((height - menuY - 2) * .8f)
//        );
//        int fieldWidth = newChatPanel.getWidth() - 1, newChatPanelHeight = newChatPanel.getHeight();
//
//        newChatPanel.addLabel(
//                "Send Message:",
//                1, 1
//        );
//
//        TField ips = newChatPanel.addField(
//                1, 2,
//                fieldWidth,
//                false,
//                "IP..."
//        );
//
//        TField port = newChatPanel.addField(
//                1, 3,
//                fieldWidth,
//                false,
//                "Port..."
//        );
//
//        TText messageBox = newChatPanel.addText(
//                "Message...",
//                1, 4,
//                fieldWidth,
//                newChatPanelHeight - 5
//        );
//
//        newChatPanel.addButton(
//                "Send Message",
//                1, newChatPanelHeight - 1,
//                new TAction() {
//                    @Override
//                    public void DO() {
//                        String[] receivers = ips.getText().strip().split(", ");
//                        if (receivers.length == 0) {
//                            displayNotif("At Least 1 Receiver Is Required For New Chat");
//                            Logging.log("Tried To Create New Chat With No Receivers", Level.WARNING);
//                            return;
//                        } if (port.getText().isBlank() || messageBox.getText().isBlank()) {
//                            displayNotif("All Text Boxes Must Be Filled In");
//                            Logging.log("Tried To Create New Chat Without Filling In All Boxes", Level.WARNING);
//                            return;
//                        }
//
//                        ArrayList<String> members = new ArrayList<>(Arrays.asList(receivers));
//                        Chat newChat = new Chat(
//                                chats.size(),
//                                members,
//                                messageBox.getText().strip(),
//                                user
//                        );
//                        // TODO chat creation and message send action
//                    }
//                }
//        );
//        // endregion
//    }
}
