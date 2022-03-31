import Navbar from "./Navbar";
import {useState} from "react";
import "./Client.css"
import axios from "axios";
import ClientTable from "./ClientTable";

export default function Clients(){
const [clientName, setClientName] = useState('')
const [street, setStreet] = useState('')
const [location, setLocation] = useState('')

const [tax, setTaxes] =useState(false)
const handleTaxes = ()=>setTaxes(!tax);
const [fee, setFee] =useState(2)
const[skonto, setSkonto] = useState(0)
const [errorMessage, setErrorMessage] =useState('')




   const createClient = () =>{
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
       ).catch(error => setErrorMessage(error))
   }

    //fetches Post,Put, Get, Delete



    return (
        <div>
            <div><Navbar/></div>
            <div className="main">
                <div>Mandantenanlage
                    <p>Hier können die Mandaten angelegt oder bearbeitet werden. Achte darauf, dass je Mandatenname nur
                        ein
                        Datensatz existieren kann.</p>
                    <button className="buttonFrame" onClick={createClient}>Mandaten anlegen</button>
                    {errorMessage.length>2 ?{errorMessage} :''}
                </div>
                <div className="clientForm">
                    <div className="address">
                        <div>Adressdaten</div>
                        <div><textarea className="input" placeholder={"Unternehmensname"} value={clientName}
                                       onChange={e => setClientName(e.target.value)}/></div>
                        <div><textarea className="input" placeholder={"Straße & Hausnummer"} value={street}
                                       onChange={e => setStreet(e.target.value)}/></div>
                        <div><input className="input" type='text' placeholder={"PLZ & Ort"} value={location}
                                    onChange={e => setLocation(e.target.value)}/></div>
                    </div>
                    <div className="calcData">
                        <div className="calcPair">
                        <div>Verrechnungsdaten: Zuschläge</div>
                            <input className="inputCalc" type="checkbox" onClick={handleTaxes}/>
                            <span className="description">19% Mehrwertsteuer</span>
                        </div>
                        <div className="calcPair">
                            <input className="inputCalc" value={fee} type="number"
                                   onChange={e => setFee(e.target.valueAsNumber)}/>
                            <span className="description">Bearbeitungsgebühr in % der jeweiligen Gesamtsumme</span>
                        </div>
                        <div className="calcPair">
                            <div>Verrechnungsdaten: Abzüge</div>
                            <input className="inputCalc" value={skonto} type="number"
                                   onChange={e => setSkonto(e.target.valueAsNumber)}/>
                            <span className="description">Skonto in % der jeweiligen Gesamtsumme</span>
                        </div>
                    </div>

                </div>

                <div><ClientTable/></div>
            </div>

        </div>


    )



}