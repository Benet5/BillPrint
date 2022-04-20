
import {Outlet} from "react-router-dom";
import"./App.css"

import img2 from "./Data/duck.png";
import Footer from "./Footer";

function App() {


    return (
        <div>
            <div className="header">
                <h2 className="headerTitle">BillPrint</h2>
                <div className="imgContainer"><img className="logo2" src={img2} alt="OTTOLogo" /></div>
            </div>

        <div className="basic">
            <div className="body"><Outlet /> </div>
            <div className="foot"> <Footer/></div>
        </div>

        </div>
    );
}

export default App;
