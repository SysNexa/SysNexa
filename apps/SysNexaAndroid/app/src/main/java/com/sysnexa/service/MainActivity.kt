package com.sysnexa.service

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private val ink = Color.rgb(247, 247, 255)
    private val muted = Color.rgb(167, 169, 194)
    private val dim = Color.rgb(119, 122, 152)
    private val cyan = Color.rgb(102, 228, 230)
    private val violet = Color.rgb(193, 173, 255)
    private val night = Color.rgb(16, 17, 36)
    private val panel = Color.rgb(28, 29, 53)
    private val stroke = Color.rgb(57, 59, 83)

    private lateinit var deviceGroup: RadioGroup
    private lateinit var detailsPanel: LinearLayout
    private lateinit var serviceSpinner: Spinner
    private lateinit var brandInput: EditText
    private lateinit var modelInput: EditText
    private lateinit var issueSpinner: Spinner
    private lateinit var formFactorSpinner: Spinner
    private lateinit var operatingSystemSpinner: Spinner
    private lateinit var issueInput: EditText
    private lateinit var urgencySpinner: Spinner
    private lateinit var backupSpinner: Spinner
    private lateinit var nameInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var areaInput: EditText
    private lateinit var consentCheck: CheckBox

    private data class DeviceConfig(
        val services: List<String>,
        val issues: List<String>,
        val auxiliaryChoices: List<String> = emptyList()
    )

    private val deviceConfigs = linkedMapOf(
        "Desktop" to DeviceConfig(
            services = listOf(
                "Diagnosis & troubleshooting", "Hardware repair",
                "Motherboard troubleshooting", "Windows / software support",
                "Virus & security cleanup", "Upgrade / custom build"
            ),
            issues = listOf(
                "Does not power on", "Does not boot / startup error",
                "Display or graphics problem", "Running slowly or freezing",
                "Overheating or loud fan", "Blue screen / unexpected restarts",
                "Motherboard or component fault", "Upgrade or new PC build",
                "Other desktop issue"
            ),
            auxiliaryChoices = listOf(
                "Custom-built tower", "Branded tower", "All-in-one PC", "Mini PC", "Other"
            )
        ),
        "Laptop" to DeviceConfig(
            services = listOf(
                "Diagnosis & troubleshooting", "Screen / keyboard repair",
                "Battery / charging repair", "Hardware repair",
                "Windows / software support", "Virus & security cleanup",
                "Upgrade / performance tune-up"
            ),
            issues = listOf(
                "Does not power on or boot", "Battery drains / will not charge",
                "Broken screen or display issue", "Keyboard, touchpad or port issue",
                "Running slowly or freezing", "Overheating or loud fan",
                "Liquid or physical damage", "Operating system or software issue",
                "Other laptop issue"
            )
        ),
        "Mobile" to DeviceConfig(
            services = listOf(
                "Diagnosis & troubleshooting", "Screen / touch repair",
                "Battery / charging repair", "Software / OS support",
                "Firmware flashing / recovery", "Data transfer / backup guidance",
                "Other hardware repair"
            ),
            issues = listOf(
                "Will not power on / boot loop", "Screen, touch or display problem",
                "Battery, charging or port issue", "Camera, speaker or microphone issue",
                "Software update or OS problem", "Firmware flashing / recovery",
                "Network, SIM or connectivity issue", "Liquid or physical damage",
                "Other mobile issue"
            ),
            auxiliaryChoices = listOf("Android", "iOS", "Other / not sure")
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = night
        window.navigationBarColor = night
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(true)
        }
        buildScreen()
    }

    private fun buildScreen() {
        val scrollView = ScrollView(this).apply {
            fillViewport = true
            isSmoothScrollingEnabled = true
            setBackgroundColor(night)
        }
        val page = column()
        page.setPadding(dp(20), dp(12), dp(20), dp(32))

        val logo = ImageView(this).apply {
            setImageResource(R.drawable.sysnexa_logo)
            contentDescription = "SysNexa — Repair, Upgrade, Connect"
            scaleType = ImageView.ScaleType.FIT_START
            layoutParams = LinearLayout.LayoutParams(dp(190), dp(60)).apply {
                gravity = Gravity.CENTER_HORIZONTAL
                bottomMargin = dp(15)
            }
        }
        page.addView(logo)
        page.addView(label("DEVICE REPAIR · UPGRADES · SUPPORT", cyan, 10f, true).apply {
            gravity = Gravity.CENTER
            letterSpacing = .13f
            setPadding(0, dp(4), 0, dp(7))
        })
        page.addView(label("Let’s get your tech\nback in flow.", ink, 30f, true).apply {
            gravity = Gravity.CENTER
            setLineSpacing(dp(2).toFloat(), 1f)
            setPadding(0, 0, 0, dp(9))
        })
        page.addView(label(
            "Tell us what’s happening. We’ll prepare a clear service request you can review and send on WhatsApp.",
            muted, 14f
        ).apply {
            gravity = Gravity.CENTER
            setPadding(dp(8), 0, dp(8), dp(20))
        })

        val formCard = column().apply {
            setPadding(dp(18), dp(20), dp(18), dp(19))
            background = rounded(panel, 22, stroke)
        }
        page.addView(formCard)
        formCard.addView(label("REQUEST A REPAIR", cyan, 10f, true).apply {
            letterSpacing = .12f
        })
        formCard.addView(label("Tell us what’s wrong", ink, 22f, true).apply {
            setPadding(0, dp(4), 0, dp(3))
        })
        formCard.addView(label(
            "A few details help us understand what your device needs.",
            muted, 12f
        ).apply { setPadding(0, 0, 0, dp(17)) })

        addSectionTitle(formCard, "01", "Which device needs attention?")
        deviceGroup = RadioGroup(this).apply {
            orientation = RadioGroup.HORIZONTAL
            gravity = Gravity.CENTER
        }
        val deviceIds = linkedMapOf<String, Int>()
        deviceConfigs.keys.forEachIndexed { index, device ->
            val id = View.generateViewId()
            deviceIds[device] = id
            val radio = RadioButton(this).apply {
                this.id = id
                text = device
                textSize = 12f
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(ink)
                buttonTintList = android.content.res.ColorStateList.valueOf(cyan)
                gravity = Gravity.CENTER
                minHeight = dp(48)
                background = rounded(Color.rgb(20, 21, 41), 12, stroke)
                setPadding(dp(9), dp(4), dp(9), dp(4))
            }
            val params = RadioGroup.LayoutParams(0, dp(54), 1f).apply {
                if (index > 0) marginStart = dp(8)
            }
            deviceGroup.addView(radio, params)
        }
        formCard.addView(deviceGroup)

        detailsPanel = column().apply {
            visibility = View.GONE
            setPadding(0, dp(16), 0, 0)
        }
        formCard.addView(detailsPanel)
        brandInput = addTextField(detailsPanel, "Brand", "e.g. Dell, HP, Apple")
        modelInput = addTextField(detailsPanel, "Model (if known)", "Model name or number")
        serviceSpinner = addSpinnerField(detailsPanel, "What kind of service do you need? *", listOf("Choose a device first"))
        issueSpinner = addSpinnerField(detailsPanel, "What is the device doing?", listOf("Choose a device first"))
        formFactorSpinner = addSpinnerField(detailsPanel, "Desktop type (optional)", listOf("Select if known"))
        operatingSystemSpinner = addSpinnerField(detailsPanel, "Operating system (optional)", listOf("Select if known"))

        deviceGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedDevice = deviceIds.entries.firstOrNull { it.value == checkedId }?.key
            updateDeviceDetails(selectedDevice)
        }

        addSectionTitle(formCard, "02", "Describe the problem")
        issueInput = addTextField(
            formCard,
            "What happened? *",
            "When did it start? What have you noticed? Include any error message."
        ).apply {
            setSingleLine(false)
            minLines = 4
            maxLines = 6
            gravity = Gravity.TOP or Gravity.START
            inputType = InputType.TYPE_CLASS_TEXT or
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or
                InputType.TYPE_TEXT_FLAG_MULTI_LINE
        }
        formCard.addView(label(
            "Please don’t share passwords, verification codes or other sensitive information.",
            dim, 10f
        ).apply { setPadding(0, dp(5), 0, 0) })
        urgencySpinner = addSpinnerField(formCard, "How soon do you need help?", listOf(
            "Choose one", "As soon as possible", "Within a few days",
            "Flexible / just exploring options"
        ))
        backupSpinner = addSpinnerField(formCard, "Are important files backed up?", listOf(
            "Choose one", "Yes, files are backed up",
            "No, I need help preserving data", "Not sure", "Not applicable"
        ))

        addSectionTitle(formCard, "03", "How can we reach you?")
        nameInput = addTextField(formCard, "Your name *", "Name to address you by").apply {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        }
        phoneInput = addTextField(formCard, "Your phone number *", "Best number to reach you").apply {
            inputType = InputType.TYPE_CLASS_PHONE
        }
        emailInput = addTextField(formCard, "Email (optional)", "you@example.com").apply {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }
        areaInput = addTextField(formCard, "Your area (optional)", "e.g. Vidyaranyapura, Bengaluru").apply {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        }

        consentCheck = CheckBox(this).apply {
            text = "I understand this opens WhatsApp with my details so I can review and send the request. *"
            textSize = 11f
            setTextColor(muted)
            buttonTintList = android.content.res.ColorStateList.valueOf(cyan)
            setPadding(0, dp(8), 0, dp(4))
        }
        formCard.addView(consentCheck)

        val submitButton = Button(this).apply {
            text = "Prepare my WhatsApp request  ↗"
            textSize = 14f
            typeface = Typeface.DEFAULT_BOLD
            isAllCaps = false
            setTextColor(night)
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(cyan, Color.rgb(193, 173, 255))
            ).apply { cornerRadius = dp(13).toFloat() }
            minHeight = dp(54)
            setPadding(dp(12), dp(8), dp(12), dp(8))
            setOnClickListener { submitRequest() }
        }
        formCard.addView(submitButton, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, dp(54)
        ).apply { topMargin = dp(16) })
        formCard.addView(label(
            "WhatsApp opens with your message ready. You can review it before sending.",
            dim, 10f
        ).apply {
            gravity = Gravity.CENTER
            setPadding(0, dp(10), 0, 0)
        })

        val infoCard = column().apply {
            setPadding(dp(17), dp(17), dp(17), dp(17))
            background = rounded(Color.rgb(30, 30, 56), 17, stroke)
        }
        val infoParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { topMargin = dp(16) }
        page.addView(infoCard, infoParams)
        infoCard.addView(label("GOOD TO KNOW", violet, 10f, true).apply { letterSpacing = .12f })
        infoCard.addView(label("More detail, better diagnosis.", ink, 16f, true).apply {
            setPadding(0, dp(4), 0, dp(5))
        })
        infoCard.addView(label(
            "Even a rough description helps. If you know the model or error message, include it and we’ll take it from there.",
            muted, 12f
        ))
        infoCard.addView(label(
            "01  Share your device and issue\n\n02  Review your request in WhatsApp\n\n03  We’ll help you find a way forward",
            muted, 12f
        ).apply { setPadding(0, dp(13), 0, 0) })

        page.addView(label(
            "© ${java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)} SysNexa · Repair thoughtfully.",
            dim, 10f
        ).apply {
            gravity = Gravity.CENTER
            setPadding(0, dp(22), 0, dp(8))
        })

        scrollView.addView(page)
        setContentView(scrollView)
    }

    private fun updateDeviceDetails(device: String?) {
        val config = device?.let { deviceConfigs[it] }
        detailsPanel.visibility = if (config == null) View.GONE else View.VISIBLE
        if (config == null) return

        setSpinnerItems(serviceSpinner, listOf("Select a service") + config.services)
        setSpinnerItems(issueSpinner, listOf("Choose the closest match") + config.issues)

        val isDesktop = device == "Desktop"
        formFactorSpinner.visibility = if (isDesktop) View.VISIBLE else View.GONE
        operatingSystemSpinner.visibility = if (device == "Mobile") View.VISIBLE else View.GONE

        if (isDesktop) {
            setSpinnerItems(formFactorSpinner, listOf("Select if known") + config.auxiliaryChoices)
        } else if (device == "Mobile") {
            setSpinnerItems(operatingSystemSpinner, listOf("Select if known") + config.auxiliaryChoices)
        }
    }

    private fun submitRequest() {
        val deviceButton = deviceGroup.findViewById<RadioButton>(deviceGroup.checkedRadioButtonId)
        val device = deviceButton?.text?.toString()
        if (device == null || device !in deviceConfigs) {
            showMessage("Choose Desktop, Laptop or Mobile to continue.")
            deviceGroup.requestFocus()
            return
        }

        val missing = when {
            serviceSpinner.selectedItemPosition == 0 -> "Choose the service you need."
            issueInput.text.toString().trim().isEmpty() -> "Describe the problem with your device."
            nameInput.text.toString().trim().isEmpty() -> "Enter your name."
            phoneInput.text.toString().filter(Char::isDigit).length !in 7..15 ->
                "Enter a valid phone number with 7–15 digits."
            emailInput.text.toString().trim().isNotEmpty() &&
                !android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput.text.toString().trim()).matches() ->
                "Check the email address or leave it blank."
            !consentCheck.isChecked -> "Please confirm you want to review the request in WhatsApp."
            else -> null
        }
        if (missing != null) {
            showMessage(missing)
            when {
                serviceSpinner.selectedItemPosition == 0 -> serviceSpinner.requestFocus()
                issueInput.text.toString().trim().isEmpty() -> issueInput.requestFocus()
                nameInput.text.toString().trim().isEmpty() -> nameInput.requestFocus()
                phoneInput.text.toString().filter(Char::isDigit).length !in 7..15 -> phoneInput.requestFocus()
                emailInput.text.toString().trim().isNotEmpty() &&
                    !android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput.text.toString().trim()).matches() ->
                    emailInput.requestFocus()
                else -> consentCheck.requestFocus()
            }
            return
        }

        val message = buildList {
            add("Hello SysNexa! I'd like to request a device service.")
            add("")
            add("Device: $device")
            add("Service: ${serviceSpinner.selectedItem}")
            addOptional("Brand", brandInput.text.toString())
            addOptional("Model", modelInput.text.toString())
            addOptional("Issue type", issueSpinner.selectedItem?.toString().orEmpty(), issueSpinner.selectedItemPosition > 0)
            if (device == "Desktop") {
                addOptional("Desktop type", formFactorSpinner.selectedItem?.toString().orEmpty(), formFactorSpinner.selectedItemPosition > 0)
            }
            if (device == "Mobile") {
                addOptional("Operating system", operatingSystemSpinner.selectedItem?.toString().orEmpty(), operatingSystemSpinner.selectedItemPosition > 0)
            }
            add("")
            add("Problem details: ${issueInput.text.toString().trim()}")
            addOptional("Urgency", urgencySpinner.selectedItem?.toString().orEmpty(), urgencySpinner.selectedItemPosition > 0)
            addOptional("Data backup", backupSpinner.selectedItem?.toString().orEmpty(), backupSpinner.selectedItemPosition > 0)
            add("")
            add("Name: ${nameInput.text.toString().trim()}")
            add("Phone: ${phoneInput.text.toString().trim()}")
            addOptional("Email", emailInput.text.toString())
            addOptional("Area", areaInput.text.toString())
            add("")
            add("Please let me know the next steps.")
        }.joinToString("\n")

        val whatsappUri = Uri.parse("https://wa.me/919591478095")
            .buildUpon()
            .appendQueryParameter("text", message)
            .build()
        val intent = Intent(Intent.ACTION_VIEW, whatsappUri)
        try {
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            showMessage("No browser or WhatsApp app is available to open this request.")
        }
    }

    private fun MutableList<String>.addOptional(label: String, rawValue: String, include: Boolean = true) {
        val value = rawValue.trim()
        if (include && value.isNotEmpty()) add("$label: $value")
    }

    private fun addSectionTitle(parent: LinearLayout, number: String, title: String) {
        val heading = label("$number   $title", violet, 13f, true).apply {
            setPadding(0, dp(20), 0, dp(11))
        }
        parent.addView(heading)
    }

    private fun addTextField(parent: LinearLayout, title: String, hintText: String): EditText {
        val wrapper = column().apply {
            setPadding(0, 0, 0, dp(13))
        }
        wrapper.addView(label(title, ink, 11f, true).apply {
            setPadding(0, 0, 0, dp(5))
        })
        val input = EditText(this).apply {
            hint = hintText
            textSize = 13f
            setTextColor(ink)
            setHintTextColor(dim)
            singleLine = true
            maxLines = 1
            setPadding(dp(12), dp(10), dp(12), dp(10))
            background = rounded(Color.rgb(18, 19, 39), 11, stroke)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                textCursorDrawable = null
            }
        }
        wrapper.addView(input, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, dp(48)
        ))
        parent.addView(wrapper)
        return input
    }

    private fun addSpinnerField(parent: LinearLayout, title: String, items: List<String>): Spinner {
        val wrapper = column().apply { setPadding(0, 0, 0, dp(13)) }
        wrapper.addView(label(title, ink, 11f, true).apply {
            setPadding(0, 0, 0, dp(5))
        })
        val spinner = Spinner(this).apply {
            setPadding(dp(8), 0, dp(8), 0)
            background = rounded(Color.rgb(18, 19, 39), 11, stroke)
            setPopupBackgroundDrawable(rounded(panel, 10, stroke))
        }
        setSpinnerItems(spinner, items)
        wrapper.addView(spinner, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, dp(48)
        ))
        parent.addView(wrapper)
        return spinner
    }

    private fun setSpinnerItems(spinner: Spinner, items: List<String>) {
        val adapter = object : ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item, items
        ) {
            override fun getView(position: Int, convertView: View?, parent: android.view.ViewGroup): View {
                return (super.getView(position, convertView, parent) as TextView).apply {
                    setTextColor(if (position == 0) dim else ink)
                    textSize = 12f
                    gravity = Gravity.CENTER_VERTICAL
                }
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: android.view.ViewGroup): View {
                return (super.getDropDownView(position, convertView, parent) as TextView).apply {
                    setTextColor(ink)
                    textSize = 13f
                    setPadding(dp(14), dp(12), dp(14), dp(12))
                    setBackgroundColor(panel)
                }
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun column(): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    private fun label(
        textValue: String,
        color: Int,
        size: Float,
        bold: Boolean = false
    ): TextView = TextView(this).apply {
        text = textValue
        setTextColor(color)
        textSize = size
        if (bold) typeface = Typeface.create("sans-serif", Typeface.BOLD)
        setLineSpacing(dp(2).toFloat(), 1f)
    }

    private fun rounded(fill: Int, radiusDp: Int, border: Int? = null): GradientDrawable =
        GradientDrawable().apply {
            setColor(fill)
            cornerRadius = dp(radiusDp).toFloat()
            if (border != null) setStroke(dp(1), border)
        }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density + 0.5f).toInt()
}
