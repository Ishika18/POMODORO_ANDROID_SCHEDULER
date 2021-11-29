# My Pomodoro

## Android Mobile app to create a Schedule for you.

# Generate SHA1 key for your project

1. Click "run anything" in android studio, to run gradle commands.
   Note - you can also click "double ctr" if you are using Android Studio
2. Run "gradle signingreport"
3. Save your SHA1 key for later use.
4. Make sure to add the same SHA1 key to google console and firebase project.
   Note - Adding to firebase project settings (SHA certificate fingerprints) 
   auto updates google console. In case of any error, double check if the key
   is the same.

# Google OAuth Initlization

https://developers.google.com/identity/sign-in/android/start-integrating
1. Configure a google project, if not already done.
2. Generate SHA1 key for your project, you will need this to create oauth credentials.
3. Create new android Oauth credential for your app, it will create 2 oauth clients for you.
4. Add the web oauth client inside "google_services.json" oauth_client.
5. Add the web oauth client to the string resources with name "server_client_id"

# Firebase Initalization
https://firebase.google.com/

1. Make a firebase account 
2. Go to firebase console and create a new app if you don't have one made already.
3. Register the app and add firebase SDKs to build.gradle of your app.
4. Update your "google_services.json" file.
5. Go to firebase project settings and add SHA1 key to the SHA certificate fingerprints. 

# How App Works?
1. Sign in using your google Account.
2. Click "+" on action menu and create new tasks or commitments.
3. Go to the calendar view, pick the date you want to see schedule for.
4. Check your auto created schedule.
5. To edit your schedule click the pencil sign on the app bar and select what you want to edit.
## Notes
1. Goals are colored green, breaks are colored blue and commitments red.
2. Schedule starts from the today's date, not earlier.

## Members

## Shagun - A01204521
## Ryan Leung - A01204521
## Owen Carando - A01034466
