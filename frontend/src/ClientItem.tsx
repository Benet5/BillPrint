import {ClientStructure} from "./model";
import "./DataTable.css";


interface ClientItemProps{
    item : ClientStructure

}

export default function ClientItem(props: ClientItemProps){

    const fee = props.item.tax ? 19 : 0;



    return(
        <div>



        <div className="parentClientTable">
            <p  className="firstColumn">{props.item.name}</p>
            <p  className="secondColumn">{props.item.street}</p>
            <p  className="thirdColumn">{props.item.location}</p>
            <p  className="fiveColumn">{props.item.tax}</p>
            <p className="fourthColumn">{fee}</p>
            <p  className="sixColumn">{props.item.skonto}</p>
            <div className="sevenColumn"><button  className="buttonFrame">Button</button> </div>
        </div>

    </div>




    )

}