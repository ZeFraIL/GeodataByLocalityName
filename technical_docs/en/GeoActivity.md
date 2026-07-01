# Technical Description of the GeoActivity Class

This document provides a detailed analysis of the main class of the GeodataByLocalityName application.

---

## 1. General Information
*   **Class Name:** `GeoActivity`
*   **Type:** Activity (Application Screen)
*   **Purpose:** It is the main and only screen of the application. The class is responsible for interacting with the user (data input), retrieving geographic coordinates via the Internet, and displaying the result.
*   **Interaction:** Directly works with the interface layout (`activity_geo.xml`), the system service `Geocoder`, and the internal class `NetworkReceiver`.

---

## 2. Variables (Class Fields)

| Name | Type | Purpose | Where used |
| :--- | :--- | :--- | :--- |
| `etCityName` | `EditText` | Field for entering the city name. | `onCreate`, `findCoordinates` |
| `etCountryName` | `EditText` | Field for entering the country name. | `onCreate`, `findCoordinates` |
| `btnFindCoordinates` | `Button` | Button to start the search. | `onCreate`, `NetworkReceiver` |
| `tvResult` | `TextView` | Text field to display the result or errors. | Throughout the class |
| `networkReceiver` | `NetworkReceiver` | Object for tracking network status. | `onCreate`, `onDestroy` |
| `progressBar` | `ProgressBar` | Loading indicator (spinning wheel). | `onCreate`, `findCoordinates` |

---

## 3. Class Methods

### Method: `onCreate`
*   **Type:** `protected`
*   **Return value:** `void` (nothing)
*   **Parameters:** `Bundle savedInstanceState` (stored state of the screen).
*   **What it does:** Initializes the Activity. It links variables to UI elements, sets a click listener on the button, and registers the network status receiver (`NetworkReceiver`).
*   **When called:** When the screen is first launched by the Android system.

### Method: `onDestroy`
*   **Type:** `protected`
*   **Return value:** `void`
*   **Parameters:** None.
*   **What it does:** Performs resource cleanup. In this case, it unregisters the `networkReceiver` so that the app doesn't waste resources after it's closed.
*   **When called:** Just before the Activity is finally closed.

### Method: `findCoordinates`
*   **Type:** `private`
*   **Return value:** `void`
*   **Parameters:**
    | Name | Type | Description |
    | :--- | :--- | :--- |
    | `cityName` | `String` | Text containing the city name. |
    | `countryName` | `String` | Text containing the country name. |
*   **What it does:**
    1. Creates a `Geocoder` object to work with Google maps services.
    2. Starts a new background `Thread`, as network operations are forbidden in the main (UI) thread.
    3. Attempts to find coordinates by address using `geocoder.getFromLocationName`.
    4. Returns to the main thread via `runOnUiThread` to update the text on the screen and hide the `ProgressBar`.
*   **When called:** When the "Find coordinates" button is clicked (after input validation).
*   **Important to understand:** If the internet is slow or Google servers are unreachable, an `IOException` might occur, which is handled in the `catch` block.

---

## 4. Lifecycle (Activity)
*   **`onCreate()`**: Called during creation. This is where we "build" the screen and prepare data.
*   **`onDestroy()`**: Called during destruction. This is where we disconnect network tracking to avoid memory leaks.

---

## 5. Interface Interaction (UI)
*   **Elements:** `EditText` (input), `Button` (action), `TextView` (output), `ProgressBar` (indication).
*   **Linkage:** The `findViewById(R.id...)` method is used to find elements defined in the XML file.
*   **Events:** Button clicks are handled via `setOnClickListener`.

---

## 6. Inner Class: NetworkReceiver
This class is located inside `GeoActivity` and is a **BroadcastReceiver**.

*   **Purpose:** Monitors whether the Internet is enabled on the device.
*   **Method `onReceive`**: Called by the Android system every time the network state changes (e.g., Wi-Fi turned off).
*   **Logic:** If there is no internet, the method disables the search button (`setEnabled(false)`) and displays a warning. If the internet becomes available, it re-enables the button.

---

## 7. General Class Logic
1. The app starts and checks for internet connectivity.
2. The user enters the city and country.
3. The user clicks the button.
4. In a background thread, the app "asks" the Google service for coordinates.
5. The result (latitude and longitude) is displayed on the screen.

---

## 8. Simplified Explanation (In Simple Words)
Imagine that `GeoActivity` is a **clerk at an information desk**.
1. The clerk has **forms** (`EditText`) for recording the question.
2. When you click the button, the clerk doesn't search for the answer himself (to avoid holding up the line); instead, he sends a **messenger** (a new `Thread`) to a huge library (Google's server).
3. While the messenger is running, an **hourglass** (`ProgressBar`) spins on the desk.
4. When the messenger returns, the clerk writes the answer on a **scoreboard** (`TextView`).
5. Also, the clerk has a **radio receiver** (`NetworkReceiver`) that reports if communication with the outside world is cut off. If the connection is lost, the clerk hangs a "Closed" sign on the button.

---

## ⚠️ Recommendations for Improvement
*   **Bugs/Bad Practices:** Using `new Thread(...).start()` is considered an outdated approach. In modern Android development, `Coroutines` (for Kotlin) or `Executors` (for Java) are recommended.
*   **Improvement:** Add a check for Google Services availability, as `Geocoder` might not work without them.
