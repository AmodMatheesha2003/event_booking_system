import { initializeApp } from "https://www.gstatic.com/firebasejs/9.21.0/firebase-app.js";
import { getDatabase, ref, get } from "https://www.gstatic.com/firebasejs/9.21.0/firebase-database.js";

// Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyApcNs5iWII4a1jNl89UFg50nZiJzbm4ZY",
  authDomain: "myevents-5d34e.firebaseapp.com",
  databaseURL: "https://myevents-5d34e-default-rtdb.firebaseio.com",
  projectId: "myevents-5d34e",
  storageBucket: "myevents-5d34e.appspot.com",
  messagingSenderId: "354612365177",
  appId: "1:354612365177:web:24461e1fc59e8276efd1a8",
  measurementId: "G-0RBTH315RX"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const database = getDatabase(app);

// Handle login form submission
document.getElementById("loginForm").addEventListener("submit", async function (event) {
  event.preventDefault();

  const enteredEmail = document.getElementById("email").value.trim();
  const enteredPassword = document.getElementById("password").value.trim();

  const adminRef = ref(database, "admin");

  try {
    const snapshot = await get(adminRef);

    if (snapshot.exists()) {
      const adminData = snapshot.val();

      // Ensure both email and password are strings before comparing
      const correctEmail = String(adminData.email).trim();
      const correctPassword = String(adminData.password).trim();

      if (
        enteredEmail.toLowerCase() === correctEmail.toLowerCase() &&
        enteredPassword === correctPassword
      ) {
        alert("Login successful!");
        window.location.href = "index.html";
      } else {
        alert("Incorrect email or password.");
      }
    } else {
      alert("Admin credentials not found in database.");
    }
  } catch (error) {
    console.error("Error logging in:", error);
    alert("Login failed due to an error.");
  }
});
