import React, { useState, useEffect } from 'react';
import {Outlet} from "react-router-dom";
import"./App.css"
import img from "./Data/Otto_Logo_rgb.png";

function App() {


    return (
        <div>
            <div className="header">
                <h2 className="headerTitle">BillPrint</h2>
                <div className="imgContainer"><img className="logo" src={img} /></div>
            </div>

        <div>

            <Outlet/>

        </div>
        </div>
    );
}

export default App;
