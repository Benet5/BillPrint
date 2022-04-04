import {ClientStructure, Link} from "./model";
import "./DataTable.css";


interface ClientItemProps{
    item : ClientStructure
    setToForm: (clientName :string, street:string, location:string, tax: boolean, fee: number, skonto: number, links: Array<Link>) => void;
}

export default function ClientItem(props: ClientItemProps){

    const tax = props.item.tax ? 19 : 0

    const edit = () => {
        props.setToForm(props.item.name, props.item.street,props.item.location, props.item.tax, props.item.fee, props.item.skonto, props.item.links);
    }


    return(
        <div>



        <div className="parentClientTable">
            <p  className="firstColumn">{props.item.name}</p>
            <p  className="secondColumn">{props.item.street}</p>
            <p  className="thirdColumn">{props.item.location}</p>
            <p  className="fourthColumn">{tax}</p>
            <p className="fiveColumn">{props.item.fee}</p>
            <p  className="sixColumn">{props.item.skonto}</p>
            <div className="sevenColumn"><button onClick={edit}  className="buttonFrame">Auswählen</button> </div>
        </div>

    </div>




    )

}