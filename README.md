


# 🎮 First Assignment Game – Android (Kotlin)

An Android arcade-style game developed in **Kotlin**
featuring:
- **grid-based gameplay**, 
- **button & tilt controls**,
- **dynamic difficulty**,
- **sound & vibration feedback**,
- **location-based leaderboard with map integration**.

---

## 🕹️ Game Overview

The player controls a character moving **left and right** on a **5×5 grid**.  
Objects fall from the top of the screen and move downward over time.

### 🎯 Goal
Survive as long as possible, collect coins, avoid energy blasts, and achieve a high score.

---

## 🎮 Controls

### Button Mode
- ⬅️ Left Button – move left
- ➡️ Right Button – move right

### Tilt Mode (Accelerometer)
- Tilt **left / right** → move player
- Tilt **forward** → increase game speed
- Tilt **backward** → decrease game speed

---

## ⚙️ Difficulty

- **Slow** – slower obstacle movement
- **Fast** – faster obstacle movement

In **Tilt Mode**, speed can also be adjusted dynamically by tilting the device forward/backward.

---

## 🧱 Game Objects & Rules

### ☄️Energy Blast 
A hazardous object. When the player collides with an Energy Blast, one life is lost.

### Senzu Bean 🌱
A collectible item that restores one life when picked up.

### Coin 🟡
A collectible item that increases the player’s score by 50 points.

### Player 🧍
The character controlled by the user. The player can move left and right using on-screen buttons or by tilting the device.

---

## ❤️ Lives System

- Player starts with **3 lives**
- Losing a life:
  - Sound effect
  - Vibration
  - Toast message
- When lives reach **0** → Game Over
- Player can collect a senzu bean to regain one life (up to 3 max)
---

## 🧮 Scoring System

Final Score is calculated as:

(Coins Collected × 50) + (Distance Traveled × 7)

---

## 🏁 Game Over

At game end:
- Final score is displayed
- If score is in **Top 10**:
  - Player enters name
  - Score is saved locally
  - GPS location is attached (if permission granted) if not granted a default location will be saved

---

## 🏆 Leaderboard

- Displays **Top 10 scores**
- Each entry includes:
  - Player name
  - Score
  - Latitude & Longitude
- Clicking an entry zooms the map to the score location 📍

---

## 🗺️ Map Integration

- Implemented using **Google Maps**
- Default placeholder location shown
- Selecting a score:
  - Clears previous markers
  - Adds a new marker
  - zooms camera to saved location

---


### Audio
- Background music (looped)
- Sound effects for:
  - Button clicks
  - Collisions
  - Coin pickup
  - Life gain
  - Game over
  - High score events



## 🚀 How to Clone and Run the Game

- Clone the Repository
- Open the project folder in Android Studio.
- Connect a physical Android device (recommended) or start an emulator
- Run the app.

---


## 👨‍💻 Author
Rotem Bar
Developed as part of an Android programming assignment.
