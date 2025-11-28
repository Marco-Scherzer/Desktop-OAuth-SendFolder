#  Desktop OAuth Send Folder
## A spontaneous mini project focused on simplicity and security.
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

This repository is offered for sale at its current code state.  
If you are interested, please contact me via my listed email address.

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

To prevent abuse or phishing, there are no other websites, email addresses, or communication channels connected to the software on this page except the listed official email address.

If you find the code or binaries anywhere other than at  
[https://github.com/Marco-Scherzer](https://github.com/Marco-Scherzer),  
it is abuse, a scam, and theft of law‑protected intellectual property.

In such a case, please inform GitHub and send me an email.

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

### A simple, security focused OAuth mail client for sending emails with attachments, triggered by a desktop folder icon.
### Requires an email account (tested with Gmail) and a clientSecret.json file provided by Google.
### Redirects you to the OAuth login page hosted by Google.
### Creates a special outgoing mail folder icon linked on the desktop to send mail. 
### After the folder-icon is clicked or contents are added to the folder by drag and drop or script, an email-send dialog appears to ask the user for consent and to change the recipient, subject or mail text.
### If the user agrees the mail is sent from the configured sender email address to the receiver email address.
### Multiple send windows can be opened by using the same simple click or drag-and-drop gesture.

 ## Features

### - OAuth 2.0 authentication without persisting OAuth tokens.

### - client_secret.json file is stored in secure encrypted PKCS#12 database

### - Application startup access password protection

### - Attachments Desktop Folder triggerd email window appearance:
    If attachments are added by drag and drop or script to the special outgoing mail folder icon linked on the Desktop, 
    this part of the Application sends the links via TCP to the main part of the Application 
    and an email send-dialog appears to send a mail.
    Introduced for security reasons, the dialog requests user consent before sending.
    Additional advantages: The user can also change the the recipient, the subject line or write a simple text message.
    Multiple send windows can be opened by using the same simple click or drag-and-drop gesture.

### - One time setup of default sender address

### - usable inside of scripts (timed backups,...) but a secure way

### - Sender email address is stored in secure encrypted PKCS#12 database

### - Uses App Directory UUID-Name Folders as Mail Folder Names

### - Uses the generated UUID in both the internal client name and default email metadata for additional verifiability

### - Writes links of sent files to a "Sent Things" Desktop link folder and stores them with a timestamp name.

### - Writes links of not sent files to a "Not Sent Things" Desktop link folder and stores them with a timestamp name.






<br>

## Runtime Output

### > Please enter your password: testTesttest-123

### loading keystore Z:\OAuth-Desktop-FileSend-Folder\mystore.p12

### Please open the following address in your browser:
###  https://accounts.google.com/o/oauth2/...

==========================================================================
##              OAuth Desktop FileSend Folder
## (A mini project focusing on simplicity and security)
## Author : Marco Scherzer Copyright: © Marco Scherzer. All rights reserved. 
==========================================================================

## Welcome Mail Sender!

## Base Path: Z:\MarcoScherzer-Projects\MSendBackupMail\mail
## Outgoing Folder: 6a132b05-9f36-49f0-8399-301e4692c643
## Sent Folder: 6a132b05-9f36-49f0-8399-301e4692c643-sent

## Sender Address : fahrservice.1@gmail.com 
## Receiver Address : fahrservice.1@gmail.com

## New files added to the 'Outgoing Things' folder on your Desktop 
## will be automatically sent via email.
## After sending, they will be moved to the 'Sent Things' folder.



---
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





