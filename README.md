# My Movie App

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Android app for discovering movies using www.freetestapi.com API with pagination, favorites, and detailed views.

## Features
- Browse trending movies with pagination
- Add/remove movies from favorites
- View movie details (title, poster, overview)
- Error handling and empty states
- Grid/List layout toggle

## Architecture
**MVVM Pattern**  
- **Presentation Layer**: Fragments + ViewModels  
- **Domain Layer**: Use Cases + Repository Interface  
- **Data Layer**: Repository Implementation (API + Room DB)  

**Key Decisions**:
- Single source of truth with repository pattern
- Paging 3 for efficient data loading
- Hilt for dependency injection
- StateFlow for UI state management

## Tech Stack
- **Kotlin**  
- **Jetpack Components**: ViewModel, Room, Navigation  
- **Retrofit** + **OkHttp** for networking  
- **Glide** for image loading  
- **Paging 3** for pagination  
- **Hilt** for DI  

## How to Run
1. Get API www.freetestapi.com
2. Create `local.properties` in root directory with:
   ```properties
  
