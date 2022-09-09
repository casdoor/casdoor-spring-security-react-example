import React from "react";
import { isSilentSigninRequired, SilentSignin } from "casdoor-react-sdk";
import * as Setting from "./Setting";
import LoginPage from "./LoginPage";

class HomePage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      account: undefined,
    };
  }

  componentDidMount() {
    if (Setting.isLoggedIn()) {
      Setting.getUserinfo().then((res) => {
        if (res?.status === "ok") {
          this.setState({
            account: {
              username: res.data.displayName,
              avatar: res.data.avatar,
            },
          });
        } else {
          Setting.showMessage(res?.status);
        }
      });
    }
  }

  logout() {
    Setting.logout();
    Setting.showMessage("logout successfully");
    Setting.goToLink("/");
  }

  render() {
    if (Setting.isLoggedIn()) {
      if (this.state.account) {
        return (
          <div
            style={{
              marginTop: 200,
              textAlign: "center",
              alignItems: "center",
            }}
          >
            <img
              width={100}
              height={100}
              src={this.state.account.avatar}
              alt={this.state.account.username}
            />
            <br />
            <p>{this.state.account.username}</p>
            <br />
            <button onClick={this.logout}>Logout</button>
          </div>
        );
      } else {
        return <p>Loading...</p>;
      }
    }

    if (isSilentSigninRequired()) {
      return (
        <div
          style={{ marginTop: 200, textAlign: "center", alignItems: "center" }}
        >
          <SilentSignin
            sdk={Setting.CasdoorSDK}
            isLoggedIn={Setting.isLoggedIn}
            handleReceivedSilentSigninSuccessEvent={() => Setting.goToLink("/")}
            handleReceivedSilentSigninFailureEvent={() => {}}
          />
          <p>Logging in...</p>
        </div>
      );
    }

    return (
      <div style={{ marginTop: 200 }}>
        <LoginPage />
      </div>
    );
  }
}

export default HomePage;
