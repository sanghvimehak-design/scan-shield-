# SCAN SHIELD — Digital Safety Companion for Pensioners

A senior-friendly, Gen Z/Alpha aesthetic, AI-powered digital safety and anti-fraud companion built for elderly pensioners in India. Features a comforting lavender-and-mint visual theme, Gemini multimodal AI analysis combined with instant local threat rules, an interactive "Pause Before You Pay" guardian, emergency SOS (1930 & bank hotlines), voice assistance, trusted contact alerts, volunteer community mentoring, and 12 scenario-based educational quizzes.

Tagline: *“Pause. Check. Stay Safe. 💜”*

---

## User Review & Critical Decisions

> [!IMPORTANT]
> The following architectural and user experience decisions have been aligned based on user clarification and will guide the build:

- **Confirmed Decision 1 (Senior-First Design)**: High contrast, large touch targets (minimum 56dp for primary buttons), large readable typography, friendly card hierarchy, and instant audio guidance so first-time smartphone users and elderly pensioners can navigate without anxiety.
- **Confirmed Decision 2 (Hybrid AI & Local Threat Scanning)**: Dual-layer engine featuring real Gemini multimodal AI via Firebase AI Logic / Gemini models for deep screenshot analysis, paired with an instant on-device pattern recognition engine for sub-second threat detection (KYC fraud, fake pension arrears, deceptive UPI QR codes, remote access APKs).
- **Confirmed Decision 3 (Language & Accessibility Architecture)**: Instant multilingual switcher accessible from the top bar supporting 9 Indian languages (English, Hindi, Gujarati, Tamil, Bengali, Marathi, Telugu, Kannada, Malayalam), alongside persistent Normal / Large / Extra Large global font scaling and high contrast mode.

---

## 1. Overview & Core Concept

- **What It Does**: SCAN SHIELD acts as an everyday digital guardian for Indian pensioners. It inspects suspicious SMS, WhatsApp forwards, payment requests, and screenshots (e.g. fake Jeevan Pramaan / Digital Life Certificate alerts, electricity bill suspension threats, deceptive lottery links), guides users before initiating UPI payments, connects them to family in emergencies, and empowers them through bite-sized lessons.
- **Target Audience / Persona**: Indian retirees and elderly pensioners (ages 58+), often unfamiliar with sophisticated social engineering, fear-based urgency, and digital payment nuances; supported by family members and youth digital safety volunteers.
- **Key Value**: Provides calm, clear, and reassuring clarity at the exact moment of pressure (“Pause. Check. Stay Safe. 💜”), eliminating the shame and panic associated with online financial fraud.

---

## 2. User Experience & Visual Design

### Key User Flows

1. **First-Time Welcome & Personalization**:
   - Splash screen featuring the lavender shield mascot "Raksha" with mint sparkles.
   - Quick senior-friendly onboarding: pick primary language (default English with prominent Hindi / regional buttons) and choose comfort text size (Normal / Large / Extra Large).
