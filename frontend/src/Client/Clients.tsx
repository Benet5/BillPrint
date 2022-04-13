import Navbar from "../Navbar";
import {useCallback, useEffect, useState} from "react";
import "./Client.css"
import axios from "axios";
import ClientTable from "./ClientTable";
import {ClientStructure, Link} from "../model";
import ClientForm from "./ClientForm";
import {useAuth} from "../Auth/AuthProvider";
import {useNavigate} from "react-router-dom";



export default function Clients() {

    const [errorMessage, setErrorMessage] = useState('')
    const [allClients, setAllClients] = useState([] as Array<ClientStructure>)
    const [clientToChange, setClientToChange] = useState( {name: "", street: "", location: "", tax: false, fee: 0, skonto: 0, links: []} as ClientStructure);
    const {token} = useAuth()
    const navigate = useNavigate();





    const getClientData = useCallback(() => {
        axios.get(`${process.env.REACT_APP_BASE_URL}/api/clients`, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            },
        }).then(response => {
            if (response.status === 200) {
                return response.data;
            }
        }).then((response2: Array<ClientStructure>) => {
            setAllClients(response2)
        }).catch(() =>
            setErrorMessage("Daten konnten nicht geladen werden!")
        )
    },[token])

    const createClient = (clientName: string, street: string, location: string, tax: boolean, fee: number, skonto: number) => {
        const streetExport = street.replace(/(\r\n|\n|\r)/gm, "")
        const locationExport = location.replace(/(\r\n|\n|\r)/gm, "")
        const clientNameExport = clientName.replace(/(\r\n|\n|\r)/gm, "")
        if (clientName.length > 3) {
        axios.post(`${process.env.REACT_APP_BASE_URL}/api/clients`, {
            name: clientNameExport,
            street: streetExport,
            location: locationExport,
            tax: tax,
            fee: fee,
            skonto: skonto,
         headers: {
                'Content-Type': 'application/json',
                 Authorization: `Bearer ${token}`
            }})
            .then(getClientData)
            .catch(error => {
                if(error.response.status === 409)
                {
                    setErrorMessage("Dieser Client existiert schon!");
                }else {
                    setErrorMessage("Unbekannter Fehler");
                }
            })
        } else setErrorMessage("Bitte gib einen Namen ein.")
    }

    const changeClient = (clientName: string, street: string, location: string, tax: boolean, fee: number, skonto: number, links: Array<Link>) => {
        const streetExport = street.replace(/(\r\n|\n|\r)/gm, "")
        const locationExport = location.replace(/(\r\n|\n|\r)/gm, "")
        const clientNameExport = clientName.replace(/(\r\n|\n|\r)/gm, "")
        if (clientName.length > 3) {
            axios.put(`${process.env.REACT_APP_BASE_URL}${links.find(l => l.rel === 'self')?.href}`, {
                name: clientNameExport,
                street: streetExport,
                location: locationExport,
                tax: tax,
                fee: fee,
                skonto: skonto
            }, {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                }
            }).then(getClientData)
                .then(() => setClientToChange({
                    name: "",
                    street: "",
                    location: "",
                    tax: false,
                    fee: 0,
                    skonto: 0,
                    links: []
                }))
                .catch(error => {
                    if (error.response.status === 409) {
                        setErrorMessage("Client konnte nicht aktualisiert werden!");
                    } else {
                        setErrorMessage("Unbekannter Fehler bei der Mandantenanlage.")
                    }
                })
        } else setErrorMessage("Bitte gib einen Namen ein.")
    }


    const setToForm = (name: string, street: string, location: string, tax: boolean, fee: number, skonto: number, links: Array<Link>) => {
        setClientToChange({
            name, street, location, tax, fee, skonto, links
        })
    }


        useEffect(() => {
            if(token.length<2){
            navigate("/auth/login")}
         else{
            getClientData()
            setErrorMessage('')
        }}, [token, navigate, getClientData])


        useEffect(() =>{
            const timeoutId = setTimeout(() => setErrorMessage(''), 10000);
            return () => clearTimeout(timeoutId);
        },[errorMessage])



        return (
            <div>
                <Navbar/>
                <div className="main">
                    <div className="error">{errorMessage}</div>
                    <div className=""><ClientForm createClient={createClient} allClients={allClients}
                                                  clientToChange={clientToChange} changeClient={changeClient} /></div>
                    <div className="clientBody"><ClientTable getClientData={getClientData} allClients={allClients}
                                                        setToForm={setToForm} /></div>

                </div>


            </div>


        )



}