/**
 * @author Marco Scherzer, Copyright Marco Scherzer
 * Web-API Scopes
 */
package com.marcoscherzer.msimplegoauthhelper.scopes;

// Basis / Identity
import com.google.api.services.oauth2.Oauth2Scopes;

// Produktivität
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.docs.v1.DocsScopes;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.slides.v1.SlidesScopes;
import com.google.api.services.keep.v1.KeepScopes;

// Kommunikation
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.google.api.services.contacts.ContactsScopes;
import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.classroom.ClassroomScopes;
import com.google.api.services.chat.v1.ChatScopes;

// Video / Medien
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtubeAnalytics.YouTubeAnalyticsScopes;
import com.google.api.services.photoslibrary.v1.PhotosLibraryScopes;

// Analytics
import com.google.api.services.analytics.AnalyticsScopes;

// Cloud Platform
import com.google.api.services.cloudresourcemanager.CloudResourceManagerScopes;
import com.google.api.services.bigquery.BigqueryScopes;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.translate.TranslateScopes;
import com.google.api.services.pubsub.PubsubScopes;
import com.google.api.services.spanner.SpannerScopes;
import com.google.api.services.sqladmin.SQLAdminScopes;
import com.google.api.services.firestore.v1.FirestoreScopes;
import com.google.api.services.logging.LoggingScopes;
import com.google.api.services.monitoring.MonitoringScopes;
import com.google.api.services.cloudkms.v1.CloudKMSScopes;
import com.google.api.services.cloudiot.v1.CloudIotScopes;
import com.google.api.services.cloudfunctions.v1.CloudFunctionsScopes;
import com.google.api.services.run.v1.CloudRunScopes;
import com.google.api.services.container.v1.ContainerScopes;
import com.google.api.services.deploymentmanager.DeploymentManagerScopes;
import com.google.api.services.servicenetworking.v1.ServiceNetworkingScopes;
import com.google.api.services.cloudidentity.v1.CloudIdentityScopes;
import com.google.api.services.iam.v1.IamScopes;
import com.google.api.services.ml.v1.CloudMachineLearningEngineScopes;
import com.google.api.services.dialogflow.v2.DialogflowScopes;
import com.google.api.services.apigee.v1.ApigeeScopes;
import com.google.api.services.cloudbilling.v1.CloudBillingScopes;

// Ads / Monetarisierung
import com.google.api.services.adsense.AdsenseScopes;
import com.google.api.services.adexchangebuyer.AdExchangeBuyerScopes;
import com.google.api.services.adexchangeseller.AdExchangeSellerScopes;
import com.google.api.services.displayvideo.DisplayVideoScopes;
import com.google.api.services.ddmconversions.DdmConversionsScopes;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.api.services.mybusiness.MyBusinessScopes;

// Weitere APIs
import com.google.api.services.admin.directory.DirectoryScopes;
import com.google.api.services.admin.reports.ReportsScopes;
import com.google.api.services.playdeveloperreporting.PlayDeveloperReportingScopes;
import com.google.api.services.playcustomapp.PlayCustomAppScopes;
import com.google.api.services.fitness.FitnessScopes;
import com.google.api.services.tagmanager.TagManagerScopes;
import com.google.api.services.webmasters.WebmastersScopes;

import java.util.EnumSet;
/**
 * @author Marco Scherzer, Copyright Marco Scherzer
 * Web-API Scopes
 */
public class MGoAuthScopes2 {

    private MGoAuthScopes2() {}

