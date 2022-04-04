import {Ad, Address, ImportedData, Link} from "./model";
interface DataItemProps{
    item : ImportedData
    mapSelected : (name : string, ad : Ad, customer : string, listingID : string, address : Address, links : Array<Link>) => void;

}

export default function DataItem(props : DataItemProps){

const map = () =>{
    props.mapSelected(props.item.name, props.item.ad, props.item.customer, props.item.listingID, props.item.address, props.item.links);
    }

    return (

<div className="parent">
    <p  className="firstColumn">{props.item.name}</p>
    <p  className="secondColumn">{props.item.ad.title}</p>
    <p  className="thirdColumn">{props.item.ad.type}</p>
    <p className="fourthColumn">{props.item.ad.runtime}</p>
    <p  className="fiveColumn">{props.item.ad.listingAction}</p>
    <p  className="sixColumn">{props.item.ad.jobLocation}</p>
    <p  className="sevenColumn">{props.item.customer}</p>
    <p className="eigthColumn">{props.item.address.name}</p>
    <p className="nineColumn">{props.item.address.street}</p>
    <p className="tenColumn">{props.item.address.location}</p>
    <div className="elevenColumn"><button  onClick={map} className="buttonFrame">Mappen</button> </div>
</div>

    )

}