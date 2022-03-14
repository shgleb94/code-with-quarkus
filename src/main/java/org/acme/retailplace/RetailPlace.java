package org.acme.retailplace;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import org.acme.organization.LegalAddress;
import org.acme.organization.Organization;

import static org.acme.organization.LegalAddress.from;

public class RetailPlace {

    private long id;
    private String retailPlaceTitle;
    private RetailPlaceAddress retailPlaceAddress;

    public RetailPlace() {
    }

    public RetailPlace(long id, String retailPlaceTitle) {
        this.id = id;
        this.retailPlaceTitle = retailPlaceTitle;
    }

    public RetailPlace(long id, String retailPlaceTitle, RetailPlaceAddress retailPlaceAddress) {
        this.id = id;
        this.retailPlaceTitle = retailPlaceTitle;
        this.retailPlaceAddress = retailPlaceAddress;
    }

    public static RetailPlace from(Row row) {
        return new RetailPlace(row.getLong("id"), row.getString("title"));
    }

    public static Uni<Long> create(PgPool client, RetailPlace retailPlace) {
        return client.withTransaction(conn -> conn
                .preparedQuery("INSERT INTO retail_place (title) VALUES ($1) RETURNING id")
                .execute(Tuple.of(retailPlace.getRetailPlaceTitle()))
                .onItem().transformToUni(id -> conn.
                        preparedQuery("INSERT INTO retail_place_address (retail_place_id,region_code,city,locality_id,apartment,house,region,street) " +
                                "VALUES ($1,$2,$3,$4,$5,$6,$7,$8) returning retail_place_id")
                        .execute(Tuple.tuple(Arrays.asList(id.iterator().next().getLong("id"), retailPlace.getRetailPlaceAddress().getRegionCode(),
                                retailPlace.getRetailPlaceAddress().getCity(), retailPlace.getRetailPlaceAddress().getLocalityId(),
                                retailPlace.getRetailPlaceAddress().getApartment(), retailPlace.getRetailPlaceAddress().getHouse(),
                                retailPlace.getRetailPlaceAddress().getRegion(), retailPlace.getRetailPlaceAddress().getStreet())))))
                .onItem().transform(pgRowSet -> pgRowSet.iterator().next().getLong("retail_place_id"));
    }



    public static Uni<RetailPlace> get(PgPool client, long id){

        return client.preparedQuery("select * from retail_place where id = $1")
                .execute(Tuple.of(id)).onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRetailPlaceTitle() {
        return retailPlaceTitle;
    }

    public void setRetailPlaceTitle(String retailPlaceTitle) {
        this.retailPlaceTitle = retailPlaceTitle;
    }

    public RetailPlaceAddress getRetailPlaceAddress() {
        return retailPlaceAddress;
    }

    public void setRetailPlaceAddress(RetailPlaceAddress retailPlaceAddress) {
        this.retailPlaceAddress = retailPlaceAddress;
    }
}