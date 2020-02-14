package com.han.createbankrecfileapi.common.helper;

import java.io.Serializable;

public interface LabelAndValue<T> extends Serializable {

    T getValue();

    String getLabel();
}
