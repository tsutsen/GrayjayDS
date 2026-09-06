# Handoff — bluejay (FutoVideo)

Quick state snapshot + what's left. Written 2026-07-09, updated after the #35–#41 session.

## Project
- **FutoVideo** (`bluejay`): Android/Kotlin video platform player, Material You + M3 **Expressive** UI.
- Package: `com.tsutsen.platformplayer` (stable) / `.d` (unstable). minSdk 28, compileSdk 37.
- Stack: Compose, Hilt, Room (v10), Media3 1.9.0, Material3 `1.5.0-alpha27` (expressive), DataStore.
- Repo `/home/leon/Projects/bluejay`, remote `github.com/tsutsen/bluejay`.
- Expressive component reference: `/home/leon/Projects/Compose-Material-3-Expressive-Catalog` (e.g. `AppBarSamples.kt` → `ExitAlwaysBottomAppBarSpacedEvenly`).

## Current state
- Branch: **`feat/platform-sample-updates`**
- Latest commit: `da73a4659` — "layout: portrait nav bar becomes an expressive FlexibleBottomAppBar (#41)".
- Working tree clean (this file was the only untracked file; committed with the updates below).
- All previously-open tasks are **done and committed** this session:
  - `cb5e8c4af` **#35** channel card containers + empty-state placeholder cards (incl. the optimistic subscribe toggle in `ChannelViewModel.kt`).
  - `d804328a5` **#36** playlist cards uniform size + always-on count line.
  - `93c348c36` **#37** wide channel tab rail: selected section icon sits in a `FilledTonalIconButton` circle bubble; unselected stay bare icon buttons.
  - `da73a4659` **#41** portrait nav bar is now a `FlexibleBottomAppBar` with `exitAlwaysScrollBehavior` (scroll up = bar slides away, scroll down = returns, draggable). `remember(barVisible)` recreates the `BottomAppBarState` on fullscreen toggle so the bar always re-enters fully visible. Landscape rail path untouched.

## Stale TODOs — verified done in code (this session)
- #27 badge semibold + `2.0×` format — `PlayerView.kt:803` (trim-trailing-zero + `×`) + `PlayerIndicators.kt:219` (`FontWeight.SemiBold`).
- #28 hold speed = controller behavior — `GestureActionHandler.kt:245` (multiplicative: "holding at 2x goes to 4x, not to a fixed 2x").
- #29 hold-seek mode — `GestureActionHandler.kt` `handleHoldSeek` (pause + scrub via `SEEK_HOLD_PX_TO_MS`, `SeekPreview` badge event, commit + resume on release) and `PlayerView.kt:816` badge UI.
- #39 player loading indicator → contained — `PlayerContent.kt:471` (`ContainedLoadingIndicator`, 48dp).
- #40 store video metadata at library-save time — Room v10: `AppDatabase` `version = 10` + `MIGRATION_9_10` (`postedAt` on `saved_video`/`history`/`playlist_videos`, registered in `AppDatabaseProvider`); write paths: `PlayerViewModel.kt:289` (watch time) and `VideoOptionsViewModel.kt:78` / `VideoOptionsSheetHost.kt:118` (library save). Existing rows keep 0 until re-saved.
- #33/#34/#38 already marked complete earlier.

## Tasks LEFT
- (none from the previous list — see "Current state")

## Build / test gates
- App compile: `./gradlew :app:compileStableDebugKotlin` (and `:app:compileUnstableDebugKotlin`).
- Feature modules have **no** `compileUnstableDebugKotlin` — use `:app:` or `:feature:player:impl:compileDebugKotlin`.
- Gesture unit tests: `./gradlew :feature:player:impl:testDebugUnitTest --tests '*PlayerGestureRecognizerTest'` (24 tests).
- Install to device: build arm64 stable APK → `adb install` (device flavor is **stable**). Pass `--no-configuration-cache` on post-commit install builds.

## Conventions / gotchas
- **No fixed heights/widths** for cards — adapt via aspect ratio + `weight` (cards may shrink/expand to fit parents).
- **Do not store thumbnails** in the DB (URL only).
- New Room fields: add a `Migration` + bump `AppDatabase.version` + register in `AppDatabaseProvider`; existing rows keep the default until re-saved.
- Media3 1.9.0 `PlayerView` has **no** public `setUseTextureView`; **PiP requires SurfaceView** — do not switch surface type.
- **Compose BOM 2026.05.01 resolves animation artifacts to 1.12.0-beta01, where `animateColorAsState` is REMOVED** — use `animateValueAsState` / `animateFloatAsState` etc. (verified via `javap` on `AnimateAsStateKt` in the cached AAR).
- In material3 1.5.0-alpha27, `NavigationBarItem` is a **`RowScope` extension** — nav items must be emitted from inside a `Row`.
- `FlexibleBottomAppBar` (M3 expressive) details: default `expandedHeight` is 64dp (pass 80dp for `NavigationBarItem`s or labels clip); its default `windowInsets` already pad horizontal + bottom system bars (background bleeds edge-to-edge); with a scroll behavior the bar itself becomes draggable.
- LSP (kotlin-language-server) gives stale phantom 🔴s after new files/deps — Gradle compile is the source of truth (see AGENTS.md).
- Devices: AYN Thor `192.168.1.214:5555`, Nothing Phone (3) `192.168.1.159:5555` (both TCP ADB, no root).
