# A Spring Boot application just to test

This repo is just to update my knowledge on Spring Boot, with a simple REST API.  
The idea is to do it for other technologies too.

## Running the Application

This application is protected by Google OAuth2. You need credentials from the [Google Cloud Console](https://console.cloud.google.com/).

### Setup

1.  Go to **APIs & Services > Credentials** in Google Cloud Console.
2.  Create an **OAuth 2.0 Client ID** (Web application type).
3.  Add `http://localhost:8080/login/oauth2/code/google` as an **Authorized redirect URI**.
4.  Note your **Client ID** and **Client Secret**.

### Start the Application

```bash
GOOGLE_CLIENT_ID=your-client-id GOOGLE_CLIENT_SECRET=your-client-secret ./mvnw spring-boot:run
```

### Testing

1.  Open `http://localhost:8080/api/zikresources` in your browser.
2.  You will be redirected to the Google login page.
3.  After logging in, you will be redirected back to the API.