/**
 * @author Marco Scherzer, Copyright Marco Scherzer
 * G. Web-API Scopes
 * unready
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
import com.google.api.services.docs.v1.DocsScopes;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.slides.v1.SlidesScopes;
import com.google.api.services.keep.v1.KeepScopes;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.translate.v3.TranslateScopes;
import com.google.api.services.pubsub.PubsubScopes;
import com.google.api.services.spanner.v1.SpannerScopes;
import com.google.api.services.sqladmin.SQLAdminScopes;
import com.google.api.services.firestore.v1.FirestoreScopes;
import com.google.api.services.logging.v2.LoggingScopes;
import com.google.api.services.monitoring.v3.MonitoringScopes;
import com.google.api.services.cloudkms.v1.CloudKMSScopes;
import com.google.api.services.cloudiot.v1.CloudIotScopes;
import com.google.api.services.cloudfunctions.v2.CloudFunctionsScopes;
import com.google.api.services.run.v2.CloudRunScopes;
import com.google.api.services.container.ContainerScopes;
import com.google.api.services.deploymentmanager.DeploymentManagerScopes;
import com.google.api.services.servicenetworking.v1.ServiceNetworkingScopes;
import com.google.api.services.cloudidentity.v1.CloudIdentityScopes;
import com.google.api.services.iam.v2.IamScopes;
import com.google.api.services.ml.v1.CloudMachineLearningEngineScopes;
import com.google.api.services.dialogflow.v3.DialogflowScopes;
import com.google.api.services.apigee.v1.ApigeeScopes;
import com.google.api.services.cloudbilling.CloudbillingScopes;
import com.google.api.services.playcustomapp.PlaycustomappScopes;
import java.util.*;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer
 * G. Web-API Scopes
 * unready
 */
public class MGWebApis {

    private MGWebApis() {}

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    private static final Map<String, Class<? extends Enum<? extends MScopeEnum>>> ENUM_MAP = new HashMap<>();

