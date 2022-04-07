import axios from "axios";
import {Ad, Address, ImportedData, Link} from "./model";
import {useEffect, useState} from "react";
import "./DataTable.css";
import DataItem from "./DataItem";
import Navbar from "./Navbar";

export default function DataTable() {
    const [allData, setAllData] = useState([] as Array<ImportedData>)
    const [errorMessage, setErrorMessage] = useState('');
    const [loadingMessage, setLoadingMessage] = useState("");

    useEffect(() => {
            getImportedData()
        }, []
    )

    const getImportedData = () => {
        axios.get(`${process.env.REACT_APP_BASE_URL}/api/import`, {
            headers: {
                'Content-Type': 'application/json'
            },
        }).then(response => {
            if (response.status === 200) {
                return response.data;
            }
        }).then((response2: Array<ImportedData>) => {
            setAllData(response2)
        })
            .catch(() => {
                setErrorMessage("Daten konnten nicht geladen werden!");
            })
    }


    const mapAllAdresses = () => {
        axios.put(`${process.env.REACT_APP_BASE_URL}/api/mapping`, {
            headers: {
                'Content-Type': 'application/json'
            },
        }).then(getImportedData)
            .catch(() => {
                setErrorMessage("Daten konnten nicht geladen werden!");
            })
    }


    const mapSelected = (name: string, ad: Ad, customer: string, listingID: string, address: Address, links: Array<Link>) => {
        axios.put(`${process.env.REACT_APP_BASE_URL}${links.find(l => l.rel === 'self')?.href}`, {
            headers: {
                'Content-Type': 'application/json'
            },
        }).then(getImportedData)
            .catch(() => {
                setErrorMessage("Daten konnten nicht geladen werden!");
            })
    }


    const createClientToPrint = () => {
        axios.put(`${process.env.REACT_APP_BASE_URL}/api/mapping/convert`, {
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => {
            if (response.status === 200) {
                return response.data;
            }
        }).then(response => setLoadingMessage(response))
            .catch(() => {
                setErrorMessage("Daten konnten nicht erstellt werden!");
            })
    }

    const downloadPDF = () => {
        axios.put(`${process.env.REACT_APP_BASE_URL}/generate`, {
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => {
            if (response.status === 200) {
                setLoadingMessage("PDFs wurden generiert.")
            }
        }).catch(() => {
                setErrorMessage("Daten konnten nicht erstellt werden!");
            })
    }








    //Sortieren vor dem Map!
    return (
    <div>
        <div><Navbar/></div>


        <div className="main">
            <div>
                <button  className="buttonFrame" onClick={mapAllAdresses}>1. Alle Itemadressen mappen</button>
                <span><button className="buttonFrame" onClick={createClientToPrint}>2. Rechnungen vorbereiten</button></span>
                <span><button className="buttonFrame" onClick={downloadPDF}>3. Rechnungen herunterladen</button></span>
            </div>
        <div className="success"> {loadingMessage} </div>
    <div className="parent">
    <h5>Name</h5>
        <h5>Title</h5>
        <h5>Anzeigentyp</h5>
        <h5>Laufzeit (in Tagen)</h5>
        <h5>Auftragsart</h5>
        <h5>Ausschreibungsort</h5>
        <h5>Auftraggeber</h5>
        <h5>Anschrift</h5>
        <h5>Straße, Hausnummer</h5>
        <h5>PLZ, Ort</h5>

    </div>



    {allData.length>0 ?
        allData.map((e: ImportedData, index) => <div key={e.name + index}>

            <DataItem item={e} key={e.name + index} mapSelected={mapSelected}/></div>)
        :

            <div className="error">  {errorMessage}</div>
    }


        </div>
    </div>

    )
}

