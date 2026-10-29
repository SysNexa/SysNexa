# SysNexa Android app

Native Kotlin Android app for desktop, laptop, and mobile service requests. It
uses the SysNexa website theme and prepares a WhatsApp message containing the
customer's selected device, issue, and contact details.

## Open and build

1. Install Android Studio (with Android SDK Platform 35) and a JDK supported by
   Android Gradle Plugin 8.7.3.
2. In Android Studio, choose **Open** and select this `SysNexaAndroid` folder.
3. Allow Gradle sync to finish, then choose **Build > Build App Bundle(s) /
   APK(s) > Build APK(s)**.
4. Install the generated debug APK on an Android device, or run the `app`
   configuration on a connected device.

The app requires Android 6.0 (API 23) or newer. Submitting a request opens
WhatsApp or the device's browser with the message filled in; the user reviews
and sends it. The support number is configured in `MainActivity.kt` as
`919591478095` (country code included, digits only).

No backend or runtime internet permission is required for the native form.
WhatsApp / a browser is needed to open the prepared request.
