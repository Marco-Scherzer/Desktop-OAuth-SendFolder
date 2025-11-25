/**
 * @author Marco Scherzer, Copyright Marco Scherzer
 * G. Web-API Scopes
 */
package com.marcoscherzer.msimplegoauthhelper;

// Basis / Identity
import com.google.api.services.oauth2.Oauth2Scopes;

// Produktivität
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.calendar.CalendarScopes;


// Kommunikation
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.classroom.ClassroomScopes;

// Video / Medien
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtubeAnalytics.v2.YouTubeAnalyticsScopes;
import com.google.api.services.photoslibrary.v1.PhotosLibraryScopes;

// Analytics
import com.google.api.services.analytics.AnalyticsScopes;

// Cloud Platform
import com.google.api.services.cloudresourcemanager.v3.CloudResourceManagerScopes;
import com.google.api.services.bigquery.BigqueryScopes;
import com.google.api.services.storage.StorageScopes;


// Ads / Monetarisierung
import com.google.api.services.adsense.v2.AdsenseScopes;
import com.google.api.services.adexchangebuyer.AdExchangeBuyerScopes;
import com.google.api.services.adexchangeseller.AdExchangeSellerScopes;
import com.google.api.services.displayvideo.v4.DisplayVideoScopes;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;

// Weitere APIs
import com.google.api.services.admin.directory.DirectoryScopes;
import com.google.api.services.admin.reports.ReportsScopes;
import com.google.api.services.playdeveloperreporting.v1beta1.PlaydeveloperreportingScopes;
import com.google.api.services.fitness.FitnessScopes;
import com.google.api.services.tagmanager.TagManagerScopes;
import com.google.api.services.webmasters.WebmastersScopes;

import java.util.EnumSet;
/**
 * @author Marco Scherzer, Copyright Marco Scherzer
 * G. Web-API Scopes
 */
public final class MGoAuthScopes {

    private MGoAuthScopes() {}

    /**
     * Login-Arten für Google Service APIs
     * @author Marco Scherzer, Copyright Marco Scherzer
     */
    public static enum MSupportedLogins {
        OAUTHUSER,
        OAUTHSERVICEACCOUNT,
        APIKEY,

        // Kombinationen
        OAUTHUSER_OAUTHSERVICEACCOUNT,
        OAUTHUSER_APIKEY,
        OAUTHSERVICEACCOUNT_APIKEY,
        OAUTHUSER_OAUTHSERVICEACCOUNT_APIKEY,

        UNKNOWN
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * Web-API Scopes
     */
    public static final class MScopeInfo {
        private final String url;

        public MScopeInfo(String scope, EnumSet<MSupportedLogins> loginTypes) {
            this.url = scope;
            this.loginTypes = loginTypes;
        }

        private final EnumSet<MSupportedLogins> loginTypes;
        public String getUrl() {
            return url;
        }

        public final EnumSet<MSupportedLogins> getLoginTypes() {
            return loginTypes;
        }

        @Override
        public final String toString() {
            return url;
        }
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * Web-API Scopes
     */
    //Interface für alle Scope-Enums
    public static interface MScopeEnum {
        MScopeInfo getInfo();
        default String getUrl() { return getInfo().getUrl(); }
        default EnumSet<MSupportedLogins> getLoginTypes() { return getInfo().getLoginTypes(); }
    }

