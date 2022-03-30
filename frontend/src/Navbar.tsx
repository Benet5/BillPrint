import {Link} from "react-router-dom";
import "./App.css";



export default function Navbar(){

    return(

        <div className="parentNavbar">
            <Link to="/api/import" className="link">CSV importieren</Link>
            <Link to="/table" className="link">Datenübersicht</Link>
            <Link to ="/clients" className="link">Mandantenverwaltung</Link>

        </div>


    )


}