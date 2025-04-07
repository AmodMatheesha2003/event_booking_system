import { initializeApp } from "https://www.gstatic.com/firebasejs/9.21.0/firebase-app.js";
import { getDatabase, ref, push, set, onValue, remove } from "https://www.gstatic.com/firebasejs/9.21.0/firebase-database.js";

const firebaseConfig = {
    apiKey: "AIzaSyApcNs5iWII4a1jNl89UFg50nZiJzbm4ZY",
    authDomain: "myevents-5d34e.firebaseapp.com",
    databaseURL: "https://myevents-5d34e-default-rtdb.firebaseio.com",
    projectId: "myevents-5d34e",
    storageBucket: "myevents-5d34e.firebasestorage.app",
    messagingSenderId: "354612365177",
    appId: "1:354612365177:web:24461e1fc59e8276efd1a8",
    measurementId: "G-0RBTH315RX"
};

const app = initializeApp(firebaseConfig);
const database = getDatabase(app);

document.getElementById("giveAccessBtn").addEventListener("click", function () {
    const email = document.getElementById("emailInput").value.trim();

    if (email === "") {
        alert("Please enter an email address.");
        return;
    }

    const newOrganizerRef = push(ref(database, "eventorganizer"));

    set(newOrganizerRef, {
        email: email,
        access: "approved"
    }).then(() => {
        alert("Access granted to " + email);
        document.getElementById("emailInput").value = ""; 
    }).catch(error => {
        console.error("Error adding event organizer:", error);
    });
});

function deleteOrganizer(id) {
    if (confirm("Are you sure you want to delete this organizer?")) {
        remove(ref(database, "eventorganizer/" + id))
            .then(() => {
                alert("Organizer deleted successfully.");
            })
            .catch(error => {
                console.error("Error deleting organizer:", error);
            });
    }
}

function loadEventOrganizers() {
    const tableBody = document.getElementById("event-income-table");

    onValue(ref(database, "eventorganizer"), (snapshot) => {
        tableBody.innerHTML = ""; 

        if (snapshot.exists()) {
            snapshot.forEach((childSnapshot) => {
                const data = childSnapshot.val();
                const email = data.email || "N/A";
                const access = data.access || "N/A";
                const id = childSnapshot.key; 

                const row = `<tr>
                                <td>${email}</td>
                                <td>${access}</td>
                                <td><button class="delete-btn" data-id="${id}">Remove</button></td>
                            </tr>`;
                tableBody.innerHTML += row;
            });

            document.querySelectorAll(".delete-btn").forEach(button => {
                button.addEventListener("click", function () {
                    const organizerId = this.getAttribute("data-id");
                    deleteOrganizer(organizerId);
                });
            });
        } else {
            tableBody.innerHTML = `<tr><td colspan="3">No event organizers found.</td></tr>`;
        }
    });
}

loadEventOrganizers();
