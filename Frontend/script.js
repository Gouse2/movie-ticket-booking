const API_BASE = 'http://localhost:8080';

// Popup controls
const bookingPopup = document.getElementById('booking-popup');
const userNameInput = document.getElementById('userNameInput');
const userPhoneInput = document.getElementById('userPhoneInput');
const confirmBookBtn = document.getElementById('confirm-book-btn');
const cancelBookBtn = document.getElementById('cancel-book-btn');

function showLoading(sectionId) {
    const element = document.getElementById(sectionId);
    if (element) {
        element.innerHTML = '<p class="loading">Loading...</p>';
    }
}

let selectedSeats = [];
let currentShowId = null;

// Load on page start
window.onload = () => {
    loadMovies();
    loadBookings();
};

async function loadMovies() {
    showLoading('movies-list');

    try {
        const res = await fetch(`${API_BASE}/movies`);
        if (!res.ok) throw new Error(`Movies fetch failed: ${res.status}`);
        const movies = await res.json();
        const list = document.getElementById('movies-list');
        list.innerHTML = '';

        // Poster URLs for all movies (real ones where available)
        const posters = {
            "Kalki 2898 AD": "https://m.media-amazon.com/images/M/MV5BMjE4ZTZlNDAtM2Q3YS00YjFhLThjN2UtODg2ZGNlN2E2MWU1XkEyXkFqcGc@._V1_.jpg",
            "The Raja Saab": "https://upload.wikimedia.org/wikipedia/en/9/9e/The_RajaSaab_poster.jpg",
            "Mana Shankara Vara Prasad Garu": "https://upload.wikimedia.org/wikipedia/en/4/43/Mana_Shankara_Vara_Prasad_Garu.jpg",
            "Anaganaga Oka Raju": "https://upload.wikimedia.org/wikipedia/en/1/1a/Anaganaga_Oka_Raju.jpg",
            "Nari Nari Naduma Murari": "https://assets-in.bmscdn.com/iedb/movies/images/mobile/thumbnail/xlarge/nari-nari-naduma-murari-et00478367-1766561142.jpg",
            "Bhartha Mahasayulaku Wignyapthi": "https://upload.wikimedia.org/wikipedia/en/d/de/Bhartha_Mahasayulaku_Wignyapthi.jpg",
            "Funky": "https://via.placeholder.com/300x450/3498db/ffffff?text=Funky"
        };
       
        movies.forEach(movie => {
            const card = document.createElement('div');
            card.className = 'movie-card';
            card.style.textAlign = 'center';
            card.style.padding = '15px';

            const img = document.createElement('img');
            img.src = posters[movie.title] || 'https://via.placeholder.com/300x450/cccccc/000000?text=' + encodeURIComponent(movie.title);
            img.alt = movie.title;
            img.style.width = '220px';
            img.style.height = '320px';
            img.style.objectFit = 'cover';
            img.style.borderRadius = '12px';
            img.style.marginBottom = '15px';
            img.style.boxShadow = '0 6px 15px rgba(0,0,0,0.2)';

            const title = document.createElement('p');
            title.textContent = movie.title;
            title.style.fontWeight = 'bold';
            title.style.fontSize = '1.3rem';
            title.style.marginBottom = '8px';

            const genre = document.createElement('p');
            genre.textContent = `(${movie.genre})`;
            genre.style.color = '#555';
            genre.style.fontSize = '1rem';

            card.appendChild(img);
            card.appendChild(title);
            card.appendChild(genre);

            card.onclick = () => {
                window.location.href = `shows.html?id=${movie.id}&title=${encodeURIComponent(movie.title)}`;
            };
            list.appendChild(card);
        });
    } catch (err) {
        console.error("Error loading movies:", err);
        document.getElementById('movies-list').innerHTML = '<p>Error loading movies. Try refresh.</p>';
    }
}

async function showShowsForMovie(movieId, title) {
    document.getElementById('selected-movie-title').textContent = title;
    document.getElementById('shows-section').style.display = 'block';
    document.getElementById('seats-section').style.display = 'none';
    showLoading('shows-list');

    try {
        const res = await fetch(`${API_BASE}/shows/movie/${movieId}`);
        if (!res.ok) throw new Error(`Shows fetch failed: ${res.status}`);
        const shows = await res.json();
        console.log("Shows data:", shows);

        const list = document.getElementById('shows-list');
        list.innerHTML = '';

        if (shows.length === 0) {
            list.innerHTML = '<p>No shows available for this movie.</p>';
            return;
        }

        shows.forEach(show => {
            const div = document.createElement('div');
            div.className = 'show-card';
            div.textContent = `${show.time} - Screen ${show.screenNumber} - ₹${show.baseTicketPrice}`;
            div.onclick = () => showSeatsForShow(show.id, show.time);
            list.appendChild(div);
        });
    } catch (err) {
        console.error("Error loading shows:", err);
        document.getElementById('shows-list').innerHTML = '<p>Error loading shows. Try again.</p>';
    }
}

