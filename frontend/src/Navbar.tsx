import {Link} from "react-router-dom";
import "./App.css";
import {useAuth} from "./Auth/AuthProvider";



export default function Navbar(){
    const {logout} = useAuth()
    return(

        <div className="parentNavbar">
            <Link style={{justifySelf:"start"}} to="/import" className="link">CSV importieren</Link>
            <Link style={{justifySelf:"start"}} to="/table" className="link">Datenübersicht</Link>
            <Link style={{justifySelf:"start"}} to ="/clients" className="link">Mandantenverwaltung</Link>
            <Link style={{justifySelf:"start"}} to="/user" className="link">Nutzer*innen-Verwaltung</Link>
            <span style={{background:"transparent", justifySelf:"end"}}><button className="link" onClick={logout}>Logout</button></span>

        </div>


    )


}