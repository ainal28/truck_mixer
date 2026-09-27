# 🚚 Truck Mixer Management System

Aplikasi Android untuk membantu pengelolaan data dan aktivitas operasional Truck Mixer. 
Aplikasi ini dikembangkan menggunakan Kotlin dan terintegrasi dengan REST API berbasis Node.js serta database MySQL.

## 📱 About The Project

Truck Mixer Management System dirancang untuk membantu proses pengelolaan Truck Mixer, mulai dari autentikasi pengguna, pencatatan Truck Mixer masuk, data loading & perbaikan, hingga sistem antrean.

Project ini merupakan implementasi aplikasi mobile yang terintegrasi dengan backend dan database melalui REST API.

## ✨ Features

- 🔐 User Login
- 🚚 Data Truck Mixer
- 📋 Truck Mixer Masuk
- 📅 Data Booking
- 🚦 Sistem Antrean Truck Mixer
- 🔄 Integrasi REST API
- 🗄️ Database MySQL
- 📱 Android Mobile Application

## 🛠️ Technologies

### Mobile Application

- Kotlin
- Android Studio
- Android SDK
- XML Layout
- Volley

### Backend

- Node.js
- Express.js
- REST API

### Database

- MySQL
- XAMPP
- phpMyAdmin

## 🏗️ System Architecture

```text
┌─────────────────────────┐
│   Android Application   │
│         Kotlin          │
└────────────┬────────────┘
             │
             │ HTTP / REST API
             ▼
┌─────────────────────────┐
│     Node.js + Express   │
│        Backend API      │
└────────────┬────────────┘
             │
             │ MySQL Connection
             ▼
┌─────────────────────────┐
│       MySQL Database    │
│     Truck Mixer Data    │
└─────────────────────────┘
