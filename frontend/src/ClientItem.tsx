import {ClientStructure, Link} from "./model";
import "./DataTable.css";


interface ClientItemProps{
    item : ClientStructure
    setToForm: (clientName :string, street:string, location:string, tax: boolean, fee: number, skonto: number, links: Array<Link>) => void;
}

export default function ClientItem(props: ClientItemProps){

    const fee = props.item.tax ? 19 : 0;

    const edit = () => {
        props.setToForm(props.item.name, props.item.street,props.item.location, props.item.tax, fee, props.item.skonto, props.item.links);
    }


    return(
        <div>



        <div className="parentClientTable">
            <p  className="firstColumn">{props.item.name}</p>
            <p  className="secondColumn">{props.item.street}</p>
            <p  className="thirdColumn">{props.item.location}</p>
            <p  className="fiveColumn">{props.item.tax}</p>
            <p className="fourthColumn">{fee}</p>
            <p  className="sixColumn">{props.item.skonto}</p>
            <div className="sevenColumn"><button onClick={edit}  className="buttonFrame">Button</button> </div>
        </div>

    </div>




    )

}