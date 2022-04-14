import React, {useEffect, useState} from "react";
import axios from "axios";
import Navbar from "../Navbar";
import "./DataTable.css";
import {useNavigate} from "react-router-dom";
import {useAuth} from "../Auth/AuthProvider";
import img1 from "../Data/BeispielUpload.png";

export default function CSVImport() {
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate()
    const {token} = useAuth()

    useEffect( () => {
            if (token.length<2)
            {navigate("/auth/login")}
        }, [token, navigate]
    )

    useEffect(() =>{
        const timeoutId = setTimeout(() => setErrorMessage(''), 10000);
        return () => clearTimeout(timeoutId);
    },[errorMessage])

     const importData = (file: File | null) => {
        const fileData = new FormData();
        fileData.append('csv', file!);
        axios.post(`${process.env.REACT_APP_BASE_URL}/api/import`, fileData, {
            headers: {
                'Content-Type': 'multipart/form-data',
                Authorization: `Bearer ${token}`
            },
            onUploadProgress: progressEvent => {
                console.log("Uploading : " + ((progressEvent.loaded / progressEvent.total) * 100).toString() + "%")
            }
        }).catch(error => {
            if (error.response.status === 422) {
                setErrorMessage('Nicht alle Einträge konnten importiert werden!');
            } else if (error.response.status === 400) {
                setErrorMessage('Die Einträge wurde nicht importiert. Guck ins Log!!!');
            }
        }).then(() =>navigate("/table"));
    }

        return (
            <div >
                <Navbar/>

                <div className="error">{errorMessage}</div>
                <div className="main">
                    <div className="main">


                        <h4>Allgemeine Hinweise zum Upload</h4>
                        <ul>
                            <li>Als Separator in der CSV ist das Komma zu nutzen! Die einzelnen Einträge sind durch einen Zeilenumbruch voneinander getrennt. Dies betrifft auch die Kopfzeile.</li>
                            <li>Die Kopfzeile muss folgende Struktur aufweisen und darf nicht größer sein:
                               <div style={{fontStyle: "italic"}}>name,listingID,title,type,runtime,listingAction,jobLocation,date,customer,addressName,addressStreet,addressLocation</div></li>
                             <li>Eine Beispiel-Datei kann hier eingesehen werden:
                                 <div className="imgContainer"><img src={img1} alt="UploadBeispiel" /></div></li>
                            <li>Leere Einträge sind durch ", ," zu kennzeichnen. </li>
                            <li>Die Datei muss nach UTF-8 kodiert sein.</li>

                        </ul>
                    </div>
                    <label className="buttonFrame big">
                    <input type="file" accept=".csv"onChange={ev => importData(ev.target.files![0])}/> CSV Upload
                    </label>
                    </div>
                </div>



        )

}