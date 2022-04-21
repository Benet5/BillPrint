import {ReactNode, useContext, useState} from "react";
import AuthContext from "./AuthContext";
import axios from "axios";


export const useAuth = () => useContext(AuthContext);
export default function AuthProvider({children}:{children : ReactNode}) {

    const [token, setToken] = useState('');


    const login = (userEmail : string, userPassword : string) => {
        if (userEmail.length > 3) {
            return axios.post(`${process.env.REACT_APP_BASE_URL}/auth/login`, {
                    email: userEmail,
                    password: userPassword,
                headers: {
                    'Content-Type': 'application/json',
                }
            }).then(response => {
                if (response.status ===200) {
                     setToken(response.data)
                } else if (response.status === 400){
                    throw new Error ("Fehler beim Login.")
                }
            })
        } else {
            return Promise.reject("Eingabe zu kurz!");
        }

    }

    const logout = () => {
        setToken('')
    }


    return <AuthContext.Provider value={{token, login, logout}}> {children}</AuthContext.Provider>;
}