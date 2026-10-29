import SwiftUI
import UIKit

@main
struct SysNexaApp: App {
    var body: some Scene {
        WindowGroup {
            ServiceRequestView()
                .preferredColorScheme(.dark)
        }
    }
}

private enum SysNexaStyle {
    static let background = Color(red: 0.063, green: 0.067, blue: 0.141)
    static let panel = Color(red: 0.122, green: 0.125, blue: 0.223)
    static let field = Color(red: 0.055, green: 0.059, blue: 0.125)
    static let ink = Color(red: 0.969, green: 0.969, blue: 1.0)
    static let muted = Color(red: 0.655, green: 0.663, blue: 0.761)
    static let dim = Color(red: 0.467, green: 0.478, blue: 0.596)
    static let cyan = Color(red: 0.4, green: 0.894, blue: 0.902)
    static let violet = Color(red: 0.757, green: 0.678, blue: 1.0)
    static let lime = Color(red: 0.784, green: 0.953, blue: 0.416)
    static let border = Color.white.opacity(0.12)
    static let whatsappNumber = "919591478095"
}

private enum ServiceDevice: String, CaseIterable, Identifiable {
    case desktop = "Desktop"
    case laptop = "Laptop"
    case mobile = "Mobile"

    var id: String { rawValue }

    var symbol: String {
        switch self {
        case .desktop: "desktopcomputer"
        case .laptop: "laptopcomputer"
        case .mobile: "iphone"
        }
    }

    var subtitle: String {
        switch self {
        case .desktop: "PC or all-in-one"
        case .laptop: "Notebook or MacBook"
        case .mobile: "Phone or smartphone"
        }
    }

    var services: [String] {
        switch self {
        case .desktop:
            [
                "Diagnosis & troubleshooting",
                "Hardware repair",
                "Motherboard troubleshooting",
                "Windows / software support",
                "Virus & security cleanup",
                "Upgrade / custom build"
            ]
        case .laptop:
            [
                "Diagnosis & troubleshooting",
                "Screen / keyboard repair",
                "Battery / charging repair",
                "Hardware repair",
                "Windows / software support",
                "Virus & security cleanup",
                "Upgrade / performance tune-up"
            ]
        case .mobile:
            [
                "Diagnosis & troubleshooting",
                "Screen / touch repair",
                "Battery / charging repair",
                "Software / OS support",
                "Firmware flashing / recovery",
                "Data transfer / backup guidance",
                "Other hardware repair"
            ]
        }
    }

    var issueOptions: [String] {
        switch self {
        case .desktop:
            [
                "Does not power on",
                "Does not boot / startup error",
                "Display or graphics problem",
                "Running slowly or freezing",
                "Overheating or loud fan",
                "Blue screen / unexpected restarts",
                "Motherboard or component fault",
                "Upgrade or new PC build",
                "Other desktop issue"
            ]
        case .laptop:
            [
                "Does not power on or boot",
                "Battery drains / will not charge",
                "Broken screen or display issue",
                "Keyboard, touchpad or port issue",
                "Running slowly or freezing",
                "Overheating or loud fan",
                "Liquid or physical damage",
                "Operating system or software issue",
                "Other laptop issue"
            ]
        case .mobile:
            [
                "Will not power on / boot loop",
                "Screen, touch or display problem",
                "Battery, charging or port issue",
                "Camera, speaker or microphone issue",
                "Software update or OS problem",
                "Firmware flashing / recovery",
                "Network, SIM or connectivity issue",
                "Liquid or physical damage",
                "Other mobile issue"
            ]
        }
    }
}

private struct ServiceRequestView: View {
    @State private var device: ServiceDevice = .desktop
    @State private var service = ""
    @State private var brand = ""
    @State private var model = ""
    @State private var issueType = ""
    @State private var desktopType = ""
    @State private var operatingSystem = ""
    @State private var problem = ""
    @State private var urgency = ""
    @State private var backupStatus = ""
    @State private var name = ""
    @State private var phone = ""
    @State private var email = ""
    @State private var area = ""
    @State private var hasConsent = false
    @State private var showingAlert = false
    @State private var alertMessage = ""

