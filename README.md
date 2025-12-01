## This is the Page of  
## 1. OAuth Secure Login Nano Framework ( implemented to work with Google Services )
## 2. In productivity usable Example GUI OAuth-Flow ( Example implementation for OAuth Secure Login Nano Framework )
## 3. OAuth Secure Login Email Client for sending emails with attachments ( Example implementation for GUI OAuth-Flow example implementation )
<br>
<br>

## Legal Notice

 This software is proprietary and protected by copyright law.  
 Idea, Author, and Copyright: Marco Scherzer  
 All rights reserved.

 Strictly prohibited:  
 Forking, copying, reverse engineering, decompiling, modifying, redistributing, or any unauthorized use of this software.

About this Software:
Until now, all of my work has been closed-source. I have always worked independently and entirely on my own. 
Everything I have built has been done only on my local computer, and everything I create is formally protected by law. 
Only now, in my later years, I have published something here on GitHub for the first time. 
Except for what I publish here on this GitHub page (https://github.com/Marco-Scherzer), 
all of my software projects remain closed-source.

My source code and any compiled versions that may sometimes appear here,  
as well as any texts or other content on this page, are protected by copyright.

All rights are reserved, which means that any kind of use, copying, linking or downloading and many things more is not permitted.
If I ever decide to write a license for the binary, so that other people may at least be allowed to download the executable or installer,  
this text will also include the license and the exact location where the binaries can be downloaded.

## Repository Sale Notice

This repository is offered for sale in its current, up‑to‑date code state. 
If you are interested, please contact me via my listed email address

**Important Notice:** 
For security reasons, contracts are concluded exclusively after personal identification and presentation of the buyer’s official ID document in the presence of my trusted notary in Karlsruhe, Germany.
I always identify with ID-Card. 
Since my childhood, I have always had exactly and only one banking account at a trusted local bank, ensuring protocolized secure banking. 
I never  accept any transactions other than traditional, documented transactions processed directly through my local bank.


Contact: fahrservice.1@gmail.com

# Declaration to Avoid Scamming, Theft of Intellectual Property, and Deception by Fraudsters

To prevent scamming, theft of intellectual property, and the deception of persons by fraudulent actions, I hereby make the following statement once and for all, clearly and explicitly:

**Please note:** I never grant any permissions, not in the past, not now, and not in the future.

---

## 1. Abuse and Phishing

To protect against abuse and phishing, please note:
There are **no other websites, email addresses, or communication channels** associated with this software except the official contact listed here.

If you encounter the code or binaries anywhere other than:

- [https://github.com/Marco-Scherzer](https://github.com/Marco-Scherzer)
- The same repository archived in the Wayback Machine (pre‑publication archiving)

then it constitutes **abuse, a scam, and theft of law‑protected intellectual property**.

In such a case, please inform GitHub and email me.

---

## 2. False Claims of Involvement or Permission

Any false claim by any person to be in any way involved in my projects, or to have received any permission from me – whether for usage, reproduction, replication, especially of APIs, functionality, modularity, architecture, or for public display – is untrue and constitutes a **serious criminal offense**.

This includes in particular:
- Scamming and fraudulent deception
- Theft of intellectual property
- Always implicit defamation of the true author of a work and his business, since the truth about the origin of a work is reputation‑critical

I explicitly declare that I **never grant any licenses of any kind for an open source work and especially not for its code – not in the past, not now, and not in the future.**

---

## 3. Reporting Criminal Acts

If you have information pointing to criminal acts as described under points 1–2, I request that you immediately:

- Inform the **Economic Cybercrime Division of the German Police (Zentrale Ansprechstellen Cybercrime, ZAC)**
    - [Polizei.de – Zentrale Ansprechstellen Cybercrime](https://www.polizei.de/Polizei/DE/Einrichtungen/ZAC/zac_node.html)
    - [ZAC Contact List (Bund & Länder, PDF)](https://www.wirtschaftsschutz.info/DE/Themen/Cybercrime/Ansprechpartner/ZACErreichbarkeiten.pdf?__blob=publicationFile&v=3)

- Contact **GitHub** via its official abuse reporting email: **abuse@github.com**
    - [GitHub Docs – Reporting Abuse or Spam](https://docs.github.com/en/communities/maintaining-your-safety-on-github/reporting-abuse-or-spam)

---

**Your civil courage counts. Help prevent such crimes, make Open Source safer, and protect the reputation of authors.**

---


<br>
<br>
<br>
# OAuth Secure Login Nano Framework (implemented to work with Google Services)

## Central class MSimpleKeystore / MSimpleOAuthKeystoreManager
### Creates or uses a password-protected `.p12` database and securely stores `clientSecret.json` in it. Handleable like a map.

## Central class MGWebApis
### Hierarchic constant class of many web-apis and scope-urls Google offers as a service.
### (Internally used classes are auto-updatable (with up-to-date URLs) by versions-properties/build script)
### Current top-level constants: 
``` 
Identity, Gmail, Drive, Calendar, People, Tasks, Classroom, YouTube, YouTubeAnalytics
Photos, Analytics, Cloud, Ads, Admin, Play, Fitness, TagManager, Webmasters, Docs  
Sheets, Slides, Keep, Vision, Translate, Pubsub, Spanner, SQLAdmin, Firestore, Logging  
 Monitoring, CloudKMS, CloudIot, CloudFunctions, CloudRun, Container, DeploymentManager  
ServiceNetworking, CloudIdentity, Iam, CloudML, Dialogflow, Apigee, Cloudbilling, Playcustomapp
```

### Every top-level constant has one or more selectable scope-urls Google offers as a service:
### For example:
```
MGWebApis.GMail.GMAIL_SEND, MGWebApis.GMail.GMAIL_READONLY, 
MGWebApis.GMail.GMAIL_COMPOSE, MGWebApis.GMail.GMAIL_MODIFY, MGWebApis.GMail.GMAIL_LABELS
```

### Legal Notice
The constant names listed above refer to publicly documented **Google Web APIs and their services**.  
They are the property of Google LLC and are used here solely for technical integration and programming purposes.  
The use of these constants is subject to the [Google API Terms of Service](https://developers.google.com/terms).


## Central class MSimpleOAuthHelper (Implements the whole OAuth flow)
### Takes the `.p12` keystore with the clientSecret and the scope constants and performs the OAuth.
### Has an abstract method `onStartOAuth(String oAuthLink, MMutableBoolean continueOAuthOrNot)`
### to help implement an OAuth Flow user dialog.
### Has an abstract method  `onOAuthSucceeded(Credential credential, String applicationName)` to easily work with the resulting credential (for example to use it in a service client).
### Has methods to clean up securely: `revokeOAuthTokenFromServer();` and `removePersistedOAuthToken()`.

```java
public abstract class MSimpleOAuthHelper {
    public MSimpleOAuthHelper(MSimpleOAuthKeystoreManager keystore, String applicationName, boolean doNotPersistOAuthToken, String... scopes);
    protected abstract void onStartOAuth(String oAuthLink, MMutableBoolean continueOAuthOrNot);
    protected abstract void onOAuthSucceeded(Credential credential, String applicationName);
    public final void revokeOAuthTokenFromServer();
    public final void removePersistedOAuthToken();
    
}

```
# In productivity usable Example GUI OAuth-Flow (Example implementation for OAuth Secure Login Nano Framework)

### 1. Setup dialog for storing `clientSecret.json` in secure `.p12` database  
### 2.1 OAuth login user info dialog (optionally selectable to show only this time)  
### 2.2 If set up, a password dialog is shown next time before starting OAuth login  
### Usable also as general application password dialog (like in OAuth Secure Login Email Client Example)  
### Waiting-overlay(including blue spinner) for guiding the secure browser login and offering a "back" possibility  

---

# OAuth Secure Login Email Client for sending emails with attachments (Example Implementation for GUI OAuth-Flow Example Implementation)

### Works file-drop triggered by dropping files on its desktop icon.
### After the folder-icon is clicked or contents are added to the folder by drag and drop or script, an email-send dialog appears to ask the user for consent and to change the recipient, subject, or mail text
### Saves the client secret in a secure database  
### A simple, security-focused OAuth mail client for sending emails with attachments, triggered by a desktop folder icon  
### Requires an email account (tested with Gmail) and a `clientSecret.json` file provided by Google  
### Redirects you to the OAuth login page hosted by Google  
### Creates a special outgoing mail folder icon linked on the desktop to send mail  

### If the user agrees, the mail is sent from the configured sender email address to the receiver email address  
### Multiple send windows can be opened by using the same simple click or drag-and-drop gesture

## Features

- OAuth 2.0 authentication without persisting OAuth tokens
- `client_secret.json` file is stored in secure encrypted PKCS#12 database
- Application startup access password protection
- Attachments Desktop Folder triggered email window appearance:  
  If attachments are added by drag and drop or script to the outgoing mail folder icon linked on the Desktop,  
  this part of the application sends the file-paths via TCP to the main part of the application,  
  and an email send-dialog appears to send a mail and to request user consent.
- If the outgoing mail folder icon linked on the Desktop is clicked, just the email send dialog opens.  
  The user can change the recipient, the subject line, or write a simple text message.  
  Multiple send windows can be opened by using the same simple click or drag-and-drop gesture.
- One-time setup of account email address
- Usable inside of scripts (timed backups, …)
- Account email address is stored in secure encrypted PKCS#12 database
- Uses App Directory UUID-Name Folders as Mail Folder Names
- Writes links of sent files to a "Sent Things" Desktop link folder and stores them with a timestamp name
- Writes links of not sent files to a "Not Sent Things" Desktop link folder and stores them with a timestamp name


<br>
<br>
<br>

## Legal Notice

This software is proprietary and protected by copyright law.  
Idea, Author, and Copyright: Marco Scherzer 
All rights reserved.

Forking, copying, reverse engineering, decompiling, modifying, redistributing,  
or any unauthorized use of this software is strictly forbidden.

**Contact**: fahrservice.1@gmail.com





