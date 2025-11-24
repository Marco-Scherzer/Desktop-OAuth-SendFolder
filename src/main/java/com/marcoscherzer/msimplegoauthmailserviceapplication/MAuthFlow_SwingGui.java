package com.marcoscherzer.msimplegoauthmailserviceapplication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.gmail.GmailScopes;
import com.marcoscherzer.msimplegoauthhelper.MSimpleOAuthHelper;
import com.marcoscherzer.msimplegoauthhelper.MSimpleOAuthKeystore;
import com.marcoscherzer.msimplegoauthhelper.swinggui.MAppRedirectLinkDialog;
import com.marcoscherzer.msimplegoauthhelper.swinggui.MSpinnerOverlayFrame;
import com.marcoscherzer.msimplegoauthmailserviceapplication.util.MMutableBoolean;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public abstract class MAuthFlow_SwingGui {

    private MSimpleOAuthHelper oAuthHelper;
    private MSimpleOAuthKeystore store;
    private MAppRedirectLinkDialog appRedirectLinkDialog;
    private MSpinnerOverlayFrame loginOverlay;
    private String keystorePath;


    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     *   userDir = dir;
     *         keystorePath = userDir + "\\" + keystoreFileName;
     */
    public MAuthFlow_SwingGui(String keystoreFilePathName) {
        keystorePath = keystoreFilePathName;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     */
    public final MSimpleOAuthKeystore  getKeystore() {
        return store;
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final void createMessageDialogAndWait(String message, String title){
        JOptionPane.showMessageDialog(null,message, title, JOptionPane.ERROR_MESSAGE);
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private void setup(){
        try {
            System.out.println("showing setup dialog");

            String[] setupedValues = new MAppSetupDialog().showAndWait();
            if (setupedValues == null) onException(null); // canceled
            String from = setupedValues[0];
            String to = setupedValues[1];
            String pw = setupedValues[2];
            String clientSecretPath = setupedValues[3];

            // Keystore erstellen mit ausgewähltem client_secret.json
            store = new MSimpleOAuthKeystore(pw, clientSecretPath, keystorePath);
            store.getKeyStore().put("fromAddress", from);
            store.getKeyStore().put("toAddress", to);

            statusMsg("Setup completed.");
            System.out.println("setup completed");
        } catch (Exception exc){
            System.err.println(exc.getMessage());
            createMessageDialogAndWait("Error:\n" + exc.getMessage(),"Error");
            onException(exc);
        }
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private void checkPassword(){
        try {
            System.out.println("showing password dialog");
            statusMsg("Password required.\n");

            new MAppPwDialog()
                    .setOkHandler(pw -> {
                        try {
                            store = new MSimpleOAuthKeystore(pw, "", keystorePath);
                            System.out.println("Access-level 1 granted: Application");
                            statusMsg("Access-level 1 granted: Application\n");
                        } catch (Exception exc){
                            System.err.println(exc.getMessage());
                            createMessageDialogAndWait("Error:\n" + exc.getMessage(),"Error");
                            onException(exc);
                        }
                    }).showAndWait();
        } catch (Exception exc){
            System.err.println(exc.getMessage());
            createMessageDialogAndWait("Error:\n" + exc.getMessage(),"Error");
            onException(exc);
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * uready
     */
    public final void swingGuiAuthFlow(List scopes, boolean persistOAuthToken) {
        boolean setup = !Files.exists(Paths.get(keystorePath));
        if (setup) setup(); else checkPassword();
        if(store!=null) {
            oAuthHelper = new MSimpleOAuthHelper(store, "BackupMailer", Collections.singletonList(GmailScopes.GMAIL_SEND), persistOAuthToken) {
                /**
                 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
                 */
                @Override
                protected final void onOAuthSucceeded(Credential credential, String applicationName) {
                    try {
                        appRedirectLinkDialog.dispose();
                        loginOverlay.dispose();
                        initializeServices(credential, applicationName);

                    } catch (Exception exc) {
                        System.err.println(exc.getMessage());
                        onException(exc);
                    }
                }

                /**
                 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
                 */
                @Override
                protected final void onStartOAuth(String oAuthLink, MMutableBoolean continueOAuthOrNot) {
                    System.out.println("Additional authentification needed " + oAuthLink);
                    try {
                        appRedirectLinkDialog = new MAppRedirectLinkDialog();
                        appRedirectLinkDialog.showAndWait(oAuthLink,continueOAuthOrNot);
                        appRedirectLinkDialog.setVisible(false);
                        loginOverlay = new MSpinnerOverlayFrame();
                        loginOverlay.setMouseHandler(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                System.out.println("Overlay clicked at: " + e.getPoint());
                                loginOverlay.setVisible(true);
                            }
                        });
                        loginOverlay.showOverlay();

                    } catch (Exception exc) {
                        System.err.println(exc.getMessage());
                        onException(exc);
                    }
                    statusMsg("Additional authentification needed\n");
                }

                @Override
                protected final void onOAuthFailure(Exception exc) {
                    System.err.println(exc.getMessage());
                    onException(exc);
                }
            };
            oAuthHelper.startOAuth();
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    protected abstract void onException(Exception exc);
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    protected abstract void statusMsg(String message);

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    protected abstract void initializeServices(Credential credential, String applicationName) throws Exception;
}
