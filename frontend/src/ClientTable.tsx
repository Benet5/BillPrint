import { useEffect} from "react";
import {ClientStructure} from "./model";

import ClientItem from "./ClientItem";
import "./DataTable.css";

interface ClientTableProps{
    getClientData : () => void;
    allClients : Array <ClientStructure>;

}
export default function ClientTable(props: ClientTableProps){


    useEffect(() => {
            props.getClientData()
        }, []
    )

    return (

        <div>

                <div className="parentClientTable">
                    <h5>Name</h5>
                    <h5>Straße & Hausnummer</h5>
                    <h5>PLZ & Ort</h5>
                    <h5>MwSt.</h5>
                    <h5>Gebühr [%]</h5>
                    <h5>Skonto [%]</h5>
                </div>

                <div>
                    {props.allClients.length &&
                        props.allClients.map((e: ClientStructure, index) => <div key={e.name + index}>

                            <ClientItem item={e} key={e.name + index}/></div>)

                    }
                </div>
        </div>


    )
}