    var body: some View {
        ZStack {
            SysNexaStyle.background.ignoresSafeArea()

            ScrollView {
                VStack(spacing: 20) {
                    header
                    deviceSection
                    problemSection
                    contactSection
                    submitButton
                    nextStepsCard
                    footer
                }
                .frame(maxWidth: 620)
                .padding(.horizontal, 18)
                .padding(.top, 14)
                .padding(.bottom, 30)
                .frame(maxWidth: .infinity)
            }
            .scrollIndicators(.hidden)
        }
        .tint(SysNexaStyle.cyan)
        .alert("Check your request", isPresented: $showingAlert) {
            Button("OK", role: .cancel) { }
        } message: {
            Text(alertMessage)
        }
        .onChange(of: device) { _ in
            service = ""
            issueType = ""
            desktopType = ""
            operatingSystem = ""
        }
    }

    private var header: some View {
        VStack(spacing: 13) {
            Image("SysNexaLogo")
                .resizable()
                .scaledToFit()
                .frame(maxWidth: 250)
                .frame(height: 76)
                .accessibilityLabel("SysNexa — Repair, Upgrade, Connect")

            HStack(spacing: 8) {
                Circle()
                    .fill(SysNexaStyle.lime)
                    .frame(width: 7, height: 7)
                    .shadow(color: SysNexaStyle.lime.opacity(0.7), radius: 7)
                Text("DEVICE REPAIR · UPGRADES · SUPPORT")
                    .font(.system(size: 10, weight: .heavy, design: .rounded))
                    .tracking(1.5)
                    .foregroundStyle(SysNexaStyle.cyan)
            }

            VStack(spacing: 2) {
                Text("Let’s get your tech")
                    .foregroundStyle(SysNexaStyle.ink)
                Text("back in flow.")
                    .foregroundStyle(
                        LinearGradient(
                            colors: [SysNexaStyle.cyan, SysNexaStyle.violet],
                            startPoint: .leading,
                            endPoint: .trailing
                        )
                    )
            }
            .font(.system(size: 34, weight: .heavy, design: .rounded))
            .tracking(-1.5)
            .multilineTextAlignment(.center)
            .accessibilityElement(children: .combine)

            Text("Tell us what’s happening. We’ll prepare a clear service request you can review and send on WhatsApp.")
                .font(.system(size: 14))
                .foregroundStyle(SysNexaStyle.muted)
                .multilineTextAlignment(.center)
                .fixedSize(horizontal: false, vertical: true)
        }
        .padding(.bottom, 2)
    }

    private var deviceSection: some View {
        FormCard {
            SectionHeading(number: "01", title: "Which device needs attention?")

            HStack(spacing: 9) {
                ForEach(ServiceDevice.allCases) { option in
                    DeviceCard(
                        device: option,
                        isSelected: device == option
                    ) {
                        device = option
                    }
                }
            }

            VStack(spacing: 14) {
                FormField(title: "Brand", hint: "e.g. Dell, HP, Apple", text: $brand)
                    .textInputAutocapitalization(.words)
                FormField(title: "Model (if known)", hint: "Model name or number", text: $model)
                    .textInputAutocapitalization(.words)

                MenuField(title: "What kind of service do you need? *", selection: $service) {
                    Text("Select a service").tag("")
                    ForEach(device.services, id: \.self) { item in
                        Text(item).tag(item)
                    }
                }

                MenuField(title: "What is the device doing?", selection: $issueType) {
                    Text("Choose the closest match").tag("")
                    ForEach(device.issueOptions, id: \.self) { item in
                        Text(item).tag(item)
                    }
                }

                if device == .desktop {
                    MenuField(title: "Desktop type (optional)", selection: $desktopType) {
                        Text("Select if known").tag("")
                        Text("Custom-built tower").tag("Custom-built tower")
                        Text("Branded tower").tag("Branded tower")
                        Text("All-in-one PC").tag("All-in-one PC")
                        Text("Mini PC").tag("Mini PC")
                        Text("Other").tag("Other")
                    }
                }

                if device == .mobile {
                    MenuField(title: "Operating system (optional)", selection: $operatingSystem) {
                        Text("Select if known").tag("")
                        Text("Android").tag("Android")
                        Text("iOS").tag("iOS")
                        Text("Other / not sure").tag("Other / not sure")
                    }
                }
            }
            .padding(.top, 5)
        }
    }

