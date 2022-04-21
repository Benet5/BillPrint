import Navbar from "./Navbar";
import {useNavigate} from "react-router-dom";
import {useAuth} from "./Auth/AuthProvider";
import {useCallback, useEffect, useState} from "react";
import axios from "axios";
import {UserData} from "./model";

export default function User() {

    const navigate = useNavigate();
    const {token} = useAuth()
    const [errorMessage, setErrorMessage] = useState('');
    const [email, setEmail] = useState("");
    const [emailValidate, setEmailValidate] = useState("");
    const [allWhitelistData, setAllWhitelistData] = useState([] as Array<UserData>);
    const [allUserData, setAllUserData] = useState([] as Array<UserData>);
    const [toDelete, setToDelete] = useState("")
    const [toDeleteValidate, setToDeleteValidate] = useState("")


    const addUser = () => {
        const emailExp = email.replace(/(\r\n|\n|\r)/gm, "")
        const emailValidateExp = emailValidate.replace(/(\r\n|\n|\r)/gm, "")
        if (emailExp === emailValidateExp) {
            fetch(`${process.env.REACT_APP_BASE_URL}/auth/whitelist/add`, {
                method: "POST",
                body: JSON.stringify({email: email}),
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`,
                    'X-XSRF-TOKEN': document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*=\s*([^;]*).*$)|^.*$/, '$1'),
                }
            })
                .then(getWhitelistet)
                .then(() => {
                    setEmail('');
                    setEmailValidate('')
                })
                .catch(error => {
                    if (error.response.status === 409) {
                        setErrorMessage("Diese*r Nutzer*in existiert schon!");
                    } else if (error.response.status === 403) {
                        setErrorMessage("Du bist nicht berechtigt!");
                    } else {
                        setErrorMessage("Unbekannter Fehler");
                    }
                })
        } else setErrorMessage("Email-Adressen stimmen nicht überein.");
    }


    const deleteWhitelist = () =>{
        if(toDelete === toDeleteValidate) {
            fetch(`${process.env.REACT_APP_BASE_URL}/auth/whitelist?email=` + toDelete, {
                method: "DELETE",
                headers: {
                    'Content-Type': "text/plain",
                    Authorization: `Bearer ${token}`,
                    'X-XSRF-TOKEN': document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*=\s*([^;]*).*$)|^.*$/, '$1'),
                }
            }).then(getWhitelistet)
              .then(() => {
                setToDelete('');
                setToDeleteValidate('')
            })
                .catch(error => {
                    if (error.response.status === 400) {
                        setErrorMessage("Eintrag nicht gefunden");
                    } else {
                        setErrorMessage("Unbekannter Fehler");
                    }
                })
        }else{ setErrorMessage("Die Nutzernamen stimmen nicht überein.")}
    }

    const deleteUser = () =>{
        if(toDelete === toDeleteValidate) {
            fetch(`${process.env.REACT_APP_BASE_URL}/auth?email=` + toDelete, {
                method: "DELETE",
                headers: {
                    'Content-Type': "text/plain",
                    Authorization: `Bearer ${token}`,
                    'X-XSRF-TOKEN': document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*=\s*([^;]*).*$)|^.*$/, '$1'),
                }
            }).then(getWhitelistet)
                .catch(error => {
                    if (error.response.status === 400) {
                        setErrorMessage("Eintrag nicht gefunden");
                    } else {
                        setErrorMessage("Unbekannter Fehler");
                    }
                })
        }else{ setErrorMessage("Die Nutzernamen stimmen nicht überein.")}
    }

    const getWhitelistet = useCallback(() => {
        axios.get(`${process.env.REACT_APP_BASE_URL}/auth/whitelist`, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            }
        }).then(response => {
            if (response.status === 200) {
                return response.data;
            }
        })
            .then((response1: Array<UserData>) => {
                setAllWhitelistData(response1)
            })
            .catch(error => {
                if (error.response.status === 400) {
                    setErrorMessage("Du bist nicht berechtigt!");
                } else {
                    setErrorMessage("Unbekannter Fehler");
                }
            })
    }, [token])


    const getUser = useCallback(() => {
        axios.get(`${process.env.REACT_APP_BASE_URL}/auth`, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            }
        }).then(response => {
            if (response.status === 200) {
                return response.data;
            }
        })
            .then((response1: Array<UserData>) => {
                setAllUserData(response1)
            })
            .catch(error => {
                if (error.response.status === 400) {
                    setErrorMessage("Du bist nicht berechtigt!");
                } else {
                    setErrorMessage("Unbekannter Fehler");
                }
            })
    }, [token])


    useEffect(() => {
            if (token.length < 2) {
                navigate("/auth/login")
            } else {
                getWhitelistet()
                getUser()
            }
        }, [token, navigate, getWhitelistet, getUser]
    )

    useEffect(() =>{
        const timeoutId = setTimeout(() => setErrorMessage(''), 10000);
        return () => clearTimeout(timeoutId);
    },[errorMessage])

    return (
        <div>
            <Navbar/>
            <div className="error">{errorMessage}</div>
            <div className="main">
                <div className="userform">
                    <div className="userCreate">
                        <h3>Nutzer*in auf die Whitelist schreiben</h3>
                        <div><input className="input" type='text' placeholder={"Email"} value={email}
                                    onChange={e => setEmail(e.target.value)}/></div>
                        <div><input className="input" type='text' placeholder={"Email-Validierung"} value={emailValidate}
                                    onChange={e => setEmailValidate(e.target.value)}/></div>
                        <button className="buttonFrame" onClick={addUser}>Nutzer*in freigeben</button>
                    </div>
                    <div className="userDelete">
                        <h3>Nutzer*in löschen</h3>
                    <div><input className="input" type='text' placeholder={"Email"} value={toDelete}
                                onChange={e => setToDelete(e.target.value)}/></div>
                    <div><input className="input" type='text' placeholder={"Email-Validierung"} value={toDeleteValidate}
                                onChange={e => setToDeleteValidate(e.target.value)}/></div>
                    <button className="buttonFrame" onClick={deleteWhitelist}>Nutzer*in von der Whitelist löschen</button>
                     <button className="buttonFrame" onClick={deleteUser}>Nutzer*inaccount löschen</button>
                    </div>
                </div>
                <div className="userform">
                <div className="userCreate">
                    <h3>Whitelist</h3>
                    <div style={{marginBottom: "10px"}}>Liste aller freigeschalteter Nutzer*innen</div>
                    {allWhitelistData.length > 0
                        ?
                        allWhitelistData.map((e, index) => <div className="usertable" key={e.email + index}>
                            <div className="userC1">Nr: {index+1}</div>
                            <div className="userC2">{e.email}</div>
                        </div>)
                        :
                        <div>{errorMessage}</div>
                    }
                </div>
                <div className="userDelete">
                    <h3>Registered User</h3>
                    <div style={{marginBottom: "10px"}}>Liste aller registrierter Nutzer*innen</div>
                    {allUserData.length > 0
                        ?
                        allUserData.map((e, index) => <div className="usertable" key={e.email + index}>
                            <div className="userC1">Nr: {index+1}</div>
                            <div className="userC2">{e.email}</div>
                        </div>)
                        :
                        <div>{errorMessage}</div>
                    }
                </div>

                </div>
            </div>
        </div>


    )
}