import Navbar from "./Navbar";
import {useNavigate} from "react-router-dom";
import {useAuth} from "./Auth/AuthProvider";
import {useCallback, useEffect, useState} from "react";
import axios from "axios";

export default function User(){

    const navigate = useNavigate();
    const {token} = useAuth()
    const [errorMessage, setErrorMessage] = useState('');
    const [email, setEmail] = useState("" );
    const [emailValidate, setEmailValidate] = useState("");
    const[allData, setAllData] = useState([]as Array <string>);




    const addUser=() => {
            const emailExp = email.replace(/(\r\n|\n|\r)/gm, "")
            const emailValidateExp = emailValidate.replace(/(\r\n|\n|\r)/gm, "")
            if (emailExp === emailValidateExp) {
                fetch(`${process.env.REACT_APP_BASE_URL}/auth/whitelist/add`, {
                    method: "POST",
                    body: JSON.stringify({email: email}),
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${token}`,
                    }})
                    .catch(error => {
                        if(error.response.status === 409) {
                            setErrorMessage("Diese*r Nutzer*in existiert schon!");
                        }else if (error.response.status === 403){
                            setErrorMessage("Du bist nicht berechtigt!");
                        } else {
                            setErrorMessage("Unbekannter Fehler");
                        }
                    })
            } else setErrorMessage("Email-Adressen stimmen nicht überein.");
        }


        const getUser =useCallback(() =>{
            axios.get(`${process.env.REACT_APP_BASE_URL}/whitelist`, {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                }}).then(response => {
                if (response.status === 200) {
                    return response.data;
                }
            })
                .then((response1 : Array<string> ) =>{
                    setAllData(response1)
                })
                .catch(error => {
                    if(error.response.status === 409) {
                        setErrorMessage("Diese*r Nutzer*in existiert schon!");
                    }else if (error.response.status === 403){
                        setErrorMessage("Du bist nicht berechtigt!");
                    } else {
                        setErrorMessage("Unbekannter Fehler");
                    }
                })
        },[token])

    useEffect(() => {
            if (token.length < 2) {
                navigate("/auth/login")
            } getUser()
        }, [token, navigate, getUser]
    )


    return(
        <div>
            <Navbar/>
            <div>{errorMessage}</div>
            <div className="userform">
                <div> Nutzer*infreigabe
                    <div><input className="input" type='text' placeholder={"Email"} value={email}
                                onChange={e => setEmail(e.target.value)}/></div>
                    <div><input className="input" type='text' placeholder={"Email"} value={emailValidate}
                                onChange={e => setEmailValidate(e.target.value)}/></div>
                    <button className="buttonFrame" onClick={addUser}>Nutzer*in freigeben</button>
                </div>

            </div>
            <div>
                {allData.map((e: string, index) => <div>{e}</div>)}

            </div>

        </div>


    )
}