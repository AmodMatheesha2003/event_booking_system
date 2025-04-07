import { initializeApp } from "https://www.gstatic.com/firebasejs/9.6.1/firebase-app.js";
import { getDatabase, ref, get } from "https://www.gstatic.com/firebasejs/9.6.1/firebase-database.js";

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

const app = initializeApp(firebaseConfig);
const db = getDatabase(app);

async function fetchData() {
    try {
        const eventsRef = ref(db, "eventForm");
        const incomeRef = ref(db, "Income");

        const [eventsSnapshot, incomeSnapshot] = await Promise.all([get(eventsRef), get(incomeRef)]);

        const events = eventsSnapshot.val() || {};
        const income = incomeSnapshot.val() || {};

        let incomeData = {};
        for (const key in income) {
            const entry = income[key];
            const eventId = entry.eventId;
            const totalPrice = parseFloat(entry.totalPrice) || 0;  

            if (eventId in incomeData) {
                incomeData[eventId] += totalPrice;
            } else {
                incomeData[eventId] = totalPrice;
            }
        }

        const tableBody = document.getElementById("event-income-table");
        tableBody.innerHTML = "";

        for (const eventId in events) {
            const event = events[eventId];
            const totalIncome = incomeData[eventId] || 0;

            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${event.eventName}</td>
                <td>${totalIncome.toLocaleString()}</td>
            `;
            tableBody.appendChild(row);
        }
    } catch (error) {
        console.error("Error fetching data:", error);
    }
}

window.onload = fetchData;
