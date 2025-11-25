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

/**
 * @author Marco Scherzer, Copyright Marco Scherzer
 * Web-API Scopes
 */
public final class MGoAuthScopes {

    private MGoAuthScopes() {}

    // --- Basis / Identity ---
    public static final class Identity {
        public static final String OPENID = Oauth2Scopes.OPENID;
        public static final String USERINFO_EMAIL = Oauth2Scopes.USERINFO_EMAIL;
        public static final String USERINFO_PROFILE = Oauth2Scopes.USERINFO_PROFILE;
    }

    // --- Gmail ---
    public static final class Gmail {
        public static final String SEND = GmailScopes.GMAIL_SEND;
        public static final String READONLY = GmailScopes.GMAIL_READONLY;
        public static final String COMPOSE = GmailScopes.GMAIL_COMPOSE;
        public static final String MODIFY = GmailScopes.GMAIL_MODIFY;
        public static final String LABELS = GmailScopes.GMAIL_LABELS;
        public static final String ALL = GmailScopes.GMAIL;
    }

    // --- Drive ---
    public static final class Drive {
        public static final String FULL = DriveScopes.DRIVE;
        public static final String FILE = DriveScopes.DRIVE_FILE;
        public static final String READONLY = DriveScopes.DRIVE_READONLY;
        public static final String METADATA_READONLY = DriveScopes.DRIVE_METADATA_READONLY;
        public static final String APPDATA = DriveScopes.DRIVE_APPDATA;
        public static final String SCRIPTS = DriveScopes.DRIVE_SCRIPTS;
    }

    // --- Calendar ---
    public static final class Calendar {
        public static final String FULL = CalendarScopes.CALENDAR;
        public static final String READONLY = CalendarScopes.CALENDAR_READONLY;
        public static final String EVENTS = CalendarScopes.CALENDAR_EVENTS;
        public static final String EVENTS_READONLY = CalendarScopes.CALENDAR_EVENTS_READONLY;
    }

    // --- People / Contacts ---
    public static final class People {
        public static final String FULL = PeopleServiceScopes.PEOPLE;
        public static final String READONLY = PeopleServiceScopes.PEOPLE_READONLY;
    }

    public static final class Contacts {
        public static final String FULL = ContactsScopes.CONTACTS;
        public static final String READONLY = ContactsScopes.CONTACTS_READONLY;
    }

    // --- Tasks ---
    public static final class Tasks {
        public static final String FULL = TasksScopes.TASKS;
    }

    // --- Classroom ---
    public static final class Classroom {
        public static final String COURSES = ClassroomScopes.CLASSROOM_COURSES;
        public static final String ROSTERS = ClassroomScopes.CLASSROOM_ROSTERS;
        public static final String ANNOUNCEMENTS = ClassroomScopes.CLASSROOM_ANNOUNCEMENTS;
    }

    // --- Chat ---
    public static final class Chat {
        public static final String FULL = ChatScopes.CHAT;
    }

    // --- YouTube ---
    public static final class YouTube {
        public static final String FULL = YouTubeScopes.YOUTUBE;
        public static final String READONLY = YouTubeScopes.YOUTUBE_READONLY;
        public static final String UPLOAD = YouTubeScopes.YOUTUBE_UPLOAD;
        public static final String FORCE_SSL = YouTubeScopes.YOUTUBE_FORCE_SSL;
        public static final String PARTNER = YouTubeScopes.YOUTUBEPARTNER;
        public static final String PARTNER_CHANNEL_AUDIT = YouTubeScopes.YOUTUBEPARTNER_CHANNEL_AUDIT;
    }

    // --- YouTube Analytics ---
    public static final class YouTubeAnalytics {
        public static final String READONLY = YouTubeAnalyticsScopes.YT_ANALYTICS_READONLY;
        public static final String MONETARY_READONLY = YouTubeAnalyticsScopes.YT_ANALYTICS_MONETARY_READONLY;
    }

    // --- Photos ---
    public static final class Photos {
        public static final String FULL = PhotosLibraryScopes.PHOTOSLIBRARY;
        public static final String READONLY = PhotosLibraryScopes.PHOTOSLIBRARY_READONLY;
    }

