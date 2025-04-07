import { initializeApp } from "https://www.gstatic.com/firebasejs/9.21.0/firebase-app.js";
import { getDatabase, ref, get, update } from "https://www.gstatic.com/firebasejs/9.21.0/firebase-database.js";

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

const urlParams = new URLSearchParams(window.location.search);
const eventId = urlParams.get("eventId");

const eventRef = ref(database, 'eventForm/' + eventId);
const eventName = document.getElementById("eventName");
const eventDetails = document.getElementById("eventDetails");
const ticketPrice = document.getElementById("ticketPrice");
const location = document.getElementById("location");
const date = document.getElementById("date");
const stime = document.getElementById("stime");
const etime = document.getElementById("etime");
const imageUrl = document.getElementById("imageUrl");
const eventIdInput = document.getElementById("eventId");
const ticketAmount = document.getElementById("ticketAmount");

get(eventRef).then((snapshot) => {
  if (snapshot.exists()) {
    const eventData = snapshot.val();
    eventIdInput.value = eventId;
    eventName.value = eventData.eventName;
    eventDetails.value = eventData.eventDetails;
    ticketPrice.value = eventData.ticketPrice;
    ticketAmount.value = eventData.ticketAmount;
    location.value = eventData.location;
    date.value = eventData.date;
    stime.value = eventData.stime;
    etime.value = eventData.etime;
    imageUrl.value = eventData.imageUrl;
  } else {
    alert("Event not found!");
  }
});

document.getElementById("updateEventForm").addEventListener("submit", (e) => {
  e.preventDefault();

  const updatedEvent = {
    eventName: eventName.value,
    eventDetails: eventDetails.value,
    ticketPrice: ticketPrice.value,
    ticketAmount: ticketAmount.value,
    location: location.value,
    date: date.value,
    stime: stime.value,
    etime: etime.value,
    imageUrl: imageUrl.value
  };

  const updates = {};
  updates['eventForm/' + eventId] = updatedEvent;

  update(ref(database), updates)
    .then(() => {
      alert("Event updated successfully!");
      window.location.href = "index.html";
    })
    .catch((error) => {
      alert("Error updating event: " + error);
    });
});
