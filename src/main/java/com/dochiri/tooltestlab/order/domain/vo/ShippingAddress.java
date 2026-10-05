package com.dochiri.tooltestlab.order.domain.vo;

import com.dochiri.tooltestlab.order.domain.exception.ShippingAddressRequiredException;

import java.util.Objects;

public final class ShippingAddress {

    private final RecipientName recipientName;
    private final Phone phone;
    private final StreetAddress address;
    private final ZipCode zipCode;

    private ShippingAddress(
            final RecipientName recipientName,
            final Phone phone,
            final StreetAddress address,
            final ZipCode zipCode
    ) {
        if (recipientName == null || phone == null || address == null || zipCode == null) {
            throw new ShippingAddressRequiredException();
        }
        this.recipientName = recipientName;
        this.phone = phone;
        this.address = address;
        this.zipCode = zipCode;
    }

    public static ShippingAddress of(
            final RecipientName recipientName,
            final Phone phone,
            final StreetAddress address,
            final ZipCode zipCode
    ) {
        return new ShippingAddress(recipientName, phone, address, zipCode);
    }

    public RecipientName recipientName() {
        return recipientName;
    }

    public Phone phone() {
        return phone;
    }

    public StreetAddress address() {
        return address;
    }

    public ZipCode zipCode() {
        return zipCode;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final ShippingAddress other)) {
            return false;
        }
        return recipientName.equals(
                other.recipientName)
                && phone.equals(
                        other.phone)
                && address.equals(
                        other.address)
                && zipCode.equals(
                        other.zipCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipientName, phone, address, zipCode);
    }

    @Override
    public String toString() {
        return "ShippingAddress[recipientName=" + recipientName + ", phone=" + phone + ", address=" + address + ", zipCode=" + zipCode + "]";
    }

}