    private var problemSection: some View {
        FormCard {
            SectionHeading(number: "02", title: "Describe the problem")

            VStack(alignment: .leading, spacing: 7) {
                Text("What happened? *")
                    .font(.system(size: 12, weight: .semibold))
                    .foregroundStyle(SysNexaStyle.ink)
                TextField(
                    "When did it start? What have you noticed? Include any error message.",
                    text: $problem,
                    axis: .vertical
                )
                .lineLimit(4...7)
                .font(.system(size: 14))
                .padding(12)
                .background(SysNexaStyle.field, in: RoundedRectangle(cornerRadius: 12))
                .overlay {
                    RoundedRectangle(cornerRadius: 12)
                        .stroke(SysNexaStyle.border, lineWidth: 1)
                }
                .accessibilityLabel("Describe what happened")

                Text("Please don’t share passwords, verification codes or other sensitive information.")
                    .font(.system(size: 10))
                    .foregroundStyle(SysNexaStyle.dim)
            }

            MenuField(title: "How soon do you need help?", selection: $urgency) {
                Text("Choose one").tag("")
                Text("As soon as possible").tag("As soon as possible")
                Text("Within a few days").tag("Within a few days")
                Text("Flexible / just exploring options").tag("Flexible / just exploring options")
            }

            MenuField(title: "Are important files backed up?", selection: $backupStatus) {
                Text("Choose one").tag("")
                Text("Yes, files are backed up").tag("Yes, files are backed up")
                Text("No, I need help preserving data").tag("No, I need help preserving data")
                Text("Not sure").tag("Not sure")
                Text("Not applicable").tag("Not applicable")
            }
        }
    }

    private var contactSection: some View {
        FormCard {
            SectionHeading(number: "03", title: "How can we reach you?")

            FormField(title: "Your name *", hint: "Name to address you by", text: $name)
                .textInputAutocapitalization(.words)
                .textContentType(.name)

            FormField(title: "Your phone number *", hint: "Best number to reach you", text: $phone)
                .keyboardType(.phonePad)
                .textContentType(.telephoneNumber)

            FormField(title: "Email (optional)", hint: "you@example.com", text: $email)
                .keyboardType(.emailAddress)
                .textInputAutocapitalization(.never)
                .textContentType(.emailAddress)
                .autocorrectionDisabled()

            FormField(title: "Your area (optional)", hint: "e.g. Vidyaranyapura, Bengaluru", text: $area)
                .textInputAutocapitalization(.words)

            Toggle(isOn: $hasConsent) {
                Text("I understand this opens WhatsApp with my details so I can review and send the request. *")
                    .font(.system(size: 11))
                    .foregroundStyle(SysNexaStyle.muted)
                    .fixedSize(horizontal: false, vertical: true)
            }
            .tint(SysNexaStyle.cyan)
            .padding(.top, 4)
        }
    }

    private var submitButton: some View {
        Button(action: submitRequest) {
            HStack(spacing: 10) {
                Image(systemName: "message.fill")
                Text("Prepare my WhatsApp request")
                Image(systemName: "arrow.up.right")
            }
            .font(.system(size: 14, weight: .bold))
            .foregroundStyle(SysNexaStyle.background)
            .frame(maxWidth: .infinity)
            .padding(.vertical, 17)
            .background(
                LinearGradient(
                    colors: [SysNexaStyle.cyan, SysNexaStyle.violet],
                    startPoint: .leading,
                    endPoint: .trailing
                ),
                in: RoundedRectangle(cornerRadius: 14)
            )
        }
        .buttonStyle(.plain)
        .accessibilityHint("Checks your service request and opens it in WhatsApp")
    }

    private var nextStepsCard: some View {
        FormCard {
            Text("GOOD TO KNOW")
                .font(.system(size: 10, weight: .heavy))
                .tracking(1.4)
                .foregroundStyle(SysNexaStyle.cyan)

            Text("More detail, better diagnosis.")
                .font(.system(size: 18, weight: .bold, design: .rounded))
                .foregroundStyle(SysNexaStyle.ink)

            Text("Your request opens in WhatsApp for you to review. Share only information needed to understand the repair; never include passwords or one-time codes.")
                .font(.system(size: 12))
                .foregroundStyle(SysNexaStyle.muted)
                .fixedSize(horizontal: false, vertical: true)

            Label("01  Tell us about your device", systemImage: "1.circle")
            Label("02  Review your request in WhatsApp", systemImage: "2.circle")
            Label("03  Find the right next step", systemImage: "3.circle")
        }
        .labelStyle(StepsLabelStyle())
    }

