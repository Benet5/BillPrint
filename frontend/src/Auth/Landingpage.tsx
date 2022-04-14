import {useAuth} from "./AuthProvider";
import {FormEvent, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import "./User.css";

export default function Landingpage() {
    const {token, login} = useAuth()
    const [errorMessage, setErrorMessage] = useState('')
    const [loginUserEmail, setLoginUserEmail] = useState('')
    const [loginUserPassword, setLoginUserPassword] = useState('')
    const [loadingMessage, setLoadingMessage] = useState('')

    const [registerUserEmail, setRegisterUserEmail] = useState('')
    const [registerUserPassword, setRegisterUserPassword] = useState('')
    const [registerUserPasswordValidate, setRegisterUserPasswordValidate] = useState('')
    const navigate = useNavigate();

    useEffect(() => {
            setErrorMessage('')
            if (token.length > 2) {
                navigate("/table")
            }
        }, [token, navigate]
    )

    const loginService = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (loginUserEmail.length > 3)
            login(loginUserEmail, loginUserPassword)
                .then(() => navigate("/table"))
                .catch(error => {
                    if (error.response.status === 400){
                        setErrorMessage("Deine Logindaten sind fehlerhaft.");
                    } else{
                        setErrorMessage("Unbekannter Fehler während der Anmeldung.")
                    }
                })

    }


    const registerService = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (registerUserEmail.length > 2 && registerUserPassword === registerUserPasswordValidate) {
            return axios.post(`${process.env.REACT_APP_BASE_URL}/auth/create`, {
                    email: registerUserEmail,
                    password: registerUserPassword,
                    passwordValidate: registerUserPasswordValidate
                , headers: {
                    'Content-Type': 'application/json',
                }
            }).then(response => {
                if (response.status === 201) {
                    setLoadingMessage("Registrierung war erfolgreich, du kannst dich nun einloggen.")
                }}).catch(error => {
                    if(error.response.status === 404) {
                    setErrorMessage("Deine Email-Adresse ist nicht zugelassen.")
                } else if (error.response.status === 409) {
                    setErrorMessage("Dieser Nutzer existiert schon.")
                }else {
                    setErrorMessage("Unbekannter Fehler, bitte wende dich an den Admin.")
                }
            }).then(() => {
                setRegisterUserEmail("");
                setRegisterUserPassword("");
                setRegisterUserPasswordValidate("");
            })
        } else {
            setErrorMessage("Passwörter stimmen nicht überein.")
        }
    }

        return (

            <div className="main">
                <div className="error">{errorMessage}</div>
                <div className="error">{loadingMessage}</div>
                <div className="welcome">
                    <h4>Willkommen bei BillPrint!</h4>
                    <p>Mit dieser App kannst du aus importierten Daten(CSV) deine Rechnungen als PDF drucken lassen.</p>
                    <p>Verantwortlich für den Inhalt und die Verwaltung ist: <a href="mailto: admin@billprint.de">Admin Billprint</a></p>
                    <p>Falls du für eine Registrierung noch nicht freigeschaltet bist, melde dich bitte beim Admin.</p>
                </div>
                <div className="landingpage">

                <div className="login">
                    <form onSubmit={ev => loginService(ev)}>
                        <h3>Login</h3>
                        <div><input className="input" type={'text'} placeholder={"Email-Adresse"} value={loginUserEmail}
                                    onChange={e => setLoginUserEmail(e.target.value)}/></div>
                        <div><input className="input" type={'password'} placeholder={"Passwort"}
                                    value={loginUserPassword} onChange={e => setLoginUserPassword(e.target.value)}/>
                        </div>
                        <button className="buttonFrame" type='submit'>Login</button>
                    </form>
                </div>
                <div className="register">
                    <form onSubmit={ev => registerService(ev)}>
                        <h3>Registrierung</h3>
                        <div><input className="input" type={'text'} placeholder={"Email-Adresse"}
                                    value={registerUserEmail} onChange={e => setRegisterUserEmail(e.target.value)}/>
                        </div>
                        <div><input className="input" type={'password'} placeholder={"Passwort"}
                                    value={registerUserPassword}
                                    onChange={e => setRegisterUserPassword(e.target.value)}/></div>
                        <div><input className="input" type={'password'} placeholder={"Passwort"}
                                    value={registerUserPasswordValidate}
                                    onChange={e => setRegisterUserPasswordValidate(e.target.value)}/></div>
                        <button className="buttonFrame" type='submit'>Registrieren</button>
                    </form>
                </div>
                </div>

            </div>
        )


}


