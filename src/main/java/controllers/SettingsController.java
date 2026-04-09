package controllers;

import app.App;
import app.Config;
import models.Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SettingsController extends AbstractController {
    private final int UNSIGNED_16_BIT_CONSTANT = ~0 >>> 16, // 65,535
            MAX_FONT_SIZE = 50,
            MAX_USERNAME_SIZE = 1 << 6;  // 64
    
    private JPanel contentPanel;
    private JButton backButton;
    private JRadioButton darkModeRadioOn;
    private JRadioButton darkModeRadioOff;
    private JSpinner fontSizeSpinner;
    private JSpinner portSpinner;
    private JRadioButton hideIPRadioTrue;
    private JRadioButton hideIPRadioFalse;
    private JTextField usernameField;
    private JTextArea ipBlacklistTextArea;
    private JTextArea ipWhitelistTextArea;
    private JTextArea macBlacklistTextArea;
    private JTextArea macWhitelistTextArea;
    private JButton resetToDefaultsButton;
    private JButton applyButton;
    
    public SettingsController(JFrame appFrame) {
        super(appFrame, Screens.SETTINGS);
        
        backButton.addActionListener(e -> App.changeScreen(Screens.HOME));
        
        bindKey("ESCAPE", "back", () -> App.changeScreen(Screens.HOME));
        Config config = App.getConfig();
        
        if (config.isDarkMode())
            darkModeRadioOn.setSelected(true);
        else
            darkModeRadioOff.setSelected(true);
        
        fontSizeSpinner.setModel(new SpinnerNumberModel(config.getFontSize(), 1, MAX_FONT_SIZE, 1));
        
        portSpinner.setModel(new SpinnerNumberModel(config.getPort(), 0, UNSIGNED_16_BIT_CONSTANT, 1));
        
        if (config.getHideIp())
            hideIPRadioTrue.setSelected(true);
        else
            hideIPRadioFalse.setSelected(true);
        
        usernameField.setText(config.getUsername());
        usernameField.setTransferHandler(null);
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (usernameField.getText().length() >= MAX_USERNAME_SIZE)
                    e.consume();
            }
        });
        
        ipBlacklistTextArea.setText(formatArrays(config.getIpBlacklist()));
        ipWhitelistTextArea.setText(formatArrays(config.getIpWhitelist()));
        macBlacklistTextArea.setText(formatArrays(config.getMacBlacklist()));
        macWhitelistTextArea.setText(formatArrays(config.getMacWhitelist()));
        
        resetToDefaultsButton.addActionListener(e -> {
            config.resetToDefaults();
            App.changeScreen(Screens.SETTINGS);
        });
        
        applyButton.addActionListener(e -> {
            if (darkModeRadioOn.isSelected()) {
                config.setDarkMode(true);
                App.setDarkMode(true);
            } else if (darkModeRadioOff.isSelected()) {
                config.setDarkMode(false);
                App.setDarkMode(false);
            } else {
                displayNotif("Neither Dark Mode Option Selected");
                return;
            }
            
            config.setFontSize((int) fontSizeSpinner.getValue());
            UIManager.put("defaultFont", new Font("Hack FC Ligatured", Font.PLAIN, config.getFontSize()));
            
            config.setPort((int) portSpinner.getValue());
            
            if (hideIPRadioTrue.isSelected())
                config.setHideIp(true);
            else if (hideIPRadioFalse.isSelected())
                config.setHideIp(false);
            else {
                displayNotif("Neither IP Option Selected");
                return;
            }
            
            config.setUsername(usernameField.getText());
            
            if (isInvalidIPs(ipBlacklistTextArea.getText()) || isInvalidIPs(ipWhitelistTextArea.getText())) {
                displayNotif("Invalid IP Listing(s)");
                return;
            }
            
            config.setIpBlacklist(new ArrayList<>(List.of(ipBlacklistTextArea.getText().split(",\\s*"))));
            config.setIpWhitelist(new ArrayList<>(List.of(ipWhitelistTextArea.getText().split(",\\s*"))));
            
            if (isInvalidMacs(macBlacklistTextArea.getText()) || isInvalidMacs(macWhitelistTextArea.getText())) {
                displayNotif("Invalid MAC Listing(s)");
                return;
            }
            
            config.setMacBlacklist(new ArrayList<>(List.of(macBlacklistTextArea.getText().split(",\\s*"))));
            config.setMacWhitelist(new ArrayList<>(List.of(macWhitelistTextArea.getText().split(",\\s*"))));
            
            App.changeScreen(Screens.SETTINGS);
        });
    }
    
    @Override
    protected JPanel getContentPanel() {
        return contentPanel;
    }
    
    private String formatArrays(ArrayList<String> arrayList) {
        int last = arrayList.size() - 1;
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < arrayList.size(); i++) {
            formatted.append(arrayList.get(i));
            if (i != last)
                formatted.append(", ");
        }
        return formatted.toString();
    }
    
    private boolean isInvalidIPs(String ipSequence) {
        String regex = "^\\s*$|^(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d?|0)" +
                "(?:\\.(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d?|0)){3}" +
                "(?:\\s*,\\s*(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d?|0)" +
                "(?:\\.(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d?|0)){3})*$";
        
        return !ipSequence.matches(regex);
    }
    
    private boolean isInvalidMacs(String macSequence) {
        String regex = "^(?:\\s*$|[0-9A-Fa-f]{2}" +
                "([-:])(?:[0-9A-Fa-f]{2}\\1){4}" +
                "[0-9A-Fa-f]{2}(?:\\s*,\\s*[0-9A-Fa-f]{2}" +
                "([-:])(?:[0-9A-Fa-f]{2}\\2){4}[0-9A-Fa-f]{2})*$)";
        
        return !macSequence.matches(regex);
    }
}
