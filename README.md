#  OAuth Desktop FileSend Folder
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

To prevent abuse or phishing, there are no other websites, email addresses, 
or communication channels connected to the software on this page except the listed email address.  
If you find the code or binaries anywhere else than on
https://github.com/Marco-Scherzer it is abuse, a scam, and theft of law-protected intellectual property.  
In such a case, please inform GitHub and send me an email.

My source code and any compiled versions that may sometimes appear here,  
as well as any texts or other content on this page, are protected by copyright.
  
All rights are reserved, which means that any kind of use, copying, linking or downloading and many things more is not permitted.
If I ever decide to write a license for the binary, so that other people may at least be allowed to download the executable or installer,  
this text will also include the license and the exact location where the binaries can be downloaded.

Contact: fahrservice.1@gmail.com
<br>
<br>
<br>

### A simple, secure OAuth Mail client for sending mails with attachments that is attachment Desktop folder triggerd.
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

### - Recipient email address is stored in secure encrypted PKCS#12 database

### - Generates at setup time internal Mail Folder Names using UUIDs.

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





