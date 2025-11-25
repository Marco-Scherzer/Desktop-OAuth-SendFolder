package com.marcoscherzer.msimplegoauthhelper.scopes;
import java.util.EnumSet;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer
 * Web-API Scopes
 */
public final class MScopeInfo {
    private final String scope;
    private final EnumSet<MSupportedLogins> loginTypes;

    public MScopeInfo(String scope, EnumSet<MSupportedLogins> loginTypes) {
        this.scope = scope;
        this.loginTypes = loginTypes;
    }

    public String getScope() {
        return scope;
    }

    public EnumSet<MSupportedLogins> getLoginTypes() {
        return loginTypes;
    }

    @Override
    public String toString() {
        return scope;
    }
}