    private var footer: some View {
        VStack(spacing: 4) {
            Text("SysNexa")
                .font(.system(size: 13, weight: .bold, design: .rounded))
                .foregroundStyle(SysNexaStyle.ink)
            Text("Repair thoughtfully. Get back to what matters.")
                .font(.system(size: 10))
                .foregroundStyle(SysNexaStyle.dim)
        }
        .padding(.top, 2)
    }

    private func submitRequest() {
        guard !service.isEmpty else {
            showValidation("Choose the service you need.")
            return
        }
        guard !problem.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else {
            showValidation("Describe the problem with your device.")
            return
        }
        guard !name.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else {
            showValidation("Enter your name.")
            return
        }

        let digits = phone.filter { "0123456789".contains($0) }
        guard (7...15).contains(digits.count) else {
            showValidation("Enter a valid contact phone number with 7–15 digits.")
            return
        }

        let cleanEmail = email.trimmingCharacters(in: .whitespacesAndNewlines)
        if !cleanEmail.isEmpty {
            let emailPattern = #"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$"#
            guard cleanEmail.range(
                of: emailPattern,
                options: [.regularExpression, .caseInsensitive]
            ) != nil else {
                showValidation("Check your email address or leave it blank.")
                return
            }
        }

        guard hasConsent else {
            showValidation("Please confirm you want to review the request in WhatsApp.")
            return
        }

        var lines = [
            "Hello SysNexa! I'd like to request a device service.",
            "",
            "Device: \(device.rawValue)",
            "Service: \(service)"
        ]
        addIfPresent("Brand", brand, to: &lines)
        addIfPresent("Model", model, to: &lines)
        addIfPresent("Issue type", issueType, to: &lines)
        if device == .desktop {
            addIfPresent("Desktop type", desktopType, to: &lines)
        }
        if device == .mobile {
            addIfPresent("Operating system", operatingSystem, to: &lines)
        }
        lines += [
            "",
            "Problem details: \(problem.trimmingCharacters(in: .whitespacesAndNewlines))"
        ]
        addIfPresent("Urgency", urgency, to: &lines)
        addIfPresent("Data backup", backupStatus, to: &lines)
        lines += [
            "",
            "Name: \(name.trimmingCharacters(in: .whitespacesAndNewlines))",
            "Phone: \(phone.trimmingCharacters(in: .whitespacesAndNewlines))"
        ]
        addIfPresent("Email", email, to: &lines)
        addIfPresent("Area", area, to: &lines)
        lines += ["", "Please let me know the next steps."]

        var components = URLComponents(string: "https://wa.me/\(SysNexaStyle.whatsappNumber)")
        components?.queryItems = [URLQueryItem(name: "text", value: lines.joined(separator: "\n"))]

        guard let url = components?.url else {
            showValidation("We couldn’t prepare the WhatsApp link. Please try again.")
            return
        }

        UIApplication.shared.open(url) { success in
            if !success {
                showValidation("Unable to open WhatsApp or a browser on this device.")
            }
        }
    }

    private func addIfPresent(_ label: String, _ value: String, to lines: inout [String]) {
        let trimmed = value.trimmingCharacters(in: .whitespacesAndNewlines)
        if !trimmed.isEmpty {
            lines.append("\(label): \(trimmed)")
        }
    }

    private func showValidation(_ message: String) {
        alertMessage = message
        showingAlert = true
    }
}

private struct FormCard<Content: View>: View {
    @ViewBuilder var content: Content

    var body: some View {
        VStack(alignment: .leading, spacing: 14) {
            content
        }
        .padding(18)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(
            LinearGradient(
                colors: [SysNexaStyle.panel, SysNexaStyle.panel.opacity(0.78)],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            ),
            in: RoundedRectangle(cornerRadius: 20)
        )
        .overlay {
            RoundedRectangle(cornerRadius: 20)
                .stroke(SysNexaStyle.border, lineWidth: 1)
        }
    }
}

private struct SectionHeading: View {
    let number: String
    let title: String

