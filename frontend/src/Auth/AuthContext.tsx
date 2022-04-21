import {createContext} from "react";
export interface AuthInterface{
    token: string,
    login : (userMail :string, userPassword:string) => Promise<void>,
    logout: () => void

}


export default createContext({}as AuthInterface)