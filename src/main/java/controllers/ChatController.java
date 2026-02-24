package controllers;

import app.App;
import models.*;

import javax.swing.*;

public class ChatController extends AbstractController implements Loadable {
    private JPanel contentPanel;
    private User user;
    private final Chat data;
    private JButton backButton;
    private JLabel chatNameLabel;
    private JPanel messagesScrollPanel;
    private final DefaultListModel<String> memberModel;
    private JList<String> membersList;
    
    public ChatController(JFrame appFrame, Chat data) {
        super(appFrame, Screens.CHAT);
        this.data = data;
        
        memberModel = new DefaultListModel<>();
        membersList.setModel(memberModel);
        
        backButton.addActionListener(actionEvent -> App.changeScreen(Screens.CHATS));
    }

    @Override
    public void load(User user) {
        this.user = user;
        
        for (int i = 0; i < data.getMessages().size(); i++) {
            Message message = data.getMessages().get(i);
            MessagePanel panel = new MessagePanel(message.sender().getUsername(), message.text());
            messagesScrollPanel.add(panel, i);
        }
        
        for (String member : data.getUsers())
            memberModel.addElement(member);
    }
    
    @Override
    protected JPanel getContentPanel() {
        return contentPanel;
    }

//    private void sendMessage(String message) {
//        data.addMessage(message, user);
//        chatModel.addElement(new MessagePanel(user.getUsername(), message));
//    }
}