    var body: some View {
        HStack(spacing: 10) {
            Text(number)
                .font(.system(size: 10, weight: .heavy, design: .rounded))
                .foregroundStyle(SysNexaStyle.violet)
                .frame(width: 29, height: 29)
                .background(SysNexaStyle.violet.opacity(0.11), in: RoundedRectangle(cornerRadius: 9))
                .overlay {
                    RoundedRectangle(cornerRadius: 9)
                        .stroke(SysNexaStyle.violet.opacity(0.22), lineWidth: 1)
                }

            Text(title)
                .font(.system(size: 14, weight: .bold, design: .rounded))
                .foregroundStyle(SysNexaStyle.ink)
                .fixedSize(horizontal: false, vertical: true)
        }
        .padding(.bottom, 2)
    }
}

private struct DeviceCard: View {
    let device: ServiceDevice
    let isSelected: Bool
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            VStack(alignment: .leading, spacing: 7) {
                Image(systemName: device.symbol)
                    .font(.system(size: 19, weight: .medium))
                    .foregroundStyle(SysNexaStyle.cyan)
                    .frame(height: 24)

                Text(device.rawValue)
                    .font(.system(size: 12, weight: .bold))
                    .foregroundStyle(SysNexaStyle.ink)

                Text(device.subtitle)
                    .font(.system(size: 9))
                    .foregroundStyle(SysNexaStyle.muted)
                    .lineLimit(2)
                    .multilineTextAlignment(.leading)

                if isSelected {
                    Image(systemName: "checkmark.circle.fill")
                        .font(.system(size: 12))
                        .foregroundStyle(SysNexaStyle.cyan)
                        .accessibilityLabel("Selected")
                }
            }
            .frame(maxWidth: .infinity, minHeight: 105, alignment: .leading)
            .padding(11)
            .background(
                isSelected ? SysNexaStyle.cyan.opacity(0.09) : SysNexaStyle.field.opacity(0.8),
                in: RoundedRectangle(cornerRadius: 14)
            )
            .overlay {
                RoundedRectangle(cornerRadius: 14)
                    .stroke(isSelected ? SysNexaStyle.cyan.opacity(0.6) : SysNexaStyle.border, lineWidth: 1)
            }
        }
        .buttonStyle(.plain)
        .accessibilityAddTraits(isSelected ? .isSelected : [])
        .accessibilityLabel("\(device.rawValue), \(device.subtitle)")
    }
}

private struct FormField: View {
    let title: String
    let hint: String
    @Binding var text: String

    var body: some View {
        VStack(alignment: .leading, spacing: 7) {
            Text(title)
                .font(.system(size: 12, weight: .semibold))
                .foregroundStyle(SysNexaStyle.ink)

            TextField(hint, text: $text)
                .font(.system(size: 14))
                .foregroundStyle(SysNexaStyle.ink)
                .padding(.horizontal, 12)
                .frame(minHeight: 48)
                .background(SysNexaStyle.field, in: RoundedRectangle(cornerRadius: 11))
                .overlay {
                    RoundedRectangle(cornerRadius: 11)
                        .stroke(SysNexaStyle.border, lineWidth: 1)
                }
        }
    }
}

private struct MenuField<SelectionValue: Hashable, Content: View>: View {
    let title: String
    @Binding var selection: SelectionValue
    @ViewBuilder let content: Content

    var body: some View {
        VStack(alignment: .leading, spacing: 7) {
            Text(title)
                .font(.system(size: 12, weight: .semibold))
                .foregroundStyle(SysNexaStyle.ink)

            Picker(title, selection: $selection) {
                content
            }
            .pickerStyle(.menu)
            .tint(SysNexaStyle.ink)
            .frame(maxWidth: .infinity, minHeight: 48, alignment: .leading)
            .padding(.horizontal, 11)
            .background(SysNexaStyle.field, in: RoundedRectangle(cornerRadius: 11))
            .overlay {
                RoundedRectangle(cornerRadius: 11)
                    .stroke(SysNexaStyle.border, lineWidth: 1)
            }
        }
    }
}

private struct StepsLabelStyle: LabelStyle {
    func makeBody(configuration: Configuration) -> some View {
        HStack(spacing: 9) {
            configuration.icon
                .foregroundStyle(SysNexaStyle.cyan)
            configuration.title
                .font(.system(size: 11, weight: .medium))
                .foregroundStyle(SysNexaStyle.muted)
        }
    }
}