    // --- Basis / Identity ---
    public enum Identity implements MScopeEnum {
        PLUS_ME(new MScopeInfo(Oauth2Scopes.PLUS_ME, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        PLUS_LOGIN(new MScopeInfo(Oauth2Scopes.PLUS_LOGIN, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        USERINFO_EMAIL(new MScopeInfo(Oauth2Scopes.USERINFO_EMAIL, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        USERINFO_PROFILE(new MScopeInfo(Oauth2Scopes.USERINFO_PROFILE, EnumSet.of(MSupportedLogins.OAUTHUSER)));

        private final MScopeInfo info;
        Identity(MScopeInfo info) { this.info = info; }
        @Override public MScopeInfo getInfo() { return info; }
    }

    // --- Gmail ---
    public enum Gmail implements MScopeEnum {
        GMAIL_SEND(new MScopeInfo(GmailScopes.GMAIL_SEND, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        GMAIL_READONLY(new MScopeInfo(GmailScopes.GMAIL_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        GMAIL_COMPOSE(new MScopeInfo(GmailScopes.GMAIL_COMPOSE, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        GMAIL_MODIFY(new MScopeInfo(GmailScopes.GMAIL_MODIFY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        GMAIL_LABELS(new MScopeInfo(GmailScopes.GMAIL_LABELS, EnumSet.of(MSupportedLogins.OAUTHUSER)));

        private final MScopeInfo info;
        Gmail(MScopeInfo info) { this.info = info; }
        @Override public MScopeInfo getInfo() { return info; }
    }

    // --- Drive ---
    public enum Drive implements MScopeEnum {
        DRIVE(new MScopeInfo(DriveScopes.DRIVE, EnumSet.of(MSupportedLogins.OAUTHUSER, MSupportedLogins.OAUTHSERVICEACCOUNT))),
        DRIVE_FILE(new MScopeInfo(DriveScopes.DRIVE_FILE, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        DRIVE_READONLY(new MScopeInfo(DriveScopes.DRIVE_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        DRIVE_METADATA_READONLY(new MScopeInfo(DriveScopes.DRIVE_METADATA_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        DRIVE_APPDATA(new MScopeInfo(DriveScopes.DRIVE_APPDATA, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        DRIVE_SCRIPTS(new MScopeInfo(DriveScopes.DRIVE_SCRIPTS, EnumSet.of(MSupportedLogins.OAUTHUSER)));

        private final MScopeInfo info;
        Drive(MScopeInfo info) { this.info = info; }
        @Override public MScopeInfo getInfo() { return info; }
    }

    // --- Calendar ---
    public enum Calendar implements MScopeEnum {
        CALENDAR_READONLY(new MScopeInfo(CalendarScopes.CALENDAR_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        CALENDAR(new MScopeInfo(CalendarScopes.CALENDAR, EnumSet.of(MSupportedLogins.OAUTHUSER)));

        private final MScopeInfo info;
        Calendar(MScopeInfo info) { this.info = info; }
        @Override public MScopeInfo getInfo() { return info; }
    }

    // --- People / Contacts ---
    public enum People implements MScopeEnum {
        CONTACTS(new MScopeInfo(PeopleServiceScopes.CONTACTS, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        CONTACTS_READONLY(new MScopeInfo(PeopleServiceScopes.CONTACTS_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        USERINFO_EMAIL(new MScopeInfo(PeopleServiceScopes.USERINFO_EMAIL, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        USERINFO_PROFILE(new MScopeInfo(PeopleServiceScopes.USERINFO_PROFILE, EnumSet.of(MSupportedLogins.OAUTHUSER)));

        private final MScopeInfo info;
        People(MScopeInfo info) { this.info = info; }
        @Override public MScopeInfo getInfo() { return info; }
    }

    // --- Tasks ---
    public enum Tasks implements MScopeEnum {
        TASKS(new MScopeInfo(TasksScopes.TASKS, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        TASKS_READONLY(new MScopeInfo(TasksScopes.TASKS_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER)));

        private final MScopeInfo info;
        Tasks(MScopeInfo info) { this.info = info; }
        @Override public MScopeInfo getInfo() { return info; }
    }

    // --- Classroom ---
    public enum Classroom implements MScopeEnum {
        CLASSROOM_COURSES(new MScopeInfo(ClassroomScopes.CLASSROOM_COURSES, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        CLASSROOM_ROSTERS(new MScopeInfo(ClassroomScopes.CLASSROOM_ROSTERS, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        CLASSROOM_ANNOUNCEMENTS(new MScopeInfo(ClassroomScopes.CLASSROOM_ANNOUNCEMENTS, EnumSet.of(MSupportedLogins.OAUTHUSER)));

        private final MScopeInfo info;
        Classroom(MScopeInfo info) { this.info = info; }
        @Override public MScopeInfo getInfo() { return info; }
    }

    // --- YouTube ---
    public enum YouTube implements MScopeEnum {
        YOUTUBE(new MScopeInfo(YouTubeScopes.YOUTUBE, EnumSet.of(MSupportedLogins.OAUTHUSER, MSupportedLogins.APIKEY))),
        YOUTUBE_READONLY(new MScopeInfo(YouTubeScopes.YOUTUBE_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER, MSupportedLogins.APIKEY))),
        YOUTUBE_UPLOAD(new MScopeInfo(YouTubeScopes.YOUTUBE_UPLOAD, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        YOUTUBE_FORCE_SSL(new MScopeInfo(YouTubeScopes.YOUTUBE_FORCE_SSL, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        YOUTUBEPARTNER(new MScopeInfo(YouTubeScopes.YOUTUBEPARTNER, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        YOUTUBEPARTNER_CHANNEL_AUDIT(new MScopeInfo(YouTubeScopes.YOUTUBEPARTNER_CHANNEL_AUDIT, EnumSet.of(MSupportedLogins.OAUTHUSER)));

        private final MScopeInfo info;
        YouTube(MScopeInfo info) { this.info = info; }
        @Override public MScopeInfo getInfo() { return info; }
    }

    // --- YouTube Analytics ---
    public enum YouTubeAnalytics implements MScopeEnum {
        YT_ANALYTICS_READONLY(new MScopeInfo(YouTubeAnalyticsScopes.YT_ANALYTICS_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        YT_ANALYTICS_MONETARY_READONLY(new MScopeInfo(YouTubeAnalyticsScopes.YT_ANALYTICS_MONETARY_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER)));

        private final MScopeInfo info;
        YouTubeAnalytics(MScopeInfo info) { this.info = info; }
        @Override public MScopeInfo getInfo() { return info; }
    }

    // --- Photos ---
    public enum Photos implements MScopeEnum {
        PHOTOSLIBRARY(new MScopeInfo(PhotosLibraryScopes.PHOTOSLIBRARY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        PHOTOSLIBRARY_READONLY(new MScopeInfo(PhotosLibraryScopes.PHOTOSLIBRARY_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER)));

        private final MScopeInfo info;
        Photos(MScopeInfo info) { this.info = info; }
        @Override public MScopeInfo getInfo() { return info; }
    }

    // --- Analytics ---
    public enum Analytics implements MScopeEnum {
        ANALYTICS(new MScopeInfo(AnalyticsScopes.ANALYTICS, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        ANALYTICS_READONLY(new MScopeInfo(AnalyticsScopes.ANALYTICS_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER)));

        private final MScopeInfo info;
        Analytics(MScopeInfo info) { this.info = info; }
        @Override public MScopeInfo getInfo() { return info; }
    }

    // --- Cloud Platform ---
    // --- Cloud Platform ---
    public enum Cloud implements MScopeEnum {
        CLOUD_PLATFORM(new MScopeInfo(CloudResourceManagerScopes.CLOUD_PLATFORM, EnumSet.of(MSupportedLogins.OAUTHUSER, MSupportedLogins.OAUTHSERVICEACCOUNT))),
        BIGQUERY(new MScopeInfo(BigqueryScopes.BIGQUERY, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT))),
        BIGQUERY_READONLY(new MScopeInfo(BigqueryScopes.BIGQUERY_READONLY, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT))),
        DEVSTORAGE_FULL_CONTROL(new MScopeInfo(StorageScopes.DEVSTORAGE_FULL_CONTROL, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT))),
        DEVSTORAGE_READ_WRITE(new MScopeInfo(StorageScopes.DEVSTORAGE_READ_WRITE, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT))),
        DEVSTORAGE_READ_ONLY(new MScopeInfo(StorageScopes.DEVSTORAGE_READ_ONLY, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT)));

        private final MScopeInfo info;

        Cloud(MScopeInfo info) {
            this.info = info;
        }

        @Override
        public MScopeInfo getInfo() {
            return info;
        }
    }

    // --- Ads / Monetarisierung ---
    public enum Ads implements MScopeEnum {
        ADSENSE(new MScopeInfo(AdsenseScopes.ADSENSE, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        ADSENSE_READONLY(new MScopeInfo(AdsenseScopes.ADSENSE_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        AD_EXCHANGE_BUYER(new MScopeInfo(AdExchangeBuyerScopes.ADEXCHANGE_BUYER, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        AD_EXCHANGE_SELLER(new MScopeInfo(AdExchangeSellerScopes.ADEXCHANGE_SELLER, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        AD_EXCHANGE_SELLER_READONLY(new MScopeInfo(AdExchangeSellerScopes.ADEXCHANGE_SELLER_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        DISPLAY_VIDEO(new MScopeInfo(DisplayVideoScopes.DISPLAY_VIDEO, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        DISPLAY_VIDEO_MEDIAPLANNING(new MScopeInfo(DisplayVideoScopes.DISPLAY_VIDEO_MEDIAPLANNING, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        DISPLAY_VIDEO_USER_MANAGEMENT(new MScopeInfo(DisplayVideoScopes.DISPLAY_VIDEO_USER_MANAGEMENT, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        DOUBLECLICKBIDMANAGER(new MScopeInfo(DisplayVideoScopes.DOUBLECLICKBIDMANAGER, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        ANDROID_PUBLISHER(new MScopeInfo(AndroidPublisherScopes.ANDROIDPUBLISHER, EnumSet.of(MSupportedLogins.OAUTHUSER)));

        private final MScopeInfo info;
        Ads(MScopeInfo info) { this.info = info; }
        @Override public MScopeInfo getInfo() { return info; }
    }

    // --- Admin ---
    public enum Admin implements MScopeEnum {
        ADMIN_DIRECTORY_USER(new MScopeInfo(DirectoryScopes.ADMIN_DIRECTORY_USER, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        ADMIN_REPORTS_USAGE_READONLY(new MScopeInfo(ReportsScopes.ADMIN_REPORTS_USAGE_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER)));

        private final MScopeInfo info;
        Admin(MScopeInfo info) { this.info = info; }
        @Override public MScopeInfo getInfo() { return info; }
    }

    // --- Play ---
    public enum Play implements MScopeEnum {
        PLAYDEVELOPERREPORTING(new MScopeInfo(PlaydeveloperreportingScopes.PLAYDEVELOPERREPORTING, EnumSet.of(MSupportedLogins.OAUTHUSER)));

        private final MScopeInfo info;
        Play(MScopeInfo info) { this.info = info; }
        @Override public MScopeInfo getInfo() { return info; }
    }

    // --- Fitness ---
    public enum Fitness implements MScopeEnum {
        FITNESS_ACTIVITY_READ(new MScopeInfo(FitnessScopes.FITNESS_ACTIVITY_READ, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        FITNESS_ACTIVITY_WRITE(new MScopeInfo(FitnessScopes.FITNESS_ACTIVITY_WRITE, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        FITNESS_BODY_READ(new MScopeInfo(FitnessScopes.FITNESS_BODY_READ, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        FITNESS_BODY_WRITE(new MScopeInfo(FitnessScopes.FITNESS_BODY_WRITE, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        FITNESS_LOCATION_READ(new MScopeInfo(FitnessScopes.FITNESS_LOCATION_READ, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        FITNESS_LOCATION_WRITE(new MScopeInfo(FitnessScopes.FITNESS_LOCATION_WRITE, EnumSet.of(MSupportedLogins.OAUTHUSER)));

        private final MScopeInfo info;
        Fitness(MScopeInfo info) { this.info = info; }
        @Override public MScopeInfo getInfo() { return info; }
    }

    // --- Tag Manager ---
    public enum TagManager implements MScopeEnum {
        TAGMANAGER_EDIT_CONTAINERS(new MScopeInfo(TagManagerScopes.TAGMANAGER_EDIT_CONTAINERS, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        TAGMANAGER_READONLY(new MScopeInfo(TagManagerScopes.TAGMANAGER_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        TAGMANAGER_PUBLISH(new MScopeInfo(TagManagerScopes.TAGMANAGER_PUBLISH, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        TAGMANAGER_MANAGE_ACCOUNTS(new MScopeInfo(TagManagerScopes.TAGMANAGER_MANAGE_ACCOUNTS, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        TAGMANAGER_MANAGE_USERS(new MScopeInfo(TagManagerScopes.TAGMANAGER_MANAGE_USERS, EnumSet.of(MSupportedLogins.OAUTHUSER)));

        private final MScopeInfo info;
        TagManager(MScopeInfo info) { this.info = info; }
        @Override public MScopeInfo getInfo() { return info; }
    }

    // --- Webmasters ---
    public enum Webmasters implements MScopeEnum {
        WEBMASTERS(new MScopeInfo(WebmastersScopes.WEBMASTERS, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        WEBMASTERS_READONLY(new MScopeInfo(WebmastersScopes.WEBMASTERS_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER)));

        private final MScopeInfo info;
        Webmasters(MScopeInfo info) { this.info = info; }
        @Override public MScopeInfo getInfo() { return info; }
    }


}
