package com.ninjaone.backendinterviewproject.exceptions;

public class DeviceFeeNotConfiguredException extends Exception{

    public DeviceFeeNotConfiguredException() {
    }

    public DeviceFeeNotConfiguredException(String message) {
        super(message);
    }
}
