# 📱 Android App Documentation: GeodataByLocalityName

________________________________________
🧾 General Information
**Project Name:**  
GeodataByLocalityName

**Author(s):**  
Zeev Fraiman

**Date:**  
November 2024

**Language:**  
Java

**IDE:**  
Android Studio

**Android Version (minSdk / targetSdk):**  
28 / 34
________________________________________
🎯 Project Goals
• **What problem does the app solve:**  
Allows users to retrieve geographic coordinates (latitude and longitude) of any locality based on its name and country.

• **Why is this task important:**  
It is a fundamental tool for travelers, logisticians, and developers who need to quickly determine the exact location of a place without using complex mapping services.

• **Target Audience:**  
Regular users needing coordinates for GPS navigators or reference purposes.
________________________________________
📌 Requirements
**Functional Requirements**
• Input city and country name.
• Retrieve coordinates via Google Geocoder API.
• Display execution progress (ProgressBar).
• Real-time network status monitoring.

**Non-functional Requirements**
• **Performance:** Network requests performed in a background thread to prevent UI freezing.
• **Usability:** Minimalist interface, intuitive input fields.
• **Reliability:** Error handling for lack of internet and situations where the address is not found.
________________________________________
🧠 General Architecture
• **Approach:**  
MVC (Model-View-Controller) based on a single Activity.

• **Why this approach:**  
For a small utility app, this ensures minimal code overhead and high development speed while maintaining readability.

• **Main System Components:**  
- `GeoActivity`: Main controller and screen.
- `Geocoder`: System service for direct geocoding.
- `NetworkReceiver`: Component for tracking internet status.
________________________________________
🧩 UML Diagram
`[GeoActivity] ──> [Geocoder]`  
`[GeoActivity] ──> [NetworkReceiver]`  
`[GeoActivity] ──> [UI Layout]`
________________________________________
**Explanation:**
- **Why these components:** Data retrieval logic is separated from network monitoring logic.
- **Scalability:** The `Geocoder` can be easily replaced with a Retrofit request to an external API, or the logic can be moved to a `ViewModel` (MVVM) in the future.
________________________________________
🧩 Class Description
📌 **Class: GeoActivity**
**Role:**  
Main entry point and only screen of the app.

**Responsibility:**  
Life cycle management, user input handling, UI updates.

**Main Methods:**  
- `onCreate()` — UI initialization and network receiver registration.
- `findCoordinates()` — starts a background thread to call Geocoder.
- `onDestroy()` — resource cleanup and receiver unregistration.

**Interaction:**  
Interacts with `NetworkReceiver` to disable the search button when offline.
________________________________________
🔄 Workflow Diagram
1. User enters "Tel Aviv" and "Israel".
2. Clicks "Find Coordinates".
3. App checks for internet connection.
4. Background thread starts.
5. `Geocoder` returns a list of addresses.
6. The first result is displayed on the screen.
________________________________________
🎨 UI/UX Analysis
• **Why the interface is designed this way:**  
Maximum simplicity for quick results.

• **Principles used:**  
- **Simplicity:** Only two fields and one button.
- **Logic:** Top-to-bottom: input -> action -> result.
- **Accessibility:** Button active only when online, preventing useless clicks.

• **Improvements:**  
Add city name autocomplete and a "Show on Map" button.
________________________________________
⚙️ Threading
• **Used:**  
- `Thread` (for network requests).
- `runOnUiThread` (for UI updates).

• **Why this method:**  
Direct use of `Thread` is justified in educational and small projects to understand Android threading basics without bloating with external libraries.

• **Prevention:**  
- **Freezing (ANR):** Heavy operations moved out of the main thread.
- **Memory Leaks:** Receiver is unregistered in `onDestroy()`.
________________________________________
💾 Data Management
• **Storage:**  
Data is not stored locally; it is requested dynamically.

• **Why this method:**  
Coordinates may change or be updated, and local storage of a city database is impractical due to size.
________________________________________
🌐 Networking
• **How requests are made:**  
Via the standard `android.location.Geocoder` library.

• **Error Handling:**  
`try-catch` block is used to catch `IOException`.

• **Offline behavior:**  
`NetworkReceiver` disables the search button and shows a warning.
________________________________________
🔐 Security
• The app does not collect or transmit personal data.
________________________________________
🧪 Testing
• **Unit Tests:** Input string validity checks.
• **UI Tests:** Button availability check during network state changes.
________________________________________
🐞 Error Handling
• **Covered:** No internet, empty input, address not found, API errors.
• **Reaction:** Displaying a text message to the user and hiding the progress bar.
________________________________________
⚡ Performance
• Optimization: Using `Locale.getDefault()` for correct Geocoder behavior across different regions.
________________________________________
🚀 Expansion Opportunities
• Google Maps integration.
• Query history storage in SQLite/Room.
• Support for multiple input languages.
________________________________________
📊 Self-Assessment
| Criterion | Rating (1–10) |
| :--- | :--- |
| Architecture | 7 |
| Code | 9 |
| UI/UX | 8 |
| Reliability | 9 |
| **Overall Level** | **8.2** |
________________________________________
🏁 Conclusion
• **Best Achievement:**  
Reliable network state handling implementation.

• **Challenges:**  
Configuring proper `Geocoder` behavior in a background thread.

• **Skills Acquired:**  
Working with multithreading, Android system services, and BroadcastReceivers.
________________________________________
📎 Appendix
• Repository links: [GitHub](https://github.com/zeevfraiman/GeodataByLocalityName)
