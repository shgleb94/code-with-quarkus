package org.acme.organization;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.PreparedStatement;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Organization {

    private long id;
    private String iinBin;
    private String title;
    private String email;
    private String phone;
    private String bic;
    private String paymentAccount;
    private Address address;
    private String billingAccountNumber;

    public Organization() {
    }

    public Organization(long id, String iinBin, String title, String email, String phone, String bic, String paymentAccount, Address address, String billingAccountNumber) {
        this.id = id;
        this.iinBin = iinBin;
        this.title = title;
        this.email = email;
        this.phone = phone;
        this.bic = bic;
        this.paymentAccount = paymentAccount;
        this.address = address;
        this.billingAccountNumber = billingAccountNumber;
    }

    public static Uni<RowSet<Row>> create(PgPool client, Organization organization) throws SQLException {
Long id_sec = null;
client.preparedQuery("select nextval('organization_id_seq')").execute().onItem().transform(m->m.iterator().next().getLong("nextval"));


        return client
                .preparedQuery("INSERT INTO organization (id,iin_bin,title,email,phone,bic,payment_account) " +
                        "VALUES (60,$1,$2,$3,$4,$5,$6)")
                .execute(Tuple.of(organization.getIinBin(),organization.getTitle(),organization.getEmail(),organization.getPhone(),organization.getBic(),organization.getPaymentAccount()));

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIinBin() {
        return iinBin;
    }

    public void setIinBin(String iinBin) {
        this.iinBin = iinBin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getBillingAccountNumber() {
        return billingAccountNumber;
    }

    public void setBillingAccountNumber(String billingAccountNumber) {
        this.billingAccountNumber = billingAccountNumber;
    }
}
