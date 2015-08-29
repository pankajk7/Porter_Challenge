package com.porter.entities;

import java.io.Serializable;

public class ParcelListInfo implements Serializable{
	Parcel[] parcels;

	public Parcel[] getParcels() {
		return parcels;
	}

	public void setParcels(Parcel[] parcels) {
		this.parcels = parcels;
	}

}
