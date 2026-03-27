# SplitPay

An expense splitting Android app built with Jetpack Compose and Clean Architecture.

## Features

- Create expense groups with named members
- Add expenses with equal or custom splits
- Automatic debt simplification (minimizes number of transactions)
- Per-member balance tracking with visual indicators
- Transaction history per group
- One-tap settle up flow

## Tech Stack

- **UI**: Jetpack Compose + Material3
- **Architecture**: MVVM + Clean Architecture
- **Async**: Kotlin Coroutines + Flow
- **DI**: Manual ServiceLocator pattern
- **Data**: In-memory mock implementations (Firebase Firestore/Auth interfaces)
- **Navigation**: Jetpack Navigation Compose

## Architecture

```
┌─────────────────────────────────────────────┐
│              Presentation Layer              │
│  ┌─────────┐  ┌──────────┐  ┌───────────┐  │
│  │ Screens  │  │ViewModels│  │Components │  │
│  └────┬─────┘  └────┬─────┘  └───────────┘  │
│       │              │                       │
├───────┼──────────────┼───────────────────────┤
│       ▼              ▼   Domain Layer        │
│  ┌──────────┐  ┌──────────┐  ┌───────────┐  │
│  │Use Cases │  │  Models  │  │Repo Ifaces│  │
│  └────┬─────┘  └──────────┘  └─────┬─────┘  │
│       │                            │         │
├───────┼────────────────────────────┼─────────┤
│       ▼        Data Layer          ▼         │
│  ┌──────────┐              ┌─────────────┐   │
│  │Repo Impls│◄─────────────│Mock Firebase│   │
│  └──────────┘              └─────────────┘   │
└─────────────────────────────────────────────┘
```

## Debt Simplification Algorithm

Uses a greedy approach with priority queues to minimize the number of transactions needed to settle all debts within a group. Matches the largest creditor with the largest debtor iteratively.

## Build Instructions

1. Open the project in Android Studio (Hedgehog or later)
2. Sync Gradle files
3. Run on an emulator or device (minSdk 26 / Android 8.0+)

```bash
./gradlew assembleDebug
```
