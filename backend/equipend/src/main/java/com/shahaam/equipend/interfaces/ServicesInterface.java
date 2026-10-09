package com.shahaam.equipend.interfaces;

public interface ServicesInterface<R, P, U> {

    public R add(P req);
    public R get(String query);
    public R update(U req);
    public void delete(U identier);
}