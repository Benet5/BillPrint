import axios from "axios";
import {ImportedData} from "./model";
import {useEffect, useState} from "react";
import "./DataTable.css";
import DataItem from "./DataItem";
import Navbar from "./Navbar";

export default function DataTable() {
    const [allData, setAllData] = useState([] as Array <ImportedData>)
    const [errorMessage, setErrorMessage] = useState('');
    const loadingMessage =("Die Seite lädt nicht!");

    useEffect(()=>{
        getImportedData()
        },[]
        )

    const getImportedData = () => {
        axios.get(`${process.env.REACT_APP_BASE_URL}/api/import`, {
            headers: {
                'Content-Type': 'application/json'
            },
        }).then(response => {
            if (response.status === 200) {
                return response.data;
            }else {
                setErrorMessage("Daten konnten nicht geladen werden!")
            }
        }).then((response2:Array<ImportedData>) =>{setAllData(response2)})
    }

    //Sortieren vor dem Map!
    return (
    <div>
        <div><Navbar/></div>

        <div className="main">
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

            <DataItem item={e} key={e.name + index}/></div>)
        :

            <div> {loadingMessage} {errorMessage}</div>
    }


        </div>
    </div>

    )
}

