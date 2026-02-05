// API Configuration
const API_BASE_URL = 'http://localhost:8080/api/playlist';

// DOM Elements
const form = document.getElementById('playlistForm');
const playlistUrlInput = document.getElementById('playlistUrl');
const fromIndexInput = document.getElementById('fromIndex');
const toIndexInput = document.getElementById('toIndex');
const calculateBtn = document.getElementById('calculateBtn');
const btnText = document.getElementById('btnText');
const btnLoader = document.getElementById('btnLoader');
const errorMessage = document.getElementById('errorMessage');
const results = document.getElementById('results');

// Result elements
const totalLength = document.getElementById('totalLength');
const averageLength = document.getElementById('averageLength');
const at1_25x = document.getElementById('at1_25x');
const at1_50x = document.getElementById('at1_50x');
const at1_75x = document.getElementById('at1_75x');
const at2_00x = document.getElementById('at2_00x');

// Extract playlist ID from YouTube URL
function extractPlaylistId(url) {
  const patterns = [
    /[?&]list=([^&]+)/,
    /youtube\.com\/playlist\?list=([^&]+)/,
    /youtube\.com\/watch\?v=[^&]+&list=([^&]+)/
  ];

  for (const pattern of patterns) {
    const match = url.match(pattern);
    if (match) return match[1];
  }
  return url.trim();
}

// Show error message
function showError(message) {
  errorMessage.textContent = message;
  errorMessage.classList.remove('hidden');
  results.classList.add('hidden');
}

// Hide error message
function hideError() {
  errorMessage.classList.add('hidden');
}

// Show loading state
function setLoading(isLoading) {
  if (isLoading) {
    calculateBtn.disabled = true;
    btnText.classList.add('hidden');
    btnLoader.classList.remove('hidden');
  } else {
    calculateBtn.disabled = false;
    btnText.classList.remove('hidden');
    btnLoader.classList.add('hidden');
  }
}

// Display results
function displayResults(data) {
  // Update duration values
  totalLength.textContent = data.totalLength || '-';
  averageLength.textContent = data.averageLength || '-';
  at1_25x.textContent = data.at1_25x || '-';
  at1_50x.textContent = data.at1_50x || '-';
  at1_75x.textContent = data.at1_75x || '-';
  at2_00x.textContent = data.at2_00x || '-';

  // Show results
  results.classList.remove('hidden');

  // Smooth scroll to results
  results.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
}

// Fetch playlist duration from API
async function fetchPlaylistDuration(playlistId, fromIndex, toIndex) {
  // Build query parameters
  let queryParams = `?playlistId=${encodeURIComponent(playlistId)}`;

  if (fromIndex) queryParams += `&fromIndex=${fromIndex}`;
  if (toIndex) queryParams += `&toIndex=${toIndex}`;

  const response = await fetch(`${API_BASE_URL}/duration${queryParams}`);

  // Try to parse JSON always (backend returns JSON for errors too)
  let payload = null;
  try {
    payload = await response.json();
  } catch (e) {
    // ignore JSON parse errors
  }

  if (!response.ok) {
    const msg = payload?.message || payload?.error || 'Failed to fetch playlist duration';
    throw new Error(msg);
  }

  return payload;
}

// Form submission handler
form.addEventListener('submit', async (e) => {
  e.preventDefault();

  hideError();
  setLoading(true);

  try {
    const playlistUrl = playlistUrlInput.value.trim();
    const fromIndex = fromIndexInput.value ? parseInt(fromIndexInput.value, 10) : null;
    const toIndex = toIndexInput.value ? parseInt(toIndexInput.value, 10) : null;

    if (!playlistUrl) throw new Error('Please enter a playlist URL or ID');

    if (fromIndex && fromIndex < 1) throw new Error('From index must be at least 1');
    if (toIndex && toIndex < 1) throw new Error('To index must be at least 1');
    if (fromIndex && toIndex && fromIndex > toIndex) throw new Error('From index cannot be greater than To index');

    const playlistId = extractPlaylistId(playlistUrl);
    const data = await fetchPlaylistDuration(playlistId, fromIndex, toIndex);

    displayResults(data);
  } catch (error) {
    console.error('Error:', error);
    showError(error.message || 'An error occurred while calculating playlist duration');
  } finally {
    setLoading(false);
  }
});

// Clear error on input
playlistUrlInput.addEventListener('input', hideError);
fromIndexInput.addEventListener('input', hideError);
toIndexInput.addEventListener('input', hideError);