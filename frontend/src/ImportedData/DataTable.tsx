import axios from "axios";
import {Ad, Address, ImportedData, Link} from "../model";
import {useCallback, useEffect, useState} from "react";
import "./DataTable.css";
import DataItem from "./DataItem";
import Navbar from "../Navbar";
import {useNavigate} from "react-router-dom";
import {useAuth} from "../Auth/AuthProvider";

export default function DataTable() {
    const [allData, setAllData] = useState([] as Array<ImportedData>)
    const [errorMessage, setErrorMessage] = useState('');
    const [loadingMessage, setLoadingMessage] = useState("");
    const navigate = useNavigate();
    const {token} = useAuth()


    const getImportedData = useCallback(() => {
        axios.get(`${process.env.REACT_APP_BASE_URL}/api/import`, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            }
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
    }, [token])


    const mapAllAdresses = () => {
        axios.put(`${process.env.REACT_APP_BASE_URL}/api/mapping`, null, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            }
        }).then(getImportedData)
            .catch(() => {
                setErrorMessage("Daten konnten nicht geladen werden!");
            })
    }


    const mapSelected = (name: string, ad: Ad, customer: string, listingID: string, address: Address, links: Array<Link>) => {
        axios.put(`${process.env.REACT_APP_BASE_URL}${links.find(l => l.rel === 'self')?.href}`, null, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            }
        }).then(getImportedData)
            .catch(() => {
                setErrorMessage("Daten konnten nicht geladen werden!");
            })
    }


    const createClientToPrint = () => {
        axios.put(`${process.env.REACT_APP_BASE_URL}/api/mapping/convert`, null, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
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
        axios.get(`${process.env.REACT_APP_BASE_URL}/api/zip`, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            }, responseType: 'blob',
        }).then(response => {
            if (response.status === 200) {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const a = document.createElement("a");
                a.style.display = "none";
                a.href = url;
                a.setAttribute('download', "Invoices.zip")
                document.body.appendChild(a);
                a.click();
                window.URL.revokeObjectURL(url);
                alert("Die Rechnungen wurden heruntergeladen, prüfe deinen Download-Ordner!");
            } else {
                setErrorMessage("Daten konnten nicht erstellt werden!")
            }
        })

    }

    const deleteAll = () =>{
        axios.delete(`${process.env.REACT_APP_BASE_URL}/api/import`,{
            headers:{
                Authorization: `Bearer ${token}`
            }
        }).then(getImportedData)
            .catch(() => setErrorMessage("Daten konnten nicht gelöscht werden."))
    }

    useEffect(() => {
            if (token.length < 2) {
                navigate("/auth/login")
            } else {
                getImportedData()
            }
        }, [token, navigate, getImportedData]
    )

    useEffect(() =>{
        const timeoutId = setTimeout(() => setErrorMessage(''), 10000);
        return () => clearTimeout(timeoutId);
    },[errorMessage])


    // <h5>Auftraggeber</h5>
    //Sortieren vor dem Map!
    return (
        <div>
            <Navbar/>


            <div className="main">
                <div className="ui main">
                    <div className="ui1">
                        <button className="buttonFrame" onClick={mapAllAdresses}>1. Daten mappen</button>
                        <div className="info">Mappe deine Items mit den Mandanten. Somit Aktualisierst du die Adresse und die
                            Zahlungsmodalitäten für die einzelnen Items. Sollten noch keine Mandanten angelegt sein, erstellst du neue.
                        </div>
                    </div>
                    <div className="ui2">
                        <button className="buttonFrame" onClick={createClientToPrint}>2. Rechnungen vorbereiten</button>
                        <div className="info">Erstelle die Rechnungsobjekte, die als PDF gedruckt werden sollen.</div>
                    </div>
                    <div className="ui3">
                        <button className="buttonFrame" onClick={downloadPDF}>3. Rechnungen herunterladen</button>
                        <div className="info">Erstelle die PDF-Rechnungen und lade sie dir als ZIP-Archiv herunter.
                        </div>
                    </div>
                </div>
                {allData.length > 0 &&
                    <div style={{display: "flex", justifyContent: "flex-end", alignItems: "center"}}>
                        <button className="buttonDelete" onClick={deleteAll}>Alle Rechnungsdaten löschen</button>
                    </div>
                }
                    <div className="success"> {loadingMessage} </div>
                <div className="tableBody">
                <div className="parent">
                    <h5> </h5>
                    <h5>Name</h5>
                    <h5>Title</h5>
                    <h5>Anzeigentyp</h5>
                    <h5>Laufzeit (in Tagen)</h5>
                    <h5>Auftragsart</h5>
                    <h5>Ausschreibungs-Ort</h5>
                    <h5>Anschrift</h5>
                    <h5>Straße, Hausnummer</h5>
                    <h5>PLZ, Ort</h5>

                </div>

                <div >

                    {allData.length > 0 ?
                        allData.map((e: ImportedData, index) => <div key={e.name + index}>

                            <DataItem item={e} key={e.name + index} mapSelected={mapSelected}/></div>)
                        :

                        <div className="error">  {errorMessage}</div>
                    }
                </div>
                </div>
            </div>
        </div>


    )
}

