import React, { useState, useEffect } from 'react';
import CSVImport from "./CSVImport";

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
            <CSVImport/>
        </div>
    );
}

export default App;
