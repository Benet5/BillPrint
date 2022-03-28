import {useState} from "react";
import axios from "axios";

export default function CSVImport(){
    const [errorMessage, setErrorMessage] = useState('');

    const performFileUpload = (file: File | null) => {
        const fileData = new FormData();
        fileData.append('csv', file!);

        axios.post(`${process.env.REACT_APP_BASE_URL}/api/import`, fileData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            },
            onUploadProgress:progressEvent => {
                console.log("Uploading : " + ((progressEvent.loaded / progressEvent.total) * 100).toString() + "%")
            }
        })
            .catch(error => {
                if (error.response.status === 422) {
                    setErrorMessage('Nicht alle Einträge konnten importiert werden!');
                } else if (error.response.status === 400) {
                    setErrorMessage('Die Einträge wurde nicht importiert. Guck ins Log!!!');
                }
            })
    };

    return(
        <div>
            {errorMessage}
        <div>
            <input type="file" onChange={ev => performFileUpload(ev.target.files![0])} />
        </div>
        </div>

    )
}