async function showSeatsForShow(showId, time) {
    document.getElementById('selected-show-time').textContent = time;
    document.getElementById('seats-section').style.display = 'block';

    selectedSeats = [];
    currentShowId = showId;
    updateBookButton();

    showLoading('seats-grid');

    try {
        const res = await fetch(`${API_BASE}/seats/show/${showId}`);
        if (!res.ok) throw new Error(`Seats fetch failed: ${res.status}`);
        const seats = await res.json();
        console.log("Seats data:", seats);

        const grid = document.getElementById('seats-grid');
        grid.innerHTML = '';

        seats.forEach(seat => {
            const div = document.createElement('div');
            div.className = 'seat';
            div.textContent = `${seat.row}-${seat.column}`;
            div.title = `Row ${seat.row}, Col ${seat.column}, ₹${seat.price.toFixed(2)}`;

            if (seat.status && seat.status.toUpperCase() === 'AVAILABLE') {
                div.classList.add('available');
                div.onclick = () => {
                    div.classList.toggle('selected');
                    toggleSeat(seat.id, seat.price);
                };
            } else {
                div.classList.add('booked');
            }
            grid.appendChild(div);
        });
    } catch (err) {
        console.error("Error loading seats:", err);
        document.getElementById('seats-grid').innerHTML = '<p>Error loading seats. Try again.</p>';
    }
}

function toggleSeat(seatId, price) {
    if (selectedSeats.some(s => s.id === seatId)) {
        selectedSeats = selectedSeats.filter(s => s.id !== seatId);
    } else {
        selectedSeats.push({ id: seatId, price });
    }
    updateBookButton();
}

function updateBookButton() {
    const btn = document.getElementById('book-btn');
    const totalEl = document.getElementById('total-price');
    const total = selectedSeats.reduce((sum, s) => sum + s.price, 0) * 1.18;
    totalEl.textContent = `Total (incl. GST): ₹${total.toFixed(2)}`;
    btn.disabled = selectedSeats.length === 0;
    btn.onclick = bookSeats;
}

async function bookSeats() {
    if (selectedSeats.length === 0) return;

    // Show the popup form
    bookingPopup.style.display = 'flex';
    userNameInput.value = '';  // clear old values
    userPhoneInput.value = '';

    // When user clicks "Confirm Booking"
    confirmBookBtn.onclick = async () => {
        const userName = userNameInput.value.trim();
        const userPhone = userPhoneInput.value.trim();

        if (!userName || !userPhone) {
            alert("Please enter both name and phone number!");
            return;
        }

        const payload = {
            userName,
            userPhone,
            show: { id: currentShowId },
            bookedSeats: selectedSeats.map(s => ({ id: s.id }))
        };

        try {
            const res = await fetch(`${API_BASE}/bookings`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (res.ok) {
                alert("Booking successful!");
                selectedSeats = [];
                updateBookButton();
                loadBookings();
                bookingPopup.style.display = 'none';  // hide popup
            } else {
                alert("Booking failed! Check console.");
                console.error("Booking error:", await res.text());
            }
        } catch (err) {
            console.error("Booking fetch error:", err);
            alert("Network error during booking.");
        }
    };

    // When user clicks "Cancel"
    cancelBookBtn.onclick = () => {
        bookingPopup.style.display = 'none';
    };
}

async function loadBookings() {
    console.log("Loading bookings now...");
    showLoading('bookings-list');

    try {
        const res = await fetch(`${API_BASE}/bookings`);
        console.log("Bookings response status:", res.status);
        if (!res.ok) throw new Error(`Bookings fetch failed: ${res.status}`);
        const bookings = await res.json();
        console.log("Bookings data:", bookings);

        const list = document.getElementById('bookings-list');
        list.innerHTML = '';

        if (bookings.length === 0) {
            list.innerHTML = '<p>No bookings yet.</p>';
            return;
        }

        bookings.forEach(b => {
            const div = document.createElement('div');
            div.className = 'booking-item';
            div.innerHTML = `
                <p><strong>${b.userName}</strong> (${b.userPhone})</p>
                <p>Movie: ${b.movieTitle || 'Unknown'}</p>
                <p>Show: ${b.showTime || 'N/A'} - Screen ${b.showScreenNumber || 'N/A'}</p>
                <p>Total: ₹${b.totalAmount.toFixed(2)}</p>
                <p>Seats: ${b.bookedSeats.map(s => `${s.row}-${s.column}`).join(', ')}</p>
                <button onclick="cancelBooking(${b.id})">Cancel Booking</button>
                <hr>
            `;
            list.appendChild(div);
        });
    } catch (err) {
        console.error("Error loading bookings:", err);
        document.getElementById('bookings-list').innerHTML = '<p>Error loading bookings. Check console.</p>';
    }
}

async function cancelBooking(bookingId) {
    if (!confirm("Cancel this booking? Seats will be available again.")) return;

    try {
        const res = await fetch(`${API_BASE}/bookings/${bookingId}`, {
            method: 'DELETE'
        });

        if (res.ok) {
            alert("Booking cancelled!");
            loadBookings();
        } else {
            alert("Cancel failed!");
            console.error("Cancel error:", await res.text());
        }
    } catch (err) {
        console.error("Cancel fetch error:", err);
        alert("Network error during cancel.");
    }
}

// Movie search filter - hides/shows cards as you type (moved outside cancelBooking)
document.getElementById('movie-search').addEventListener('input', (e) => {
    const searchText = e.target.value.toLowerCase().trim();
    const movieCards = document.querySelectorAll('.movie-card');

    movieCards.forEach(card => {
        const cardText = card.textContent.toLowerCase();
        card.style.display = cardText.includes(searchText) ? 'inline-block' : 'none';
    });
});