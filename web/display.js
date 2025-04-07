import { initializeApp } from "https://www.gstatic.com/firebasejs/9.21.0/firebase-app.js";
import { getDatabase, ref, onValue, remove } from "https://www.gstatic.com/firebasejs/9.21.0/firebase-database.js";

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
const eventRef = ref(database, 'eventForm');
const eventContainer = document.getElementById('eventContainer');

onValue(eventRef, (snapshot) => {
  eventContainer.innerHTML = ''; 
  snapshot.forEach((childSnapshot) => {
    const eventData = childSnapshot.val();

    const eventCard = document.createElement('div');
    eventCard.classList.add('event-card');

    eventCard.innerHTML = `
      <img src="${eventData.imageUrl}" alt="${eventData.eventName} Image" class="event-image">
      <div class="event-details">
        <h2>${eventData.eventName}</h2>
        <p><strong>Details:</strong> ${eventData.eventDetails}</p>
        <p><strong>Ticket Price:</strong> RS ${eventData.ticketPrice}</p>
        <p><strong>Ticket Amount:</strong> ${eventData.ticketAmount}</p>
        <p><strong>Location:</strong> ${eventData.location}</p>
        <p><strong>Date:</strong> ${eventData.date}</p>
        <p><strong>Time:</strong> ${eventData.stime} - ${eventData.etime}</p>
      </div>
      <div class="button-container">
        <button class="delete-event-button" onclick="deleteEvent('${childSnapshot.key}')">Delete Event</button> 
        <button onclick="window.location.href='update_event.html?eventId=${childSnapshot.key}'">Update Event</button>
        
    </div>
    `;

    eventContainer.appendChild(eventCard);
  });
});

window.deleteEvent = function(eventId) {
  const eventToDelete = ref(database, `eventForm/${eventId}`);
  
  if (confirm('Are you sure you want to delete this event?')) {
    remove(eventToDelete)
      .then(() => {
        alert('Event deleted successfully!');
        location.reload(); 
      })
      .catch((error) => {
        console.error('Error deleting event:', error);
        alert('Error deleting event.');
      });
  }
};