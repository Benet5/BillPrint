import axios from "axios";
import {ImportedData} from "./model";
import {useEffect, useState} from "react";

export default function DataTable() {
    const [allData, setAllData] = useState([] as Array <ImportedData>)
    const [errorMessage, setErrorMessage] = useState('');


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
            if (response.status === 200 || 201) {
                return response.data;
            }throw new Error()
        })
            .then((response2:Array<ImportedData>) =>{setAllData(response2)})
            .catch(error => {
            if (error.response.status === 422) {
                setErrorMessage('Nicht alle Einträge konnten importiert werden!');
            } else if (error.response.status === 400) {
                setErrorMessage('Die Einträge wurde nicht importiert. Guck ins Log!!!');
            }
        })
    }


    return (


        //table of Items, rows und Columns festlegen
        <div>
            {allData ?

                allData.map((e : ImportedData, index) => <div key={e.name+index}>
                    {e.name}
                </div>)




                : {errorMessage}
            }


        </div>
    )
}

