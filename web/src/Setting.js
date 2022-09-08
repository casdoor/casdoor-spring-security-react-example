import Sdk from "casdoor-js-sdk";

export const ServerUrl = "http://localhost:8080";

const sdkConfig = {
  serverUrl: "http://localhost:8000",
  clientId: "ab9a5aaf648ae8ab4f1c",
  appName: "application_rm47vn",
  organizationName: "organization_carg1b",
  redirectPath: "/callback",
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

export const getRedirectUrl = () => {
  return fetch(`${ServerUrl}/api/redirect-url`, {
    method: "GET",
  }).then((res) => res.json());
};

export const getUserinfo = () => {
  return fetch(`${ServerUrl}/api/userinfo`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  }).then((res) => res.json());
};

export const showMessage = (message) => {
  alert(message);
};
