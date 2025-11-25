package com.marcoscherzer.msimplegoauthhelper.scopes;

import com.google.api.services.gmail.GmailScopes;

import java.util.EnumSet;

public class MGoAuthScopes2 {

    private MGoAuthScopes2() {}

    // --- Gmail ---
    public static final class Gmail {
        public static final MScopeInfo SEND =
                new MScopeInfo(GmailScopes.GMAIL_SEND, EnumSet.of(MSupportedLogins.OAUTH_USER));
        public static final MScopeInfo READONLY =
                new MScopeInfo(GmailScopes.GMAIL_READONLY, EnumSet.of(MSupportedLogins.OAUTH_USER));
    }

}
