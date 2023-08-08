# casdoor-spring-security-react-example

A silent sign-in example is implemented through spring security and casdoor react SDK.

### Installation:

First, you need to clone the repository.

```shell
git clone https://github.com/casdoor/casdoor-spring-security-react-example
```

Then, download the corresponding Maven dependency and front-end dependency respectively.

```shell
# backend
mvn dependency:resolve

# frontend
cd web

yarn install
or 
npm install
```

Next, you need to configure two places.

- First, only 7 parameters in `src/main/resources/application.yml` need to be configured.

  ```yaml
  server:
      port: 8080
  casdoor:
      endpoint: http://localhost:8000
      client-id: <your client id>
      client-secret: <your client secret> 
      certificate: <your certificate>
      organization-name: <your organization name>
      application-name: <your application name>
      redirect-url: <your frontend url>/callback
  ```

- Second, you only need to configure the 6 parameters in `web/src/Setting.js`.

    ```js
    export const ServerUrl = "http://localhost:8080";
    
    const sdkConfig = {
      serverUrl: "http://localhost:8000",
      clientId: "<your client id>",
      appName: "<your application name>",
      organizationName: "<your organization name>",
      redirectPath: "/callback",
    };
    ```


### Start

First, run the `mvn package` command in the project root directory.

Then enter the target directory and execute the `java -jar example-0.0.1-SNAPSHOT.jar` command.

Finally, enter the web directory and execute the command `yarn start`.