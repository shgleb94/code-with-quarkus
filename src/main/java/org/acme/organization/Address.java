package org.acme.organization;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;

public class Address {

    private long regionCode;
    private String street;
    private String house;
    private String housing;
    private String apartment;

    public Address() {
    }

    public Address(long regionCode, String street, String house, String housing, String apartment) {
        this.regionCode = regionCode;
        this.street = street;
        this.house = house;
        this.housing = housing;
        this.apartment = apartment;
    }
    public static Address from(Row row) {
        return new Address(row.getLong("region_code"), row.getString("street"), row.getString("house"),
                row.getString("housing"), row.getString("apartment"));
    }

    public static Multi<Address> findAll(PgPool client) {
        return client.query("SELECT * FROM public.address").execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Address::from);
    }

    public long getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(long regionCode) {
        this.regionCode = regionCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getHousing() {
        return housing;
    }

    public void setHousing(String housing) {
        this.housing = housing;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }
}
