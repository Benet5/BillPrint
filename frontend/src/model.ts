

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

interface Link {
    href: string;
    rel: string
}