import React, {useState} from "react";
import axios from "axios";
import Navbar from "./Navbar";
import "./DataTable.css";

export default function CSVImport() {
    const [errorMessage, setErrorMessage] = useState('');

     const importData = (file: File | null) => {
        const fileData = new FormData();
        fileData.append('csv', file!);
        axios.post(`${process.env.REACT_APP_BASE_URL}/api/import`, fileData, {
            headers: {
                'Content-Type': 'multipart/form-data'
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
        })
    }

        return (
            <div >
                <div><Navbar/></div>

                 <div>
                {errorMessage}
                <div className="main">
                    <h5>Es dürfen nur CSV-Dateien im UTF8 Format hochgeladen werden. Achte bitte auf die vorgegebene Spaltenstruktur.</h5>
                    <label className="buttonFrame">
                    <input type="file" accept=".csv"onChange={ev => importData(ev.target.files![0])}/> CSV Upload
                    </label>
                </div>

            </div>

            </div>
        )

}