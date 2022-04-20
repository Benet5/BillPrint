
import {Link} from "react-router-dom";

export default function Footer() {

    return (

        <div className="footer">
            <div className="footer1">Duck Inc., Am GeldSpeicher 1, 1234567 Entenhausen. </div>
            <div className="footer3">
                <Link className="linkFooter" to="https://www.google.de" >Impressum</Link>
                <Link className="linkFooter" to="https://www.google.de">Hinweise zum Datenschutz</Link></div>
        </div>


    )

}
