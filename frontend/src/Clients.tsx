import Navbar from "./Navbar";
import {useEffect, useState} from "react";
import "./Client.css"
import axios from "axios";
import ClientTable from "./ClientTable";
import {ClientStructure, Link} from "./model";
import ClientForm from "./ClientForm";



export default function Clients() {

    const [errorMessage, setErrorMessage] = useState('')
    const [allClients, setAllClients] = useState([] as Array<ClientStructure>)
    const [clientToChange, setClientToChange] = useState( {name: "", street: "", location: "", tax: false, fee: 0, skonto: 0, links: []} as ClientStructure);

    const getClientData = () => {
        axios.get(`${process.env.REACT_APP_BASE_URL}/api/clients`, {
            headers: {
                'Content-Type': 'application/json'
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
    }

    const createClient = (clientName: string, street: string, location: string, tax: boolean, fee: number, skonto: number) => {
        const streetExport = street.replace(/(\r\n|\n|\r)/gm, "")
        const locationExport = location.replace(/(\r\n|\n|\r)/gm, "")
        const clientNameExport = clientName.replace(/(\r\n|\n|\r)/gm, "")
        axios.post(`${process.env.REACT_APP_BASE_URL}/api/clients`, {
            name: clientNameExport,
            street: streetExport,
            location: locationExport,
            tax: tax,
            fee: fee,
            skonto: skonto,
        })
            .then(getClientData)
            .catch(error => {
                if(error.response.status === 409)
                {
                    setErrorMessage("Dieser Client existiert schon!");
                }else {
                    setErrorMessage("Unbekannter Fehler");
                }
            })
    }

    const changeClient = (clientName: string, street: string, location: string, tax: boolean, fee: number, skonto: number, links: Array<Link>) => {
        const streetExport = street.replace(/(\r\n|\n|\r)/gm, "")
        const locationExport = location.replace(/(\r\n|\n|\r)/gm, "")
        const clientNameExport = clientName.replace(/(\r\n|\n|\r)/gm, "")
        axios.put(`${process.env.REACT_APP_BASE_URL}${links.find(l => l.rel === 'self')?.href}`, {
            name: clientNameExport,
            street: streetExport,
            location: locationExport,
            tax: tax,
            fee: fee,
            skonto: skonto
        }, {
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(getClientData)
          .then(() => setClientToChange({name: "", street: "", location: "", tax: false, fee: 0, skonto: 0, links: []}))
          .catch(error => {
                if (error.response.status === 409) {
                    setErrorMessage("Client konnte nicht aktualisiert werden!");
                } else {
                    setErrorMessage("Unbekannter Fehler bei der Mandantenanlage.")
                }
            })
    }


    const setToForm = (name: string, street: string, location: string, tax: boolean, fee: number, skonto: number, links: Array<Link>) => {
        setClientToChange({
            name, street, location, tax, fee, skonto, links
        })
    }


        useEffect(() => {
            getClientData()
        }, [])






        return (
            <div>
                <div><Navbar/></div>
                <div className="main">
                    <div>{errorMessage}</div>
                    <div className=""><ClientForm createClient={createClient} allClients={allClients}
                                                  clientToChange={clientToChange} changeClient={changeClient} /></div>
                    <div className="table"><ClientTable getClientData={getClientData} allClients={allClients}
                                                        setToForm={setToForm} /></div>

                </div>


            </div>


        )



}