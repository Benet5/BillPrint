import React, { Suspense } from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import reportWebVitals from './reportWebVitals';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import CSVImport from "./ImportedData/CSVImport";
import DataTable from "./ImportedData/DataTable";
import Clients from "./Client/Clients";

ReactDOM.render(
    <React.StrictMode>
    <Suspense fallback ="Loading...">
       <BrowserRouter>
            <Routes>
                <Route path ="/" element ={<App/>}>
                    <Route path ="/api/import" element ={<CSVImport/>}/>
                    <Route path ="/table" element ={<DataTable/>}/>
                    <Route path= "/clients" element ={<Clients/>}/>
                    <Route path="/*" element={<CSVImport/>}/>
                </Route>
            </Routes>
       </BrowserRouter>
    </Suspense>
    </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
