package com.marcoscherzer.msimplegoauthhelper.swinggui;
import com.google.api.client.auth.oauth2.Credential;
import com.marcoscherzer.msimplegoauthhelper.MSimpleOAuthHelper;
import com.marcoscherzer.msimplegoauthhelper.MSimpleOAuthKeystore;
import com.marcoscherzer.msimplegoauthmailserviceapplication.MAppSetupDialog;
import com.marcoscherzer.msimplekeystore.MSimpleKeystore;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    public final MSimpleKeystore getKeystore() {
        return store.getKeystore();
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final void createMessageDialogAndWait(String message, String title){
        JOptionPane.showMessageDialog(null,message, title, JOptionPane.ERROR_MESSAGE);
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     *  unready
     */
    private void setup(){
        try {
            System.out.println("showing setup dialog");
            statusMsg("Setup started." + "\nInfo: Use the SystemTray Icon to view log Information");
            String[] setupedValues = new MAppSetupDialog().showAndWait();
            if (setupedValues == null) onException_(null); // canceled
            String from = setupedValues[0];
            String to = setupedValues[1];
            String pw = setupedValues[2];
            String clientSecretPath = setupedValues[3];

            // Keystore erstellen mit ausgewähltem client_secret.json
            store = new MSimpleOAuthKeystore(pw, clientSecretPath, keystorePath);
            store.getKeystore().put("fromAddress", from);
            store.getKeystore().put("toAddress", to);//unready

            statusMsg("Setup completed.");
            System.out.println("setup completed");
        } catch (Exception exc){
            System.err.println(exc.getMessage());
            createMessageDialogAndWait("Error:\n" + exc.getMessage(),"Error");
            onException_(exc);
        }
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private void checkPassword(){
        try {
            System.out.println("showing password dialog");
            //statusMsg("Password required.\n");

            new MAppPwDialog()
                    .setOkHandler(pw -> {
                        try {
                            store = new MSimpleOAuthKeystore(pw, "", keystorePath);
                            System.out.println("Access-level 1 granted: Application");
                            //statusMsg("Access-level 1 granted: Application\n");
                        } catch (Exception exc){
                            System.err.println(exc.getMessage());
                            createMessageDialogAndWait("Error:\n" + exc.getMessage(),"Error");
                            onException_(exc);
                        }
                    }).showAndWait();
        } catch (Exception exc){
            System.err.println(exc.getMessage());
            createMessageDialogAndWait("Error:\n" + exc.getMessage(),"Error");
            onException_(exc);
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * uready
     */
    public final void createAuthFlow(boolean persistOAuthToken,String... scopes) {
        boolean setup = !Files.exists(Paths.get(keystorePath));
        if (setup) setup(); else checkPassword();
        if(store!=null) {
            oAuthHelper = new MSimpleOAuthHelper(store, "BackupMailer", persistOAuthToken,scopes) {
                /**
                 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
                 */
                @Override
                protected final void onOAuthSucceeded(Credential credential, String applicationName) {
                    try {
                        appRedirectLinkDialog.dispose();
                        loginOverlay.dispose();
                        System.out.println("intializing services...");
                        initializeServices(credential, applicationName);
                    } catch (Exception exc) {
                        System.err.println(exc.getMessage());
                        onException_(exc);
                    }
                }

                /**
                 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
                 */
                @Override
                protected final void onStartOAuth(String oAuthLink, MMutableBoolean continueOAuthOrNot) {
                    System.out.println("Additional authentification required" + oAuthLink);
                    statusMsg("Additional authentification required\n");
                    try {
                        appRedirectLinkDialog = new MAppRedirectLinkDialog();
                        appRedirectLinkDialog.showAndWait(oAuthLink,continueOAuthOrNot);
                        appRedirectLinkDialog.setVisible(false);
                        loginOverlay = new MSpinnerOverlayFrame();
                        loginOverlay.setMouseHandler(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                appRedirectLinkDialog.setVisible(true);
                            }
                        });
                        loginOverlay.setVisible(true);

                    } catch (Exception exc) {
                        System.err.println(exc.getMessage());
                        onException_(exc);
                    }
                }

                @Override
                protected final void onOAuthFailure(Exception exc) {
                    System.err.println(exc.getMessage());
                    onException_(exc);
                }
            };
            oAuthHelper.startOAuth();
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private void onException_(Exception exc){
        try {
            if (oAuthHelper != null && oAuthHelper.isInDoNotPersistOAuthTokenMode())  oAuthHelper.revokeOAuthTokenFromServer();
        }catch (Exception exc2) {
                System.err.println(exc2.getMessage());
        }
        onException(exc);
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
