# CRM-Lite

> Never miss a lead again.

CRM-Lite is a minimalist, mobile-first, call-driven CRM application designed for small business owners, freelancers, manufacturers, traders, and sales professionals who handle a high volume of customer calls daily.

The application automatically monitors incoming and outgoing phone calls, allowing users to instantly convert customer interactions into actionable leads with minimal friction.

Unlike traditional CRMs filled with dashboards, spreadsheets, and complex workflows, CRM-Lite focuses on one core philosophy:

> Every call automatically becomes a lead.

The app is designed to reduce mental overload, simplify follow-ups, and help business owners remember every important customer interaction without slowing down their workflow.

---

# Features

## Automatic Call Logging

- Detect incoming and outgoing calls
- Automatically capture:
  - phone number
  - call duration
  - timestamp
  - incoming/outgoing status
- Create new leads automatically from unknown numbers
- Maintain call history for every customer

---

## Lead Management

- Auto-create leads from calls
- Edit lead names quickly
- Search leads instantly
- Track recent interactions
- Maintain customer history

---

## Smart Notes

- Add notes for every customer interaction
- Notes are prominently displayed for quick scanning
- Designed for instant context recall

Example notes:

```text
Need 24 inch black planter for hotel project
Call back tomorrow evening
Quotation sent on WhatsApp
```

---

## Quick Actions

Each lead supports:

- WhatsApp shortcut
- Direct call shortcut
- Quick note access
- Edit lead details

---

## Follow-Up System

Track pending actions:

- Follow up today
- Follow up tomorrow
- Callback reminders

The app is designed to aggressively surface pending follow-ups so important leads are never forgotten.

---

# Product Philosophy

CRM-Lite is not an enterprise CRM.

It is:

- a business memory system
- a call-first CRM
- a mobile operational tool
- a lightweight follow-up engine

The app is optimized for:

- fast lead capture
- one-hand mobile usage
- high-volume calling workflows
- minimal typing
- quick actions
- instant recall of customer context

---

# UI/UX Principles

CRM-Lite is built with:

- dark modern UI
- minimalist interactions
- fast scanning layouts
- high readability
- touch-friendly controls

The most important information is always prioritized:

1. Customer Name
2. Customer Need / Note
3. Follow-Up Status
4. Quick Actions

The UI avoids:

- complex dashboards
- spreadsheet-style layouts
- excessive forms
- unnecessary analytics

The goal is operational speed.

---

# Architecture & Tech Stack

CRM-Lite follows modern Android development best practices using a clean MVVM architecture.

## Tech Stack

### Frontend

- Kotlin
- Jetpack Compose
- Material 3

### Architecture

- MVVM (Model-View-ViewModel)

### Database

- Room Database
- SQLite

### Async Operations

- Kotlin Coroutines
- Kotlin Flow

### Build System

- Kotlin Gradle DSL (`.gradle.kts`)

---

# Project Structure

```text
app/src/main/java/com/example/
│
├── data/
│   ├── AppDatabase.kt
│   ├── CallLogAndLeadDao.kt
│   ├── LeadEntity.kt
│   ├── CallLogEntity.kt
│   ├── DismissedCallEntity.kt
│   └── CrmRepository.kt
│
├── viewmodel/
│   └── CrmViewModel.kt
│
├── ui/theme/
│   ├── Color.kt
│   ├── Theme.kt
│   └── Type.kt
│
├── MainActivity.kt
└── CallReceiver.kt
```

---

# Screens

## Calls Screen

Displays:

- recent calls
- incoming/outgoing indicators
- quick lead conversion
- call history

Built for fast customer interaction tracking.

---

## Leads Screen

Displays:

- customer names
- notes
- follow-up badges
- quick action buttons
- edit functionality
- search functionality

Optimized for instant customer recall.

---

# Android Permissions

CRM-Lite requires native Android telephony permissions for automatic call tracking functionality.

Required permissions:

```xml
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.READ_CALL_LOG" />
<uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS" />
```

These permissions allow the app to:

- detect call states
- read call logs
- track outgoing calls
- create automatic lead entries

---

# Installation

## Prerequisites

- Android Studio (latest recommended)
- Android SDK 34+
- JDK 17+

---

## Clone Repository

```bash
git clone https://github.com/destroyer690420/crm-lite.git
cd crm-lite
```

---

## Open in Android Studio

1. Open Android Studio
2. Click:

```text
Open Existing Project
```

3. Select the `crm-lite` folder

---

## Sync Gradle

Allow Android Studio to:

- download dependencies
- sync Gradle files
- build the project

---

## Run the App

Connect an Android device or emulator.

Then run:

```bash
Shift + F10
```

or click:

```text
Run
```

---

# Future Roadmap

Planned features:

- voice notes
- AI transcription
- AI follow-up suggestions
- WhatsApp automation
- quotation generation
- OCR business card scanner
- export/import support
- cloud sync
- team collaboration

---

# Why CRM-Lite Exists

Most CRMs are built for:

- large sales teams
- desktop workflows
- enterprise pipelines

CRM-Lite is built for:

- real business owners
- people constantly on calls
- mobile-first workflows
- chaotic day-to-day operations

The goal is simple:

> Help business owners remember every customer interaction without slowing them down.

---

