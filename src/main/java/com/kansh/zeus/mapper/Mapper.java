package com.kansh.zeus.mapper;

public interface Mapper<A,B> {

    B mapTo(A a);

    A mapFrom(B b);
}
