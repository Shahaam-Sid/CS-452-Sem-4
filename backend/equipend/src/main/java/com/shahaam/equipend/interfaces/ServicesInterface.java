package com.shahaam.equipend.interfaces;

import java.util.UUID;

import com.shahaam.equipend.exceptions.UpdatingServiceNotApplicableException;

public interface ServicesInterface<R, P, U> {

    public R add(P req);
    public R get(UUID id);
    public default R update(U req) {
        throw new UpdatingServiceNotApplicableException(this.getClass().getSimpleName());
    }
    public default void delete(String identier) {
        throw new UpdatingServiceNotApplicableException(this.getClass().getSimpleName());
    }
}