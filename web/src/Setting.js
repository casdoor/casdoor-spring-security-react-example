// Copyright 2022 The Casdoor Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import Sdk from "casdoor-js-sdk";

// the Spring Boot backend
export const ServerUrl = "http://localhost:8080";

// the Casdoor application, the defaults are the public demo server https://door.casdoor.com
const sdkConfig = {
  serverUrl: "https://door.casdoor.com",
  clientId: "294b09fbc17f95daf2fe",
  appName: "app-vue-python-example",
  organizationName: "casbin",
  redirectPath: "/callback",
  signinPath: "/api/signin",
};

export const CasdoorSDK = new Sdk(sdkConfig);

export const isLoggedIn = () => {
  const token = localStorage.getItem("token");
  return token !== null && token.length > 0;
};

export const setToken = (token) => {
  localStorage.setItem("token", token);
};

export const goToLink = (link) => {
  window.location.href = link;
};

// the sign-in URL carries a random state, kept in sessionStorage and checked by CasdoorSDK.signin() on the callback
export const getSigninUrl = () => {
  return CasdoorSDK.getSigninUrl();
};

export const getUserinfo = () => {
  return fetch(`${ServerUrl}/api/userinfo`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  }).then((res) => res.json());
};

export const logout = () => {
  const token = localStorage.getItem("token");
  localStorage.removeItem("token");
  // end the Casdoor session too, so the next sign-in asks for the password again
  return fetch(`${ServerUrl}/api/logout`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  }).catch(() => {});
};

export const showMessage = (message) => {
  alert(message);
};
