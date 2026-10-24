export const AuthenticateRequestForm = Object.freeze({
    identifier: "",
    password: ""
  });
  
  export const RegisterRequestForm = Object.freeze({
    fullName: "",
    username: "",
    email: "",
    dob: "",
    country: "",
    password: ""
  });

export const createAuthenticateRequestForm = () => ({ ...AuthenticateRequestForm });
export const createRegisterRequestForm = () => ({ ...RegisterRequestForm });