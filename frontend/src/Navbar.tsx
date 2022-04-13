import {Link} from "react-router-dom";
import "./App.css";
import {useAuth} from "./Auth/AuthProvider";



export default function Navbar(){
    const {logout} = useAuth()
    return(

        <div className="parentNavbar">
            <Link to="/api/import" className="link">CSV importieren</Link>
            <Link to="/table" className="link">Datenübersicht</Link>
            <Link to ="/clients" className="link">Mandantenverwaltung</Link>
            <Link to="/user" className="link">Nutzer*in freischalten</Link>
            <span style={{background:"transparent"}}><button className="link" onClick={logout}>Logout</button></span>

        </div>


    )


}