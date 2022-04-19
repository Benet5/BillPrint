import {ClientStructure, Link} from "../model";
import {useEffect, useState} from "react";

interface ClientFormProps {
    createClient: (clientName: string, street: string, location: string, tax: boolean, fee: number, skonto: number) => void;
    clientToChange: ClientStructure;
    allClients: Array<ClientStructure>;
    changeClient: (clientName: string, street: string, location: string, tax: boolean, fee: number, skonto: number, links: Array<Link>) => void;
    deleteClient: (links: Array<Link> ) => void;
}


export default function ClientForm(props: ClientFormProps) {

    const [clientName, setClientName] = useState('')
    const [street, setStreet] = useState('')
    const [location, setLocation] = useState('')

    const [tax, setTaxes] = useState(false)
    const handleTaxes = () => {
        setTaxes(tax ? false : true)
    }
    const [fee, setFee] = useState(2)
    const [skonto, setSkonto] = useState(0)


    useEffect(() => {
        if (props.clientToChange) {
            setClientName(props.clientToChange.name)
            setStreet(props.clientToChange.street)
            setLocation(props.clientToChange.location)
            setTaxes(props.clientToChange.tax)
            setFee(props.clientToChange.fee)
            setSkonto(props.clientToChange.skonto)
        }
    }, [props.clientToChange])

    const changeData = () => {
        props.changeClient(clientName, street, location, tax, fee, skonto, props.clientToChange.links)

    }

    const setData = () => {
        props.createClient(clientName, street, location, tax, fee, skonto);
        setClientName("")
        setStreet("")
        setLocation("")
        setTaxes(false)
        setFee(0)
        setSkonto(0)
    }

    const deleteClient = () =>{
        props.deleteClient(props.clientToChange.links)
    }


    return (

        <div>
            <div> <h4>Mandantenanlage</h4>
                <p>Hier können die Mandaten angelegt oder bearbeitet werden. Achte darauf, dass je Mandantenname nur
                    ein
                    Datensatz existieren kann.</p>
                <div className="clientNav">
                <button className="buttonFrame cnav1" onClick={setData}>Mandanten anlegen</button>
                <button className="buttonFrame cnav2" onClick={changeData}>Mandanten ändern</button>
                <div style= {{display: "flex", justifyContent:"flex-end"}}><button className="buttonDelete" onClick={deleteClient}>Mandanten löschen</button></div>
                </div>
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
                        <div style={{marginBottom: "5px"}}>Verrechnungsdaten: Zuschläge</div>
                        <div className="add">
                        <input className="inputCalc check f1" type="checkbox" checked={tax} onChange={handleTaxes}/>
                        <div className="description f2">19% Mehrwertsteuer</div>


                        <input className="inputCalc f3" value={fee} type="number"
                               onChange={e => setFee(e.target.valueAsNumber)}/>
                        <div className="description f4">Bearbeitungsgebühr in % der jeweiligen Gesamtsumme</div>

                    </div>

                    <div className="calcPair2 neg">
                        <div style={{marginBottom: "5px"}}>Verrechnungsdaten: Abzüge</div>
                        <input className="inputCalc small" value={skonto} type="number"
                               onChange={e => setSkonto(e.target.valueAsNumber)}/>
                        <span className="description">Skonto in % der jeweiligen Gesamtsumme</span>
                    </div>
                </div>

            </div>
        </div>
    )
}