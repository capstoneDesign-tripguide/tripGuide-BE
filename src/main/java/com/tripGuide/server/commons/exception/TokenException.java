package com.tripGuide.server.commons.exception;

public class TokenException extends CustomException{

    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