    static {
        ENUM_MAP.put("Identity", Identity.class);
        ENUM_MAP.put("Gmail", Gmail.class);
        ENUM_MAP.put("Drive", Drive.class);
        ENUM_MAP.put("Calendar", Calendar.class);
        ENUM_MAP.put("People", People.class);
        ENUM_MAP.put("Tasks", Tasks.class);
        ENUM_MAP.put("Classroom", Classroom.class);
        ENUM_MAP.put("YouTube", YouTube.class);
        ENUM_MAP.put("YouTubeAnalytics", YouTubeAnalytics.class);
        ENUM_MAP.put("Photos", Photos.class);
        ENUM_MAP.put("Analytics", Analytics.class);
        ENUM_MAP.put("Cloud", Cloud.class);
        ENUM_MAP.put("Ads", Ads.class);
        ENUM_MAP.put("Admin", Admin.class);
        ENUM_MAP.put("Play", Play.class);
        ENUM_MAP.put("Fitness", Fitness.class);
        ENUM_MAP.put("TagManager", TagManager.class);
        ENUM_MAP.put("Webmasters", Webmasters.class);
        ENUM_MAP.put("Docs", Docs.class);
        ENUM_MAP.put("Sheets", Sheets.class);
        ENUM_MAP.put("Slides", Slides.class);
        ENUM_MAP.put("Keep", Keep.class);
        ENUM_MAP.put("Vision", Vision.class);
        ENUM_MAP.put("Translate", Translate.class);
        ENUM_MAP.put("Pubsub", Pubsub.class);
        ENUM_MAP.put("Spanner", Spanner.class);
        ENUM_MAP.put("SQLAdmin", SQLAdmin.class);
        ENUM_MAP.put("Firestore", Firestore.class);
        ENUM_MAP.put("Logging", Logging.class);
        ENUM_MAP.put("Monitoring", Monitoring.class);
        ENUM_MAP.put("CloudKMS", CloudKMS.class);
        ENUM_MAP.put("CloudIot", CloudIot.class);
        ENUM_MAP.put("CloudFunctions", CloudFunctions.class);
        ENUM_MAP.put("CloudRun", CloudRun.class);
        ENUM_MAP.put("Container", Container.class);
        ENUM_MAP.put("DeploymentManager", DeploymentManager.class);
        ENUM_MAP.put("ServiceNetworking", ServiceNetworking.class);
        ENUM_MAP.put("CloudIdentity", CloudIdentity.class);
        ENUM_MAP.put("Iam", Iam.class);
        ENUM_MAP.put("CloudML", CloudML.class);
        ENUM_MAP.put("Dialogflow", Dialogflow.class);
        ENUM_MAP.put("Apigee", Apigee.class);
        ENUM_MAP.put("Cloudbilling", Cloudbilling.class);
        ENUM_MAP.put("Playcustomapp", Playcustomapp.class);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public static Class<? extends Enum<? extends MScopeEnum>> get(String name) {
        return ENUM_MAP.get(name);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public static final Set<String> getAllWebApiNames() {
        return Collections.unmodifiableSet(ENUM_MAP.keySet());
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public static final Set<Class<? extends Enum<? extends MScopeEnum>>> getAllWebApis() {
        return Collections.unmodifiableSet(new HashSet<>(ENUM_MAP.values()));
    }

    /**
     * Login-Arten für Google Service APIs
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
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
     * unready
     */
    public static final class MScope {
        private final String url;
        private final EnumSet<MSupportedLogins> loginTypes;
        /**
         * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         * unready
         */
        public MScope(String goscope, EnumSet<MSupportedLogins> loginTypes) {
            this.url = goscope;
            this.loginTypes = loginTypes;
        }
        /**
         * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public String getUrl() {
            return url;
        }
        /**
         * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public final EnumSet<MSupportedLogins> getLoginTypes() {
            return loginTypes;
        }
        /**
         * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        @Override
        public final String toString() {
            return url;
        }
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * Web-API Scopes
     * unready
     */
    //Interface für alle Scope-Enums
    public static interface MScopeEnum {
        public String get();
        @Override public String toString();
        public EnumSet<MSupportedLogins> getLoginTypes();
    }


    // --- Basis / Identity ---
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Identity implements MScopeEnum {
        PLUS_ME(new MScope(Oauth2Scopes.PLUS_ME, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        PLUS_LOGIN(new MScope(Oauth2Scopes.PLUS_LOGIN, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        USERINFO_EMAIL(new MScope(Oauth2Scopes.USERINFO_EMAIL, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        USERINFO_PROFILE(new MScope(Oauth2Scopes.USERINFO_PROFILE, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        Identity(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- Gmail ---
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Gmail implements MScopeEnum {
        GMAIL_SEND(new MScope(GmailScopes.GMAIL_SEND, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        GMAIL_READONLY(new MScope(GmailScopes.GMAIL_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        GMAIL_COMPOSE(new MScope(GmailScopes.GMAIL_COMPOSE, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        GMAIL_MODIFY(new MScope(GmailScopes.GMAIL_MODIFY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        GMAIL_LABELS(new MScope(GmailScopes.GMAIL_LABELS, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        Gmail(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- Drive ---
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Drive implements MScopeEnum {
        DRIVE(new MScope(DriveScopes.DRIVE, EnumSet.of(MSupportedLogins.OAUTHUSER, MSupportedLogins.OAUTHSERVICEACCOUNT))),
        DRIVE_FILE(new MScope(DriveScopes.DRIVE_FILE, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        DRIVE_READONLY(new MScope(DriveScopes.DRIVE_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        DRIVE_METADATA_READONLY(new MScope(DriveScopes.DRIVE_METADATA_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        DRIVE_APPDATA(new MScope(DriveScopes.DRIVE_APPDATA, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        DRIVE_SCRIPTS(new MScope(DriveScopes.DRIVE_SCRIPTS, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        Drive(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- Calendar ---
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Calendar implements MScopeEnum {
        CALENDAR_READONLY(new MScope(CalendarScopes.CALENDAR_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        CALENDAR(new MScope(CalendarScopes.CALENDAR, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        Calendar(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- People / Contacts ---
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum People implements MScopeEnum {
        CONTACTS(new MScope(PeopleServiceScopes.CONTACTS, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        CONTACTS_READONLY(new MScope(PeopleServiceScopes.CONTACTS_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        USERINFO_EMAIL(new MScope(PeopleServiceScopes.USERINFO_EMAIL, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        USERINFO_PROFILE(new MScope(PeopleServiceScopes.USERINFO_PROFILE, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        People(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- Tasks ---
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Tasks implements MScopeEnum {
        TASKS(new MScope(TasksScopes.TASKS, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        TASKS_READONLY(new MScope(TasksScopes.TASKS_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        Tasks(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- Classroom ---
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Classroom implements MScopeEnum {
        CLASSROOM_COURSES(new MScope(ClassroomScopes.CLASSROOM_COURSES, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        CLASSROOM_ROSTERS(new MScope(ClassroomScopes.CLASSROOM_ROSTERS, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        CLASSROOM_ANNOUNCEMENTS(new MScope(ClassroomScopes.CLASSROOM_ANNOUNCEMENTS, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        Classroom(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- YouTube ---
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum YouTube implements MScopeEnum {
        YOUTUBE(new MScope(YouTubeScopes.YOUTUBE, EnumSet.of(MSupportedLogins.OAUTHUSER, MSupportedLogins.APIKEY))),
        YOUTUBE_READONLY(new MScope(YouTubeScopes.YOUTUBE_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER, MSupportedLogins.APIKEY))),
        YOUTUBE_UPLOAD(new MScope(YouTubeScopes.YOUTUBE_UPLOAD, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        YOUTUBE_FORCE_SSL(new MScope(YouTubeScopes.YOUTUBE_FORCE_SSL, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        YOUTUBEPARTNER(new MScope(YouTubeScopes.YOUTUBEPARTNER, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        YOUTUBEPARTNER_CHANNEL_AUDIT(new MScope(YouTubeScopes.YOUTUBEPARTNER_CHANNEL_AUDIT, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        YouTube(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- YouTube Analytics ---
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum YouTubeAnalytics implements MScopeEnum {
        YT_ANALYTICS_READONLY(new MScope(YouTubeAnalyticsScopes.YT_ANALYTICS_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        YT_ANALYTICS_MONETARY_READONLY(new MScope(YouTubeAnalyticsScopes.YT_ANALYTICS_MONETARY_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        YouTubeAnalytics(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- Photos ---
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Photos implements MScopeEnum {
        PHOTOSLIBRARY(new MScope(PhotosLibraryScopes.PHOTOSLIBRARY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        PHOTOSLIBRARY_READONLY(new MScope(PhotosLibraryScopes.PHOTOSLIBRARY_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        Photos(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- Analytics ---
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Analytics implements MScopeEnum {
        ANALYTICS(new MScope(AnalyticsScopes.ANALYTICS, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        ANALYTICS_READONLY(new MScope(AnalyticsScopes.ANALYTICS_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        Analytics(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- Cloud Platform ---
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Cloud implements MScopeEnum {
        CLOUD_PLATFORM(new MScope(CloudResourceManagerScopes.CLOUD_PLATFORM, EnumSet.of(MSupportedLogins.OAUTHUSER, MSupportedLogins.OAUTHSERVICEACCOUNT))),
        BIGQUERY(new MScope(BigqueryScopes.BIGQUERY, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT))),
        BIGQUERY_READONLY(new MScope(BigqueryScopes.BIGQUERY_READONLY, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT))),
        DEVSTORAGE_FULL_CONTROL(new MScope(StorageScopes.DEVSTORAGE_FULL_CONTROL, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT))),
        DEVSTORAGE_READ_WRITE(new MScope(StorageScopes.DEVSTORAGE_READ_WRITE, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT))),
        DEVSTORAGE_READ_ONLY(new MScope(StorageScopes.DEVSTORAGE_READ_ONLY, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT)));
        private final MScope info;
        Cloud(MScope info) { this.info = info;}
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- Ads / Monetarisierung ---
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Ads implements MScopeEnum {
        ADSENSE(new MScope(AdsenseScopes.ADSENSE, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        ADSENSE_READONLY(new MScope(AdsenseScopes.ADSENSE_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        AD_EXCHANGE_BUYER(new MScope(AdExchangeBuyerScopes.ADEXCHANGE_BUYER, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        AD_EXCHANGE_SELLER(new MScope(AdExchangeSellerScopes.ADEXCHANGE_SELLER, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        AD_EXCHANGE_SELLER_READONLY(new MScope(AdExchangeSellerScopes.ADEXCHANGE_SELLER_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        DISPLAY_VIDEO(new MScope(DisplayVideoScopes.DISPLAY_VIDEO, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        DISPLAY_VIDEO_MEDIAPLANNING(new MScope(DisplayVideoScopes.DISPLAY_VIDEO_MEDIAPLANNING, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        DISPLAY_VIDEO_USER_MANAGEMENT(new MScope(DisplayVideoScopes.DISPLAY_VIDEO_USER_MANAGEMENT, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        DOUBLECLICKBIDMANAGER(new MScope(DisplayVideoScopes.DOUBLECLICKBIDMANAGER, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        ANDROID_PUBLISHER(new MScope(AndroidPublisherScopes.ANDROIDPUBLISHER, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        Ads(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- Admin ---
    public enum Admin implements MScopeEnum {
        ADMIN_DIRECTORY_USER(new MScope(DirectoryScopes.ADMIN_DIRECTORY_USER, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        ADMIN_REPORTS_USAGE_READONLY(new MScope(ReportsScopes.ADMIN_REPORTS_USAGE_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        Admin(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- Play ---
    public enum Play implements MScopeEnum {
        PLAYDEVELOPERREPORTING(new MScope(PlaydeveloperreportingScopes.PLAYDEVELOPERREPORTING, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        Play(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- Fitness ---
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Fitness implements MScopeEnum {
        FITNESS_ACTIVITY_READ(new MScope(FitnessScopes.FITNESS_ACTIVITY_READ, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        FITNESS_ACTIVITY_WRITE(new MScope(FitnessScopes.FITNESS_ACTIVITY_WRITE, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        FITNESS_BODY_READ(new MScope(FitnessScopes.FITNESS_BODY_READ, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        FITNESS_BODY_WRITE(new MScope(FitnessScopes.FITNESS_BODY_WRITE, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        FITNESS_LOCATION_READ(new MScope(FitnessScopes.FITNESS_LOCATION_READ, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        FITNESS_LOCATION_WRITE(new MScope(FitnessScopes.FITNESS_LOCATION_WRITE, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        Fitness(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- Tag Manager ---
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum TagManager implements MScopeEnum {
        TAGMANAGER_EDIT_CONTAINERS(new MScope(TagManagerScopes.TAGMANAGER_EDIT_CONTAINERS, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        TAGMANAGER_READONLY(new MScope(TagManagerScopes.TAGMANAGER_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        TAGMANAGER_PUBLISH(new MScope(TagManagerScopes.TAGMANAGER_PUBLISH, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        TAGMANAGER_MANAGE_ACCOUNTS(new MScope(TagManagerScopes.TAGMANAGER_MANAGE_ACCOUNTS, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        TAGMANAGER_MANAGE_USERS(new MScope(TagManagerScopes.TAGMANAGER_MANAGE_USERS, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        TagManager(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- Webmasters ---
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Webmasters implements MScopeEnum {
        WEBMASTERS(new MScope(WebmastersScopes.WEBMASTERS, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        WEBMASTERS_READONLY(new MScope(WebmastersScopes.WEBMASTERS_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        Webmasters(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- Produktivität ---
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Docs implements MScopeEnum {
        DOCUMENTS(new MScope(DocsScopes.DOCUMENTS, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        DOCUMENTS_READONLY(new MScope(DocsScopes.DOCUMENTS_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        Docs(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Sheets implements MScopeEnum {
        SPREADSHEETS(new MScope(SheetsScopes.SPREADSHEETS, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        SPREADSHEETS_READONLY(new MScope(SheetsScopes.SPREADSHEETS_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        Sheets(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Slides implements MScopeEnum {
        PRESENTATIONS(new MScope(SlidesScopes.PRESENTATIONS, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        PRESENTATIONS_READONLY(new MScope(SlidesScopes.PRESENTATIONS_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        Slides(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Keep implements MScopeEnum {
        KEEP(new MScope(KeepScopes.KEEP, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        KEEP_READONLY(new MScope(KeepScopes.KEEP_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        Keep(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- KI / Vision / Übersetzung ---
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Vision implements MScopeEnum {
        CLOUD_PLATFORM(new MScope(VisionScopes.CLOUD_PLATFORM, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        Vision(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Translate implements MScopeEnum {
        CLOUD_TRANSLATION(new MScope(TranslateScopes.CLOUD_TRANSLATION, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        Translate(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- Cloud Platform ---
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Pubsub implements MScopeEnum {
        PUBSUB(new MScope(PubsubScopes.PUBSUB, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT))),
        CLOUD_PLATFORM(new MScope(PubsubScopes.CLOUD_PLATFORM, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT)));
        private final MScope info;
        Pubsub(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }


    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Spanner implements MScopeEnum {
        CLOUD_PLATFORM(new MScope(SpannerScopes.CLOUD_PLATFORM, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT)));
        private final MScope info;
        Spanner(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum SQLAdmin implements MScopeEnum {
        SQLSERVICE_ADMIN(new MScope(SQLAdminScopes.SQLSERVICE_ADMIN, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT)));
        private final MScope info;
        SQLAdmin(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Firestore implements MScopeEnum {
        CLOUD_PLATFORM(new MScope(FirestoreScopes.CLOUD_PLATFORM, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT)));
        private final MScope info;
        Firestore(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Logging implements MScopeEnum {
        LOGGING_ADMIN(new MScope(LoggingScopes.LOGGING_ADMIN, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT))),
        LOGGING_READ(new MScope(LoggingScopes.LOGGING_READ, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT)));
        private final MScope info;
        Logging(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Monitoring implements MScopeEnum {
        MONITORING(new MScope(MonitoringScopes.MONITORING, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT))),
        MONITORING_READ(new MScope(MonitoringScopes.MONITORING_READ, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT)));
        private final MScope info;
        Monitoring(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum CloudKMS implements MScopeEnum {
        CLOUD_PLATFORM(new MScope(CloudKMSScopes.CLOUD_PLATFORM, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT)));
        private final MScope info;
        CloudKMS(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum CloudIot implements MScopeEnum {
        CLOUD_PLATFORM(new MScope(CloudIotScopes.CLOUD_PLATFORM, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT)));
        private final MScope info;
        CloudIot(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum CloudFunctions implements MScopeEnum {
        CLOUD_PLATFORM(new MScope(CloudFunctionsScopes.CLOUD_PLATFORM, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT)));
        private final MScope info;
        CloudFunctions(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum CloudRun implements MScopeEnum {
        CLOUD_PLATFORM(new MScope(CloudRunScopes.CLOUD_PLATFORM, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT)));
        private final MScope info;
        CloudRun(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Container implements MScopeEnum {
        CLOUD_PLATFORM(new MScope(ContainerScopes.CLOUD_PLATFORM, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT)));
        private final MScope info;
        Container(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum DeploymentManager implements MScopeEnum {
        CLOUD_PLATFORM(new MScope(DeploymentManagerScopes.CLOUD_PLATFORM, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT)));
        private final MScope info;
        DeploymentManager(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum ServiceNetworking implements MScopeEnum {
        CLOUD_PLATFORM(new MScope(ServiceNetworkingScopes.CLOUD_PLATFORM, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT)));
        private final MScope info;
        ServiceNetworking(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum CloudIdentity implements MScopeEnum {
        CLOUD_IDENTITY_GROUPS(new MScope(CloudIdentityScopes.CLOUD_IDENTITY_GROUPS, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        CLOUD_IDENTITY_GROUPS_READONLY(new MScope(CloudIdentityScopes.CLOUD_IDENTITY_GROUPS_READONLY, EnumSet.of(MSupportedLogins.OAUTHUSER))),
        CLOUD_IDENTITY_CLOUD_PLATFORM(new MScope(CloudIdentityScopes.CLOUD_PLATFORM, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        CloudIdentity(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Iam implements MScopeEnum {
        CLOUD_PLATFORM(new MScope(IamScopes.CLOUD_PLATFORM, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT)));
        private final MScope info;
        Iam(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum CloudML implements MScopeEnum {
        CLOUD_PLATFORM(new MScope(CloudMachineLearningEngineScopes.CLOUD_PLATFORM, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT)));
        private final MScope info;
        CloudML(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Dialogflow implements MScopeEnum {
        CLOUD_PLATFORM(new MScope(DialogflowScopes.CLOUD_PLATFORM, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT)));
        private final MScope info;
        Dialogflow(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Apigee implements MScopeEnum {
        CLOUD_PLATFORM(new MScope(ApigeeScopes.CLOUD_PLATFORM, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT)));
        private final MScope info;
        Apigee(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- Cloud Platform / Billing ---
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Cloudbilling implements MScopeEnum {
        CLOUD_PLATFORM(new MScope(CloudbillingScopes.CLOUD_PLATFORM, EnumSet.of(MSupportedLogins.OAUTHSERVICEACCOUNT)));

        private final MScope info;
        Cloudbilling(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }

    // --- Play Custom App ---
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer
     * unready
     */
    public enum Playcustomapp implements MScopeEnum {
        ANDROIDPUBLISHER(new MScope(PlaycustomappScopes.ANDROIDPUBLISHER, EnumSet.of(MSupportedLogins.OAUTHUSER)));
        private final MScope info;
        Playcustomapp(MScope info) { this.info = info; }
        public final String get() { return info.getUrl(); }
        @Override public final String toString() { return info.getUrl(); }
        public final EnumSet<MSupportedLogins> getLoginTypes() { return info.getLoginTypes(); }
    }



}
