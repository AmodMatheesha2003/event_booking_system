import { initializeApp } from "https://www.gstatic.com/firebasejs/9.21.0/firebase-app.js";
import { getDatabase, ref, push, set } from "https://www.gstatic.com/firebasejs/9.21.0/firebase-database.js";

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
const contactFormDB = ref(database, 'eventForm');

const getElementVal = (id) => {
  return document.getElementById(id).value;
};

document.getElementById("eventForm").addEventListener("submit", submitForm);

function submitForm(e) {
  e.preventDefault();

  const eventName = getElementVal("eventName");
  const eventDetails = getElementVal("eventDetails");
  const ticketPrice = getElementVal("ticketPrice");
  const location = getElementVal("location");
  const date = getElementVal("date");
  const stime = getElementVal("stime");
  const etime = getElementVal("etime");
  const imageUrl = getElementVal("imageUrl");
  const ticketAmount = getElementVal("ticketAmount");

  saveMessages(eventName, eventDetails, ticketPrice, ticketAmount, location, date, stime, etime, imageUrl);
}

const saveMessages = (eventName, eventDetails, ticketPrice, ticketAmount, location, date, stime, etime, imageUrl) => {
  const newContactForm = push(contactFormDB);
  set(newContactForm, {
    eventName,
    eventDetails,
    ticketPrice,
    ticketAmount,
    location,
    date,
    stime,
    etime,
    imageUrl
  })
  .then(() => {
    alert('Event added successfully!');
    window.location.href = "index.html";
  })
  .catch((error) => {
    console.error('Error adding event: ', error);
    alert('There was an error adding the event. Please try again.');
  });
};
