import React, { useState, useEffect } from 'react';
import {Outlet} from "react-router-dom";

function App() {

    const [greeting, setGreeting] = useState('')

    useEffect(() => {
        fetch('/api/greeting', {
            method: 'GET',
            headers: {
                'Accept': 'text/plain'
            }
        })
            .then(response => response.text())
            .then(text => setGreeting(text))
            .catch(err => setGreeting('Da ist etwas schief gelaufen'));
    }, []);

    return (
        <div>
            {greeting}
            <Outlet/>

        </div>
    );
}

export default App;
