import Navbar from "./Navbar";
import {useEffect, useState} from "react";
import "./Client.css"
import axios from "axios";
import ClientTable from "./ClientTable";
import {ClientStructure} from "./model";
import ClientForm from "./ClientForm";





export default function Clients(){

    const [errorMessage, setErrorMessage] =useState('')
    const[allClients, setAllClients] = useState([] as Array <ClientStructure>)


    const getClientData = () => {
        axios.get(`${process.env.REACT_APP_BASE_URL}/api/clients`, {
            headers: {
                'Content-Type': 'application/json'
            },
        }).then(response => {
            if (response.status === 200) {
                return response.data;
            } else {
                setErrorMessage("Daten konnten nicht geladen werden!");
            }
        }).then((response2: Array<ClientStructure>) => {
            setAllClients(response2)
        })
    }

   const createClient = (clientName: string, street: string, location : string, tax :boolean, fee : number, skonto: number) =>{
       const streetExport= street.replace(/(\r\n|\n|\r)/gm, "")
       const locationExport= location.replace(/(\r\n|\n|\r)/gm, "")
       const clientNameExport= clientName.replace(/(\r\n|\n|\r)/gm, "")
       axios.post(`${process.env.REACT_APP_BASE_URL}/api/clients`, {
                   name: clientNameExport,
                   street: streetExport,
                   location: locationExport,
                   tax: tax,
                   fee: fee,
                   skonto: skonto,
           }
       ).then(getClientData)
           .catch(error => setErrorMessage(error))
// falls error number gleich 409 = xx noch ienbauen
   }


    useEffect(()=>{
        getClientData()
    },[])




    //fetches Post,Put, Get, Delete



    return (
        <div>
            <div><Navbar/></div>
            <div className="main">
                {errorMessage.length > 2 ? {errorMessage} : ""}
                    <div className=""><ClientForm createClient={createClient} allClients={allClients}/></div>
                    <div className="table"><ClientTable getClientData={getClientData} allClients={allClients} /></div>

                </div>


            </div>




    )



}