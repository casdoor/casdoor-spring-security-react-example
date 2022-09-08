import React from "react";
import * as Setting from "./Setting";

class LoginPage extends React.Component {
  casdoorLogin() {
    Setting.getRedirectUrl().then((res) => {
      if (res?.status === "ok") {
        Setting.goToLink(res.data);
      } else {
        Setting.showMessage("failed to get redirect url");
      }
    });
  }

  render() {
    return (
      <div
        style={{
          textAlign: "center",
          alignItems: "center",
        }}
      >
        <button onClick={this.casdoorLogin}>Casdoor Login</button>
      </div>
    );
  }
}

export default LoginPage;
