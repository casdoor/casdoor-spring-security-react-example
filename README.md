# Casdoor Spring Security + React Example

[![Build](https://github.com/casdoor/casdoor-spring-security-react-example/actions/workflows/build.yml/badge.svg)](https://github.com/casdoor/casdoor-spring-security-react-example/actions/workflows/build.yml)
[![License](https://img.shields.io/github/license/casdoor/casdoor-spring-security-react-example)](https://github.com/casdoor/casdoor-spring-security-react-example/blob/master/LICENSE)
[![Discord](https://img.shields.io/discord/1022748306096537660?logo=discord&label=discord&color=5865F2)](https://discord.gg/5rPsrAzK7S)

An example app that signs users in with [Casdoor](https://casdoor.ai/): a React frontend, and a Spring Security backend that accepts Casdoor access tokens. It also shows silent sign-in.

| Part     | SDK                                                                                 | Language                 | Port |
|----------|-------------------------------------------------------------------------------------|--------------------------|------|
| Frontend | [casdoor-react-sdk](https://github.com/casdoor/casdoor-react-sdk), [casdoor-js-sdk](https://github.com/casdoor/casdoor-js-sdk) | JavaScript + React       | 3000 |
| Backend  | [casdoor-spring-boot-starter](https://github.com/casdoor/casdoor-spring-boot-starter) | Java + Spring Security 6 | 8080 |

## How it works

1. **Casdoor Login** sends the user to the Casdoor sign-in page (`CasdoorSDK.getSigninUrl()`); the random `state` in the URL is kept in sessionStorage.
2. Casdoor redirects back to `http://localhost:3000/callback`. `AuthCallback` of casdoor-react-sdk checks the state and posts the code to the backend: `POST /api/signin?code=...&state=...`.
3. The backend exchanges the code for an access token (`AuthService.getOAuthToken()`) and returns it. The frontend keeps it in localStorage.
4. The frontend calls the other APIs with `Authorization: Bearer <access token>`. The backend is an OAuth2 resource server: Spring Security verifies the token (a JWT) against Casdoor's JWKS, casdoor-spring-boot-starter sets that up from the `casdoor.*` properties.
5. **Logout** removes the token and calls `POST /api/logout`, which ends the Casdoor session (`AuthService.logoutCurrentSession()`).

Silent sign-in: open `http://localhost:3000/?silentSignin=1` while you are signed in to Casdoor in the same browser. `SilentSignin` signs in through a hidden iframe without showing the Casdoor page.

| API                  | Auth         | Description                                       |
|----------------------|--------------|---------------------------------------------------|
| `POST /api/signin`   | public       | Exchanges the code for an access token            |
| `GET /api/userinfo`  | bearer token | Returns the claims of the token, i.e. the user    |
| `POST /api/logout`   | bearer token | Ends the Casdoor session of the token             |

## Prerequisites

- Java 17+ and Maven 3.9+
- Node.js 18+ and Yarn
- A Casdoor server. The example is preconfigured for the public demo server https://door.casdoor.com, so it runs as is. To use your own, see [Casdoor installation](https://casdoor.ai/docs/basic/server-installation).

## Configuration

Skip this section to try the example with the public demo server.

In your Casdoor, create (or reuse) an organization and an application, and add `http://localhost:3000/callback` to the application's **Redirect URLs**. Then fill in both parts:

### Backend

[src/main/resources/application.yml](src/main/resources/application.yml):

```yaml
casdoor:
  endpoint: https://door.casdoor.com          # Casdoor server URL
  client-id: 294b09fbc17f95daf2fe             # client ID of the application
  client-secret: dd8982f7046ccba1bbd7851d5c1ece4e52bf039d  # client secret of the application
  organization-name: casbin                   # organization of the application
  application-name: app-vue-python-example    # name of the application

# the React frontend, allowed to call the APIs (CORS)
frontend-url: http://localhost:3000
```

### Frontend

[web/src/Setting.js](web/src/Setting.js):

```js
export const ServerUrl = "http://localhost:8080"; // the backend

const sdkConfig = {
  serverUrl: "https://door.casdoor.com", // Casdoor server URL
  clientId: "294b09fbc17f95daf2fe", // client ID of the application
  appName: "app-vue-python-example", // name of the application
  organizationName: "casbin", // organization of the application
  redirectPath: "/callback",
  signinPath: "/api/signin",
};
```

## Run

```shell
git clone https://github.com/casdoor/casdoor-spring-security-react-example
cd casdoor-spring-security-react-example
```

Backend, at http://localhost:8080:

```shell
mvn spring-boot:run
```

Frontend, at http://localhost:3000:

```shell
cd web
yarn install
yarn start
```

Open http://localhost:3000 and click **Casdoor Login**. On the demo server, sign in with username `admin` and password `123`.

Run the tests:

```shell
mvn verify
cd web && yarn test
```

## Resources

- [Casdoor documentation](https://casdoor.ai/docs/overview)
- [Casdoor Spring Security integration](https://casdoor.ai/docs/integration/java/spring-security/spring-security-oauth/)
- [casdoor-spring-boot-starter](https://github.com/casdoor/casdoor-spring-boot-starter)
- [casdoor-react-sdk](https://github.com/casdoor/casdoor-react-sdk)
- [Spring Security OAuth2 resource server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html)

## License

[Apache-2.0](LICENSE)