2. **Dashboard & Quick Triage**:
   - Greeting: *“Namaste! 💜 Welcome to Scan Shield.”*
   - Prominent, colorful quick-action cards: "Scan a Screenshot", "Check Suspicious Message", "Pause Before You Pay", and "Talk to Scan Shield".
   - Floating / Top Emergency SOS button in Warning Rose (#D95766) that never obscures content.
   - Daily Safety Tip card with audio read-aloud button.
3. **Multimodal AI Screenshot & Message Scanner**:
   - Select screenshot via Android Photo Picker (`ActivityResultContracts.PickVisualMedia`), paste text message, or input phone number / URL.
   - Test directly with 12 realistic Indian scam presets or 4 benign samples (e.g., genuine SBI pension credit vs. fake EPFO arrears link).
   - Analysis results card with color-coded safety level: **High Risk** (Red), **Suspicious** (Amber), **No Obvious Warning Signs** (Mint), or **Unable to Verify** (Neutral).
   - Clear breakdown: Evidence found (e.g. urgency words, unofficial APK link, asking for UPI PIN to receive money), Plain-language explanation, Safe verification instructions, and Save to Scan History.
4. **Signature "Pause Before You Pay" Guardian**:
   - Step-by-step 5-question interactive questionnaire: Recipient familiarity, reason for money transfer, urgency/threat presence, OTP/PIN/screen share requests, independent verification.
   - Highlights the golden rule: *"A UPI PIN is only needed to SEND money, never to RECEIVE money."*
   - Actionable outcome: **Stop and Verify**, **Proceed With Caution**, or **High Risk**.
5. **Scam Emergency Centre & SOS**:
   - Instant direct dialer intent to verified National Cybercrime Helpline **1930** and official portal `https://cybercrime.gov.in/`.
   - Direct dialer to user's Trusted Family Contact with customizable pre-filled SOS SMS template.
   - Directory of verified bank fraud emergency numbers (SBI, PNB, HDFC, ICICI, Bank of Baroda, Canara Bank, Axis Bank).
   - Step-by-step account freeze and evidence preservation checklist.
6. **Voice Assistant & Spoken Guidance**:
   - Large mic button with animated wave indicator.
   - Speech-to-text input with quick command chips (*"Is this message fake?"*, *"How to report cyber fraud?"*, *"Help me verify a payment"*).
   - Text-to-speech spoken answers in chosen Indian language with repeat, pause, and speed adjustments (0.75x, 1.0x, 1.25x).
7. **Learning Centre & 12 Scenario Quizzes**:
   - 12 realistic interactive scenario quizzes (e.g., "The Electricity Bill WhatsApp Notice", "The Fake Jeevan Pramaan App", "The Lottery Prize Tax QR Code", "AnyDesk Customer Care Trap").
   - Instant feedback, explanation, score tracking, and collectible safety badges.
8. **Volunteer Community**:
   - Connects elderly learners with youth safety mentors.
   - Browse vetted volunteer profiles with language filters, requested skill areas, and request a 1-on-1 friendly coaching session.

### Visual Identity & Theme

- **Aesthetic Direction**: "Cozy Guardian" — Soft Gen Z / Gen Alpha aesthetic meets high-accessibility senior-friendly ergonomics. Rounded bubbly cards (20-28dp corner radii), soft drop shadows, sticker badges, and warm tones.
- **Color Tokens**:
  - `Lavender Primary`: `#B8A4FF`
  - `Deep Purple On-Primary`: `#5B45A6`
  - `Mint Accent / Safe`: `#BCEBD7` (Dark contrast text `#1A4D38`)
  - `Butter Yellow / Attention`: `#FFE69A` (Dark contrast text `#5C4500`)
  - `Peach Secondary`: `#FFC6B0`
  - `Warm Cream Surface`: `#FFF9F2`
  - `Dark Ink Body`: `#292638`
  - `Warning Rose / SOS`: `#D95766`
- **Typography & Accessibility Scale**:
  - Normal (100% scale), Large (120% scale), Extra Large (140% scale).
  - Bold, legible headers, generous line-height, and minimum 48-56dp interactive component height.

---

## 3. Key Product Decisions & Trade-Offs

- **Decision 1: Dual-Layer AI & Offline Rule Engine**:
  - *Chosen Approach*: Implement a rich local pattern-matching heuristic engine that runs synchronously and instantly, alongside a Firebase AI Logic Gemini multimodal connector.
  - *Why*: Seniors often have intermittent network connectivity or low data bandwidth in rural and semi-urban India. Instant local threat signatures ensure critical safety advice is rendered with zero latency, while Gemini provides comprehensive conversational explanations.
- **Decision 2: Safe Android Dialing & SMS Hand-off**:
  - *Chosen Approach*: Use system `Intent(Intent.ACTION_DIAL)` and `Intent(Intent.ACTION_SENDTO)` with explicit confirmation dialogs before making calls or sending messages.
  - *Why*: Never trigger background calls or SMS without explicit pensioner review, complying with Android least-privilege permissions and preventing accidental dials.
- **Decision 3: Room Database Local Persistence**:
  - *Chosen Approach*: Store scan records, user settings (language, text size, speech speed, high contrast), trusted contacts, and completed quiz progress locally in an encrypted Room SQLite database.
  - *Why*: Guarantees total privacy for seniors' personal scan history and trusted family contacts without uploading sensitive financial screenshots to third-party databases unless requested.

---

## 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                          SCAN SHIELD ARCHITECTURE                      │
└────────────────────────────────────────────────────────────────────────┘
                                   │
         ┌─────────────────────────┴─────────────────────────┐
         ▼                                                   ▼
┌──────────────────────────────────┐        ┌──────────────────────────────────┐
│        UI Layer (Compose)        │        │      Accessibility Provider      │
│  • Senior-First M3 Screens       │        │  • 9 Language Localizer          │
│  • Mascot & Visual Stickers      │        │  • Normal/Large/XL Text Scaling  │
│  • Audio Prompt Controls         │        │  • High-Contrast Color Schemes   │
└──────────────────────────────────┘        └──────────────────────────────────┘
                 │                                            │
                 ▼                                            ▼
┌──────────────────────────────────────────────────────────────────────────────┐
│                            State / ViewModel Layer                           │
│  • ScanShieldViewModel: Hybrid AI Scanner, Pay-Check Flow, SOS Dispatch      │
│  • VoiceAssistantManager: Android SpeechRecognizer + TextToSpeech engine     │
│  • LearningQuizViewModel: 12 Interactive scenarios, progress & badges       │
│  • CommunityViewModel: Volunteer matching & help requests                   │
└──────────────────────────────────────────────────────────────────────────────┘
                 │                                            │
                 ▼                                            ▼
┌──────────────────────────────────┐        ┌──────────────────────────────────┐
│      Hybrid Threat Engine        │        │       Data Layer (Room DB)       │
│  • Gemini Multimodal Analysis    │        │  • ScanHistoryDao                │
│  • Indian Cybercrime Heuristics  │        │  • TrustedContactsDao            │
│  • UPI PIN & QR Verification     │        │  • UserPreferencesDao / State    │
│  • 12 Scam / 4 Benign Presets    │        │  • QuizProgressDao               │
└──────────────────────────────────┘        └──────────────────────────────────┘
```

### Data Models

1. **`ScanRecord`**: `id`, `timestamp`, `sourceType` (SCREENSHOT, MESSAGE, PHONE, LINK), `inputText`, `imageUri`, `riskLevel` (HIGH_RISK, SUSPICIOUS, SAFE, UNVERIFIED), `summaryExplanation`, `evidencePoints`, `recommendedActions`, `languageCode`.
2. **`TrustedContact`**: `id`, `name`, `relationship`, `phoneNumber`, `isPrimaryEmergency`.
3. **`QuizScenario`**: `id`, `title`, `story`, `scamCategory`, `options`, `correctOptionIndex`, `explanation`, `practicalTip`.
4. **`VolunteerProfile`**: `id`, `name`, `age`, `location`, `languages`, `bio`, `specialties`, `rating`, `badges`, `availableSlots`.
5. **`AppPreferences`**: `language`, `textScale` (NORMAL, LARGE, EXTRA_LARGE), `isHighContrast`, `isDarkMode`, `speechSpeed`, `soundFeedbackEnabled`.

---

## Verification & Execution Plan

1. **Initialization**: Set application identity and app name in `metadata.json` and `res/values/strings.xml`.
2. **Theme & Design Tokens**: Implement cute lavender/mint palette, accessible typography styles, and M3 color scheme.
3. **Core Database & Seed Data**: Build Room database with DAOs and seed 12 distinct scam examples, 4 benign examples, 12 quiz scenarios, 5 volunteer demo profiles, and 5 safety tips.
4. **AI & Hybrid Scanner**: Build Gemini multimodal parser + local threat heuristics, integrating Android Photo Picker and real-time risk grading.
5. **Pause Before You Pay**: Implement interactive 5-point questionnaire with UPI PIN education and actionable verdict.
6. **Voice Engine & Audio**: Implement Android `TextToSpeech` and `SpeechRecognizer` integration with repeat and speed controls.
7. **Emergency SOS & Helplines**: Implement 1930 Cyber Helpline launcher, verified bank directory, and trusted contact dialer.
8. **Interactive Learning & Quizzes**: Build the 12 scenario quizzes with reward badges and safety checklist.
9. **Settings & Localization**: Implement full multi-language dictionary for 9 Indian languages and persistent text size toggles.
10. **Build & Compilation**: Run `compile_applet` to verify clean compilation with zero warnings or errors.
