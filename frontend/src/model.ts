

export interface ImportedData{
name : string;
ad : Ad;
customer: string;
listingID: string;
address: Address;
links: Array<Link>;

}

export interface Ad{
   title: string;
   type: string;
   runtime: number;
   listingAction: string;
   date: string;
   jobLocation: string;
}

export interface Address{
    name: string;
    street : string;
    location: string;

}

export interface Link {
    href: string;
    rel: string
}

export interface ClientStructure{
    name:string;
    street: string;
    location : string;
    tax : boolean;
    fee: number;
    skonto: number;
    links: Array <Link>;
}

export interface UserData{
    email: string;
}