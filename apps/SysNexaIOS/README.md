# SysNexa iOS app

Native SwiftUI app for SysNexa desktop, laptop, and mobile repair requests. The
app follows the website's dark navy, cyan, and violet theme, uses the supplied
brand logo, and opens WhatsApp with a service message that the customer can
review and send.

## Open and run on iPhone

1. On a Mac, install Xcode 15 or newer.
2. Open `SysNexa.xcodeproj` in Xcode.
3. In **Signing & Capabilities**, select your Apple development team if Xcode
   requests one.
4. Select an iPhone simulator or a connected iPhone and press **Run**.

The deployment target is iOS 16. The project is native SwiftUI and uses no
third-party packages or backend. WhatsApp or a web browser is used to open the
pre-filled request; the customer reviews and sends it. The support number
(country code included, digits only) is set in `SysNexa/SysNexaApp.swift`.

Xcode and the iOS SDK are only available on macOS, so build, signing, and
device installation must be performed on a Mac.
