import Navbar from "./Navbar";
import {useState} from "react";
import "./Client.css"

export default function Clients(){
const [clientName, setClientName] = useState('')
const [street, setStreet] = useState('')
const [location, setLocation] = useState('')

const [taxes, setTaxes] =useState(false)
const handleTaxes = ()=>setTaxes(!taxes);
const [fee, setFee] =useState(2)
const[skonto, setSkonto] = useState(0)

    //fetches Post,Put, Get, Delete



    return (
        <div>
            <div><Navbar/></div>
            <div className="main">
                <div>Mandantenanlage
                    <p>Hier können die Mandaten angelegt oder bearbeitet werden. Achte darauf, dass je Mandatenname nur
                        ein
                        Datensatz existieren kann.</p>
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
                            <input className="inputCalc" checked={taxes} type="checkbox" onClick={handleTaxes}/>
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
            </div>

        </div>


    )



}