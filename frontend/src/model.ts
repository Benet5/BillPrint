
export interface ImportedData{
name : string;
ad : Ad;
customer: string;
listingID: string;
address: Address;

}

export interface Ad{
   title: string;
   type: string;
   runtime: number;
   listingAction: string;
   date: String;
   jobLocation: string;
}

export interface Address{
    addressName: string;
    addressStreet : string;
    addressLocation: string;

}