    // --- Analytics ---
    public static final class Analytics {
        public static final String FULL = AnalyticsScopes.ANALYTICS;
        public static final String READONLY = AnalyticsScopes.ANALYTICS_READONLY;
    }

    // --- Cloud Platform (Beispiel BigQuery) ---
    public static final class Cloud {
        public static final String PLATFORM = CloudResourceManagerScopes.CLOUD_PLATFORM;
        public static final String BIGQUERY = BigqueryScopes.BIGQUERY;
        public static final String BIGQUERY_READONLY = BigqueryScopes.BIGQUERY_READONLY;
        public static final String STORAGE_FULL = StorageScopes.DEVSTORAGE_FULL_CONTROL;
        public static final String STORAGE_RW = StorageScopes.DEVSTORAGE_READ_WRITE;
        public static final String STORAGE_RO = StorageScopes.DEVSTORAGE_READ_ONLY;
        // … weitere Cloud-Scopes analog ergänzen
    }

    // --- Ads / Monetarisierung ---
    public static final class Ads {
        public static final String ADWORDS = AdsenseScopes.ADSENSE;
        public static final String ADSENSE = AdsenseScopes.ADSENSE;
        public static final String ADSENSE_READONLY = AdsenseScopes.ADSENSE_READONLY;
        public static final String ADSENSE_HOST = AdsenseScopes.ADSENSEHOST;
        public static final String AD_EXCHANGE_BUYER = AdExchangeBuyerScopes.ADEXCHANGE_BUYER;
        public static final String AD_EXCHANGE_SELLER = AdExchangeSellerScopes.ADEXCHANGE_SELLER;
        public static final String AD_EXCHANGE_SELLER_READONLY = AdExchangeSellerScopes.ADEXCHANGE_SELLER_READONLY;
    }

    // --- Weitere APIs ---
    public static final class Admin {
        public static final String DIRECTORY = DirectoryScopes.ADMIN_DIRECTORY_USER;
        public static final String REPORTS = ReportsScopes.ADMIN_REPORTS_USAGE_READONLY;
    }

    // --- Play ---
    public static final class Play {
        public static final String DEVELOPER_REPORTING = PlayDeveloperReportingScopes.PLAYDEVELOPERREPORTING;
        public static final String CUSTOM_APP = PlayCustomAppScopes.PLAYCUSTOMAPP;
    }

    // --- Fitness ---
    public static final class Fitness {
        public static final String ACTIVITY_READ = FitnessScopes.FITNESS_ACTIVITY_READ;
        public static final String ACTIVITY_WRITE = FitnessScopes.FITNESS_ACTIVITY_WRITE;
        public static final String BODY_READ = FitnessScopes.FITNESS_BODY_READ;
        public static final String BODY_WRITE = FitnessScopes.FITNESS_BODY_WRITE;
        public static final String LOCATION_READ = FitnessScopes.FITNESS_LOCATION_READ;
        public static final String LOCATION_WRITE = FitnessScopes.FITNESS_LOCATION_WRITE;
        public static final String NUTRITION_READ = FitnessScopes.FITNESS_NUTRITION_READ;
        public static final String NUTRITION_WRITE = FitnessScopes.FITNESS_NUTRITION_WRITE;
    }

    // --- Tag Manager ---
    public static final class TagManager {
        public static final String FULL = TagManagerScopes.TAGMANAGER_EDIT_CONTAINERS;
        public static final String READONLY = TagManagerScopes.TAGMANAGER_READONLY;
        public static final String PUBLISH = TagManagerScopes.TAGMANAGER_PUBLISH;
        public static final String MANAGE_ACCOUNTS = TagManagerScopes.TAGMANAGER_MANAGE_ACCOUNTS;
        public static final String MANAGE_USERS = TagManagerScopes.TAGMANAGER_MANAGE_USERS;
    }

    // --- Webmasters (Search Console) ---
    public static final class Webmasters {
        public static final String FULL = WebmastersScopes.WEBMASTERS;
        public static final String READONLY = WebmastersScopes.WEBMASTERS_READONLY;
    }
}

