import "./App.css";

import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { AuthCallback } from "casdoor-react-sdk";
import * as Setting from "./Setting";
import HomePage from "./HomePage";

class App extends React.Component {
  authCallback = (
    <AuthCallback
      sdk={Setting.CasdoorSDK}
      serverUrl={Setting.ServerUrl}
      saveTokenFromResponse={(res) => {
        Setting.setToken(res?.data);
        Setting.goToLink("/");
      }}
      isGetTokenSuccessful={(res) => res?.status === "ok"}
    />
  );

  render() {
    return (
      <BrowserRouter>
        <Routes>
          <Route path="/callback" element={this.authCallback} />
          <Route path="/" element={<HomePage />} />
        </Routes>
      </BrowserRouter>
    );
  }
}

export default App;
