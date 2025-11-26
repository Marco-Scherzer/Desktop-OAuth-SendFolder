package com.marcoscherzer.msimplegoauthhelper.swinggui;
import com.google.api.client.auth.oauth2.Credential;
import com.marcoscherzer.msimplegoauthhelper.MMutableBoolean;
import com.marcoscherzer.msimplegoauthhelper.MSimpleOAuthHelper;
import com.marcoscherzer.msimplegoauthhelper.MSimpleOAuthKeystore;
import com.marcoscherzer.msimplekeystore.MSimpleKeystore;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;

import static com.marcoscherzer.msimplegoauthmailserviceapplication.MMain.exit;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public abstract class MAuthDialogsFlow {

    private MSimpleOAuthHelper oAuthHelper;
    private MSimpleOAuthKeystore store;
    private MAuthRedirectLinkDialog appRedirectLinkDialog;
    private String keystorePath;


    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     *   userDir = dir;
     *         keystorePath = userDir + "\\" + keystoreFileName;
     */
    public MAuthDialogsFlow(String keystoreFilePathName) {
        keystorePath = keystoreFilePathName;
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
    private void createSetupDialog(){
        try {
            System.out.println("showing setup dialog");
            statusMsg("Setup started." + "\nInfo: Use the SystemTray Icon to view log Information");
            String[] setupedValues = new MClientSecretKeystoreSetupDialog().showAndWait();
            if (setupedValues == null) onException_(null); // canceled
            String pw = setupedValues[0];
            String clientSecretPath = setupedValues[1];
            // Keystore erstellen mit ausgewähltem client_secret.json
            store = new MSimpleOAuthKeystore(pw, clientSecretPath, keystorePath);
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
    private void createPasswordDialog(){
        try {
            System.out.println("showing password dialog");
            //statusMsg("Password required.\n");

            new MPwDialog()
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
        if (setup) createSetupDialog(); else createPasswordDialog();
        if(store!=null) {
            oAuthHelper = new MSimpleOAuthHelper(store, "BackupMailer", persistOAuthToken,scopes) {

                /**
                 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
                 */
                @Override
                protected final void onStartOAuth(String oAuthLink, MMutableBoolean continueOAuthOrNot) {
                    System.out.println("Additional authentification required " + oAuthLink);
                    statusMsg("Additional authentification required\n");
                    try {
                        appRedirectLinkDialog = new MAuthRedirectLinkDialog() {
                            @Override
                            protected void onException(Exception exc) {
                                onException_(exc);
                            }

                            @Override
                            protected void onCanceled() {
                               exit(null,0);//unready
                            }
                        };
                        appRedirectLinkDialog.showAndWait(oAuthLink,continueOAuthOrNot);

                    } catch (Exception exc) {
                        System.err.println(exc.getMessage());
                        onException_(exc);
                    }
                }

                /**
                 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
                 */
                @Override
                protected final void onOAuthSucceeded(Credential credential, String applicationName) {
                    try {
                        appRedirectLinkDialog.dispose();
                        System.out.println("intializing services...");
                        initializeServices(new MAuthManager(oAuthHelper), credential, applicationName);
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
            if (oAuthHelper != null)  oAuthHelper.revokeOAuthTokenFromServer();
        }catch (Exception exc2) {
                System.err.println(exc2.getMessage());
        }
        onException(exc);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final class MAuthManager{
        private final MSimpleOAuthHelper oAuthHelper;
        private MAuthManager(MSimpleOAuthHelper oAuthHelper){ this.oAuthHelper = oAuthHelper;}
        public final boolean isInDoNotPersistOAuthTokenMode() throws Exception { return oAuthHelper.isInDoNotPersistOAuthTokenMode(); }
        public final void revokeOAuthTokenFromServer() throws GeneralSecurityException, IOException { oAuthHelper.revokeOAuthTokenFromServer();}
        public final void removePersistetOAuthToken() throws GeneralSecurityException, IOException { oAuthHelper.revokeOAuthTokenFromServer();}
        public final MSimpleKeystore getKeystore(){ return oAuthHelper.getKeystore(); }
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
    protected abstract void initializeServices(MAuthManager oAuthManager, Credential credential, String applicationName) throws Exception;
}