        // --- Basis / Identity ---
        public static final class Identity {
            public static final MScopeInfo OPENID =
                    new MScopeInfo(Oauth2Scopes.OPENID, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo USERINFO_EMAIL =
                    new MScopeInfo(Oauth2Scopes.USERINFO_EMAIL, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo USERINFO_PROFILE =
                    new MScopeInfo(Oauth2Scopes.USERINFO_PROFILE, EnumSet.of(MSupportedLogins.OAUTH_USER));
        }

        // --- Gmail ---
        public static final class Gmail {
            public static final MScopeInfo SEND =
                    new MScopeInfo(GmailScopes.GMAIL_SEND, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo READONLY =
                    new MScopeInfo(GmailScopes.GMAIL_READONLY, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo COMPOSE =
                    new MScopeInfo(GmailScopes.GMAIL_COMPOSE, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo MODIFY =
                    new MScopeInfo(GmailScopes.GMAIL_MODIFY, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo LABELS =
                    new MScopeInfo(GmailScopes.GMAIL_LABELS, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo ALL =
                    new MScopeInfo(GmailScopes.GMAIL, EnumSet.of(MSupportedLogins.OAUTH_USER));
        }

        // --- Drive ---
        public static final class Drive {
            public static final MScopeInfo FULL =
                    new MScopeInfo(DriveScopes.DRIVE, EnumSet.of(MSupportedLogins.OAUTH_USER, MSupportedLogins.OAUTH_SERVICE_ACCOUNT));
            public static final MScopeInfo FILE =
                    new MScopeInfo(DriveScopes.DRIVE_FILE, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo READONLY =
                    new MScopeInfo(DriveScopes.DRIVE_READONLY, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo METADATA_READONLY =
                    new MScopeInfo(DriveScopes.DRIVE_METADATA_READONLY, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo APPDATA =
                    new MScopeInfo(DriveScopes.DRIVE_APPDATA, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo SCRIPTS =
                    new MScopeInfo(DriveScopes.DRIVE_SCRIPTS, EnumSet.of(MSupportedLogins.OAUTH_USER));
        }

        // --- Calendar ---
        public static final class Calendar {
            public static final MScopeInfo FULL =
                    new MScopeInfo(CalendarScopes.CALENDAR, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo READONLY =
                    new MScopeInfo(CalendarScopes.CALENDAR_READONLY, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo EVENTS =
                    new MScopeInfo(CalendarScopes.CALENDAR_EVENTS, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo EVENTS_READONLY =
                    new MScopeInfo(CalendarScopes.CALENDAR_EVENTS_READONLY, EnumSet.of(MSupportedLogins.OAUTH_USER));
        }

        // --- People / Contacts ---
        public static final class People {
            public static final MScopeInfo FULL =
                    new MScopeInfo(PeopleServiceScopes.PEOPLE, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo READONLY =
                    new MScopeInfo(PeopleServiceScopes.PEOPLE_READONLY, EnumSet.of(MSupportedLogins.OAUTH_USER));
        }

        public static final class Contacts {
            public static final MScopeInfo FULL =
                    new MScopeInfo(ContactsScopes.CONTACTS, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo READONLY =
                    new MScopeInfo(ContactsScopes.CONTACTS_READONLY, EnumSet.of(MSupportedLogins.OAUTH_USER));
        }

        // --- Tasks ---
        public static final class Tasks {
            public static final MScopeInfo FULL =
                    new MScopeInfo(TasksScopes.TASKS, EnumSet.of(MSupportedLogins.OAUTH_USER));
        }

        // --- Classroom ---
        public static final class Classroom {
            public static final MScopeInfo COURSES =
                    new MScopeInfo(ClassroomScopes.CLASSROOM_COURSES, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo ROSTERS =
                    new MScopeInfo(ClassroomScopes.CLASSROOM_ROSTERS, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo ANNOUNCEMENTS =
                    new MScopeInfo(ClassroomScopes.CLASSROOM_ANNOUNCEMENTS, EnumSet.of(MSupportedLogins.OAUTH_USER));
        }

        // --- Chat ---
        public static final class Chat {
            public static final MScopeInfo FULL =
                    new MScopeInfo(ChatScopes.CHAT, EnumSet.of(MSupportedLogins.OAUTH_USER));
        }

        // --- YouTube ---
        public static final class YouTube {
            public static final MScopeInfo FULL =
                    new MScopeInfo(YouTubeScopes.YOUTUBE, EnumSet.of(MSupportedLogins.OAUTH_USER, MSupportedLogins.API_KEY));
            public static final MScopeInfo READONLY =
                    new MScopeInfo(YouTubeScopes.YOUTUBE_READONLY, EnumSet.of(MSupportedLogins.OAUTH_USER, MSupportedLogins.API_KEY));
            public static final MScopeInfo UPLOAD =
                    new MScopeInfo(YouTubeScopes.YOUTUBE_UPLOAD, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo FORCE_SSL =
                    new MScopeInfo(YouTubeScopes.YOUTUBE_FORCE_SSL, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo PARTNER =
                    new MScopeInfo(YouTubeScopes.YOUTUBEPARTNER, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo PARTNER_CHANNEL_AUDIT =
                    new MScopeInfo(YouTubeScopes.YOUTUBEPARTNER_CHANNEL_AUDIT, EnumSet.of(MSupportedLogins.OAUTH_USER));
        }

        // --- YouTube Analytics ---
        public static final class YouTubeAnalytics {
            public static final MScopeInfo READONLY =
                    new MScopeInfo(YouTubeAnalyticsScopes.YT_ANALYTICS_READONLY, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo MONETARY_READONLY =
                    new MScopeInfo(YouTubeAnalyticsScopes.YT_ANALYTICS_MONETARY_READONLY, EnumSet.of(MSupportedLogins.OAUTH_USER));
        }

        // --- Photos ---
        public static final class Photos {
            public static final MScopeInfo FULL =
                    new MScopeInfo(PhotosLibraryScopes.PHOTOSLIBRARY, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo READONLY =
                    new MScopeInfo(PhotosLibraryScopes.PHOTOSLIBRARY_READONLY, EnumSet.of(MSupportedLogins.OAUTH_USER));
        }

        // --- Analytics ---
        public static final class Analytics {
            public static final MScopeInfo FULL =
                    new MScopeInfo(AnalyticsScopes.ANALYTICS, EnumSet.of(MSupportedLogins.OAUTH_USER));
            public static final MScopeInfo READONLY =
                    new MScopeInfo(AnalyticsScopes.ANALYTICS_READONLY, EnumSet.of(MSupportedLogins.OAUTH_USER));
        }

        // --- Cloud Platform (Beispiel BigQuery) ---
        public static final class Cloud {
            public static final MScopeInfo PLATFORM =
                    new MScopeInfo(CloudResourceManagerScopes.CLOUD_PLATFORM, EnumSet.of(MSupportedLogins.OAUTH_USER, MSupportedLogins.OAUTH_SERVICE_ACCOUNT));
            public static final MScopeInfo BIGQUERY =
                    new MScopeInfo(BigqueryScopes.BIGQUERY, EnumSet.of(MSupportedLogins.OAUTH_SERVICE_ACCOUNT));
            public static final MScopeInfo BIGQUERY_READONLY =
                    new MScopeInfo(BigqueryScopes.BIGQUERY_READONLY, EnumSet.of(MSupportedLogins.OAUTH_SERVICE_ACCOUNT));
            public static final MScopeInfo STORAGE_FULL =
                    new MScopeInfo(StorageScopes.DEVSTORAGE_FULL_CONTROL, EnumSet.of(MSupportedLogins.OAUTH_SERVICE_ACCOUNT));
            public static final MScopeInfo STORAGE_RW =
                    new MScopeInfo(StorageScopes.DEVSTORAGE_READ_WRITE, EnumSet.of(MSupportedLogins.OAUTH_SERVICE_ACCOUNT));
            public static final MScopeInfo STORAGE_RO =
                    new MScopeInfo(StorageScopes.DEVSTORAGE_READ_ONLY, EnumSet.of(MSupportedLogins.OAUTH_SERVICE_ACCOUNT));
        }

    // --- Ads / Monetarisierung ---
    public static final class Ads {
        public static final MScopeInfo ADSENSE =
                new MScopeInfo(AdsenseScopes.ADSENSE, EnumSet.of(MSupportedLogins.OAUTH_USER));
        public static final MScopeInfo ADSENSE_READONLY =
                new MScopeInfo(AdsenseScopes.ADSENSE_READONLY, EnumSet.of(MSupportedLogins.OAUTH_USER));
        public static final MScopeInfo ADSENSE_HOST =
                new MScopeInfo(AdsenseScopes.ADSENSEHOST, EnumSet.of(MSupportedLogins.OAUTH_USER));

        public static final MScopeInfo AD_EXCHANGE_BUYER =
                new MScopeInfo(AdExchangeBuyerScopes.ADEXCHANGE_BUYER, EnumSet.of(MSupportedLogins.OAUTH_USER));
        public static final MScopeInfo AD_EXCHANGE_SELLER =
                new MScopeInfo(AdExchangeSellerScopes.ADEXCHANGE_SELLER, EnumSet.of(MSupportedLogins.OAUTH_USER));
        public static final MScopeInfo AD_EXCHANGE_SELLER_READONLY =
                new MScopeInfo(AdExchangeSellerScopes.ADEXCHANGE_SELLER_READONLY, EnumSet.of(MSupportedLogins.OAUTH_USER));

        public static final MScopeInfo DISPLAY_VIDEO =
                new MScopeInfo(DisplayVideoScopes.DISPLAY_VIDEO, EnumSet.of(MSupportedLogins.OAUTH_USER));
        public static final MScopeInfo DISPLAY_VIDEO_READONLY =
                new MScopeInfo(DisplayVideoScopes.DISPLAY_VIDEO_READONLY, EnumSet.of(MSupportedLogins.OAUTH_USER));

        public static final MScopeInfo DDM_CONVERSIONS =
                new MScopeInfo(DdmConversionsScopes.DDMCONVERSIONS, EnumSet.of(MSupportedLogins.OAUTH_USER));

        public static final MScopeInfo ANDROID_PUBLISHER =
                new MScopeInfo(AndroidPublisherScopes.ANDROIDPUBLISHER, EnumSet.of(MSupportedLogins.OAUTH_USER));

        public static final MScopeInfo MY_BUSINESS =
                new MScopeInfo(MyBusinessScopes.MYBUSINESS, EnumSet.of(MSupportedLogins.OAUTH_USER));
    }

    public static final class Admin {
        public static final MScopeInfo DIRECTORY =
                new MScopeInfo(DirectoryScopes.ADMIN_DIRECTORY_USER, EnumSet.of(MSupportedLogins.OAUTH_USER));
        public static final MScopeInfo REPORTS =
                new MScopeInfo(ReportsScopes.ADMIN_REPORTS_USAGE_READONLY, EnumSet.of(MSupportedLogins.OAUTH_USER));
    }

    public static final class Play {
        public static final MScopeInfo DEVELOPER_REPORTING =
                new MScopeInfo(PlayDeveloperReportingScopes.PLAYDEVELOPERREPORTING, EnumSet.of(MSupportedLogins.OAUTH_USER));
        public static final MScopeInfo CUSTOM_APP =
                new MScopeInfo(PlayCustomAppScopes.PLAYCUSTOMAPP, EnumSet.of(MSupportedLogins.OAUTH_USER));
    }

    public static final class Fitness {
        public static final MScopeInfo ACTIVITY_READ =
                new MScopeInfo(FitnessScopes.FITNESS_ACTIVITY_READ, EnumSet.of(MSupportedLogins.OAUTH_USER));
        public static final MScopeInfo ACTIVITY_WRITE =
                new MScopeInfo(FitnessScopes.FITNESS_ACTIVITY_WRITE, EnumSet.of(MSupportedLogins.OAUTH_USER));
        public static final MScopeInfo BODY_READ =
                new MScopeInfo(FitnessScopes.FITNESS_BODY_READ, EnumSet.of(MSupportedLogins.OAUTH_USER));
        public static final MScopeInfo BODY_WRITE =
                new MScopeInfo(FitnessScopes.FITNESS_BODY_WRITE, EnumSet.of(MSupportedLogins.OAUTH_USER));
        public static final MScopeInfo LOCATION_READ =
                new MScopeInfo(FitnessScopes.FITNESS_LOCATION_READ, EnumSet.of(MSupportedLogins.OAUTH_USER));
        public static final MScopeInfo LOCATION_WRITE =
                new MScopeInfo(FitnessScopes.FITNESS_LOCATION_WRITE, EnumSet.of(MSupportedLogins.OAUTH_USER));
        public static final MScopeInfo NUTRITION_READ =
                new MScopeInfo(FitnessScopes.FITNESS_NUTRITION_READ, EnumSet.of(MSupportedLogins.OAUTH_USER));
        public static final MScopeInfo NUTRITION_WRITE =
                new MScopeInfo(FitnessScopes.FITNESS_NUTRITION_WRITE, EnumSet.of(MSupportedLogins.OAUTH_USER));
    }

    public static final class TagManager {
        public static final MScopeInfo FULL =
                new MScopeInfo(TagManagerScopes.TAGMANAGER_EDIT_CONTAINERS, EnumSet.of(MSupportedLogins.OAUTH_USER));
        public static final MScopeInfo READONLY =
                new MScopeInfo(TagManagerScopes.TAGMANAGER_READONLY, EnumSet.of(MSupportedLogins.OAUTH_USER));
        public static final MScopeInfo PUBLISH =
                new MScopeInfo(TagManagerScopes.TAGMANAGER_PUBLISH, EnumSet.of(MSupportedLogins.OAUTH_USER));
        public static final MScopeInfo MANAGE_ACCOUNTS =
                new MScopeInfo(TagManagerScopes.TAGMANAGER_MANAGE_ACCOUNTS, EnumSet.of(MSupportedLogins.OAUTH_USER));
        public static final MScopeInfo MANAGE_USERS =
                new MScopeInfo(TagManagerScopes.TAGMANAGER_MANAGE_USERS, EnumSet.of(MSupportedLogins.OAUTH_USER));
    }

    public static final class Webmasters {
        public static final MScopeInfo FULL =
                new MScopeInfo(WebmastersScopes.WEBMASTERS, EnumSet.of(MSupportedLogins.OAUTH_USER));
        public static final MScopeInfo READONLY =
                new MScopeInfo(WebmastersScopes.WEBMASTERS_READONLY, EnumSet.of(MSupportedLogins.OAUTH_USER));
    }

}
